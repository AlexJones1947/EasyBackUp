package easybackup.place.ispl;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.util.List;

import easybackup.place.ispl.fragments.BackupsFragment;
import easybackup.place.ispl.fragments.ContactsFragment;
import easybackup.place.ispl.fragments.EditContactFragment;
import easybackup.place.ispl.fragments.NewContactFragment;
import easybackup.place.ispl.fragments.SettingsFragment;
import easybackup.place.ispl.models.ContactDetailsModel;
import easybackup.place.ispl.utils.EasyBackUpUtils;
import easybackup.place.ispl.utils.KeyUtils;

import static easybackup.place.ispl.utils.EasyBackUpUtils.mDNAME;
import static easybackup.place.ispl.utils.EasyBackUpUtils.rootPath;

public class MainActivity extends AppCompatActivity {
    private TextView mTitleBackups = null;
    private ImageView mEditIV = null, mAddIV = null, mCloseIV = null;
    private Toolbar mToolBar = null;
    private BottomNavigationView mMainNV = null, mEditNV = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mToolBar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolBar);

        List<ContactDetailsModel> contactDetailsModelList = EasyBackUpUtils.getBirthDayContactList();
        EasyBackUpUtils.v("Birthdate Count :" +contactDetailsModelList.size());

        rootPath = new File(Environment.getExternalStorageDirectory(), mDNAME);
        if(!rootPath.exists()) rootPath.mkdirs();

        getSupportActionBar().setDisplayShowTitleEnabled(false);

        mTitleBackups = (TextView) findViewById(R.id.toolbar_title_backups);
        mEditIV = (ImageView) findViewById(R.id.edit_imageview);
        mAddIV = (ImageView) findViewById(R.id.add_imageview);
        mCloseIV = (ImageView) findViewById(R.id.close_imageview);

        mAddIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setToolBarVisibility(true);
                Animation animation1 = new AlphaAnimation(0.3f, 2.0f);
                animation1.setDuration(2500);
                animation1.setBackgroundColor(Color.RED);
                mAddIV.startAnimation(animation1);
               /* Intent intent = new Intent("android.intent.action.INSERT", ContactsContract.Contacts.CONTENT_URI);
                startActivity(intent);*/
                Fragment newFragment = new NewContactFragment();
                switchFragment(newFragment, true, KeyUtils.NEW_CONTACT_FRAGMENT_TAG);
            }
        });
        mMainNV = (BottomNavigationView) findViewById(R.id.navigation);
        mMainNV.getMenu().getItem(1).setChecked(true);
        mMainNV.setVisibility(View.VISIBLE);
        mMainNV.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment selectedFragment = null;
                switch (item.getItemId()) {
                    case R.id.contacts_item:
                        selectedFragment = new ContactsFragment();
                        mTitleBackups.setText(R.string.contacts);
                        setEditBtnVisibility(false);
                        setToolBarVisibility(true);
                        mAddIV.setVisibility(View.VISIBLE);
                        break;
                    case R.id.backups_item:
                        selectedFragment = new BackupsFragment();
                        mTitleBackups.setText(R.string.backups);
                        setEditBtnVisibility(false);
                        setToolBarVisibility(true);
                        mAddIV.setVisibility(View.GONE);
                        break;
                    case R.id.more_item:
                        selectedFragment = new SettingsFragment();
                        mTitleBackups.setText(R.string.settings);
                        setEditBtnVisibility(false);
                        setToolBarVisibility(true);
                        mAddIV.setVisibility(View.GONE);
                        break;
                }
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.main_fragment_container, selectedFragment);
                transaction.commit();
                return true;
            }
        });

        //Manually displaying the first fragment - one time only
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.main_fragment_container, new BackupsFragment());
        transaction.commit();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (EasyBackUpUtils.sIsEdited) {
            EasyBackUpUtils.sIsEdited = false;
            Fragment editContactFragment = new EditContactFragment();
            switchFragment(editContactFragment, true, KeyUtils.EDIT_CONTACT_FRAGMENT_TAG);
        }
    }

    public void setAddBtnVisibility(Boolean visible) {
        if (visible) {
            mAddIV.setVisibility(View.VISIBLE);
        } else {
            mAddIV.setVisibility(View.GONE);
        }
    }

    public void setEditBtnVisibility(Boolean visible) {
        if (visible) {
            mEditIV.setVisibility(View.VISIBLE);
        } else {
            mEditIV.setVisibility(View.GONE);
        }
    }

    public void setCloseBtnVisibility(Boolean visible) {
        if (visible) {
            mCloseIV.setVisibility(View.VISIBLE);
        } else {
            mCloseIV.setVisibility(View.GONE);
        }
    }

    public void setToolBarVisibility(Boolean visible) {
        if (visible) {
            mToolBar.setVisibility(View.VISIBLE);
        } else {
            mToolBar.setVisibility(View.GONE);
        }
    }

    public void setMainNavigationVisibility(Boolean visible) {
        if (visible) {
            mMainNV.setVisibility(View.VISIBLE);
        } else {
            mMainNV.setVisibility(View.GONE);
        }
    }

    public void setTitleBackupsText(String title) {
        mTitleBackups.setText(title);
    }

    public void switchFragment(Fragment fragment, boolean isAddBackStack, String tag) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.main_fragment_container, fragment, tag);
        if (isAddBackStack) transaction.addToBackStack(null);
        transaction.commit();
    }

}
