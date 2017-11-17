package easybackup.place.ispl.fragments;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.ContentProviderOperation;
import android.content.Context;
import android.content.Intent;
import android.content.OperationApplicationException;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.RemoteException;
import android.provider.ContactsContract;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.annotation.RequiresApi;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import easybackup.place.ispl.MainActivity;
import easybackup.place.ispl.R;
import easybackup.place.ispl.models.AddressModel;
import easybackup.place.ispl.models.ContactDetailsModel;
import easybackup.place.ispl.utils.EasyBackUpUtils;
import easybackup.place.ispl.utils.KeyUtils;

import static android.app.Activity.RESULT_OK;

/**
 * Created by infinium on 4/14/17.
 */

public class NewContactFragment extends Fragment {
    private TextView mTitleCancelTV, mTitleDoneTV;
    private final int PICK_PHOTO = 1;
    private EditText mConactNameET, mContactCompanyET, mConactTitleET, mContactHomePhoneET, mContactWorkPhoneET,
            mContactMobilePhoneET, mContactWorkEmailET, mContactHomeEmailET,
            mContactStreet2ET, mContactCityET, mContactPinCodeET, mContactStateET, mContactCountryET;
    @SuppressLint("StaticFieldLeak")
    static TextView mContactBirthdateTV;
    Bitmap mBitmap;
    ImageView mAddContactImage;
    private String mSelectedImagePath = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View layout_view = inflater.inflate(R.layout.fragment_new_contact, container, false);
        mTitleCancelTV = (TextView) layout_view.findViewById(R.id.title_cancel_tv);
        mTitleDoneTV = (TextView) layout_view.findViewById(R.id.title_done_tv);
        mConactNameET = (EditText) layout_view.findViewById(R.id.add_contact_name_et);
        mContactCompanyET = (EditText) layout_view.findViewById(R.id.add_contact_company_et);
        mConactTitleET = (EditText) layout_view.findViewById(R.id.add_contact_title_et);
        mContactMobilePhoneET = (EditText) layout_view.findViewById(R.id.add_contact_mobile_et);
        mContactHomePhoneET = (EditText) layout_view.findViewById(R.id.add_contact_home_et);
        mContactWorkPhoneET = (EditText) layout_view.findViewById(R.id.add_contact_work_et);
        mContactWorkEmailET = (EditText) layout_view.findViewById(R.id.add_contact_work_email_et);
        mContactHomeEmailET = (EditText) layout_view.findViewById(R.id.add_contact_home_email_et);
        mAddContactImage = (ImageView) layout_view.findViewById(R.id.add_cotacts_image);
        mContactStreet2ET = (EditText) layout_view.findViewById(R.id.add_contact_street2_et);
        mContactCityET = (EditText) layout_view.findViewById(R.id.add_contact_city_et);
        mContactPinCodeET = (EditText) layout_view.findViewById(R.id.add_contact_pincode_et);
        mContactStateET = (EditText) layout_view.findViewById(R.id.add_contact_state_et);
        mContactCountryET = (EditText) layout_view.findViewById(R.id.add_contact_country_et);
        mContactBirthdateTV = (TextView) layout_view.findViewById(R.id.add_select_contact_birthdate_tv);

       /* //fetch default Ringtone
        Ringtone defaultRingtone = RingtoneManager.getRingtone(getActivity(),
                Settings.System.DEFAULT_RINGTONE_URI);
        //fetch current Ringtone
        Uri currentRintoneUri = RingtoneManager.getActualDefaultRingtoneUri(getActivity()
                .getApplicationContext(), RingtoneManager.TYPE_RINGTONE);
        final Ringtone currentRingtone = RingtoneManager.getRingtone(getActivity(), currentRintoneUri);
        //display Ringtone title
        EasyBackUpUtils.v("Rington" + defaultRingtone.getTitle(getActivity()) + "and " +
                "Current Ringtone:" + currentRingtone.getTitle(getActivity()));
        mContactRingtonTV.setText(currentRingtone.getTitle(getActivity()));*/


        MainActivity activity = (MainActivity) getActivity();
        activity.setToolBarVisibility(false);
        mAddContactImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                intent.setType("image/*");
                startActivityForResult(intent, PICK_PHOTO);
            }
        });
        mTitleCancelTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Animation animation1 = new AlphaAnimation(0.3f, 2.0f);
                animation1.setDuration(2500);
                animation1.setBackgroundColor(Color.RED);
                mTitleCancelTV.startAnimation(animation1);
                getActivity().onBackPressed();
            }
        });

        mContactBirthdateTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTruitonDatePickerDialog(v);
            }
        });

        mTitleDoneTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Animation animation1 = new AlphaAnimation(0.3f, 2.0f);
                animation1.setDuration(2500);
                animation1.setBackgroundColor(Color.RED);
                mTitleDoneTV.startAnimation(animation1);

                String contactName = mConactNameET.getText().toString().trim();
                String contactMobileNumber = mContactMobilePhoneET.getText().toString().trim();
                String contactHomeNumber = mContactHomePhoneET.getText().toString().trim();
                String contactWorkNumber = mContactWorkPhoneET.getText().toString().trim();
                String contactTitle = mConactTitleET.getText().toString().trim();
                String emailHome = mContactHomeEmailET.getText().toString().trim();
                String emailWork = mContactWorkEmailET.getText().toString().trim();
                String birthDate = mContactBirthdateTV.getText().toString().trim();
                String companyName = mContactCompanyET.getText().toString().trim();
                String street2 = mContactStreet2ET.getText().toString().trim();
                String city = mContactCityET.getText().toString().trim();
                String state = mContactStateET.getText().toString().trim();
                String country = mContactCountryET.getText().toString().trim();
                String pincode = mContactPinCodeET.getText().toString().trim();

                if(!contactName.isEmpty()){
                    String tempName = contactName;
                    StringBuilder nameBuilder = new StringBuilder();
                    tempName = tempName.substring(0, 1).toUpperCase() + tempName.substring(1).toLowerCase();
                    nameBuilder.append(tempName).append(",");
                    contactName = nameBuilder.toString().substring(0, nameBuilder.length() - 1);
                }else {
                    contactName = contactMobileNumber;
                }

                EasyBackUpUtils.v("ContactName Test :" + contactName);

                ArrayList<ContentProviderOperation> ops = new ArrayList<ContentProviderOperation>();
                int rawContactID = ops.size();

                // Adding insert operation to operations list
                // to insert a new raw contact in the table ContactsContract.RawContacts
                ops.add(ContentProviderOperation.newInsert(ContactsContract.RawContacts.CONTENT_URI)
                        .withValue(ContactsContract.RawContacts.ACCOUNT_TYPE, null)
                        .withValue(ContactsContract.RawContacts.ACCOUNT_NAME, null)
                        .build());

                // Adding insert operation to operations list
                // to insert display name in the table ContactsContract.Data
                ops.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
                        .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, rawContactID)
                        .withValue(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE)
                        .withValue(ContactsContract.CommonDataKinds.StructuredName.DISPLAY_NAME, contactName)
                        .build());

                // Adding insert operation to operations list
                // to insert Mobile Number in the table ContactsContract.Data
                ops.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
                        .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, rawContactID)
                        .withValue(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE)
                        .withValue(ContactsContract.CommonDataKinds.Phone.NUMBER, contactMobileNumber)
                        .withValue(ContactsContract.CommonDataKinds.Phone.TYPE, ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE)
                        .build());

                // Adding insert operation to operations list
                // to  insert Home Phone Number in the table ContactsContract.Data
                ops.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
                        .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, rawContactID)
                        .withValue(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE)
                        .withValue(ContactsContract.CommonDataKinds.Phone.NUMBER, contactHomeNumber)
                        .withValue(ContactsContract.CommonDataKinds.Phone.TYPE, ContactsContract.CommonDataKinds.Phone.TYPE_HOME)
                        .build());

                // Adding insert operation to operations list
                // to  insert Work Phone Number in the table ContactsContract.Data
                ops.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
                        .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, rawContactID)
                        .withValue(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE)
                        .withValue(ContactsContract.CommonDataKinds.Phone.NUMBER, contactWorkNumber)
                        .withValue(ContactsContract.CommonDataKinds.Phone.TYPE, ContactsContract.CommonDataKinds.Phone.TYPE_WORK)
                        .build());

                // Adding insert operation to operations list
                // to insert Work Email in the table ContactsContract.Data
                ops.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
                        .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, rawContactID)
                        .withValue(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.Email.CONTENT_ITEM_TYPE)
                        .withValue(ContactsContract.CommonDataKinds.Email.ADDRESS, emailHome)
                        .withValue(ContactsContract.CommonDataKinds.Email.TYPE, ContactsContract.CommonDataKinds.Email.TYPE_WORK)
                        .build());
                // Adding insert operation to operations list
                // to insert Home Email in the table ContactsContract.Data
                ops.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
                        .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, rawContactID)
                        .withValue(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.Email.CONTENT_ITEM_TYPE)
                        .withValue(ContactsContract.CommonDataKinds.Email.ADDRESS, emailWork)
                        .withValue(ContactsContract.CommonDataKinds.Email.TYPE, ContactsContract.CommonDataKinds.Email.TYPE_HOME)
                        .build());
                // Adding insert operation to operations list
                // to insert Company Name in the table ContactsContract.Data
                ops.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
                        .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, rawContactID)
                        .withValue(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.Organization.CONTENT_ITEM_TYPE)
                        .withValue(ContactsContract.CommonDataKinds.Organization.COMPANY, companyName)
                        .withValue(ContactsContract.CommonDataKinds.Organization.TITLE, contactTitle)
                        .withValue(ContactsContract.CommonDataKinds.Organization.TYPE, ContactsContract.CommonDataKinds.Organization.TYPE_WORK)
                        .build());

                //Postal Address

                ops.add(ContentProviderOperation
                        .newInsert(ContactsContract.Data.CONTENT_URI).withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, rawContactID)

                        .withValue(ContactsContract.Contacts.Data.MIMETYPE, ContactsContract.CommonDataKinds.StructuredPostal.CONTENT_ITEM_TYPE)
                        .withValue(ContactsContract.CommonDataKinds.StructuredPostal.STREET, street2)

                        .withValue(ContactsContract.Contacts.Data.MIMETYPE, ContactsContract.CommonDataKinds.StructuredPostal.CONTENT_ITEM_TYPE)
                        .withValue(ContactsContract.CommonDataKinds.StructuredPostal.REGION, state)

                        .withValue(ContactsContract.Contacts.Data.MIMETYPE, ContactsContract.CommonDataKinds.StructuredPostal.CONTENT_ITEM_TYPE)
                        .withValue(ContactsContract.CommonDataKinds.StructuredPostal.CITY, city)

                        .withValue(ContactsContract.Contacts.Data.MIMETYPE, ContactsContract.CommonDataKinds.StructuredPostal.CONTENT_ITEM_TYPE)
                        .withValue(ContactsContract.CommonDataKinds.StructuredPostal.POSTCODE, pincode)

                        .withValue(ContactsContract.Contacts.Data.MIMETYPE, ContactsContract.CommonDataKinds.StructuredPostal.CONTENT_ITEM_TYPE)
                        .withValue(ContactsContract.CommonDataKinds.StructuredPostal.COUNTRY, country)

                        .withValue(ContactsContract.Contacts.Data.MIMETYPE, ContactsContract.CommonDataKinds.StructuredPostal.CONTENT_ITEM_TYPE)
                        .withValue(ContactsContract.CommonDataKinds.StructuredPostal.TYPE, "3")
                        .build());

                //add birthdate
                ops.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
                        .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, rawContactID)
                        .withValue(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.Event.CONTENT_ITEM_TYPE)
                        .withValue(ContactsContract.CommonDataKinds.Event.START_DATE, birthDate)
                        .withValue(ContactsContract.CommonDataKinds.Event.TYPE, ContactsContract.CommonDataKinds.Event.TYPE_BIRTHDAY)
                        .build());

                //convert birthdate
                SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
                try {
                    Date date = formatter.parse(birthDate);
                    String OLD_FORMAT = "dd/MM";
                    SimpleDateFormat newDateFormat = new SimpleDateFormat("dd-MM-yy HH:mm:ss");
                    Date d = newDateFormat.parse(newDateFormat.format(date));
                    newDateFormat.applyPattern(OLD_FORMAT);
                    birthDate = newDateFormat.format(d);
                } catch (ParseException e) {
                    e.printStackTrace();
                }


                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                if (mBitmap != null) {    // If an image is selected successfully
                    mBitmap.compress(Bitmap.CompressFormat.PNG, 75, stream);

                    // Adding insert operation to operations list
                    // to insert Photo in the table ContactsContract.Data
                    ops.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
                            .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, rawContactID)
                            .withValue(ContactsContract.Data.IS_SUPER_PRIMARY, 1)
                            .withValue(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.Photo.CONTENT_ITEM_TYPE)
                            .withValue(ContactsContract.CommonDataKinds.Photo.PHOTO, stream.toByteArray())
                            .build());

                    try {
                        stream.flush();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                try {
                    // Asking the Contact provider to create a new contact
                    // Executing all the insert operations as a single database transaction
                    getActivity().getContentResolver().applyBatch(ContactsContract.AUTHORITY, ops);
                    Toast.makeText(getActivity(), "Contact is successfully added", Toast.LENGTH_SHORT).show();
                } catch (RemoteException | OperationApplicationException e) {
                    e.printStackTrace();
                }

                AddressModel model = new AddressModel(street2, city, state, pincode, country);
                ContactDetailsModel contactDetailsModel = new ContactDetailsModel("" + rawContactID, contactTitle, mSelectedImagePath, contactName,
                        contactMobileNumber, contactHomeNumber, contactWorkNumber, emailHome, emailWork, birthDate, companyName, "", model);

                /*List<ContactDetailsModel> detailList = EasyBackUpUtils.getContactDetailsList();

                detailList.add(contactDetailsModel);
                EasyBackUpUtils.setContactDetailsList(detailList);*/
                List<ContactDetailsModel> contactList = EasyBackUpUtils.getContactDetailsList();
                List<ContactDetailsModel> birthDayList = EasyBackUpUtils.getBirthDayContactList();
                List<ContactDetailsModel> companyNameList = EasyBackUpUtils.getCompanyDetailsList();
                List<ContactDetailsModel> jobTitleList = EasyBackUpUtils.getJobTitleDetailsList();

                contactList.add(contactDetailsModel);
                birthDayList.add(contactDetailsModel);
                companyNameList.add(contactDetailsModel);
                jobTitleList.add(contactDetailsModel);

                Fragment newContactFragment = new AllContactFragment();
                ((MainActivity) getActivity()).switchFragment(newContactFragment, true, KeyUtils.ALL_CONTACT_FRAGMENT_TAG);

            }
        });

        return layout_view;

    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case PICK_PHOTO:
                if (resultCode == RESULT_OK) {
                    onSelectFromGalleryResult(data);
                }
        }
    }

    private void onSelectFromGalleryResult(Intent data) {
        Uri selectedImageURI = data.getData();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            mSelectedImagePath = getRealPathFromURI(getActivity(), selectedImageURI);
            if (mSelectedImagePath == null) {
                mSelectedImagePath = getRealPathFromURI(selectedImageURI);
            }
        } else {
            mSelectedImagePath = getRealPathFromURI(selectedImageURI);
        }

        Glide.with(getActivity())
                .load(new File(mSelectedImagePath))
                .asBitmap()
                .into(new BitmapImageViewTarget(mAddContactImage) {
                    @Override
                    protected void setResource(Bitmap resource) {
                        // Do bitmap magic here
                        super.setResource(resource);
                        mBitmap = resource;
                    }
                });
        EasyBackUpUtils.v("ImagePath : " + mSelectedImagePath);
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public static String getRealPathFromURI(Context context, Uri uri) {
        String filePath = null;
        try {
            String wholeID = null;
            wholeID = DocumentsContract.getDocumentId(uri);
// Split at colon, use second item in the array
            String id = wholeID.split(":")[1];
            String[] column = {MediaStore.Images.Media.DATA};
// where id is equal to
            String sel = MediaStore.Images.Media._ID + "=?";
            Cursor cursor = context.getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                    column, sel, new String[]{id}, null);
            int columnIndex = cursor.getColumnIndex(column[0]);
            if (cursor.moveToFirst()) {
                filePath = cursor.getString(columnIndex);
            }
            cursor.close();
            return filePath;
        } catch (Exception e) {
            return null;
        }
    }

    private String getRealPathFromURI(Uri contentURI) {
        String result = null;
        try {
            Cursor cursor = getActivity().getContentResolver().query(contentURI, null, null, null, null);
            if (cursor == null) { // Source is Dropbox or other similar local file path
                result = contentURI.getPath();
            } else {
                cursor.moveToFirst();
                int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
                result = cursor.getString(idx);
                cursor.close();
            }
            return result;
        } catch (Exception e) {
            return null;
        }
    }

    public void showTruitonDatePickerDialog(View v) {
        DialogFragment newFragment = new DatePickerFragment();
        newFragment.show(getActivity().getSupportFragmentManager(), "datePicker");
    }

    public static class DatePickerFragment extends DialogFragment implements
            DatePickerDialog.OnDateSetListener {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current date as the default date in the picker
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);

            // Create a new instance of DatePickerDialog and return it
            return new DatePickerDialog(getActivity(), this, year, month, day);
        }

        public void onDateSet(DatePicker view, int year, int month, int day) {
            // Do something with the date chosen by the user
            mContactBirthdateTV.setText(day + "/" + (month + 1) + "/" + year);
        }
    }

    public static Bitmap scaleDown(Bitmap realImage, float maxImageSize,
                                   boolean filter) {
        float ratio = Math.min(
                (float) maxImageSize / realImage.getWidth(),
                (float) maxImageSize / realImage.getHeight());
        int width = Math.round((float) ratio * realImage.getWidth());
        int height = Math.round((float) ratio * realImage.getHeight());

        Bitmap newBitmap = Bitmap.createScaledBitmap(realImage, width,
                height, filter);
        return newBitmap;
    }
}