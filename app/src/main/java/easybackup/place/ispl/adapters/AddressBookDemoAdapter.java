package easybackup.place.ispl.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
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

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;

import org.zakariya.stickyheaders.SectioningAdapter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import easybackup.place.ispl.MainActivity;
import easybackup.place.ispl.R;
import easybackup.place.ispl.fragments.ContactDetailsViewFragment;
import easybackup.place.ispl.models.ContactDetailsModel;
import easybackup.place.ispl.utils.ColorGenerator;
import easybackup.place.ispl.utils.EasyBackUpUtils;
import easybackup.place.ispl.utils.KeyUtils;
import easybackup.place.ispl.utils.TextDrawable;

/**
 * Adapter for Person items. Sorts them by last name into sections starting with the
 * first letter of the last name.
 */
public class AddressBookDemoAdapter extends SectioningAdapter implements SectionIndexer {

    private Locale locale = Locale.getDefault();
    private static final boolean USE_DEBUG_APPEARANCE = false;
    public List<ContactDetailsModel> mContactDetailList, filterList;
    private Context mContext;
    private boolean mIsVisible = false;
    private ImageView mDeleteIV = null, mMergeIV = null;
    private ArrayList<Integer> mSectionPositions;
    private final ColorGenerator mGenerator;

    public AddressBookDemoAdapter(List<ContactDetailsModel> contactVOList, Context mContext, ImageView deleteIV, ImageView mergeIV) {
        this.mContactDetailList = contactVOList;
        this.mContext = mContext;
        this.mDeleteIV = deleteIV;
        this.mMergeIV = mergeIV;

        sections.clear();

        // sort people into buckets by the first letter of last name
        char alpha = 0;
        Section currentSection = null;
        for (ContactDetailsModel model : contactVOList) {
            if (model.getContactName().charAt(0) != alpha) {
                EasyBackUpUtils.v("ContactNameTest : " + model.getContactName());
                if (currentSection != null) {
                    sections.add(currentSection);
                   // EasyBackUpUtils.v("SectionName : " + model.getContactName());
                }

                currentSection = new Section();
                alpha = model.getContactName().charAt(0);
                currentSection.alpha = String.valueOf(alpha);
            }

            if (currentSection != null) {
                currentSection.mContactDetailList.add(model);
            }
        }

        sections.add(currentSection);
        notifyAllSectionsDataSetChanged();
        mGenerator = ColorGenerator.DEFAULT;
    }

    @Override
    public Object[] getSections() {
        List<String> sections = new ArrayList<>(26);
        mSectionPositions = new ArrayList<>(26);

        for (int i = 0, size = mContactDetailList.size(); i < size; i++) {
            ContactDetailsModel contactDetails = mContactDetailList.get(i);
            String name = contactDetails.getContactName();
            String section = String.valueOf(name.charAt(0)).toUpperCase();
            if (!sections.contains(section)) {
                sections.add(section);
                Collections.sort(sections);
                mSectionPositions.add(i);
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

    private class Section {
        String alpha;
        List<ContactDetailsModel> mContactDetailList = new ArrayList<>();
    }

    private class ItemViewHolder extends SectioningAdapter.ItemViewHolder {
        ImageView ivContactImage;
        TextView tvContactName;
        TextView tvPhoneNumber;
        LinearLayout mMainLL;
        CheckBox checkBox;
        View view;

        ItemViewHolder(View itemView) {
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

    private class HeaderViewHolder extends SectioningAdapter.HeaderViewHolder {
        TextView titleTextView;

        HeaderViewHolder(View itemView) {
            super(itemView);
            titleTextView = (TextView) itemView.findViewById(R.id.titleTextView);
        }
    }

    ArrayList<Section> sections = new ArrayList<>();

    public void updateList(List<ContactDetailsModel> contactList) {
        sections.clear();
        mContactDetailList = contactList;
        char alpha = 0;
        Section currentSection = null;
        for (ContactDetailsModel model : contactList) {
            if (model.getContactName().charAt(0) != alpha) {
                if (currentSection != null) {
                    sections.add(currentSection);
                }

                currentSection = new Section();
                alpha = model.getContactName().charAt(0);
                currentSection.alpha = String.valueOf(alpha);
            }

            if (currentSection != null) {
                currentSection.mContactDetailList.add(model);
            }
        }
        if(currentSection != null) {
            sections.add(currentSection);
            notifyAllSectionsDataSetChanged();
        }else {
            sections.clear();
            notifyAllSectionsDataSetChanged();
        }
    }

    @Override
    public int getNumberOfSections() {
        return sections.size();
    }

    @Override
    public int getNumberOfItemsInSection(int sectionIndex) {
        return sections.get(sectionIndex).mContactDetailList.size();
    }

    @Override
    public boolean doesSectionHaveHeader(int sectionIndex) {
        return true;
    }

    @Override
    public boolean doesSectionHaveFooter(int sectionIndex) {
        return false;
    }

    @Override
    public ItemViewHolder onCreateItemViewHolder(ViewGroup parent, int itemType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View v = inflater.inflate(R.layout.single_contact_view, parent, false);
        return new ItemViewHolder(v);
    }

    @Override
    public HeaderViewHolder onCreateHeaderViewHolder(ViewGroup parent, int headerType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View v = inflater.inflate(R.layout.list_item_addressbook_header, parent, false);
        return new HeaderViewHolder(v);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindItemViewHolder(SectioningAdapter.ItemViewHolder viewHolder, int sectionIndex, int itemIndex, int itemType) {
        Section s = sections.get(sectionIndex);
        final ItemViewHolder ivh = (ItemViewHolder) viewHolder;

        final int pos = itemIndex;

        final ContactDetailsModel contactDetails = s.mContactDetailList.get(pos);
        String contactName = contactDetails.getContactName();
        String contactMobile = contactDetails.getContactMobileNumber();

        if (contactName.isEmpty()) {
            ivh.tvContactName.setText(contactMobile);
        } else {
            ivh.tvContactName.setText(contactName);
        }

        //ivh.tvContactName.setText(contactDetails.name.first);
        ivh.tvPhoneNumber.setText(contactDetails.getContactMobileNumber());
        String imageUrl = contactDetails.getContactImage();
        //EasyBackUpUtils.v("Name : " + contactDetails.getContactName() + "\t Image Url : " + imageUrl);
        if (imageUrl != null && !imageUrl.equalsIgnoreCase("")) {
            // Glide.with(mContext).load(imageUrl).into(ivh.ivContactImage);
            Glide.with(mContext).load(imageUrl).asBitmap().centerCrop().into(new BitmapImageViewTarget(ivh.ivContactImage) {
                @Override
                protected void setResource(Bitmap resource) {
                    RoundedBitmapDrawable circularBitmapDrawable =
                            RoundedBitmapDrawableFactory.create(mContext.getResources(), resource);
                    circularBitmapDrawable.setCircular(true);
                    ivh.ivContactImage.setImageDrawable(circularBitmapDrawable);
                }
            });
        } else {
            //ivh.ivContactImage.setImageResource(R.drawable.contacts);
            /*TextDrawable drawable = TextDrawable.builder().buildRound("A", Color.RED);*/
            TextDrawable.IBuilder mDrawableBuilder = TextDrawable.builder().round();
            TextDrawable drawable = mDrawableBuilder.build(String.valueOf(contactName.charAt(0)), mGenerator.getColor(contactName));
            EasyBackUpUtils.v("ContactNameTest :" + contactName);
            ivh.ivContactImage.setImageDrawable(drawable);
            ivh.view.setBackgroundColor(Color.TRANSPARENT);

        }
        ivh.checkBox.setClickable(false);
        ivh.mMainLL.setTag(contactDetails);
        if (mIsVisible) {
            ivh.checkBox.setVisibility(View.VISIBLE);
            ivh.checkBox.setChecked(mContactDetailList.get(pos).isSelected());
            ivh.checkBox.setTag(mContactDetailList.get(pos));
        } else {
            ivh.checkBox.setVisibility(View.GONE);
        }


        ivh.mMainLL.setTag(contactDetails);
        ivh.mMainLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int visibility = ivh.checkBox.getVisibility();
                if (visibility == View.VISIBLE) {
                    boolean isChecked = ivh.checkBox.isChecked();
                    if (isChecked) {
                        ivh.checkBox.setChecked(false);
                        ContactDetailsModel model = (ContactDetailsModel) v.getTag();
                        model.setSelected(false);
                        mContactDetailList.get(pos).setSelected(false);
                    } else {
                        ivh.checkBox.setChecked(true);
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

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindHeaderViewHolder(SectioningAdapter.HeaderViewHolder viewHolder, int sectionIndex, int headerType) {
        Section s = sections.get(sectionIndex);
        HeaderViewHolder hvh = (HeaderViewHolder) viewHolder;

        if (USE_DEBUG_APPEARANCE) {
            hvh.itemView.setBackgroundColor(0x55ffffff);
            hvh.titleTextView.setText(pad(sectionIndex * 2) + s.alpha);
        } else {
            hvh.titleTextView.setText(s.alpha);
        }
    }

    // method to access in activity after updating selection
    public List<ContactDetailsModel> getContactDetailList() {
        return mContactDetailList;
    }

    public void setCheckboxVisibility(boolean isVisible) {
        mIsVisible = isVisible;
        //notifyDataSetChanged();
        notifyAllSectionsDataSetChanged();
    }

    private String pad(int spaces) {
        StringBuilder b = new StringBuilder();
        for (int i = 0; i < spaces; i++) {
            b.append(' ');
        }
        return b.toString();
    }

    public void disableCheckBox() {
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
