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
import easybackup.place.ispl.adapters.DuplicateContactsAdapter;
import easybackup.place.ispl.models.ContactDetailsModel;
import easybackup.place.ispl.utils.EasyBackUpUtils;
import easybackup.place.ispl.utils.KeyUtils;

/**
 * Created by infinium on 4/14/17.
 */

public class SimilarNameFragment extends Fragment {
    List<ContactDetailsModel> contactDetailsModelList = EasyBackUpUtils.getContactDetailsList();

    int diff = 1;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View layout_view = inflater.inflate(R.layout.listview_similar_name_contacts_details, container, false);
        ListView similarNameLV = (ListView) layout_view.findViewById(R.id.similar_name_contacts_list);
        final Map<String, List<ContactDetailsModel>> contactMap = new HashMap<>();

        if (contactDetailsModelList != null) {

            /*for (int i=0;i<contactDetailsModelList.size();i++){
                for(int j=i+1; j<contactDetailsModelList.size();j++){
                    ContactDetailsModel fName = contactDetailsModelList.get(i);
                    ContactDetailsModel sName = contactDetailsModelList.get(j);

                    String name = fName.getContactName();
                    String name1 = sName.getContactName();

                        if (name.equalsIgnoreCase("")) continue;
                        if (contactMap.containsKey(name)) {
//                    key.replaceAll("[0-9-+.^:,@_#$%&*()=/']","");
                            List<ContactDetailsModel> modelList = contactMap.get(name);
                            modelList.add(contactDetailsModelList.get(i));
                        } else {
                                List<ContactDetailsModel> modelList = new ArrayList<>();
                                modelList.add(contactDetailsModelList.get(i));
                                contactMap.put(name, modelList);
                        }
                    }
            }*/
            for (ContactDetailsModel model : contactDetailsModelList) {
                String key = model.getContactName().replaceFirst("[0-9-+.^:,@_#$%&*()=/']", "");

                if (key.equalsIgnoreCase("") &&  (model.getContactName().length() - key.length()) == 1 ) continue;
                if (contactMap.containsKey(key)) {
//                    key.replaceAll("[0-9-+.^:,@_#$%&*()=/']","");
                    List<ContactDetailsModel> modelList = contactMap.get(key);
                    modelList.add(model);
                } else {
                    List<ContactDetailsModel> modelList = new ArrayList<>();
                    modelList.add(model);
                    contactMap.put(key, modelList);
                }
            }

        }
        //MRI bWTTERY pATI wAIT ok BIJU LOGIC VICHARWA D
        //sAMBHAD MANE AMA LAGTU NATHI K THASE  TRY KAR BIJU VIVCHARU CHU TYA SUDI
        //QUIT FROM TEAM VIEW

        List<String> keyList = new ArrayList<>();
        for (String key : contactMap.keySet()) {
            List<ContactDetailsModel> list = contactMap.get(key);
            if (list.size() > 1)
                keyList.add(key);
            Collections.sort(keyList);
        }
        EasyBackUpUtils.v("DuplicateContactCount :" + keyList.size());

        final DuplicateContactsAdapter duplicateContactsAdapter = new DuplicateContactsAdapter(getActivity(), keyList, contactMap);
        similarNameLV.setAdapter(duplicateContactsAdapter);

        similarNameLV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String key = duplicateContactsAdapter.getItem(position);
                EasyBackUpUtils.v("Selected Item Key : " + key);
                List<ContactDetailsModel> contactList = contactMap.get(key);
                // Before Called All Contact Fragment You have to have set SetSelectedContactDetailList.
                EasyBackUpUtils.setSelectedContactDetailsList(contactList);
                Fragment similarNameFragment = new AllContactFragment();
                ((MainActivity) getActivity()).switchFragment(similarNameFragment, true, KeyUtils.ALL_CONTACT_FRAGMENT_TAG);
            }
        });
        return layout_view;

    }
    public void checkDuplic(String name,String name1){
        String slen = name1.replace(name, "");
        int len = slen.length();
        if(len == 1){

        }
    }

    @Override
    public void onResume() {
        ((MainActivity) getActivity()).setAddBtnVisibility(false);

        super.onResume();
    }
}
