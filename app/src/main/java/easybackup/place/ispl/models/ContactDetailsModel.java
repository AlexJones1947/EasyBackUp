package easybackup.place.ispl.models;

import android.widget.CheckBox;

import easybackup.place.ispl.utils.EasyBackUpUtils;

/**
 * Created by infinium on 4/3/17.
 */

public class ContactDetailsModel {

    public static final int ContactDetailsModel = 0;
    private String mContactID = null;
    private String mContactName = null;
    private String mCompanyName = null;
    private String mContactTitle = null;
    private String mContactImage = null;
    private String mContactMobileNumber = null;
    private String mContactHomeNumber = null;
    private String mContactWorkNumber = null;
    private String mContactEmailHome = null;
    private String mContactEmailWork = null;
    private String mContactBirthdate = null;
    private String mContactLastUpdated = null;

    private boolean isSelected;
    private CheckBox isChecked;
    private AddressModel mAddressModel = null;

    public ContactDetailsModel(String id, String title, String image, String name, String mobileNumber, String homeNumber, String workNumber, String emailHome, String emailWork, String birthdate, String companyName, String lastUpdated, AddressModel addressModel) {
        this.mContactID = id;
        this.mContactImage = image;
        this.mContactName = name;
        this.mContactLastUpdated = lastUpdated;
        this.mContactTitle = title;
        this.mContactMobileNumber = mobileNumber;
        this.mContactHomeNumber = homeNumber;
        this.mContactWorkNumber = workNumber;
        this.mContactEmailHome = emailHome;
        this.mContactEmailWork = emailWork;
        this.mContactBirthdate = birthdate;
        this.mCompanyName = companyName;
        this.mAddressModel = addressModel;

       /* EasyBackUpUtils.v("ContactDetailModel :-" +" ID " + id + "Title " + title + "Image " + image + " Name " + name + " MobileNumber " +mobileNumber
        +"Homme Number " + homeNumber + " Work Number " + workNumber + " Email Home " +  emailHome + " Email Work " + emailWork  );*/

       /* EasyBackUpUtils.v("ContactDetailModel :-" +" ID " + mContactID + "Title " + mContactTitle + "Image " + mContactImage + " Name " + mContactName + " MobileNumber " +mContactMobileNumber
                +"Homme Number " + mContactHomeNumber + " Work Number " + mContactWorkNumber + " Email Home " +  mContactEmailHome + " Email Work " + mContactEmailWork  );*/
    }

    public String getContactID() {
        return mContactID;
    }

    public String getContactName() {
        return mContactName;
    }

    public String getContactImage() {
        return mContactImage;
    }

    public String getCompanyName() {
        return mCompanyName;
    }

    public String getContactLastUpdated() {
        return mContactLastUpdated;
    }

    public String getContactTitle() {
        return mContactTitle;
    }

    public String getContactMobileNumber() {
        return mContactMobileNumber;
    }

    public String getContactWorkNumber() {
        return mContactWorkNumber;
    }

    public String getContactHomeNumber() { return mContactHomeNumber; }

    public String getContactEmailHome() {
        return mContactEmailHome;
    }

    public String getContactEmailWork() {
        return mContactEmailWork;
    }

    public String getContactBirthdate() {
        return mContactBirthdate;
    }

    public AddressModel getAddressModel() {
        return mAddressModel;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean isSelected) {
        this.isSelected = isSelected;
    }

    public void setCheckBoxSelected(CheckBox isChecked){
        this.isChecked = isChecked;
        EasyBackUpUtils.v("CheckBoxTest2 :" +this.isChecked);
    }

    public CheckBox getCheckBoxSelected() {
        EasyBackUpUtils.v("CheckBoxTest3 :" +isChecked);
        return isChecked;
    }
}
