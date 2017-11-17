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
import easybackup.place.ispl.utils.EasyBackUpUtils;

/**
 * Created by infinium on 4/3/17.
 */

public class CompanyAdapter extends BaseAdapter {

    private List<String> mCompanyDetailsList;
    private static LayoutInflater inflater = null;
    private Map<String, List<ContactDetailsModel>> mComapnyMap = null;
    private Context mContext;
    private final ColorGenerator mGenerator;

    public CompanyAdapter(Context context, List<String> keyList, Map<String, List<ContactDetailsModel>> companyMap) {
        this.mCompanyDetailsList = keyList;
        this.mContext = context;
        mComapnyMap = companyMap;
        inflater = (LayoutInflater) mContext.
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mGenerator = ColorGenerator.DEFAULT;
    }

    @Override
    public int getCount() {
        return mCompanyDetailsList.size();
    }

    @Override
    public String getItem(int position) {
        return mCompanyDetailsList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @SuppressLint("ViewHolder")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        final Holder holder = new Holder();
        final View view;
        view = inflater.inflate(R.layout.single_company_view, null);
        holder.ivCompanyContactImage = (ImageView) view.findViewById(R.id.ivcompany_contact_image);
        holder.tvCompanyContactName = (TextView) view.findViewById(R.id.tvcompany_contact_name);
        holder.tvCompanyName= (TextView) view.findViewById(R.id.tvcompany_name);
        holder.tvCompanyCount = (TextView) view.findViewById(R.id.tvcompany_count_tv);
        holder.mMainLL = (LinearLayout) view.findViewById(R.id.main_layout);
        holder.view = view;

        String title = mCompanyDetailsList.get(position);
        holder.tvCompanyContactName.setText(title);
        List<ContactDetailsModel> contactDetailsModelList = mComapnyMap.get(title);
        if (contactDetailsModelList != null && contactDetailsModelList.size() > 0) {
            StringBuilder nameBuilder = new StringBuilder();
            for (ContactDetailsModel model : contactDetailsModelList) {
                String name = model.getContactName();
                name = name.substring(0, 1).toUpperCase() + name.substring(1).toLowerCase();
                nameBuilder.append(name + ",");
            }
            String displayName = nameBuilder.toString().substring(0, nameBuilder.length() - 1);
            holder.tvCompanyName.setText(displayName);
            EasyBackUpUtils.v("ConatctCompanyName " +displayName);
            String count = String.valueOf(contactDetailsModelList.size());
            holder.tvCompanyCount.setText(count + " >");
            String imageUrl = contactDetailsModelList.get(0).getContactImage();
            if (imageUrl != null && !imageUrl.equalsIgnoreCase("")) {
                //holder.ivCompanyContactImage.setImageURI(Uri.parse(imageUrl));
                Glide.with(mContext).load(imageUrl).asBitmap().centerCrop().into(new BitmapImageViewTarget(holder.ivCompanyContactImage) {
                    @Override
                    protected void setResource(Bitmap resource) {
                        RoundedBitmapDrawable circularBitmapDrawable =
                                RoundedBitmapDrawableFactory.create(mContext.getResources(), resource);
                        circularBitmapDrawable.setCircular(true);
                        holder.ivCompanyContactImage.setImageDrawable(circularBitmapDrawable);
                    }
                });
            }else {
                TextDrawable.IBuilder mDrawableBuilder = TextDrawable.builder().round();
                TextDrawable drawable = mDrawableBuilder.build(String.valueOf(displayName.charAt(0)), mGenerator.getColor(displayName));
                holder.ivCompanyContactImage.setImageDrawable(drawable);
                holder.view.setBackgroundColor(Color.TRANSPARENT);
            }
        }
        return view;
    }

    //RETURN FILTER OBJ
    private class Holder {
        View view;
        ImageView ivCompanyContactImage;
        TextView tvCompanyContactName,tvCompanyName,tvCompanyCount;
        LinearLayout mMainLL;
    }

}


