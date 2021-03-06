package easybackup.place.ispl.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import easybackup.place.ispl.MainActivity;
import easybackup.place.ispl.R;
import easybackup.place.ispl.adapters.RecentlyAddedAdapter;
import easybackup.place.ispl.models.ContactDetailsModel;
import easybackup.place.ispl.utils.EasyBackUpUtils;
import easybackup.place.ispl.utils.KeyUtils;

/**
 * Created by infinium on 4/14/17.
 */

public class RecentlyAddedFragment extends Fragment {
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View layout_view = inflater.inflate(R.layout.listview_jobtitle_details, container, false);
        ListView recentlyAddedLV = (ListView) layout_view.findViewById(R.id.jobtitle_list);
        final Map<String, List<ContactDetailsModel>> contactMap = new HashMap<>();
        List<ContactDetailsModel> contactDetailsModelList = EasyBackUpUtils.getRecentlyAddedList();


        if(contactDetailsModelList != null) {
            for(ContactDetailsModel model : contactDetailsModelList) {
                String key =  model.getContactName();

                EasyBackUpUtils.v("RecentyAddedConatc111" +key);

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
        }
        final RecentlyAddedAdapter recentyAddedAdapter = new RecentlyAddedAdapter(getActivity(), keyList, contactMap);
        recentlyAddedLV.setAdapter(recentyAddedAdapter);

        recentlyAddedLV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String key = recentyAddedAdapter.getItem(position);
                EasyBackUpUtils.v("Selected Item Key : " + key);
                List<ContactDetailsModel> contactList = contactMap.get(key);
                // Before Called All Contact Fragment You have to have set SetSelectedContactDetailList.
                EasyBackUpUtils.setSelectedContactDetailsList(contactList);
                Fragment recentlyAddedFragment = new AllContactFragment();
                ((MainActivity)getActivity()).switchFragment(recentlyAddedFragment, true, KeyUtils.ALL_CONTACT_FRAGMENT_TAG);
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
