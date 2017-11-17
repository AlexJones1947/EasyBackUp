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
import easybackup.place.ispl.adapters.CompanyAdapter;
import easybackup.place.ispl.models.ContactDetailsModel;
import easybackup.place.ispl.utils.EasyBackUpUtils;
import easybackup.place.ispl.utils.KeyUtils;

/**
 * Created by infinium on 4/14/17.
 */

public class CompanyDetailFragment extends Fragment {
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View layout_view = inflater.inflate(R.layout.listview_company_details, container, false);
        ListView companyLV= (ListView) layout_view.findViewById(R.id.company_list);
        final Map<String, List<ContactDetailsModel>> contactMap = new HashMap<>();
        List<ContactDetailsModel> contactDetailsModelList = EasyBackUpUtils.getCompanyDetailsList();
        if(contactDetailsModelList != null) {
            for(ContactDetailsModel model : contactDetailsModelList) {

                String key = model.getCompanyName().toLowerCase();
                if(key.equalsIgnoreCase("")) continue;
                key= key.substring(0, 1).toUpperCase() + key.substring(1).toLowerCase();
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
        }
        final CompanyAdapter companyAdapter = new CompanyAdapter(getActivity(), keyList, contactMap);
        companyLV.setAdapter(companyAdapter);

        companyLV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String key = companyAdapter.getItem(position);
                EasyBackUpUtils.v("Selected Item Key : " + key);
                List<ContactDetailsModel> contactList = contactMap.get(key);
                // Before Called All Contact Fragment You have to have set SetSelectedContactDetailList.
                EasyBackUpUtils.setSelectedContactDetailsList(contactList);
                Fragment editContactFragment = new AllContactFragment();
                ((MainActivity)getActivity()).switchFragment(editContactFragment, true, KeyUtils.ALL_CONTACT_FRAGMENT_TAG);
            }
        });

        return layout_view;
    }
    @Override
    public void onResume() {
        ((MainActivity) getActivity()).setEditBtnVisibility(false);
        ((MainActivity) getActivity()).setAddBtnVisibility(false);
        super.onResume();
    }
}
