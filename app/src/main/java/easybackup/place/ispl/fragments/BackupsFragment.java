package easybackup.place.ispl.fragments;

import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import easybackup.place.ispl.R;
import easybackup.place.ispl.WebViewActivity;
import easybackup.place.ispl.models.ContactDetailsModel;
import easybackup.place.ispl.utils.EasyBackUpUtils;

import static easybackup.place.ispl.utils.EasyBackUpUtils.rootPath;

/**
 * Created by infinium on 4/3/17.
 */

public class BackupsFragment extends Fragment {
    private TextView mProgressTV;
    private ProgressBar progressBar;
    private Button mBackUpBtn, mSendEmailBtn;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View layout_view = inflater.inflate(R.layout.fragment_backups, container, false);
        TextView mBackupsTV = (TextView) layout_view.findViewById(R.id.backups_tv);
        progressBar = (ProgressBar) layout_view.findViewById(R.id.progressBar);
        mProgressTV = (TextView) layout_view.findViewById(R.id.progress_tv);
        mBackUpBtn = (Button) layout_view.findViewById(R.id.backup_btn);
        mSendEmailBtn = (Button) layout_view.findViewById(R.id.sendemail_btn);

        mSendEmailBtn.setVisibility(View.GONE);

        mBackupsTV.setText("Total " + EasyBackUpUtils.getContactDetailsList().size() + " Contacts");

        mBackUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AsyncTaskRunner runner = new AsyncTaskRunner();
                runner.execute();
            }
        });

        mSendEmailBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String[] values = {"VCF File", "PDF File", "CSV File"};
                if (EasyBackUpUtils.sFileSavedType.equalsIgnoreCase(values[0])) {
                    String latestFileName = getLastVcfBackupFile();
                    if (latestFileName != null) {
                        File filelocation = new File(rootPath.getAbsolutePath(), latestFileName);
                        Uri
                                path = Uri.fromFile(filelocation);
                        Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                                "mailto", "abc@gmail.com", null));
                        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Contact Back Up");
                        emailIntent.putExtra(Intent.EXTRA_STREAM, path);
                        getActivity().startActivity(Intent.createChooser(emailIntent, null));
                    }
                } else if (EasyBackUpUtils.sFileSavedType.equalsIgnoreCase(values[1])) {
                    String pdfFileName = getPdfBackupFile();
                    if (pdfFileName != null) {
                        File filelocation = new File(rootPath.getAbsolutePath(), pdfFileName);
                        Uri path = Uri.fromFile(filelocation);
                        Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                                "mailto", "abc@gmail.com", null));
                        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Contact Back Up");
                        emailIntent.putExtra(Intent.EXTRA_STREAM, path);
                        getActivity().startActivity(Intent.createChooser(emailIntent, null));
                    }
                } else {
                    String csvFileName = getCsvBackupFile();
                    if (csvFileName != null) {
                        File filelocation = new File(rootPath.getAbsolutePath(), csvFileName);
                        Uri path = Uri.fromFile(filelocation);
                        Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                                "mailto", "abc@gmail.com", null));
                        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Contact Back Up");
                        emailIntent.putExtra(Intent.EXTRA_STREAM, path);
                        getActivity().startActivity(Intent.createChooser(emailIntent, null));
                    }
                }

            }
        });
        return layout_view;
    }

    private String getPdfBackupFile() {
        String latestFileName = null;
        File sdCardRoot = rootPath;
        List<String> fNameList = new ArrayList<>();
        for (File f : sdCardRoot.listFiles()) {

            if (f.isFile()) {
                String name = f.getName();
                EasyBackUpUtils.v("File Name : " + name);
                if (name.contains(".pdf")) {
                    String fName = name.replace(".pdf", "");
                    latestFileName = name;
                    fNameList.add(fName);
                }
            }
        }
        return latestFileName;
    }

    private String getCsvBackupFile() {
        String latestFileName = null;
        File sdCardRoot = rootPath;
        List<String> fileNameList = new ArrayList<>();
        for (File f : sdCardRoot.listFiles()) {

            if (f.isFile()) {
                String name = f.getName();
                EasyBackUpUtils.v("File Name : " + name);
                if (name.contains(".csv")) {
                    String fName = name.replace(".csv", "");
                    latestFileName = name;
                    fileNameList.add(fName);
                }
            }
        }
        return latestFileName;
    }

    private String getLastVcfBackupFile() {
        String latestFileName = null;
        File sdCardRoot = rootPath;
        List<Long> lNameList = new ArrayList<>();
        for (File f : sdCardRoot.listFiles()) {

            if (f.isFile()) {
                String name = f.getName();
                EasyBackUpUtils.v("File Name : " + name);
                if (name.contains(".vcf")) {
                    String fName = name.replace(".vcf", "");
                    lNameList.add(Long.parseLong(fName));
                }
            }
        }
        if (lNameList != null && lNameList.size() > 0) {
            long largeFileName = Collections.max(lNameList);
            EasyBackUpUtils.v("Largest File Name : " + largeFileName);
            latestFileName = largeFileName + ".vcf";
        }
        return latestFileName;
    }

    private class AsyncTaskRunner extends AsyncTask<Void, Integer, Void> {
        // final Animation animation = AnimationUtils.loadAnimation(getActivity(), R.anim.animation);

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            mProgressTV.setVisibility(View.VISIBLE);
            progressBar.setProgress(0);

            //    mBackupImage.setAnimation(animation);
            //   mBackupImage.startAnimation(animation);
        }

        @Override
        protected Void doInBackground(Void... params) {
            String[] values = {"VCF File", "PDF File", "CSV File"};
            if (EasyBackUpUtils.sFileSavedType.equalsIgnoreCase(values[0])) {
                // VCF
                final String vfile = System.currentTimeMillis() + ".vcf";
                //final String vfile = "Contacts.vcf";
                int progressStatus = 0;
                Cursor phones = getActivity().getContentResolver().query(ContactsContract.Contacts.CONTENT_URI, null, null, null, ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + " ASC");
                if (phones != null && phones.getCount() > 0) {
                    if (phones.moveToFirst()) {
                        do {
                            String lookupKey = phones.getString(phones
                                    .getColumnIndex(ContactsContract.Contacts.LOOKUP_KEY));
                            Uri uri = Uri.withAppendedPath(
                                    ContactsContract.Contacts.CONTENT_VCARD_URI,
                                    lookupKey);
                            AssetFileDescriptor fd;
                            try {
                                fd = getActivity().getContentResolver().openAssetFileDescriptor(uri, "r");
                                FileInputStream fis = fd != null ? fd.createInputStream() : null;
                                byte[] buf = new byte[(int) (fd != null ? fd.getDeclaredLength() : 0)];
                                fis.read(buf);
                                String VCard = new String(buf);
                                String path = rootPath + File.separator + vfile;
                                FileOutputStream mFileOutputStream = new FileOutputStream(path,
                                        true);
                                mFileOutputStream.write(VCard.getBytes());
                                EasyBackUpUtils.v("Vcard :" + VCard);
                            } catch (Exception e1) {
                                e1.printStackTrace();
                            }
                            progressStatus++;
                            try {
                                Thread.sleep(100);
                            } catch (Exception ignored) {
                            }
                            publishProgress(progressStatus * 100 / phones.getCount());
                        } while (phones.moveToNext());
                    }

                }

            } else if (EasyBackUpUtils.sFileSavedType.equalsIgnoreCase(values[1])) {
                // PDF
                final String vfile = System.currentTimeMillis() + ".vcf";
                //final String vfile = "Contacts.vcf";
                int progressStatus = 0;
                Cursor phones = getActivity().getContentResolver().query(ContactsContract.Contacts.CONTENT_URI, null, null, null, ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + " ASC");
                if (phones != null && phones.getCount() > 0) {
                    if (phones.moveToFirst()) {
                        do {
                            String lookupKey = phones.getString(phones
                                    .getColumnIndex(ContactsContract.Contacts.LOOKUP_KEY));
                            Uri uri = Uri.withAppendedPath(
                                    ContactsContract.Contacts.CONTENT_VCARD_URI,
                                    lookupKey);
                            AssetFileDescriptor fd;
                            try {
                                fd = getActivity().getContentResolver().openAssetFileDescriptor(uri, "r");
                                FileInputStream fis = fd != null ? fd.createInputStream() : null;
                                byte[] buf = new byte[(int) (fd != null ? fd.getDeclaredLength() : 0)];
                                fis.read(buf);
                                String VCard = new String(buf);
                                String path = rootPath + File.separator + vfile;
                                FileOutputStream mFileOutputStream = new FileOutputStream(path,
                                        true);
                                mFileOutputStream.write(VCard.getBytes());
                                EasyBackUpUtils.v("Vcard :" + VCard);
                            } catch (Exception e1) {
                                // TODO Auto-generated catch block
                                e1.printStackTrace();
                            }
                            // progress % and processBar
                            progressStatus++;
                            try {
                                Thread.sleep(100);
                            } catch (Exception ignored) {
                            }
                            publishProgress(progressStatus * 100 / phones.getCount());
                        } while (phones.moveToNext());
                    }

                }
                Intent intent = new Intent(getActivity(), WebViewActivity.class);
                startActivity(intent);
            } else {
                // CSV

                final String vfile = "Contacts.csv";
                //final String vfile = "Contacts.vcf";
                int progressStatus = 0;
                Cursor phones = getActivity().getContentResolver().query(ContactsContract.Contacts.CONTENT_URI, null, null, null, ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + " ASC");
                if (phones != null && phones.getCount() > 0) {
                    if (phones.moveToFirst()) {
                        do {
                            String lookupKey = phones.getString(phones
                                    .getColumnIndex(ContactsContract.Contacts.LOOKUP_KEY));
                            Uri uri = Uri.withAppendedPath(
                                    ContactsContract.Contacts.CONTENT_VCARD_URI,
                                    lookupKey);
                            AssetFileDescriptor fd;
                            try {
                                fd = getActivity().getContentResolver().openAssetFileDescriptor(uri, "r");
                                assert fd != null;
                                FileInputStream fis = fd.createInputStream();
                                byte[] buf = new byte[(int) fd.getDeclaredLength()];
                                fis.read(buf);
                                String VCard = new String(buf);
                                String path = rootPath + File.separator + vfile;
                                FileOutputStream mFileOutputStream = new FileOutputStream(path,
                                        true);
                                mFileOutputStream.write(VCard.getBytes());
                                EasyBackUpUtils.v("Vcard :" + VCard);
                            } catch (Exception e1) {
                                // TODO Auto-generated catch block
                                e1.printStackTrace();
                            }
                            progressStatus++;
                            try {
                                Thread.sleep(100);
                            } catch (Exception ignored) {
                            }
                            publishProgress(progressStatus * 100 / phones.getCount());
                        } while (phones.moveToNext());
                    }

                }
                String path = rootPath
                        .toString() + File.separator + vfile;

                generateCsvFile(path);

            }
            return null;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            // TODO Auto-generated method stub
            progressBar.setProgress(values[0]);
            mProgressTV.setText("Backing up contact.." + values[0] + "%");
            //mBackUpBtn.setText("Backing Up Please Wait...");
        }

        @Override
        protected void onPostExecute(Void result) {
            // TODO Auto-generated method stub
            mProgressTV.setText(R.string.backup_complete);
            //mBackUpBtn.setText(R.string.send_email);
            mBackUpBtn.setVisibility(View.GONE);
            mSendEmailBtn.setVisibility(View.VISIBLE);

        }
    }

    private static void generateCsvFile(String sFileName) {
        try {
            FileWriter fileWriter = new FileWriter(sFileName, true);
            BufferedWriter bfWriter = new BufferedWriter(fileWriter);

            List<ContactDetailsModel> selectedContatList = new ArrayList<>();
            selectedContatList = null;
            selectedContatList = EasyBackUpUtils.getContactDetailsList();
            for (ContactDetailsModel model : selectedContatList) {
                bfWriter.append(model.getContactImage());
                bfWriter.append(',');
                bfWriter.append(model.getContactName());
                bfWriter.append(',');
                bfWriter.append(model.getContactMobileNumber());
                bfWriter.append(',');
                bfWriter.append(model.getContactHomeNumber());
                bfWriter.append(',');
                bfWriter.append(model.getContactWorkNumber());
                bfWriter.append(',');
                bfWriter.append(model.getContactEmailHome());
                bfWriter.append(',');
                bfWriter.append(model.getContactEmailWork());
                bfWriter.append(',');
                bfWriter.append(model.getContactTitle());
                bfWriter.append(',');
                bfWriter.append(model.getContactBirthdate());
                bfWriter.append('\n');

            }
            //generate whatever data you want

            bfWriter.flush();
            bfWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
