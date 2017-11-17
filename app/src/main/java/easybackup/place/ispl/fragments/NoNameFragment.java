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

public class NoNameFragment extends Fragment {
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View layout_view = inflater.inflate(R.layout.listview_no_name_details, container, false);

        List<ContactDetailsModel> noNameContactList = new ArrayList<>();
        List<ContactDetailsModel> contactList = EasyBackUpUtils.getContactDetailsList();

        if (contactList != null) {
            for (ContactDetailsModel model : contactList) {
                String name = model.getContactName().trim();
                String mobileNumber = model.getContactMobileNumber();
                String homeNumber = model.getContactHomeNumber();
                String workNumber = model.getContactWorkNumber();
                if (name.equalsIgnoreCase("") ||  name.equalsIgnoreCase(mobileNumber) || name.equalsIgnoreCase(homeNumber) || name.equalsIgnoreCase(workNumber)) {
                    EasyBackUpUtils.v("Name : " + name + " -> " + model.getContactName());
                    noNameContactList.add(model);
                }
            }
        }

        if(noNameContactList.size() > 0) {
            EasyBackUpUtils.setSelectedContactDetailsList(noNameContactList);
            Fragment noNameFragment = new AllContactFragment();
            ((MainActivity)getActivity()).switchFragment(noNameFragment, true, KeyUtils.ALL_CONTACT_FRAGMENT_TAG);
        }

        return layout_view;
    }
}
