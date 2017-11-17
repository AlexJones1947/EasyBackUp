package easybackup.place.ispl.utils;

import android.util.Log;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import easybackup.place.ispl.models.ContactDetailsModel;

/**
 * Created by infinium on 3/24/17.
 */

public class EasyBackUpUtils {
    private static final String TAG = "EasyBackup";
    private static final boolean isDebug = true;
    private static ContactDetailsModel mContactDetails = null;
    private static List<ContactDetailsModel> mContactDetailsList = null;
    private static List<ContactDetailsModel> mSelectedContacDetailsList = null;
    private static List<ContactDetailsModel> mBirthDateContactList = null;
    private static List<ContactDetailsModel> mCompanyDetailsList = null;
    private static List<ContactDetailsModel> mJobTitleContactList = null;
    private static List<ContactDetailsModel> mRecentlyAddedList = null;
    private static String mCount = null;
    public static boolean sIsEdited = false;
    public static String sFileSavedType = "VCF File";
    public static final String mDNAME = "EasyBackUpFiles";
    public static File rootPath = null;
    private static Map<String, String> mFragmentBundleMap = new HashMap<String, String>();

    public static void v(String value) {
        if (isDebug) Log.v(TAG, value);
    }

    public static void setSelectedContactDetails(ContactDetailsModel detail) {
        mContactDetails = detail;
    }

    public static ContactDetailsModel getSelectedContactDetails() {
        return mContactDetails;
    }

    public static void setContactDetailsList(List<ContactDetailsModel> list) {
        mContactDetailsList = list;
    }

    public static List<ContactDetailsModel> getContactDetailsList() {
        return mContactDetailsList;
    }

    public static void setSelectedContactDetailsList(List<ContactDetailsModel> list) {
        mSelectedContacDetailsList = list;
    }

    public static List<ContactDetailsModel> getSelectedContactDetailsList() {
        return mSelectedContacDetailsList;
    }

    public static void setBirthDayContactList(List<ContactDetailsModel> list) {
        mBirthDateContactList = list;
    }

    public static List<ContactDetailsModel> getBirthDayContactList() {
        return mBirthDateContactList;
    }

    public static void setCompanyDetailsList(List<ContactDetailsModel> list) {
        mCompanyDetailsList = list;
    }

    public static List<ContactDetailsModel> getCompanyDetailsList() {
        return mCompanyDetailsList;
    }

    public static void setJobTitleDetailsList(List<ContactDetailsModel> list) {
        mJobTitleContactList = list;
    }

    public static List<ContactDetailsModel> getJobTitleDetailsList() {
        return mJobTitleContactList;
    }

    public static void setRecentlyAddedList(List<ContactDetailsModel> list) {
        mRecentlyAddedList = list;
    }

    public static List<ContactDetailsModel> getRecentlyAddedList() {
        return mRecentlyAddedList;
    }

    //------------------------------------------------------------------------------------------------------------------------------------------------------------------
    public static String  getBithdateCount() {
        return mCount;
    }
    public static void setBirthdateCount(String count) {
        mCount = count;
    }

    //------------------------------------------------------------------------------------------------------------------------------------------------------------------
}
