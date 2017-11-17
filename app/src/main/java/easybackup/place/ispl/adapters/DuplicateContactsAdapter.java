package easybackup.place.ispl.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;

import java.util.List;
import java.util.Map;

import easybackup.place.ispl.R;
import easybackup.place.ispl.models.ContactDetailsModel;

/**
 * Created by infinium on 4/3/17.
 */

public class DuplicateContactsAdapter extends BaseAdapter {

    private List<String> mDublicateContactsDetailsList;
    private Map<String, List<ContactDetailsModel>> mDublicateContactsMap = null;
    private static LayoutInflater inflater = null;
    private Context mContext;
    private final ColorGenerator mGenerator;

    public DuplicateContactsAdapter(Context context, List<String> keyList, Map<String, List<ContactDetailsModel>> DublicateContactsMap) {
        this.mDublicateContactsDetailsList = keyList;
        this.mContext = context;

        mDublicateContactsMap = DublicateContactsMap;
        inflater = (LayoutInflater) mContext.
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mGenerator = ColorGenerator.DEFAULT;
    }

    @Override
    public int getCount() {
        return mDublicateContactsDetailsList.size();
    }

    @Override
    public String getItem(int position) {
        return mDublicateContactsDetailsList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @SuppressLint({"ViewHolder", "InflateParams"})
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        final Holder holder = new Holder();
        final View view;
        view = inflater.inflate(R.layout.single_duplicate_contacts_view, null);
        holder.mContactIV = (ImageView) view.findViewById(R.id.ivdublicate_contacts_contact_image);
        holder.mMainContactNameTV = (TextView) view.findViewById(R.id.tvdublicate_contacts_contact_name_m);
        holder.mContactNameTV = (TextView) view.findViewById(R.id.tvdublicate_contacts_contact_name);
        holder.mContactCountTV = (TextView) view.findViewById(R.id.count_dublicate_contacts_tv);
        holder.mMainLL = (LinearLayout) view.findViewById(R.id.main_layout);
        holder.view =view;

        String title = mDublicateContactsDetailsList.get(position);
        String name = mDublicateContactsDetailsList.get(position);
        name = name.substring(0, 1).toUpperCase() + name.substring(1).toLowerCase();
        holder.mContactNameTV.setText(title);
        holder.mMainContactNameTV.setText(name);
        List<ContactDetailsModel> contactDetailsModelList = mDublicateContactsMap.get(title);
        if (contactDetailsModelList != null && contactDetailsModelList.size() > 0) {
            StringBuilder nameBuilder = new StringBuilder();
            for (ContactDetailsModel model : contactDetailsModelList) {
                String mName = model.getContactName();
                nameBuilder.append(mName + ",");
            }
            String displayName = nameBuilder.toString().substring(0, nameBuilder.length() - 1);
            holder.mContactNameTV.setText(displayName);
            String count = String.valueOf(contactDetailsModelList.size());
            holder.mContactCountTV.setText(count + " >");
            String imageUrl = contactDetailsModelList.get(0).getContactImage();
            if (imageUrl != null && !imageUrl.equalsIgnoreCase("")) {
                //holder.mContactIV.setImageURI(Uri.parse(imageUrl));
                Glide.with(mContext).load(imageUrl).asBitmap().centerCrop().into(new BitmapImageViewTarget(holder.mContactIV) {
                    @Override
                    protected void setResource(Bitmap resource) {
                        RoundedBitmapDrawable circularBitmapDrawable =
                                RoundedBitmapDrawableFactory.create(mContext.getResources(), resource);
                        circularBitmapDrawable.setCircular(true);
                        holder.mContactIV.setImageDrawable(circularBitmapDrawable);
                    }
                });
            }else {
                TextDrawable.IBuilder mDrawableBuilder = TextDrawable.builder().round();
                TextDrawable drawable = mDrawableBuilder.build(String.valueOf(displayName.charAt(0)), mGenerator.getColor(displayName));
                holder.mContactIV.setImageDrawable(drawable);
                holder.view.setBackgroundColor(Color.TRANSPARENT);
            }
        }
        return view;
    }

    //RETURN FILTER OBJ
    private class Holder {
        View view;
        ImageView mContactIV;
        TextView mMainContactNameTV,mContactNameTV, mContactCountTV;
        LinearLayout mMainLL;
    }
}


