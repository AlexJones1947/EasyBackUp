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
import java.util.List;
import java.util.Map;

import easybackup.place.ispl.MainActivity;
import easybackup.place.ispl.R;
import easybackup.place.ispl.adapters.BirthdayAdapter;
import easybackup.place.ispl.models.ContactDetailsModel;
import easybackup.place.ispl.utils.EasyBackUpUtils;
import easybackup.place.ispl.utils.KeyUtils;

/**
 * Created by infinium on 4/14/17.
 */

public class BirthdayDetailFragment extends Fragment {
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View layout_view = inflater.inflate(R.layout.listview_birthdate_details, container, false);
        ListView birthDateLV = (ListView) layout_view.findViewById(R.id.birthdate_list);
        final Map<String, List<ContactDetailsModel>> contactMap = new HashMap<>();
        List<ContactDetailsModel> contactDetailsModelList = EasyBackUpUtils.getBirthDayContactList();
        if(contactDetailsModelList != null) {
            for(ContactDetailsModel model : contactDetailsModelList) {
                String key = model.getContactBirthdate();
                if(key.equalsIgnoreCase("")) continue;
                if(contactMap.containsKey(key)) {
                    List<ContactDetailsModel> modelList = contactMap.get(key);
                    modelList.add(model);
                } else {
                    List<ContactDetailsModel> modelList = new ArrayList<>();
                    modelList.add(model);
                    contactMap.put(key, modelList);
                }
            }
        }

        List<String> keyList = new ArrayList<>();
        for(String key : contactMap.keySet()) {
            keyList.add(key);
            Collections.sort(keyList);
            EasyBackUpUtils.v("CheckDate :" +keyList.toString());
        }
      /*  String count = String.valueOf(keyList.size());
        EasyBackUpUtils.setBirthdateCount(count + " >");*/

        final BirthdayAdapter birthdateAdapter = new BirthdayAdapter(getActivity(), keyList, contactMap);
        EasyBackUpUtils.v("BirthdayList Count:" +birthdateAdapter.getCount());
        EasyBackUpUtils.setBirthdateCount(birthdateAdapter.getCount() + " >");
        birthDateLV.setAdapter(birthdateAdapter);

        birthDateLV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String key = birthdateAdapter.getItem(position);
                List<ContactDetailsModel> contactList = contactMap.get(key);
                // Before Called All Contact Fragment You have to have set SetSelectedContactDetailList.
                EasyBackUpUtils.setSelectedContactDetailsList(contactList);
                Fragment birthdateFragment = new AllContactFragment();
                ((MainActivity)getActivity()).switchFragment(birthdateFragment, true, KeyUtils.ALL_CONTACT_FRAGMENT_TAG);
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
