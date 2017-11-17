package easybackup.place.ispl;

import android.widget.Filter;

import java.util.ArrayList;

import easybackup.place.ispl.adapters.AddressBookDemoAdapter;
import easybackup.place.ispl.models.ContactDetailsModel;

/**
 * Created by infinium on 28/06/17.
 */

public class CustomFilter extends Filter {
    AddressBookDemoAdapter mFilterAdapter;
    ArrayList<ContactDetailsModel> filterList;

    public CustomFilter(ArrayList<ContactDetailsModel> filterList,AddressBookDemoAdapter adapter)
    {
        this.mFilterAdapter=adapter;
        this.filterList=filterList;
    }

    @Override
    protected FilterResults performFiltering(CharSequence constraint) {
        FilterResults results=new FilterResults();
        //CHECK CONSTRAINT VALIDITY
        if(constraint != null && constraint.length() > 0)
        {
            //CHANGE TO UPPER
            constraint=constraint.toString().toUpperCase();
            //STORE OUR FILTERED PLAYERS
            ArrayList<ContactDetailsModel> filteredContacts=new ArrayList<>();
            for (int i=0;i<filterList.size();i++)
            {
                //CHECK
                if(filterList.get(i).getContactName().toUpperCase().contains(constraint))
                {
                    //ADD PLAYER TO FILTERED PLAYERS
                    filteredContacts.add(filterList.get(i));
                }
            }
            results.count=filteredContacts.size();
            results.values=filteredContacts;
        }else
        {
            results.count=filterList.size();
            results.values=filterList;
        }

        return results;
    }

    @Override
    protected void publishResults(CharSequence constraint, FilterResults results) {
        mFilterAdapter.mContactDetailList= (ArrayList<ContactDetailsModel>) results.values;
        //REFRESH
        mFilterAdapter.notifyDataSetChanged();
    }
}
