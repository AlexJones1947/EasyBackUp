package easybackup.place.ispl.fragments;


import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

import easybackup.place.ispl.MainActivity;
import easybackup.place.ispl.R;
import easybackup.place.ispl.models.ContactDetailsModel;
import easybackup.place.ispl.utils.EasyBackUpUtils;
import easybackup.place.ispl.utils.KeyUtils;

import static easybackup.place.ispl.utils.EasyBackUpUtils.rootPath;


/**
 * Created by infinium on 4/14/17.
 */

//delete after ateching contact is remain

public class ContactDetailsViewFragment extends Fragment {
    private TextView mContactNameTV, mContactPhoneTV,mContactHomeTV, mEmailHometv,mBirthdateTV;
    private ImageView mUserImage, mBackImageBtn;
    private FloatingActionButton mEdit;
    private LinearLayout mCallHistoryLL,mShare_ContactLL;
    String vcfFile;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View layout_view = inflater.inflate(R.layout.fragment_contacts_details_view, container, false);
        ((MainActivity)getActivity()).setToolBarVisibility(false);
        mContactNameTV = (TextView) layout_view.findViewById(R.id.ContactNametv);
        mContactPhoneTV = (TextView) layout_view.findViewById(R.id.ContactPhonetv);
        mEmailHometv = (TextView) layout_view.findViewById(R.id.EmailHometv);
        mContactHomeTV = (TextView) layout_view.findViewById(R.id.ContactHometv);
        mBirthdateTV = (TextView) layout_view.findViewById(R.id.Birthdatetv);
        mUserImage = (ImageView) layout_view.findViewById(R.id.UserImage);
        mBackImageBtn = (ImageView) layout_view.findViewById(R.id.back_image_btn);
        mEdit = (FloatingActionButton) layout_view.findViewById(R.id.fab);
        mCallHistoryLL = (LinearLayout) layout_view.findViewById(R.id.call_history_layout);
        mShare_ContactLL = (LinearLayout) layout_view.findViewById(R.id.call_share_layout);

        mShare_ContactLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shareIt();
                // Start an alpha animation for clicked item
                Animation animation1 = new AlphaAnimation(0.3f, 2.0f);
                animation1.setDuration(2500);
                animation1.setBackgroundColor(Color.RED);
                mShare_ContactLL.startAnimation(animation1);
                //create selected contact vcf
                createVcfContact();
                //shared contact
                shareIt();
            }
        });
        mBackImageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        mEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Click action
                Animation animation1 = new AlphaAnimation(0.3f, 2.0f);
                animation1.setDuration(2500);
                animation1.setBackgroundColor(Color.RED);
                mEdit.startAnimation(animation1);
                EasyBackUpUtils.sIsEdited = true;
                Fragment editContactFragment = new EditContactFragment();
                ((MainActivity) getActivity()).switchFragment(editContactFragment, true, KeyUtils.EDIT_CONTACT_FRAGMENT_TAG);
            }
        });

        mCallHistoryLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Click action
                Animation animation1 = new AlphaAnimation(0.3f, 2.0f);
                animation1.setDuration(2500);
                animation1.setBackgroundColor(Color.RED);
                mCallHistoryLL.startAnimation(animation1);

                Fragment callHistoryFragment = new CallHistoryFragment();
                ((MainActivity) getActivity()).switchFragment(callHistoryFragment, true, KeyUtils.CALL_HISTORY_FRAGMENT_TAG);

            }
        });

        ContactDetailsModel details = EasyBackUpUtils.getSelectedContactDetails();
        if (details != null) {
            String contactId = details.getContactID();
            String name = details.getContactName();
            String mobileNumber = details.getContactMobileNumber();
            String homeNumber = details.getContactHomeNumber();
            String workNumber = details.getContactWorkNumber();
            String emailHome = details.getContactEmailHome();
            String emailWork = details.getContactEmailWork();
            String image = details.getContactImage();
            String birthdate = details.getContactBirthdate();
            EasyBackUpUtils.v("Image Name : " + image);
            mContactNameTV.setText(name);
            mContactPhoneTV.setText(mobileNumber);
            mContactHomeTV.setText(homeNumber);
            mEmailHometv.setText(emailHome);
            mBirthdateTV.setText(birthdate);
            if (image != null)
                Glide.with(this).load(image).into(mUserImage);

            ///  mUserImage.setImageURI(Uri.parse(image));
            EasyBackUpUtils.v("ContactDetailView " + "ContactId "+ contactId + "SelectedName : " + name + "\tMobile Number : " + mobileNumber + "\tHome Number : " + homeNumber + "\tWork Number : " + workNumber +
                    "                       \tEmail Home : " + emailHome + "\tEmail Work : " + emailWork + "\tImage : " + image);
        }

        return layout_view;
    }

    private void shareIt() {
        //get selected vcf file
        String sharedVcf = getsharedVcfFile();
        EasyBackUpUtils.v("SharedVcfName " + sharedVcf);

        if (sharedVcf != null) {
            File filelocation = new File(rootPath.getAbsolutePath(), sharedVcf);
            Uri path = Uri.fromFile(filelocation);
            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.setType(ContactsContract.Contacts.CONTENT_VCARD_TYPE);
            intent.putExtra(Intent.EXTRA_STREAM, path);
            startActivity(intent);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
       /* if (id == R.id.action_settings) {
            return true;
        }*/

        return super.onOptionsItemSelected(item);
    }
    private String getsharedVcfFile() {
        String latestFileName = null;
        File sdCardRoot = rootPath;
        List<String> fNameList = new ArrayList<>();
        for (File f : sdCardRoot.listFiles()) {

            if (f.isFile()) {
                String name = f.getName();
                ContactDetailsModel details = EasyBackUpUtils.getSelectedContactDetails();
                EasyBackUpUtils.v("File Name : " + name);
                if (name.contains(details.getContactName())) {
                    String fName = name.replace(".vcf", "");
                    EasyBackUpUtils.v("ContactVcfName " + fName);
                    latestFileName = name;
                    fNameList.add(fName);
                }
            }
        }
        return latestFileName;
    }

    private void createVcfContact() {
        ContactDetailsModel details = EasyBackUpUtils.getSelectedContactDetails();
        vcfFile = details.getContactName() + ".vcf";

        Cursor phones = getActivity().getContentResolver().query(ContactsContract.Contacts.CONTENT_URI, null, null, null, ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + " ASC");
        if (phones != null && phones.getCount() > 0) {
            if (phones.moveToFirst()) {

                String lookupKey = phones.getString(phones
                        .getColumnIndex(ContactsContract.Contacts.LOOKUP_KEY));
                Uri uri = Uri.withAppendedPath(
                        ContactsContract.Contacts.CONTENT_VCARD_URI,
                        lookupKey);
                AssetFileDescriptor fd;
                try {
                    fd = getActivity().getContentResolver().openAssetFileDescriptor(uri, "r");
                    FileInputStream fis = fd.createInputStream();
                    byte[] buf = new byte[(int) fd.getDeclaredLength()];
                    fis.read(buf);
                    String VCard = new String(buf);
                    String path = rootPath + File.separator + vcfFile;
                    FileOutputStream mFileOutputStream = new FileOutputStream(path,
                            true);
                    mFileOutputStream.write(VCard.getBytes());
                    EasyBackUpUtils.v("Vcard :" + VCard);
                } catch (Exception e1) {
                    e1.printStackTrace();
                }
                try {
                    Thread.sleep(100);
                } catch (Exception e) {
                }
            }
        }
    }

}