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
import android.support.annotation.NonNull;
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
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.target.BitmapImageViewTarget;

import java.io.File;
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

import static android.app.Activity.RESULT_OK;

/**
 * Created by infinium on 4/14/17.
 */

public class EditContactFragment extends Fragment {
    private TextView mTitleCancelTV, mTitleDoneTV;
    private EditText mContactNameET, mContactCompanyET, mContactHomePhoneET, mContactWorkPhoneET,
            mConactTitleET, mContactMobilePhoneET, mContactWorkEmailET, mContactHomeEmailET,
            mContactStreet2ET, mContactCityET, mContactPinCodeET, mContactStateET, mContactCountryET;
    @SuppressLint("StaticFieldLeak")
    static TextView ContactBirthdayTV;
    private final int PICK_PHOTO = 1;
    private String mSelectedImagePath = null;
    ImageView mEditContactImage;
    Bitmap mBitmap;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        final View layout_view = inflater.inflate(R.layout.fragment_edit_contact, container, false);

        mTitleCancelTV = (TextView) layout_view.findViewById(R.id.title_cancel_tv);
        mTitleDoneTV = (TextView) layout_view.findViewById(R.id.title_done_tv);
        mContactNameET = (EditText) layout_view.findViewById(R.id.edit_contact_name_et);
        mContactCompanyET = (EditText) layout_view.findViewById(R.id.edit_contact_company_et);
        mConactTitleET = (EditText) layout_view.findViewById(R.id.edit_contact_title_et);
        mContactMobilePhoneET = (EditText) layout_view.findViewById(R.id.edit_contact_mobile_et);
        mContactHomePhoneET = (EditText) layout_view.findViewById(R.id.edit_contact_home_et);
        mContactWorkPhoneET = (EditText) layout_view.findViewById(R.id.edit_contact_work_et);
        mContactWorkEmailET = (EditText) layout_view.findViewById(R.id.edit_contact_work_email_et);
        mContactHomeEmailET = (EditText) layout_view.findViewById(R.id.edit_contact_home_email_et);
        mContactStreet2ET = (EditText) layout_view.findViewById(R.id.edit_contact_street2_et);
        mContactCityET = (EditText) layout_view.findViewById(R.id.edit_contact_city_et);
        mContactPinCodeET = (EditText) layout_view.findViewById(R.id.edit_contact_pincode_et);
        mContactStateET = (EditText) layout_view.findViewById(R.id.edit_contact_state_et);
        mContactCountryET = (EditText) layout_view.findViewById(R.id.edit_contact_country_et);
        ContactBirthdayTV = (TextView) layout_view.findViewById(R.id.edit_select_contact_birthdate_tv);
        mEditContactImage = (ImageView) layout_view.findViewById(R.id.edit_cotacts_image);

        ContactDetailsModel details = EasyBackUpUtils.getSelectedContactDetails();
        if (details != null) {
            String id = details.getContactID();
            String image = details.getContactImage();
            String name = details.getContactName();
            String company = details.getCompanyName();
            String title = details.getContactTitle();
            String mobilePhone = details.getContactMobileNumber();
            String homePhone = details.getContactHomeNumber();
            String workPhone = details.getContactWorkNumber();
            String emailHome = details.getContactEmailHome();
            String emailWork = details.getContactEmailWork();
            mSelectedImagePath = details.getContactImage();
            String birthdate = details.getContactBirthdate();
            AddressModel model = details.getAddressModel();
            String street2 = model.getStreet2();
            String city = model.getCity();
            String pincode = model.getZipCode();
            String state = model.getState();
            String country = model.getCountry();

            EasyBackUpUtils.v("EditContactDetails" + "Edited Contact Id : " + id + " ImageURL :-  " + image + " contactName :- " + name + " Title :" + title + " MobileNumber " + mobilePhone + " HomeNumber" + homePhone +
                    " WrokNumber" + workPhone + " EmailHome " + emailHome + " EmailWork " + emailWork + " BirthDate " + birthdate
                    + "companyName" + company + " Stree2 " + street2 + " City " + city + " State " + state + " Pincode " + pincode + " Country" + country);


            mContactNameET.setText(name);
            mContactCompanyET.setText(company);
            mConactTitleET.setText(title);
            mContactMobilePhoneET.setText(mobilePhone);
            mContactHomePhoneET.setText(homePhone);
            mContactWorkPhoneET.setText(workPhone);
            mContactHomeEmailET.setText(emailHome);
            mContactWorkEmailET.setText(emailWork);
            ContactBirthdayTV.setText(birthdate);
            mContactStreet2ET.setText(street2);
            mContactCityET.setText(city);
            mContactStateET.setText(state);
            mContactPinCodeET.setText(pincode);
            mContactStateET.setText(state);
            mContactCountryET.setText(country);
            if (mSelectedImagePath != null)
                Glide.with(this).load(mSelectedImagePath).into(mEditContactImage);
        }

        MainActivity activity = (MainActivity) getActivity();
        activity.setToolBarVisibility(false);
        mEditContactImage.setOnClickListener(new View.OnClickListener() {
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
        ContactBirthdayTV.setOnClickListener(new View.OnClickListener() {
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

                String contactName = mContactNameET.getText().toString().trim();
                String companyName = mContactCompanyET.getText().toString().trim();
                String companyTitle = mConactTitleET.getText().toString().trim();
                String contactMobileNumber = mContactMobilePhoneET.getText().toString().trim();
                String contactHomeNumber = mContactHomePhoneET.getText().toString().trim();
                String contactWorkNumber = mContactWorkPhoneET.getText().toString().trim();
                String emailHome = mContactHomeEmailET.getText().toString().trim();
                String emailWork = mContactWorkEmailET.getText().toString().trim();
                String birthDate = ContactBirthdayTV.getText().toString().trim();
                String street2 = mContactStreet2ET.getText().toString().trim();
                String city = mContactCityET.getText().toString().trim();
                String state = mContactStateET.getText().toString().trim();
                String pincode = mContactPinCodeET.getText().toString().trim();
                String country = mContactCountryET.getText().toString().trim();

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

                EasyBackUpUtils.v("EditContactDetailsUpdated" + " contactName :- " + contactName + " Title :" + companyTitle + " MobileNumber " + contactMobileNumber + " HomeNumber" + contactHomeNumber +
                        " WorkNumber" + contactWorkNumber + " EmailHome " + emailHome + " EmailWork " + emailWork + " BirthDate " + birthDate
                        + "companyName" + companyName + " Street1 "  + " Street2 " + street2 + " City " + city + " State " + state + " Pincode " + pincode + " Country" + country);
                ContactDetailsModel details = EasyBackUpUtils.getSelectedContactDetails();
                if (details != null) {
                    String id = details.getContactID();
                   /* boolean isSucess = updateContact(mSelectedImagePath,contactName, companyName, contactTitle, contactMobileNumber, contactHomeNumber, emailHome, emailWork,
                            street1, street2, city, state, country, birthDate, id);*/
                    boolean isSucess = updateContact(contactName, companyName, companyTitle, contactMobileNumber, contactHomeNumber,
                            contactWorkNumber, emailHome, emailWork, street2, city, state, country,pincode, birthDate, id);
                    if (isSucess) {

                        AddressModel model = new AddressModel(street2, city, state, pincode, country);
                        ContactDetailsModel contactDetailsModel = new ContactDetailsModel("" + id, companyTitle, mSelectedImagePath, contactName,
                                contactMobileNumber, contactHomeNumber, contactWorkNumber, emailHome, emailWork, birthDate, companyName,"", model);
                        /*EasyBackUpUtils.v("ContactDetailModel :-" +" ID " + id + "Title " + companyTitle + "Image " + mSelectedImagePath + " Name " + contactName + " MobileNumber " +contactMobileNumber
                                +"Homme Number " + contactHomeNumber + " Work Number " + contactWorkNumber + " Email Home " +  emailHome + " Email Work " + emailWork  );*/

                        EasyBackUpUtils.v("ContactID :-" + id);
                        List<ContactDetailsModel> detailList = EasyBackUpUtils.getContactDetailsList();
                        for (int i = 0; i < detailList.size(); i++) {
                            ContactDetailsModel detailModel = detailList.get(i);
                            if (detailModel.getContactID().equalsIgnoreCase(id)) {
                                detailList.remove(details);
                                detailList.add(i, contactDetailsModel);
                                EasyBackUpUtils.setSelectedContactDetails(contactDetailsModel);
                                break;
                            }
                        }
                        EasyBackUpUtils.setContactDetailsList(detailList);


                        List<ContactDetailsModel> birthdateList = EasyBackUpUtils.getBirthDayContactList();
                        for (int i = 0; i < birthdateList.size(); i++) {
                            ContactDetailsModel detailModel = birthdateList.get(i);
                            if (detailModel.getContactID().equalsIgnoreCase(id)) {
                                birthdateList.remove(details);
                                birthdateList.add(i, contactDetailsModel);
                                EasyBackUpUtils.setSelectedContactDetails(contactDetailsModel);
                                break;
                            }
                        }
                        EasyBackUpUtils.setBirthDayContactList(birthdateList);

                        List<ContactDetailsModel> companyNameList = EasyBackUpUtils.getCompanyDetailsList();
                        for (int i = 0; i < companyNameList.size(); i++) {
                            ContactDetailsModel detailModel = companyNameList.get(i);
                            if (detailModel.getContactID().equalsIgnoreCase(id)) {
                                companyNameList.remove(details);
                                companyNameList.add(i, contactDetailsModel);
                                EasyBackUpUtils.setSelectedContactDetails(contactDetailsModel);
                                break;
                            }
                        }
                        EasyBackUpUtils.setCompanyDetailsList(companyNameList);

                        List<ContactDetailsModel> jobTitleList = EasyBackUpUtils.getJobTitleDetailsList();
                        for (int i = 0; i < jobTitleList.size(); i++) {
                            ContactDetailsModel detailModel = jobTitleList.get(i);
                            if (detailModel.getContactID().equalsIgnoreCase(id)) {
                                jobTitleList.remove(details);
                                jobTitleList.add(i, contactDetailsModel);
                                EasyBackUpUtils.setSelectedContactDetails(contactDetailsModel);
                                break;
                            }
                        }
                        EasyBackUpUtils.setJobTitleDetailsList(jobTitleList);

                        getActivity().onBackPressed();
                    }
                }
            }
        });


        return layout_view;
    }

    public boolean updateContact(String name, String company, String title, String mobileNumber, String homeNumber, String workNumber, String emailHome, String emailWork,
                                 String street2, String city, String state, String country,String pinCode, String birthdate, String ContactId) {
        boolean success = true;

        try {
            /*if(image.equalsIgnoreCase(""))
            image = image.trim();*/
            name = name.trim();
            emailHome = emailHome.trim();
            emailWork = emailWork.trim();
            mobileNumber = mobileNumber.trim();
            homeNumber = homeNumber.trim();
            workNumber = workNumber.trim();
            company = company.trim();
            title = title.trim();
            street2 = street2.trim();
            city = city.trim();
            state = state.trim();
            country = country.trim();

            String where = ContactsContract.Data.CONTACT_ID + " = ? AND " + ContactsContract.Data.MIMETYPE + " = ?";

            String[] imageParams = new String[]{ContactId, ContactsContract.CommonDataKinds.Photo.CONTENT_ITEM_TYPE};
            String[] nameParams = new String[]{ContactId, ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE};
            String[] companyParams = new String[]{ContactId, ContactsContract.CommonDataKinds.Organization.CONTENT_ITEM_TYPE};
            String[] titleParams = new String[]{ContactId, ContactsContract.CommonDataKinds.Organization.CONTENT_ITEM_TYPE};
            String[] emailHomeParams = new String[]{ContactId, ContactsContract.CommonDataKinds.Email.CONTENT_ITEM_TYPE};
            String[] emailWorkParams = new String[]{ContactId, ContactsContract.CommonDataKinds.Email.CONTENT_ITEM_TYPE};
            String[] numberParams = new String[]{ContactId, ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE};
            String[] homeParams = new String[]{ContactId, ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE};
            String[] workParams = new String[]{ContactId, ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE};
            String[] street2Params = new String[]{ContactId, ContactsContract.CommonDataKinds.StructuredPostal.CONTENT_ITEM_TYPE};
            String[] cityParams = new String[]{ContactId, ContactsContract.CommonDataKinds.StructuredPostal.CONTENT_ITEM_TYPE};
            String[] stateParams = new String[]{ContactId, ContactsContract.CommonDataKinds.StructuredPostal.CONTENT_ITEM_TYPE};
            String[] countryParams = new String[]{ContactId, ContactsContract.CommonDataKinds.StructuredPostal.CONTENT_ITEM_TYPE};
            String[] pinCodeParams = new String[]{ContactId, ContactsContract.CommonDataKinds.StructuredPostal.CONTENT_ITEM_TYPE};
            String[] birthdateParams = new String[]{ContactId, ContactsContract.CommonDataKinds.Event.CONTENT_ITEM_TYPE};

            ArrayList<ContentProviderOperation> ops = new ArrayList<ContentProviderOperation>();
            /*ByteArrayOutputStream stream = new ByteArrayOutputStream();
              // If an image is selected successfully
                mBitmap.compress(Bitmap.CompressFormat.PNG, 75, stream);

                // Adding update operation to operations list
                // to update Photo in the table ContactsContract.Data
                ops.add(android.content.ContentProviderOperation.newUpdate(ContactsContract.Data.CONTENT_URI)
                        .withSelection(where, imageParams)
                        .withValue(ContactsContract.Data.IS_SUPER_PRIMARY, 1)
                        .withValue(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.Photo.CONTENT_ITEM_TYPE)
                        .withValue(ContactsContract.CommonDataKinds.Photo.PHOTO, image)
                        .build());*/

            if (!name.equals("")) {
                ops.add(ContentProviderOperation.newUpdate(ContactsContract.Data.CONTENT_URI)
                        .withSelection(where, nameParams)
                        .withValue(ContactsContract.CommonDataKinds.StructuredName.DISPLAY_NAME, name)
                        .build());
            }

            if (!company.equals("")) {
                ops.add(ContentProviderOperation.newUpdate(ContactsContract.Data.CONTENT_URI)
                        .withSelection(where, companyParams)
                        .withValue(ContactsContract.CommonDataKinds.Organization.COMPANY, company)
                        .build());
            }

            if (!title.equals("")) {
                ops.add(ContentProviderOperation.newUpdate(ContactsContract.Data.CONTENT_URI)
                        .withSelection(where, titleParams)
                        .withValue(ContactsContract.CommonDataKinds.Organization.TITLE, title)
                        .build());
            }

            if (!emailHome.equals("")) {
                ops.add(ContentProviderOperation.newUpdate(ContactsContract.Data.CONTENT_URI)
                        .withSelection(where, emailHomeParams)
                        .withValue(ContactsContract.CommonDataKinds.Email.TYPE, ContactsContract.CommonDataKinds.Email.TYPE_HOME)
                        .withValue(ContactsContract.CommonDataKinds.Email.ADDRESS, emailHome)
                        .build());
            }

            if (!emailWork.equals("")) {
                ops.add(ContentProviderOperation.newUpdate(ContactsContract.Data.CONTENT_URI)
                        .withSelection(where, emailWorkParams)
                        .withValue(ContactsContract.CommonDataKinds.Email.TYPE, ContactsContract.CommonDataKinds.Email.TYPE_WORK)
                        .withValue(ContactsContract.CommonDataKinds.Email.ADDRESS, emailWork)
                        .build());
            }

            if (!mobileNumber.equals("")) {
                ops.add(ContentProviderOperation.newUpdate(ContactsContract.Data.CONTENT_URI)
                        .withSelection(where, numberParams)
                        .withValue(ContactsContract.CommonDataKinds.Phone.NUMBER, mobileNumber)
                        .withValue(ContactsContract.CommonDataKinds.Phone.TYPE, ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE)
                        .build());
            }
            if (!homeNumber.equals("")) {

                ops.add(ContentProviderOperation.newUpdate(ContactsContract.Data.CONTENT_URI)
                        .withSelection(where, homeParams)
                        .withValue(ContactsContract.CommonDataKinds.Phone.NUMBER, homeNumber)
                        .withValue(ContactsContract.CommonDataKinds.Phone.TYPE, ContactsContract.CommonDataKinds.Phone.TYPE_HOME)
                        .build());
            }
            if (!workNumber.equals("")) {

                ops.add(ContentProviderOperation.newUpdate(ContactsContract.Data.CONTENT_URI)
                        .withSelection(where, workParams)
                        .withValue(ContactsContract.CommonDataKinds.Phone.NUMBER, workNumber)
                        .withValue(ContactsContract.CommonDataKinds.Phone.TYPE, ContactsContract.CommonDataKinds.Phone.TYPE_HOME)
                        .build());
            }
            if (!street2.equals("")) {

                ops.add(ContentProviderOperation.newUpdate(ContactsContract.Data.CONTENT_URI)
                        .withSelection(where, street2Params)
                        .withValue(ContactsContract.CommonDataKinds.StructuredPostal.STREET, street2)
                        .build());
            }
            if (!city.equals("")) {

                ops.add(ContentProviderOperation.newUpdate(ContactsContract.Data.CONTENT_URI)
                        .withSelection(where, cityParams)
                        .withValue(ContactsContract.CommonDataKinds.StructuredPostal.CITY, city)
                        .build());
            }
            if (!state.equals("")) {

                ops.add(ContentProviderOperation.newUpdate(ContactsContract.Data.CONTENT_URI)
                        .withSelection(where, stateParams)
                        .withValue(ContactsContract.CommonDataKinds.StructuredPostal.REGION, state)
                        .build());
            }
            if (!country.equals("")) {

                ops.add(ContentProviderOperation.newUpdate(ContactsContract.Data.CONTENT_URI)
                        .withSelection(where, countryParams)
                        .withValue(ContactsContract.CommonDataKinds.StructuredPostal.COUNTRY, country)
                        .build());
            }
            if (!pinCode.equals("")) {

                ops.add(ContentProviderOperation.newUpdate(ContactsContract.Data.CONTENT_URI)
                        .withSelection(where, pinCodeParams)
                        .withValue(ContactsContract.CommonDataKinds.StructuredPostal.POSTCODE, pinCode)
                        .build());
            }
            if (!birthdate.equals("")) {

                ops.add(ContentProviderOperation.newUpdate(ContactsContract.Data.CONTENT_URI)
                        .withSelection(where, birthdateParams)
                        .withValue(ContactsContract.CommonDataKinds.Event.START_DATE, birthdate)
                        .withValue(ContactsContract.CommonDataKinds.Event.TYPE, ContactsContract.CommonDataKinds.Event.TYPE_BIRTHDAY)
                        .build());
            }
            try {
                // Asking the Contact provider to create a new contact
                // Executing all the insert operations as a single database transaction
                getActivity().getContentResolver().applyBatch(ContactsContract.AUTHORITY, ops);
                Toast.makeText(getActivity(), "Contact is successfully Updated", Toast.LENGTH_SHORT).show();
            } catch (RemoteException | OperationApplicationException e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
            success = false;
        }

        return success;
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
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(new BitmapImageViewTarget(mEditContactImage) {
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
            assert cursor != null;
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

        @NonNull
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
            ContactBirthdayTV.setText(day + "/" + (month + 1) + "/" + year);
        }
    }

}