package easybackup.place.ispl.fragments;

import android.database.Cursor;
import android.os.Bundle;
import android.provider.CallLog;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.Date;

import easybackup.place.ispl.R;
import easybackup.place.ispl.models.ContactDetailsModel;
import easybackup.place.ispl.utils.EasyBackUpUtils;

/**
 * Created by infinium on 12/06/17.
 */

public class CallHistoryFragment extends Fragment {
    private String mContactMobileNumber = null;
    private TextView mCallHistory =null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View layout_view = inflater.inflate(R.layout.fragment_call_history, container, false);
        mCallHistory = (TextView) layout_view.findViewById(R.id.contact_call_history_tv);
        ContactDetailsModel model = EasyBackUpUtils.getSelectedContactDetails();

            mContactMobileNumber = model.getContactMobileNumber();
            EasyBackUpUtils.v("ContactDetailView" + mContactMobileNumber);
            getCallDetails();


        return layout_view;
    }

    private void getCallDetails() {


        StringBuffer sb = new StringBuffer();
        Cursor managedCursor = getActivity().managedQuery(CallLog.Calls.CONTENT_URI, null, null, null, null);
        int number = managedCursor.getColumnIndex(CallLog.Calls.NUMBER);
        int type = managedCursor.getColumnIndex(CallLog.Calls.TYPE);
        int date = managedCursor.getColumnIndex(CallLog.Calls.DATE);
        int duration = managedCursor.getColumnIndex(CallLog.Calls.DURATION);
        sb.append("Call Details :");
        while (managedCursor.moveToNext()) {

            String phNumber = managedCursor.getString(number);

            if (phNumber.equals(mContactMobileNumber)) {

                String callType = managedCursor.getString(type);
                String callDate = managedCursor.getString(date);
                Date callDayTime = new Date(Long.valueOf(callDate));
                String callDuration = managedCursor.getString(duration);
                String dir = null;
                int dircode = Integer.parseInt(callType);
                switch (dircode) {
                    case CallLog.Calls.OUTGOING_TYPE:
                        dir = "OUTGOING";
                        break;

                    case CallLog.Calls.INCOMING_TYPE:
                        dir = "INCOMING";
                        break;

                    case CallLog.Calls.MISSED_TYPE:
                        dir = "MISSED";
                        break;
                }
                sb.append("\nPhone Number:--- " + phNumber + " \nCall Type:--- " + dir + " \nCall Date:--- " + callDayTime + " \nCall duration in sec :--- " + callDuration);
                sb.append("\n----------------------------------");
            }
        }
        managedCursor.close();
        mCallHistory.setText(sb);
        EasyBackUpUtils.v("ContactHistory" + sb);
    }
}
