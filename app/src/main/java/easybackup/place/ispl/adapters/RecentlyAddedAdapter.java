package easybackup.place.ispl.adapters;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;
import java.util.Map;

import easybackup.place.ispl.R;
import easybackup.place.ispl.models.ContactDetailsModel;

/**
 * Created by infinium on 4/3/17.
 */

public class RecentlyAddedAdapter extends BaseAdapter {

    private List<String> mRecentlyAddedDetailsList;
    private Map<String, List<ContactDetailsModel>> mRecentlyAddedDateMap = null;
    private static LayoutInflater inflater = null;

    public RecentlyAddedAdapter(Activity activity, List<String> keyList, Map<String, List<ContactDetailsModel>> recentlyAddedMap) {
        this.mRecentlyAddedDetailsList = keyList;
        mRecentlyAddedDateMap = recentlyAddedMap;
        inflater = (LayoutInflater) activity.
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return mRecentlyAddedDetailsList.size();
    }

    @Override
    public String getItem(int position) {
        return mRecentlyAddedDetailsList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        Holder holder = new Holder();
        final View view;
        view = inflater.inflate(R.layout.single_recently_added_recently_added_view, null);
        holder.mContactIV = (ImageView) view.findViewById(R.id.ivrecenty_added_contact_image);
        holder.mContactNameTV = (TextView) view.findViewById(R.id.tvrecenty_added_contact_name);
        holder.mContactMobileNumberTV = (TextView) view.findViewById(R.id.tvrecenty_added_contact_title);
        holder.mContactCountTV = (TextView) view.findViewById(R.id.count_recenty_added_tv);
        holder.mMainLL = (LinearLayout) view.findViewById(R.id.main_layout);

        String title = mRecentlyAddedDetailsList.get(position);
        holder.mContactMobileNumberTV.setText(title);

        List<ContactDetailsModel> contactDetailsModelList = mRecentlyAddedDateMap.get(title);

        if (contactDetailsModelList != null && contactDetailsModelList.size() > 0) {
            StringBuilder phoneBuilder = new StringBuilder();
            for (ContactDetailsModel model : contactDetailsModelList) {
                String mobile = model.getContactMobileNumber();
                phoneBuilder.append(mobile + ",");
            }
            String displayName = phoneBuilder.toString().substring(0, phoneBuilder.length() - 1);
            holder.mContactNameTV.setText(displayName);
            String count = String.valueOf(contactDetailsModelList.size());
            holder.mContactCountTV.setText(count + " >");
            String imageUrl = contactDetailsModelList.get(0).getContactImage();
            if (imageUrl != null && !imageUrl.equalsIgnoreCase("")) {
                holder.mContactIV.setImageURI(Uri.parse(imageUrl));
            }
        }

        return view;
    }

    //RETURN FILTER OBJ
    private class Holder {
        ImageView mContactIV;
        TextView mContactNameTV, mContactMobileNumberTV, mContactCountTV;
        LinearLayout mMainLL;
    }
}


