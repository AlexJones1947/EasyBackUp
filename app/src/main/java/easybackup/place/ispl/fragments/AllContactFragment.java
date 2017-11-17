package easybackup.place.ispl.fragments;

import android.content.ContentProviderOperation;
import android.content.Context;
import android.content.OperationApplicationException;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.RemoteException;
import android.provider.ContactsContract;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import org.zakariya.stickyheaders.StickyHeaderLayoutManager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import easybackup.place.ispl.MainActivity;
import easybackup.place.ispl.R;
import easybackup.place.ispl.adapters.AddressBookDemoAdapter;
import easybackup.place.ispl.models.ContactDetailsModel;
import easybackup.place.ispl.utils.AlphabetItem;
import easybackup.place.ispl.utils.EasyBackUpUtils;
import in.myinnos.alphabetsindexfastscrollrecycler.IndexFastScrollRecyclerView;

/**
 * Created by infinium on 4/3/17.
 */

public class AllContactFragment extends Fragment {
    private AddressBookDemoAdapter mContactAdapter = null;
    private List<ContactDetailsModel> mContactList = null;
    private LinearLayout mBottomLL = null;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View rootView = inflater.inflate(R.layout.fragment_all_contacts, container, false);

        //ButterKnife.bind(getActivity());

        IndexFastScrollRecyclerView mRecyclerView = (IndexFastScrollRecyclerView) rootView.findViewById(R.id.fast_scroller_recycler);
        mContactList = EasyBackUpUtils.getSelectedContactDetailsList();
        if (mContactList == null) {
            mContactList = EasyBackUpUtils.getContactDetailsList();

            //Alphabet fast scroller data
            List<AlphabetItem> mAlphabetItems = new ArrayList<>();
            List<String> strAlphabets = new ArrayList<>();
            for (int i = 0; i < mContactList.size(); i++) {
                String name = String.valueOf(mContactList.get(i));
                if (name == null || name.trim().isEmpty())
                    continue;

                String word = name.substring(0, 1);
                if (!strAlphabets.contains(word)) {
                    strAlphabets.add(word);
                    mAlphabetItems.add(new AlphabetItem(i, word, false));
                }
            }
        }

        if (mContactList.size() > 0) {
            Collections.sort(mContactList, new Comparator<ContactDetailsModel>() {
                public int compare(ContactDetailsModel s1, ContactDetailsModel s2) {
                    EasyBackUpUtils.v("O1 : " + s1.getContactName());
                    EasyBackUpUtils.v("O2 : " + s2.getContactName());
                    return s1.getContactName().compareToIgnoreCase(s2.getContactName());
                }
            });
        }

        mBottomLL = (LinearLayout) rootView.findViewById(R.id.bottom_linearlayout);
        ImageView deleteIV = (ImageView) rootView.findViewById(R.id.delete_imageview);
        ImageView mergeLL = (ImageView) rootView.findViewById(R.id.merge_imageview);
        mContactAdapter = new AddressBookDemoAdapter(mContactList, getActivity(), deleteIV, mergeLL);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setAdapter(mContactAdapter);
        mRecyclerView.setIndexTextSize(12);
        mRecyclerView.setIndexBarColor("#33334c");
        mRecyclerView.setIndexBarCornerRadius(0);
        mRecyclerView.setIndexBarTransparentValue((float) 0.4);
        mRecyclerView.setIndexbarMargin(0);
        mRecyclerView.setIndexbarWidth(40);
        mRecyclerView.setPreviewPadding(0);
        mRecyclerView.setIndexBarTextColor("#FFFFFF");
        mRecyclerView.setIndexBarVisibility(true);


        StickyHeaderLayoutManager layoutManager = new StickyHeaderLayoutManager();

        // set a header position callback to set elevation on sticky headers, because why not
        layoutManager.setHeaderPositionChangedCallback(new StickyHeaderLayoutManager.HeaderPositionChangedCallback() {
            @Override
            public void onHeaderPositionChanged(int sectionIndex, View header, StickyHeaderLayoutManager.HeaderPosition oldPosition, StickyHeaderLayoutManager.HeaderPosition newPosition) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    boolean elevated = newPosition == StickyHeaderLayoutManager.HeaderPosition.STICKY;
                    header.setElevation(elevated ? 8 : 0);
                }
            }
        });

        mRecyclerView.setLayoutManager(layoutManager);

        // Edit button click event
        ImageView editIV = (ImageView) getActivity().findViewById(R.id.edit_imageview);
        editIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Start an alpha animation for clicked item
                Animation animation1 = new AlphaAnimation(0.3f, 2.0f);
                animation1.setDuration(1500);
                animation1.setBackgroundColor(Color.RED);
                v.startAnimation(animation1);
                mBottomLL.setVisibility(View.VISIBLE);
                mContactAdapter.setCheckboxVisibility(true);
                ((MainActivity) getActivity()).setMainNavigationVisibility(false);
                ((MainActivity) getActivity()).setAddBtnVisibility(false);
                ((MainActivity) getActivity()).setCloseBtnVisibility(true);
            }
        });
        // Close button click event
        final ImageView closeIV = (ImageView) getActivity().findViewById(R.id.close_imageview);
        closeIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Start an alpha animation for clicked item
                Animation animation1 = new AlphaAnimation(0.3f, 2.0f);
                animation1.setDuration(100);
                animation1.setBackgroundColor(Color.RED);
                closeIV.startAnimation(animation1);
                mContactAdapter.disableCheckBox();
                mBottomLL.setVisibility(View.GONE);
                mContactAdapter.setCheckboxVisibility(false);
                ((MainActivity) getActivity()).setCloseBtnVisibility(false);
                ((MainActivity) getActivity()).setMainNavigationVisibility(true);
                ((MainActivity) getActivity()).setAddBtnVisibility(true);
            }
        });
        // Delete button click event
        deleteIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<ContactDetailsModel> contactList = mContactAdapter.getContactDetailList();
                if (contactList != null) {
                    List<ContactDetailsModel> deletedContatList = new ArrayList<>();
                    for (ContactDetailsModel model : contactList) {
                        boolean isChecked = model.isSelected();
                        if (isChecked) {
                            deletedContatList.add(model);
                        }
                    }
                    if (deletedContatList != null && deletedContatList.size() > 0) {
                        for (ContactDetailsModel model : deletedContatList) {
                            boolean isDeleted = deleteContact(getContext(), model.getContactMobileNumber(), model.getContactName());
                            if (isDeleted) {
                                contactList.remove(model);
                                mContactAdapter.disableColor();
                                Toast.makeText(getContext(), "Contact is removed" + deletedContatList.size(), Toast.LENGTH_LONG).show();
                            }
                        }
                    }
                }
                mContactAdapter.updateList(contactList);

            }
        });

        mergeLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<ContactDetailsModel> contactList = mContactAdapter.getContactDetailList();
                if (contactList != null) {
                    List<ContactDetailsModel> selectedContatList = new ArrayList<>();
                    for (ContactDetailsModel model : contactList) {
                        boolean isChecked = model.isSelected();
                        if (isChecked) {
                            selectedContatList.add(model);
                        }
                    }
                    if (selectedContatList != null && selectedContatList.size() > 0) {
                        for (ContactDetailsModel model : selectedContatList) {
                            EasyBackUpUtils.v("Name : " + model.getContactName());
                            ArrayList<ContentProviderOperation> ops = new ArrayList<>();
                            //boolean isUpdate=ops.add(ContentProviderOperation.newUpdate(ContactsContract.AggregationExceptions.CONTENT_URI)
                            ops.add(ContentProviderOperation.newUpdate(ContactsContract.AggregationExceptions.CONTENT_URI)
                                    .withValue(ContactsContract.AggregationExceptions.TYPE, ContactsContract.AggregationExceptions.TYPE_KEEP_TOGETHER)
                                    .withValue(ContactsContract.AggregationExceptions.RAW_CONTACT_ID1, selectedContatList.get(selectedContatList.size() - 1).getContactID())
                                    .withValue(ContactsContract.AggregationExceptions.RAW_CONTACT_ID2, model.getContactID())
                                    .build());

                            try {
                                getContext().getContentResolver().applyBatch(ContactsContract.AUTHORITY, ops);
                                mContactAdapter.notifyDataSetChanged();
                                break;
                            } catch (RemoteException | OperationApplicationException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
                mContactAdapter.updateList(contactList);
                Toast.makeText(getContext(), "Contact are merge successfully", Toast.LENGTH_LONG).show();
            }

        });

        SearchView searchView = (SearchView) rootView.findViewById(R.id.search);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                //FILTER AS YOU TYPE
                filterContact(query);
                return false;
            }
        });
        return rootView;
    }

    private void filterContact(String searchString) {
        List<ContactDetailsModel> filterContactList = new ArrayList<>();
        if (mContactList != null) {
            for (int i = 0; i < mContactList.size(); i++) {
                String displayName = mContactList.get(i).getContactName();
                if (displayName != null) {
                    String smallName = displayName.toLowerCase();
                    if (displayName.contains(searchString) || smallName.contains(searchString)) {
                        filterContactList.add(mContactList.get(i));
                    }
                }
            }
        }
        mContactAdapter.updateList(filterContactList);
    }

    public static boolean deleteContact(Context ctx, String phone, String name) {
        Uri contactUri = Uri.withAppendedPath(ContactsContract.CommonDataKinds.Phone.CONTENT_FILTER_URI, Uri.encode(name));
        Cursor cur = ctx.getContentResolver().query(contactUri, null, null, null, null);
        try {
            if (cur != null && cur.moveToFirst()) {
                do {
                    if (cur.getString(cur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)).equalsIgnoreCase(phone)) {
                        String lookupKey = cur.getString(cur.getColumnIndex(ContactsContract.Contacts.LOOKUP_KEY));
                        Uri uri = Uri.withAppendedPath(ContactsContract.Contacts.CONTENT_LOOKUP_URI, lookupKey);
                        ctx.getContentResolver().delete(uri, null, null);
                        return true;
                    }

                } while (cur.moveToNext());
            }

        } catch (Exception e) {
            System.out.println(Arrays.toString(e.getStackTrace()));
        } finally {
            if (cur != null) {
                cur.close();
            }
        }
        return false;
    }

    @Override
    public void onResume() {
        ((MainActivity) getActivity()).setEditBtnVisibility(true);
        ((MainActivity) getActivity()).setToolBarVisibility(true);
        super.onResume();
    }
}
