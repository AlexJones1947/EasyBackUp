package easybackup.place.ispl;

import android.Manifest;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import easybackup.place.ispl.models.AddressModel;
import easybackup.place.ispl.models.ContactDetailsModel;
import easybackup.place.ispl.utils.EasyBackUpUtils;

/**
 * Created by infinium on 3/21/17.
 */

public class SplashScreen extends Activity {
    // Splash screen timer
    private static int SPLASH_TIME_OUT = 2000;
    public static final int MULTIPLE_PERMISSIONS = 10;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            checkPermissions();
        } else {
            initCall();
        }
    }


    private boolean checkPermissions() {
        int result;
        List<String> listPermissionsNeeded = new ArrayList<>();
        String[] permissions = new String[0];
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN) {
            permissions = new String[]{
                    Manifest.permission.READ_CONTACTS,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.READ_CALL_LOG};
        }
        for (String p : permissions) {
            result = ContextCompat.checkSelfPermission(getApplicationContext(), p);
            if (result != PackageManager.PERMISSION_GRANTED) {
                listPermissionsNeeded.add(p);
            }
        }
        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(this, listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]), MULTIPLE_PERMISSIONS);
            return false;
        } else {
            initCall();
        }
        return true;
    }

    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
        switch (requestCode) {

            case MULTIPLE_PERMISSIONS: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permissions granted.
                    initCall();
                } else {
                    // no permissions granted.
                    Toast.makeText(this, "permission denied", Toast.LENGTH_LONG).show();
                }
            }
        }
    }

    public void initCall() {
        new Handler().postDelayed(new Runnable() {

            /*
             * Showing splash screen with a timer. This will be useful when you
             * want to show case your app logo / company
             */

            @Override
            public void run() {
                List<ContactDetailsModel> contactList = new ArrayList<>();
                List<ContactDetailsModel> birthDayList = new ArrayList<>();
                List<ContactDetailsModel> companyNameList = new ArrayList<>();
                List<ContactDetailsModel> jobTitleList = new ArrayList<>();

                ContentResolver contentResolver = getApplicationContext().getContentResolver();
                Cursor cursor = contentResolver.query(ContactsContract.Contacts.CONTENT_URI, null, null, null, ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + " ASC");
                //Cursor cursor = contentResolver.query(ContactsContract.Contacts.CONTENT_URI, null, null, null,"_id DESC");
                if (cursor.getCount() > 0) {
                    while (cursor.moveToNext()) {
                        String contactId, companyTitle = "", imageURI = "", contactName, contactMobile = "", contactHome = "", contactWork = "", emailHome = "", emailWork = "",
                                birthdate = "", companyName = "", lastUpdated;
                        AddressModel addressModel = null;
                        int hasPhoneNumber = Integer.parseInt(cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER)));
                        if (hasPhoneNumber > 0) {
                            contactId = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
                            contactName = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                            lastUpdated = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.CONTACT_STATUS_TIMESTAMP));

                          //  EasyBackUpUtils.v("Name : " + contactName);
                           // EasyBackUpUtils.v("ContactID : " + contactId);
                          //  EasyBackUpUtils.v("LastUpdated : " + lastUpdated);

                            if (contactName == null) contactName = "";
                            Cursor phoneCursor = contentResolver.query(
                                    ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                                    null,
                                    ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?",
                                    new String[]{contactId},
                                    null);
                            while (phoneCursor.moveToNext()) {
                                int phoneType = phoneCursor.getInt(phoneCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.TYPE));
                                switch (phoneType) {
                                    case ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE:
                                        contactMobile = phoneCursor.getString(phoneCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                                    //    EasyBackUpUtils.v("contactDetails" + " contactName " + contactName + ": TYPE_MOBILE " + " " + contactMobile);
                                        break;
                                    case ContactsContract.CommonDataKinds.Phone.TYPE_HOME:
                                        contactHome = phoneCursor.getString(phoneCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                                    //    EasyBackUpUtils.v("contactDetails" + " contactName " + contactName + ": TYPE_HOME " + " " + contactHome);
                                        break;
                                    case ContactsContract.CommonDataKinds.Phone.TYPE_WORK:
                                        contactWork = phoneCursor.getString(phoneCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                                    //    EasyBackUpUtils.v("contactDetails" + " contactName " + contactName + ": TYPE_WORK " + " " + contactWork);
                                        break;
                                    case ContactsContract.CommonDataKinds.Phone.TYPE_OTHER:
                                        String other = phoneCursor.getString(phoneCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                                     //   EasyBackUpUtils.v("contactDetails" + " contactName " + contactName + ": TYPE_OTHER " + " " + other);
                                        break;
                                    default:
                                        break;
                                }
                                EasyBackUpUtils.v("AllConatctDetails :-" + " ID " + contactId + "Title " + companyTitle + "Image " + imageURI + " Name " + contactName + " MobileNumber " + contactMobile
                                        + "Homme Number " + contactHome + " Work Number " + contactWork + " Email Home " + emailHome + "Email Work " + emailWork);
                            }
                            phoneCursor.close();

                            // GET IMAGE
                            //------------------------------------------------------------------------------------------------------------------------
                            Cursor imageCursor = contentResolver.query(
                                    ContactsContract.CommonDataKinds.Email.CONTENT_URI,
                                    null,
                                    ContactsContract.CommonDataKinds.Email.CONTACT_ID + " = ?",
                                    new String[]{contactId}, null);
                            if (imageCursor.moveToNext()) {
                                imageURI = imageCursor.getString(imageCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.PHOTO_URI));
                              //  EasyBackUpUtils.v("Image_Uri : " + imageURI);
                            }
                            imageCursor.close();

                            // GET EMAIL
                            //------------------------------------------------------------------------------------------------------------------------

                            Cursor emailCur = contentResolver.query(
                                    ContactsContract.CommonDataKinds.Email.CONTENT_URI,
                                    null,
                                    ContactsContract.CommonDataKinds.Email.CONTACT_ID + " = ?",
                                    new String[]{contactId}, null);
                            while (emailCur.moveToNext()) {
                                int EmailType = emailCur.getInt(emailCur.getColumnIndex(ContactsContract.CommonDataKinds.Email.TYPE));
                                switch (EmailType) {
                                    case ContactsContract.CommonDataKinds.Email.TYPE_HOME:
                                        emailHome = emailCur.getString(emailCur.getColumnIndex(ContactsContract.CommonDataKinds.Email.DATA));
                                    //    EasyBackUpUtils.v("contactEmail" + " contactName " + contactName + ": TYPE_HOME " + " " + emailHome);
                                        break;
                                    case ContactsContract.CommonDataKinds.Email.TYPE_WORK:
                                        emailWork = emailCur.getString(emailCur.getColumnIndex(ContactsContract.CommonDataKinds.Email.DATA));
                                    //    EasyBackUpUtils.v("contactEmail" + " contactName " + contactName + ": TYPE_WORK " + " " + emailWork);
                                        break;
                                    default:
                                        break;
                                }
                            }
                            emailCur.close();


                            // GET BIRTHDATE
                            //--------------------------------------------------------------------------------------------------------------------------
                            String columns[] = {
                                    ContactsContract.CommonDataKinds.Event.START_DATE,
                                    ContactsContract.CommonDataKinds.Event.TYPE,
                                    ContactsContract.CommonDataKinds.Event.MIMETYPE,
                            };

                            String where = ContactsContract.CommonDataKinds.Event.TYPE + "=" + ContactsContract.CommonDataKinds.Event.TYPE_BIRTHDAY +
                                    " and " + ContactsContract.CommonDataKinds.Event.MIMETYPE + " = '" + ContactsContract.CommonDataKinds.Event.CONTENT_ITEM_TYPE + "' and "
                                    + ContactsContract.Data.CONTACT_ID + " = " + contactId;

                            String[] selectionArgs = null;
                            String sortOrder = ContactsContract.Contacts.DISPLAY_NAME;

                            Cursor birthdayCur = contentResolver.query(ContactsContract.Data.CONTENT_URI, columns, where, selectionArgs, sortOrder);
                            if (birthdayCur.getCount() > 0) {
                                while (birthdayCur.moveToNext()) {
                                    birthdate = birthdayCur.getString(birthdayCur.getColumnIndex(ContactsContract.CommonDataKinds.Event.START_DATE));
                                //    EasyBackUpUtils.v("myDate : " + birthdate);
                                }
                                //Convert Date into dd/mm format
                                SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
                                try {
                                    Date date = formatter.parse(birthdate);
                                    String OLD_FORMAT = "dd/MM";
                                    SimpleDateFormat newDateFormat = new SimpleDateFormat("dd-MM-yy HH:mm:ss");
                                    Date d = newDateFormat.parse(newDateFormat.format(date));
                                    newDateFormat.applyPattern(OLD_FORMAT);
                                    birthdate = newDateFormat.format(d);
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }

                                SimpleDateFormat formatter1 = new SimpleDateFormat("yyyy-mm-dd");
                                try {
                                    Date date = formatter1.parse(birthdate);
                                    String OLD_FORMAT = "dd/MM";
                                    SimpleDateFormat newDateFormat = new SimpleDateFormat("dd-MM-yy HH:mm:ss");
                                    Date d = newDateFormat.parse(newDateFormat.format(date));
                                    newDateFormat.applyPattern(OLD_FORMAT);
                                    birthdate = newDateFormat.format(d);
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }

                            }

                            birthdayCur.close();


                            // GET COMPANY DETAILS
                            //--------------------
                            String orgWhere = ContactsContract.Data.CONTACT_ID + " = ? AND " + ContactsContract.Data.MIMETYPE + " = ?";
                            String[] orgWhereParams = new String[]{contactId,
                                    ContactsContract.CommonDataKinds.Organization.CONTENT_ITEM_TYPE};
                            Cursor orgCur = contentResolver.query(ContactsContract.Data.CONTENT_URI,
                                    null, orgWhere, orgWhereParams, null);
                            if (orgCur.moveToFirst()) {
                                companyName = orgCur.getString(orgCur.getColumnIndex(ContactsContract.CommonDataKinds.Organization.DATA));
                                companyTitle = orgCur.getString(orgCur.getColumnIndex(ContactsContract.CommonDataKinds.Organization.TITLE));
                             //   EasyBackUpUtils.v("Comany Name :" + companyName + "Company Titile" + companyTitle);
                            }
                            orgCur.close();

                            // GET ADDRESS
                            //--------------------
                            String addrWhere = ContactsContract.Data.CONTACT_ID + " = ? AND " + ContactsContract.Data.MIMETYPE + " = ?";
                            String[] addrWhereParams = new String[]{contactId,
                                    ContactsContract.CommonDataKinds.StructuredPostal.CONTENT_ITEM_TYPE};
                            Cursor addrCur = contentResolver.query(ContactsContract.Data.CONTENT_URI,
                                    null, addrWhere, addrWhereParams, null);
                            while (addrCur.moveToNext()) {
                                String street = addrCur.getString(
                                        addrCur.getColumnIndex(ContactsContract.CommonDataKinds.StructuredPostal.STREET));
                                String city = addrCur.getString(
                                        addrCur.getColumnIndex(ContactsContract.CommonDataKinds.StructuredPostal.CITY));
                                String state = addrCur.getString(
                                        addrCur.getColumnIndex(ContactsContract.CommonDataKinds.StructuredPostal.REGION));
                                String postalCode = addrCur.getString(
                                        addrCur.getColumnIndex(ContactsContract.CommonDataKinds.StructuredPostal.POSTCODE));
                                String country = addrCur.getString(
                                        addrCur.getColumnIndex(ContactsContract.CommonDataKinds.StructuredPostal.COUNTRY));

                                addressModel = new AddressModel(street, city, state, postalCode, country);
                            }
                            addrCur.close();

                            if (addressModel == null) addressModel = new AddressModel();

                            ContactDetailsModel detailsModel = new ContactDetailsModel(contactId, companyTitle, imageURI, contactName, contactMobile, contactHome,
                                    contactWork, emailHome, emailWork, birthdate, companyName, lastUpdated, addressModel);
                            /*
                            EasyBackUpUtils.v("CheckContacts :-" + " ID " + contactId + "Title " + companyTitle + "Image " + imageURI + " Name " + contactName + " MobileNumber " + contactMobile
                                    + "Homme Number " + contactHome + " Work Number " + contactWork + " Email Home " + emailHome + "Email Work " + emailWork);*/
                            if (birthdate != null) {
                                birthDayList.add(detailsModel);
                            }
                            if (companyName != null) {
                                companyNameList.add(detailsModel);
                            }
                            if (companyTitle != null) {
                                jobTitleList.add(detailsModel);
                            }
                            contactList.add(detailsModel);
                        }
                    }
                }

                EasyBackUpUtils.setContactDetailsList(contactList);
                EasyBackUpUtils.setBirthDayContactList(birthDayList);
                EasyBackUpUtils.setJobTitleDetailsList(jobTitleList);
                EasyBackUpUtils.setCompanyDetailsList(companyNameList);
                Intent i = new Intent(SplashScreen.this, MainActivity.class);
                startActivity(i);

                // close this activity
                finish();
            }
        }, SPLASH_TIME_OUT);
    }


}
