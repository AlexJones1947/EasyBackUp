package easybackup.place.ispl;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.webkit.MimeTypeMap;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import easybackup.place.ispl.utils.EasyBackUpUtils;

import static easybackup.place.ispl.utils.EasyBackUpUtils.rootPath;

/**
 * Created by infinium on 4/13/17.
 */

public class VCFFileList extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.listview_activity_vcf_file);

        final List<String> fileList = getListOfFiles();
        ListView restoreLV = (ListView) findViewById(R.id.restore_file_listview);
        final StableArrayAdapter adapter = new StableArrayAdapter(this,
                android.R.layout.simple_list_item_1, fileList);
        restoreLV.setAdapter(adapter);

        restoreLV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                final String selectedFileName = fileList.get(position);
                final Dialog dialog = new Dialog(VCFFileList.this);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.custom_restore_vcf_dailog);
                dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                dialog.setCanceledOnTouchOutside(false);
                TextView errorTV = (TextView) dialog.findViewById(R.id.error_text);

                Button emailBtn = (Button) dialog.findViewById(R.id.dialog_email_btn);
                emailBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        File filelocation = new File(rootPath.getAbsolutePath(), selectedFileName);
                        Uri path = Uri.fromFile(filelocation);
                        Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                                "mailto", "abc@gmail.com", null));
                        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Contact Back Up");
                        emailIntent .putExtra(Intent.EXTRA_STREAM, path);
                        startActivity(Intent.createChooser(emailIntent, null));
                        dialog.dismiss();
                    }
                });

                Button restoreBtn = (Button) dialog.findViewById(R.id.dialog_restore_btn);
                restoreBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final Intent intent = new Intent();

                        final MimeTypeMap mime = MimeTypeMap.getSingleton();
                        String tmptype = mime.getMimeTypeFromExtension("vcf");
                        final File file = new File(rootPath.getAbsolutePath() +selectedFileName);

                        intent.setDataAndType(Uri.fromFile(file),tmptype);
                        startActivity(intent);

                        dialog.dismiss();
                    }
                });
                dialog.show();
            }
        });
    }

    private List<String> getListOfFiles() {
        List<String> fileNameList = new ArrayList<>();
        File sdCardRoot = rootPath;
        for (File f : sdCardRoot.listFiles()) {
            if (f.isFile()) {
                String name = f.getName();
                EasyBackUpUtils.v("File Name : " + name);
                if(name.contains(".vcf")) {
                    String fName =  name.replace(".vcf","");
                    Date date=new Date(Long.parseLong(fName));
                    SimpleDateFormat df2 = new SimpleDateFormat("dd-MM-yy hh:mm:ss");
                    String dateText = df2.format(date);
                    fileNameList.add(dateText + ".vcf");
                }
            }
        }
        return fileNameList;
    }

    private class StableArrayAdapter extends ArrayAdapter<String> {

        HashMap<String, Integer> mIdMap = new HashMap<String, Integer>();

        StableArrayAdapter(Context context, int textViewResourceId,
                           List<String> objects) {
            super(context, textViewResourceId, objects);
            for (int i = 0; i < objects.size(); ++i) {
                mIdMap.put(objects.get(i), i);
            }
        }

        @Override
        public long getItemId(int position) {
            String item = getItem(position);
            //Toast.makeText(getApplication(),"item is "+item+"clicked",Toast.LENGTH_LONG).show();
            return mIdMap.get(item);
        }

        @Override
        public boolean hasStableIds() {
            return true;
        }

    }
}
