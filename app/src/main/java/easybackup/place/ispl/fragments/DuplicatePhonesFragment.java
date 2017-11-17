package easybackup.place.ispl.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import easybackup.place.ispl.MainActivity;
import easybackup.place.ispl.R;
import easybackup.place.ispl.adapters.DuplicatePhonesAdapter;
import easybackup.place.ispl.models.ContactDetailsModel;
import easybackup.place.ispl.utils.EasyBackUpUtils;
import easybackup.place.ispl.utils.KeyUtils;

/**
 * Created by infinium on 4/14/17.
 */

public class DuplicatePhonesFragment extends Fragment {
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View layout_view = inflater.inflate(R.layout.listview_duplicate_phones_details, container, false);
        ListView duplicatePhoneLV = (ListView) layout_view.findViewById(R.id.duplicates_phones_list);
        final Map<String, List<ContactDetailsModel>> contactMap = new HashMap<>();
        List<ContactDetailsModel> contactDetailsModelList = EasyBackUpUtils.getContactDetailsList();


        if (contactDetailsModelList != null) {

            for (ContactDetailsModel model : contactDetailsModelList) {
                String key = "";
                String mobile = model.getContactMobileNumber();
                String home = model.getContactHomeNumber();
                String work = model.getContactWorkNumber();

                Set<String> strings = new HashSet<String>();
                if (mobile != null && home != null && work != null) {
                    strings.add(mobile);
                    strings.add(home);
                    strings.add(work);

                    if (strings.contains(mobile.toLowerCase())) {
                        key = mobile;
                        if (! key.equalsIgnoreCase("")){
                            if (contactMap.containsKey(key)) {
                                List<ContactDetailsModel> modelList = contactMap.get(key);
                                modelList.add(model);
                            } else {
                                List<ContactDetailsModel> modelList = new ArrayList<>();
                                modelList.add(model);
                                contactMap.put(key, modelList);
                            }
                        }
                    }
                    if (strings.contains(home.toLowerCase())) {
                        key = home;
                        if (! key.equalsIgnoreCase("")) {
                            if (contactMap.containsKey(key)) {
                                List<ContactDetailsModel> modelList = contactMap.get(key);
                                modelList.add(model);
                            } else {
                                List<ContactDetailsModel> modelList = new ArrayList<>();
                                modelList.add(model);
                                contactMap.put(key, modelList);
                            }
                        }
                    }
                    if (strings.contains(work.toLowerCase())) {
                        key = work;
                        if (! key.equalsIgnoreCase("")) {
                            if (contactMap.containsKey(key)) {
                                List<ContactDetailsModel> modelList = contactMap.get(key);
                                modelList.add(model);
                            } else {
                                List<ContactDetailsModel> modelList = new ArrayList<>();
                                modelList.add(model);
                                contactMap.put(key, modelList);
                            }
                        }
                    }
                }
            }
        }

        List<String> keyList = new ArrayList<>();
        for (String key : contactMap.keySet()) {

            List<ContactDetailsModel> list = contactMap.get(key);
            if (list.size() > 1)
                keyList.add(key);

            Collections.sort(keyList);
        }

        final DuplicatePhonesAdapter dublicateContactsAdapter = new DuplicatePhonesAdapter(getActivity(), keyList, contactMap);
        duplicatePhoneLV.setAdapter(dublicateContactsAdapter);



        duplicatePhoneLV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String key = dublicateContactsAdapter.getItem(position);
                EasyBackUpUtils.v("Selected Item Key : " + key);
                List<ContactDetailsModel> contactList = contactMap.get(key);

                // Before Called All Contact Fragment You have to have set SetSelectedContactDetailList.
                EasyBackUpUtils.setSelectedContactDetailsList(contactList);
                Fragment duplicatePhonesFragment = new AllContactFragment();
                ((MainActivity) getActivity()).switchFragment(duplicatePhonesFragment, true, KeyUtils.ALL_CONTACT_FRAGMENT_TAG);
            }
        });
        return layout_view;

    }
    @Override
    public void onResume() {
        ((MainActivity) getActivity()).setAddBtnVisibility(false);

        super.onResume();
    }
}
