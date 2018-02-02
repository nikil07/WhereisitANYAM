package com.androidworks.nikhil.whereisitanyam;

/**
 * Created by Nikhil on 26-Sep-16.
 */
public class TimeZoneItem {

    private String city;
    private String country;
    private String timezone;
    private String timezoneID;
    private String countryID;
    private String message;
    private boolean selected;

    public TimeZoneItem() {
    }

    public TimeZoneItem(String city, String country, String timezone, String timezoneID) {
        this.city = city;
        this.country = country;
        this.timezone = timezone;
        this.timezoneID = timezoneID;
    }

    public TimeZoneItem(String city, String country, String timezone, String timezoneID, String countryID, String message, boolean selected) {
        this.city = city;
        this.country = country;
        this.timezone = timezone;
        this.timezoneID = timezoneID;
        this.countryID = countryID;
        this.message = message;
        this.selected = selected;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getTimezone() {
        return timezone;
    }

    public void setTimezone(String timezone) {
        this.timezone = timezone;
    }

    public String getTimezoneID() {
        return timezoneID;
    }

    public void setTimezoneID(String timezoneID) {
        this.timezoneID = timezoneID;
    }

    public String getCountryID() {
        return countryID;
    }

    public void setCountryID(String countryID) {
        this.countryID = countryID;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }
}
