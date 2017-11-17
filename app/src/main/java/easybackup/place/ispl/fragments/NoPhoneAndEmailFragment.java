package easybackup.place.ispl.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import easybackup.place.ispl.MainActivity;
import easybackup.place.ispl.R;
import easybackup.place.ispl.models.ContactDetailsModel;
import easybackup.place.ispl.utils.EasyBackUpUtils;
import easybackup.place.ispl.utils.KeyUtils;

/**
 * Created by infinium on 4/14/17.
 */

public class NoPhoneAndEmailFragment extends Fragment {
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View layout_view = inflater.inflate(R.layout.listview_no_phone_and_email_details, container, false);
        List<ContactDetailsModel> noPhoneAndEmailContactList = new ArrayList<>();
        List<ContactDetailsModel> contactList = EasyBackUpUtils.getContactDetailsList();

        if (contactList != null) {
            for (ContactDetailsModel model : contactList) {
                String mobileNumber = model.getContactMobileNumber();
                String homeNumber = model.getContactHomeNumber();
                String workNumber = model.getContactWorkNumber();
                String name = model.getContactName();
                String emailHome =  model.getContactEmailHome();
                String emailWork =  model.getContactEmailHome();

                if(mobileNumber.equalsIgnoreCase("") &&  homeNumber.equalsIgnoreCase("") && workNumber.equalsIgnoreCase("") && emailHome.equalsIgnoreCase("") && emailWork.equalsIgnoreCase("")) {
                    EasyBackUpUtils.v("Name : " + name);
                    noPhoneAndEmailContactList.add(model);
                }
            }
        }

        if(noPhoneAndEmailContactList.size() > 0) {
            EasyBackUpUtils.setSelectedContactDetailsList(noPhoneAndEmailContactList);
            Fragment noPhoneContactFragment = new AllContactFragment();
            ((MainActivity)getActivity()).switchFragment(noPhoneContactFragment, false, KeyUtils.ALL_CONTACT_FRAGMENT_TAG);
        }
        return layout_view;
    }
}
