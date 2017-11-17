package easybackup.place.ispl.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

import easybackup.place.ispl.MainActivity;
import easybackup.place.ispl.R;
import easybackup.place.ispl.fragments.ContactDetailsViewFragment;
import easybackup.place.ispl.models.ContactDetailsModel;
import easybackup.place.ispl.utils.EasyBackUpUtils;
import easybackup.place.ispl.utils.KeyUtils;

/**
 * Created by infinium on 4/3/17.
 */

public class AllContactsAdapter extends RecyclerView.Adapter<AllContactsAdapter.ContactViewHolder> {
    private List<ContactDetailsModel> mContactDetailList;
    private Context mContext;
    private boolean mIsVisible = false;
    private ImageView mDeleteIV = null, mMergeIV = null;

    public AllContactsAdapter(List<ContactDetailsModel> contactVOList, Context mContext, ImageView deleteIV, ImageView mergeIV) {
        this.mContactDetailList = contactVOList;
        this.mContext = mContext;
        this.mDeleteIV = deleteIV;
        this.mMergeIV = mergeIV;
    }

    @Override
    public ContactViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        @SuppressLint("InflateParams") View view = LayoutInflater.from(mContext).inflate(R.layout.single_contact_view, null);
        ContactViewHolder contactViewHolder = new ContactViewHolder(view);

        return contactViewHolder;
    }

    public void updateList(List<ContactDetailsModel> contactList) {
        this.mContactDetailList = contactList;
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(final ContactViewHolder holder, int position) {
        final int pos = position;
        final ContactDetailsModel contactDetails = mContactDetailList.get(pos);
        holder.tvContactName.setText(contactDetails.getContactName());
        holder.tvPhoneNumber.setText(contactDetails.getContactMobileNumber());
        String imageUrl = contactDetails.getContactImage();
        EasyBackUpUtils.v("Name : " + contactDetails.getContactName() + "\t Image Url : " + imageUrl);
        if (imageUrl != null && !imageUrl.equalsIgnoreCase("")) {
            Glide.with(mContext).load(imageUrl).into(holder.ivContactImage);
        } else {
            holder.ivContactImage.setImageResource(R.drawable.contacts);
        }
        holder.checkBox.setClickable(false);
        holder.mMainLL.setTag(contactDetails);
        if (mIsVisible) {
            holder.checkBox.setVisibility(View.VISIBLE);
            holder.checkBox.setChecked(mContactDetailList.get(pos).isSelected());
            holder.checkBox.setTag(mContactDetailList.get(pos));
        } else {
            holder.checkBox.setVisibility(View.GONE);
        }


        holder.mMainLL.setTag(contactDetails);
        holder.mMainLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int visibility = holder.checkBox.getVisibility();
                if (visibility == View.VISIBLE) {
                    boolean isChecked = holder.checkBox.isChecked();
                    if (isChecked) {
                        holder.checkBox.setChecked(false);
                        ContactDetailsModel model = (ContactDetailsModel) v.getTag();
                        model.setSelected(false);
                        mContactDetailList.get(pos).setSelected(false);
                    } else {
                        holder.checkBox.setChecked(true);
                        ContactDetailsModel model = (ContactDetailsModel) v.getTag();
                        model.setSelected(true);
                        mContactDetailList.get(pos).setSelected(true);
                    }
                    int seletedItem = getCheckedItemListCount();
                    if (seletedItem == 1) {
                        mDeleteIV.setColorFilter(Color.WHITE);
                        mMergeIV.setColorFilter(Color.TRANSPARENT);
                        mDeleteIV.setClickable(true);
                        mMergeIV.setClickable(false);
                    } else if (seletedItem > 1) {
                        mDeleteIV.setColorFilter(Color.WHITE);
                        mMergeIV.setColorFilter(Color.WHITE);
                        mDeleteIV.setClickable(true);
                        mMergeIV.setClickable(true);
                    } else {
                        mDeleteIV.setColorFilter(Color.TRANSPARENT);
                        mMergeIV.setColorFilter(Color.TRANSPARENT);
                        mDeleteIV.setClickable(false);
                        mMergeIV.setClickable(false);
                    }
                } else {
                    ContactDetailsModel details = (ContactDetailsModel) v.getTag();
                    if (details != null) {
                        EasyBackUpUtils.setSelectedContactDetails(details);
                        EasyBackUpUtils.v("Selected Name : " + details.getContactName());
                        // Start an alpha animation for clicked item
                        Animation animation1 = new AlphaAnimation(0.3f, 2.0f);
                        animation1.setDuration(2500);
                        v.startAnimation(animation1);

                        Fragment contactviewContactFragment = new ContactDetailsViewFragment();
                        ((MainActivity) mContext).switchFragment(contactviewContactFragment, true, KeyUtils.CONTACT_VIEW_FRAGMENT_TAG);
                    }
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mContactDetailList.size();
    }

    public void setCheckboxVisibility(boolean isVisible) {
        mIsVisible = isVisible;
        notifyDataSetChanged();
    }

    public int getCheckedItemListCount() {
        int counter = 0;
        for (int i = 0; i < mContactDetailList.size(); i++) {
            boolean isChecked = mContactDetailList.get(i).isSelected();
            if (isChecked) {
                counter++;
                if (counter > 1) {
                    break;
                }
            }
        }
        return counter;
    }

    public static class ContactViewHolder extends RecyclerView.ViewHolder {

        ImageView ivContactImage;
        TextView tvContactName;
        TextView tvPhoneNumber;
        LinearLayout mMainLL;
        CheckBox checkBox;

        public ContactViewHolder(View itemView) {
            super(itemView);
            ivContactImage = (ImageView) itemView.findViewById(R.id.ivContactImage);
            tvContactName = (TextView) itemView.findViewById(R.id.tvContactName);
            tvPhoneNumber = (TextView) itemView.findViewById(R.id.tvPhoneNumber);
            mMainLL = (LinearLayout) itemView.findViewById(R.id.main_layout);
            checkBox = (CheckBox) itemView.findViewById(R.id.contact_checkbox);
            checkBox.setVisibility(View.GONE);
        }
    }

    // method to access in activity after updating selection
    public List<ContactDetailsModel> getContactDetailList() {
        return mContactDetailList;
    }

}
