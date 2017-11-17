package easybackup.place.ispl.fragments;

import android.app.Dialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

import easybackup.place.ispl.R;
import easybackup.place.ispl.VCFFileList;
import easybackup.place.ispl.utils.EasyBackUpUtils;

import static easybackup.place.ispl.R.id.restore_vcf_file_tv;

/**
 * Created by infinium on 4/3/17.
 */

public class SettingsFragment extends Fragment {
    private TextView mBackUpType, mVcfFileTV, mRestoreVcfFileTV;
    private LinearLayout mBackUpTypeLayout, mRestoreVcfFileLayout, mTellFriendsLayout;
    private Button mDeleteBtn;
    private String mSelectedItem = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View layout_view = inflater.inflate(R.layout.fragment_settings, container, false);
        mBackUpType = (TextView) layout_view.findViewById(R.id.backtype_tv);
        mVcfFileTV = (TextView) layout_view.findViewById(R.id.vcf_file_tv);
        mVcfFileTV.setText(EasyBackUpUtils.sFileSavedType);
        mBackUpTypeLayout = (LinearLayout) layout_view.findViewById(R.id.backup_type_layout);
        mRestoreVcfFileTV = (TextView) layout_view.findViewById(restore_vcf_file_tv);
        mRestoreVcfFileLayout = (LinearLayout) layout_view.findViewById(R.id.restore_vcf_file_layout);
        mTellFriendsLayout = (LinearLayout) layout_view.findViewById(R.id.tell_firends_layout);
        mDeleteBtn = (Button) layout_view.findViewById(R.id.delete_btn);

        mBackUpTypeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialog = new Dialog(getActivity());
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.custom_picker_dialog);
                dialog.setCanceledOnTouchOutside(false);

                final NumberPicker numberPicker;
                numberPicker = (NumberPicker) dialog.findViewById(R.id.number_picker);
                TextView cancel = (TextView) dialog.findViewById(R.id.picker_cancel_dailog);
                TextView done = (TextView) dialog.findViewById(R.id.picker_done_dailog);

                //Initializing a new string array with elements
                final String[] values = {"VCF File", "PDF File", "CSV File"};
                mSelectedItem = EasyBackUpUtils.sFileSavedType;
                //Populate NumberPicker values from String array values
                //Set the minimum value of NumberPicker
                numberPicker.setMinValue(0); //from array first value
                //Specify the maximum value/number of NumberPicker
                numberPicker.setMaxValue(values.length - 1); //to array last value

                //Specify the NumberPicker data source as array elements
                numberPicker.setDisplayedValues(values);

                //Gets whether the selector wheel wraps when reaching the min/max value.
                numberPicker.setWrapSelectorWheel(true);

                if(mSelectedItem.equals(values[0])) {
                    numberPicker.setValue(0);
                } else if(mSelectedItem.equals(values[1])) {
                    numberPicker.setValue(1);
                } else {
                    numberPicker.setValue(2);
                }
                cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });

                //Set a value change listener for NumberPicker
                numberPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
                    @Override
                    public void onValueChange(NumberPicker picker, int oldVal, final int newVal) {
                        mSelectedItem = values[newVal];
                    }
                });

                done.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        EasyBackUpUtils.sFileSavedType = mSelectedItem;
                        mVcfFileTV.setText(mSelectedItem);
                        dialog.dismiss();
                    }
                });
                dialog.show();
            }
        });


        mRestoreVcfFileLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Start an alpha animation for clicked item
                Animation animation1 = new AlphaAnimation(0.3f, 2.0f);
                animation1.setDuration(2500);
                animation1.setBackgroundColor(Color.RED);
                mRestoreVcfFileLayout.startAnimation(animation1);
                Intent intent = new Intent(getActivity(), VCFFileList.class);
                startActivity(intent);
            }
        });
        mTellFriendsLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Animation animation1 = new AlphaAnimation(0.3f, 2.0f);
                animation1.setDuration(2500);
                animation1.setBackgroundColor(Color.RED);
                mTellFriendsLayout.startAnimation(animation1);

                Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                        "mailto", "abc@gmail.com", null));
                emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Tell A Friends");
                getActivity().startActivity(Intent.createChooser(emailIntent, null));
            }
        });
        mDeleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Animation animation1 = new AlphaAnimation(0.3f, 2.0f);
                animation1.setDuration(2500);
                animation1.setBackgroundColor(Color.RED);
                mDeleteBtn.startAnimation(animation1);
                //---------------------------------------------------------------------------------------------------------------------------
                // marker option alert
                final Dialog dialog = new Dialog(getActivity());
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.custom_delete_all_dialog);
                dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                dialog.setCanceledOnTouchOutside(false);

                final TextView delete_all = (TextView) dialog.findViewById(R.id.delete_all_dialog_tv);

                Button searchBtn = (Button) dialog.findViewById(R.id.dialog_button_yes);
                searchBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        ContentResolver cr = getActivity().getContentResolver();
                        Cursor cur = cr.query(ContactsContract.Contacts.CONTENT_URI,
                                null, null, null, null);
                        while (cur.moveToNext()) {
                            try {
                                String lookupKey = cur.getString(cur.getColumnIndex(ContactsContract.Contacts.LOOKUP_KEY));
                                Uri uri = Uri.withAppendedPath(ContactsContract.Contacts.CONTENT_LOOKUP_URI, lookupKey);
                                cr.delete(uri, null, null);
                                EasyBackUpUtils.getContactDetailsList().clear();
                            } catch (Exception e) {
                                System.out.println(e.getStackTrace());
                            }
                        }
                        Toast.makeText(getActivity(), "All contacts are deleted..!!", Toast.LENGTH_LONG).show();
                        dialog.dismiss();
                    }
                    //  Toast.makeText(getApplicationContext(), "LatLong is:" + app_area.getText().toString().trim(), Toast.LENGTH_LONG).show();

                });
                Button cancelBtn = (Button) dialog.findViewById(R.id.dialog_button_no);
                cancelBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                dialog.show();
            }
        });

        return layout_view;
    }


}
