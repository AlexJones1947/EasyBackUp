package easybackup.place.ispl.fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.LinearLayout;
import android.widget.TextView;

import easybackup.place.ispl.MainActivity;
import easybackup.place.ispl.R;
import easybackup.place.ispl.utils.EasyBackUpUtils;
import easybackup.place.ispl.utils.KeyUtils;

/**
 * Created by infinium on 4/3/17.
 */

public class ContactsFragment extends Fragment {
    private TextView mAllContactsCountTV, mBirthdateContactsCountTV;
    private LinearLayout mAllContactsLayouts, mBirthdateLayout, mCompanyLayout, mNoNameLayout, mDuplicateEmailsLayout, mDuplicatePhonesLayout,
            mNoPhoneLayout, mJobTitleLayout, mNoPhoneAndEmailLayout, mSimilarNameLayout, mDuplicateContactsLayout, mRecentyAddedLayout;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View layout_view = inflater.inflate(R.layout.fragment_contacts, container, false);


        // mGeneralTV = (TextView) layout_view.findViewById(R.id.general_tv);
        // mAllContactsTV = (TextView) layout_view.findViewById(R.id.all_contacts_tv);
        // mContactsSmallImage = (ImageView) layout_view.findViewById(R.id.contacts_small_image);
        // mDuplicateContactsCountTV = (TextView) layout_view.findViewById(R.id.duplicate_contacts_count_tv);
        // mCompanyContactsCountTV = (TextView) layout_view.findViewById(R.id.company_contacts_count_tv);
        //mJobTitleContactsCountTV = (TextView) layout_view.findViewById(R.id.jobtitle_contacts_count_tv);
        mBirthdateContactsCountTV = (TextView) layout_view.findViewById(R.id.birthdate_contacts_count_tv);
        mNoPhoneAndEmailLayout = (LinearLayout) layout_view.findViewById(R.id.no_phone_email_layout);
        mAllContactsLayouts = (LinearLayout) layout_view.findViewById(R.id.all_contacts_layout);
        mAllContactsCountTV = (TextView) layout_view.findViewById(R.id.all_contacts_count_tv);
        mSimilarNameLayout = (LinearLayout) layout_view.findViewById(R.id.similar_name_layout);
        mBirthdateLayout = (LinearLayout) layout_view.findViewById(R.id.birthday_layout);
        mCompanyLayout = (LinearLayout) layout_view.findViewById(R.id.comany_layout);
        mJobTitleLayout = (LinearLayout) layout_view.findViewById(R.id.jobtitle_layout);
        mNoNameLayout = (LinearLayout) layout_view.findViewById(R.id.no_name_layout);
        mNoPhoneLayout = (LinearLayout) layout_view.findViewById(R.id.no_phone_layout);
        mDuplicateContactsLayout = (LinearLayout) layout_view.findViewById(R.id.duplicate_contacts_layout);
        mDuplicateEmailsLayout = (LinearLayout) layout_view.findViewById(R.id.duplicate_emails_layout);
        mDuplicatePhonesLayout = (LinearLayout) layout_view.findViewById(R.id.duplicate_phones_layout);
        mRecentyAddedLayout = (LinearLayout) layout_view.findViewById(R.id.recently_added_layout);
        mAllContactsCountTV.setText("" + EasyBackUpUtils.getContactDetailsList().size() + " >");
        /*mJobTitleContactsCountTV.setText("" + EasyBackUpUtils.getJobTitleDetailsList().size() + " >");
        mBirthdateContactsCountTV.setText("" + EasyBackUpUtils.getBirthDayContactList().size() + " >" );*/

//------------------------------------------------------------------------------------------------------------------------------------------------------------------

        mBirthdateContactsCountTV.setText(EasyBackUpUtils.getBithdateCount());

//------------------------------------------------------------------------------------------------------------------------------------------------------------------

        mAllContactsLayouts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (EasyBackUpUtils.getContactDetailsList().size() > 0) {
                    Animation animation1 = new AlphaAnimation(0.3f, 2.0f);
                    animation1.setDuration(2500);
                    animation1.setBackgroundColor(Color.RED);
                    mAllContactsLayouts.startAnimation(animation1);
                    EasyBackUpUtils.setSelectedContactDetailsList(EasyBackUpUtils.getContactDetailsList());
                    Fragment allConatctsFragment = new AllContactFragment();
                    ((MainActivity) getActivity()).switchFragment(allConatctsFragment, true, KeyUtils.ALL_CONTACT_FRAGMENT_TAG);
                }
            }
        });
        mDuplicateContactsLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Start an alpha animation for clicked item
                Animation animation1 = new AlphaAnimation(0.3f, 2.0f);
                animation1.setDuration(2500);
                animation1.setBackgroundColor(Color.RED);
                mDuplicateContactsLayout.startAnimation(animation1);
                ((MainActivity) getActivity()).setTitleBackupsText("Duplicate Contacts");
                Fragment similarNameFragment = new DuplicateContactsFragment();
                ((MainActivity) getActivity()).switchFragment(similarNameFragment, true, KeyUtils.DUBLICATE_CONTACTS_FRAGMENT_TAG);
            }
        });
        mDuplicatePhonesLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Start an alpha animation for clicked item
                Animation animation1 = new AlphaAnimation(0.3f, 2.0f);
                animation1.setDuration(2500);
                animation1.setBackgroundColor(Color.RED);
                mDuplicatePhonesLayout.startAnimation(animation1);
                ((MainActivity) getActivity()).setTitleBackupsText("Duplicate Phones");
                Fragment duplicatePhonesFragment = new DuplicatePhonesFragment();
                ((MainActivity) getActivity()).switchFragment(duplicatePhonesFragment, true, KeyUtils.DUBLICATE_PHONES_FRAGMENT_TAG);
            }
        });
        mDuplicateEmailsLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Start an alpha animation for clicked item
                Animation animation1 = new AlphaAnimation(0.3f, 2.0f);
                animation1.setDuration(2500);
                animation1.setBackgroundColor(Color.RED);
                mDuplicateEmailsLayout.startAnimation(animation1);
                ((MainActivity) getActivity()).setTitleBackupsText("Duplicate Emails");
                Fragment similarNameFragment = new DuplicateEmailsFragment();
                ((MainActivity) getActivity()).switchFragment(similarNameFragment, true, KeyUtils.DUBLICATE_EMIALS_FRAGMENT_TAG);
            }
        });
        mSimilarNameLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Start an alpha animation for clicked item
                Animation animation1 = new AlphaAnimation(0.3f, 2.0f);
                animation1.setDuration(2500);
                animation1.setBackgroundColor(Color.RED);
                mSimilarNameLayout.startAnimation(animation1);
                ((MainActivity) getActivity()).setTitleBackupsText("Similar Names");
                Fragment similarNameFragment = new SimilarNameFragment();
                ((MainActivity) getActivity()).switchFragment(similarNameFragment, true, KeyUtils.SIMILAR_NAME_FRAGMENT_TAG);
            }
        });
        mNoNameLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Start an alpha animation for clicked item
                Animation animation1 = new AlphaAnimation(0.3f, 2.0f);
                animation1.setDuration(2500);
                animation1.setBackgroundColor(Color.RED);
                mNoNameLayout.startAnimation(animation1);
                ((MainActivity) getActivity()).setTitleBackupsText("No Name");
                Fragment noNameFragment = new NoNameFragment();
                ((MainActivity) getActivity()).switchFragment(noNameFragment, true, KeyUtils.NO_NAME_FRAGMENT_TAG);
            }
        });
        mNoPhoneLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Start an alpha animation for clicked item
                Animation animation1 = new AlphaAnimation(0.3f, 2.0f);
                animation1.setDuration(2500);
                animation1.setBackgroundColor(Color.RED);
                mNoPhoneLayout.startAnimation(animation1);
                ((MainActivity) getActivity()).setTitleBackupsText("No Phone");
                Fragment noPhoneFragment = new NoPhoneFragment();
                ((MainActivity) getActivity()).switchFragment(noPhoneFragment, true, KeyUtils.NO_PHONE_FRAGMENT_TAG);
            }
        });
        mNoPhoneAndEmailLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Start an alpha animation for clicked item
                Animation animation1 = new AlphaAnimation(0.3f, 2.0f);
                animation1.setDuration(2500);
                animation1.setBackgroundColor(Color.RED);
                mNoPhoneAndEmailLayout.startAnimation(animation1);
                ((MainActivity) getActivity()).setTitleBackupsText("No Phone & Email");
                Fragment jobTitleFragment = new NoPhoneAndEmailFragment();
                ((MainActivity) getActivity()).switchFragment(jobTitleFragment, true, KeyUtils.NO_PHONE_AND_EMAIL_FRAGMENT_TAG);
            }
        });
        mRecentyAddedLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Animation animation1 = new AlphaAnimation(0.3f, 2.0f);
                animation1.setDuration(2500);
                animation1.setBackgroundColor(Color.RED);
                mRecentyAddedLayout.startAnimation(animation1);
                ((MainActivity) getActivity()).setTitleBackupsText("Recently Added");
                Fragment recentlyAddedFragment = new RecentlyAddedFragment();
                ((MainActivity) getActivity()).switchFragment(recentlyAddedFragment, true, KeyUtils.DUBLICATE_PHONES_FRAGMENT_TAG);
            }
        });
        mBirthdateLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Start an alpha animation for clicked item
                Animation animation1 = new AlphaAnimation(0.3f, 2.0f);
                animation1.setDuration(2500);
                animation1.setBackgroundColor(Color.RED);
                mBirthdateLayout.startAnimation(animation1);
                ((MainActivity) getActivity()).setTitleBackupsText("Birthdate");
                Fragment birthdayFragment = new BirthdayDetailFragment();
                ((MainActivity) getActivity()).switchFragment(birthdayFragment, true, KeyUtils.BIRTHDATE_FRAGMENT_TAG);
            }
        });
        mCompanyLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Start an alpha animation for clicked item
                Animation animation1 = new AlphaAnimation(0.3f, 2.0f);
                animation1.setDuration(2500);
                animation1.setBackgroundColor(Color.RED);
                mCompanyLayout.startAnimation(animation1);
                ((MainActivity) getActivity()).setTitleBackupsText("Company");
                Fragment companyFragment = new CompanyDetailFragment();
                ((MainActivity) getActivity()).switchFragment(companyFragment, true, KeyUtils.COMPANY_DETAIL_FRAGMENT_TAG);
            }
        });
        mJobTitleLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Start an alpha animation for clicked item
                Animation animation1 = new AlphaAnimation(0.3f, 2.0f);
                animation1.setDuration(2500);
                animation1.setBackgroundColor(Color.RED);
                mJobTitleLayout.startAnimation(animation1);
                ((MainActivity) getActivity()).setTitleBackupsText("Job Title");
                Fragment jobTitleFragment = new JobTitleFragment();
                ((MainActivity) getActivity()).switchFragment(jobTitleFragment, true, KeyUtils.JOB_TITLE_FRAGMENT_TAG);
            }
        });
        return layout_view;
    }


    @Override
    public void onResume() {
        ((MainActivity) getActivity()).setToolBarVisibility(true);
        ((MainActivity) getActivity()).setMainNavigationVisibility(true);
        ((MainActivity) getActivity()).setEditBtnVisibility(false);
        ((MainActivity) getActivity()).setCloseBtnVisibility(false);
        ((MainActivity) getActivity()).setAddBtnVisibility(true);
        ((MainActivity) getActivity()).setTitleBackupsText("Contacts");
        super.onResume();
    }
}
