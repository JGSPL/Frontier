
package com.procialize.mrgeApp20.GetterSetter;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UserData {

    @SerializedName("attendee_id")
    @Expose
    private String attendeeId;
    @SerializedName("first_name")
    @Expose
    private String firstName;
    @SerializedName("last_name")
    @Expose
    private String lastName;
    @SerializedName("api_access_token")
    @Expose
    private String apiAccessToken;
    @SerializedName("email")
    @Expose
    private String email;
    @SerializedName("company_name")
    @Expose
    private String companyName;
    @SerializedName("designation")
    @Expose
    private String designation;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("city")
    @Expose
    private String city;
    @SerializedName("country")
    @Expose
    private String country;
    @SerializedName("profile_pic")
    @Expose
    private String profilePic;
    @SerializedName("mobile")
    @Expose
    private String mobile;

    @SerializedName("exhibitor_id")
    @Expose
    private String exhibitor_id;

    public String getExhibitor_id() {
        return exhibitor_id;
    }

    public void setExhibitor_id(String exhibitor_id) {
        this.exhibitor_id = exhibitor_id;
    }

    public String getExhibitor_status() {
        return exhibitor_status;
    }

    public void setExhibitor_status(String exhibitor_status) {
        this.exhibitor_status = exhibitor_status;
    }

    @SerializedName("exhibitor_status")
    @Expose
    private String exhibitor_status;

    public String getAttendee_status() {
        return attendee_status;
    }

    public void setAttendee_status(String attendee_status) {
        this.attendee_status = attendee_status;
    }

    @SerializedName("attendee_status")
    @Expose
    private String attendee_status;

    public String getAttendeeId() {
        return attendeeId;
    }

    public void setAttendeeId(String attendeeId) {
        this.attendeeId = attendeeId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getApiAccessToken() {
        return apiAccessToken;
    }

    public void setApiAccessToken(String apiAccessToken) {
        this.apiAccessToken = apiAccessToken;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getDesignation() {
        return designation;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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

    public String getProfilePic() {
        return profilePic;
    }

    public void setProfilePic(String profilePic) {
        this.profilePic = profilePic;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

}
