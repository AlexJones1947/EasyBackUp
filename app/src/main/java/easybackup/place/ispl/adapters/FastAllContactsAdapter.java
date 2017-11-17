package easybackup.place.ispl.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SectionIndexer;
import android.widget.TextView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;

import java.util.ArrayList;
import java.util.Collections;
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

public class FastAllContactsAdapter extends RecyclerView.Adapter<FastAllContactsAdapter.ContactViewHolder> implements SectionIndexer {
    private List<ContactDetailsModel> mContactDetailList = null;
    private Context mContext;
    private boolean mIsVisible = false;
    private ArrayList<Integer> mSectionPositions = null;
    private ImageView mDeleteIV = null, mMergeIV = null;
    private final ColorGenerator mGenerator;
    private View mView = null;
    public FastAllContactsAdapter(List<ContactDetailsModel> contactVOList, Context mContext, ImageView deleteIV, ImageView mergeIV) {
        this.mContactDetailList = contactVOList;
        this.mContext = mContext;
        this.mDeleteIV = deleteIV;
        this.mMergeIV = mergeIV;
        mGenerator = ColorGenerator.DEFAULT;
    }

    @Override
    public ContactViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.single_contact_view, null);
        mView = view;
        return new ContactViewHolder(view);
    }

    public void updateList(List<ContactDetailsModel> contactList) {
        this.mContactDetailList = contactList;
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(final ContactViewHolder holder, int position) {
        final int pos = position;
        final ContactDetailsModel contactDetails = mContactDetailList.get(pos);

        StringBuilder nameBuilder = new StringBuilder();
        String name = contactDetails.getContactName();
        name = name.substring(0, 1).toUpperCase() + name.substring(1).toLowerCase();
        nameBuilder.append(name + ",");

        String displayName = nameBuilder.toString().substring(0, nameBuilder.length() - 1);

        holder.tvContactName.setText(displayName);
        holder.tvPhoneNumber.setText(contactDetails.getContactMobileNumber());
        String imageUrl = contactDetails.getContactImage();
        EasyBackUpUtils.v("Name : " + contactDetails.getContactName() + "\t Image Url : " + imageUrl);
        if (imageUrl != null && !imageUrl.equalsIgnoreCase("")) {
            // Glide.with(mContext).load(imageUrl).into(ivh.ivContactImage);
            Glide.with(mContext).load(imageUrl).asBitmap().centerCrop().into(new BitmapImageViewTarget(holder.ivContactImage) {
                @Override
                protected void setResource(Bitmap resource) {
                    RoundedBitmapDrawable circularBitmapDrawable =
                            RoundedBitmapDrawableFactory.create(mContext.getResources(), resource);
                    circularBitmapDrawable.setCircular(true);
                    holder.ivContactImage.setImageDrawable(circularBitmapDrawable);
                }
            });
        } else {
            //ivh.ivContactImage.setImageResource(R.drawable.contacts);
            /*TextDrawable drawable = TextDrawable.builder().buildRound("A", Color.RED);*/
            TextDrawable.IBuilder mDrawableBuilder = TextDrawable.builder().round();
            TextDrawable drawable = mDrawableBuilder.build(String.valueOf(displayName.charAt(0)), mGenerator.getColor(displayName));
            holder.ivContactImage.setImageDrawable(drawable);
            holder.view.setBackgroundColor(Color.TRANSPARENT);
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
                    int selectedItem = getCheckedItemListCount();
                    if (selectedItem == 1) {
                        mDeleteIV.setColorFilter(Color.WHITE);
                        mMergeIV.setColorFilter(Color.TRANSPARENT);
                        mDeleteIV.setClickable(true);
                        mMergeIV.setClickable(false);
                    } else if (selectedItem > 1) {
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
        ContactDetailsModel model = (ContactDetailsModel) mView.getTag();
        model.setCheckBoxSelected(holder.checkBox);
        EasyBackUpUtils.v("CheckBoxTest1 :" + holder.checkBox);
    }

    @Override
    public int getItemCount() {
        return mContactDetailList.size();
    }

    public void setCheckboxVisibility(boolean isVisible) {
        mIsVisible = isVisible;
        notifyDataSetChanged();
    }

    private int getCheckedItemListCount() {
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

    @Override
    public Object[] getSections() {
        List<String> sections = new ArrayList<>(26);
        mSectionPositions = new ArrayList<>(26);
        for (int i = 0, size = mContactDetailList.size(); i < size; i++) {
            ContactDetailsModel contactDetails = mContactDetailList.get(i);
            String name = contactDetails.getContactName();
                if(name.equalsIgnoreCase("")){
                    String section = contactDetails.getContactMobileNumber();
                    if (!sections.contains(section)) {
                        sections.add(section);
                        Collections.sort(sections);
                        mSectionPositions.add(i);
                    }
                    EasyBackUpUtils.v("SectionCheck Error ....");
                }else {
                    String section = String.valueOf(name.charAt(0)).toUpperCase();
                    EasyBackUpUtils.v("SectionCheck1 :" +section);

                    if (!sections.contains(section)) {
                        sections.add(section);
                        Collections.sort(sections);
                        mSectionPositions.add(i);
                    }
                }


        }
        return sections.toArray(new String[0]);
    }

    @Override
    public int getPositionForSection(int sectionIndex) {
        return mSectionPositions.get(sectionIndex);
    }

    @Override
    public int getSectionForPosition(int position) {
        return 0;
    }

    static class ContactViewHolder extends RecyclerView.ViewHolder {

        ImageView ivContactImage;
        TextView tvContactName;
        TextView tvPhoneNumber;
        LinearLayout mMainLL;
        CheckBox checkBox;
        View view;


        ContactViewHolder(View itemView) {
            super(itemView);
            this.view = itemView;
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

    public void disableCheckBox() {
        /*ContactDetailsModel model = (ContactDetailsModel) mView.getTag();
        EasyBackUpUtils.v("CheckBoxTest :" + model + "\n" + model.getCheckBoxSelected());
        CheckBox c = model.getCheckBoxSelected();
        c.setChecked(false);*/

        mDeleteIV.setColorFilter(Color.TRANSPARENT);
        mMergeIV.setColorFilter(Color.TRANSPARENT);
        mDeleteIV.setClickable(false);
        mMergeIV.setClickable(false);

    }

    public void disableColor() {
        mDeleteIV.setColorFilter(Color.TRANSPARENT);
        mMergeIV.setColorFilter(Color.TRANSPARENT);
    }
}
