package easybackup.place.ispl.models;

/**
 * Created by infinium on 4/27/17.
 */

public class AddressModel {
    private String mStreet2 = null, mCity = null, mState = null, mZipCode = null, mCountry = null;

    public AddressModel() {
        // Defaly Constructor
    }
    public AddressModel(String street2, String city, String state, String pinCode, String country) {
        this.mStreet2 = street2;
        this.mCity = city;
        this.mState = state;
        this.mZipCode = pinCode;
        this.mCountry = country;
    }

    public String getStreet2() {
        return mStreet2;
    }

    public String getCity() {
        return mCity;
    }

    public String getState() {
        return mState;
    }

    public String getZipCode() {
        return mZipCode;
    }

    public String getCountry() {
        return mCountry;
    }
}
