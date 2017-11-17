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

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import easybackup.place.ispl.R;
import easybackup.place.ispl.models.ContactDetailsModel;
import easybackup.place.ispl.utils.EasyBackUpUtils;

/**
 * Created by infinium on 4/3/17.
 */

public class DuplicatePhonesAdapter extends BaseAdapter {

    private List<String> mDublicatePhonesDetailsList;
    private Map<String, List<ContactDetailsModel>> mDublicatePhonesMap = null;
    private static LayoutInflater inflater = null;
    private Context mContext;
    private final ColorGenerator mGenerator;

    public DuplicatePhonesAdapter(Context context, List<String> keyList, Map<String, List<ContactDetailsModel>> DublicateContactsMap) {
        this.mDublicatePhonesDetailsList = keyList;
        this.mContext = context;
        mDublicatePhonesMap = DublicateContactsMap;
        inflater = (LayoutInflater) mContext.
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mGenerator = ColorGenerator.DEFAULT;
    }

    @Override
    public int getCount() {
        return mDublicatePhonesDetailsList.size();
    }

    @Override
    public String getItem(int position) {
        return mDublicatePhonesDetailsList.get(position);
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
        view = inflater.inflate(R.layout.single_duplicate_phones_view, null);
        holder.mContactIV = (ImageView) view.findViewById(R.id.ivduplicate_phones_contact_image);
        holder.mContactPhoneTV = (TextView) view.findViewById(R.id.tvduplicate_phones_contact_phone);
        holder.mContactNameTV = (TextView) view.findViewById(R.id.tvduplicate_phones_contact_name);
        holder.mContactCountTV = (TextView) view.findViewById(R.id.count_duplicate_phones_tv);
        holder.mMainLL = (LinearLayout) view.findViewById(R.id.main_layout);
        holder.view = view;

        String title = mDublicatePhonesDetailsList.get(position);
        holder.mContactPhoneTV.setText(title);

        List<ContactDetailsModel> contactDetailsModelList = mDublicatePhonesMap.get(title);
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
               // holder.mContactIV.setImageURI(Uri.parse(imageUrl));
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
            Collections.sort(contactDetailsModelList, new Comparator<ContactDetailsModel>() {
                @Override
                public int compare(ContactDetailsModel o1, ContactDetailsModel o2) {
                    EasyBackUpUtils.v("O1 : " + o1.getContactName());
                    EasyBackUpUtils.v("O2 : " + o2.getContactName());
                    return o1.getContactName().compareToIgnoreCase(o2.getContactName());
                }
            });
        }
        return view;
    }

    //RETURN FILTER OBJ
    private class Holder {
        View view;
        ImageView mContactIV;
        TextView mContactPhoneTV,mContactNameTV, mContactCountTV;
        LinearLayout mMainLL;
    }
}


