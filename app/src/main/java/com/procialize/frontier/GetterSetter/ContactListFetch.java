package com.procialize.frontier.GetterSetter;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Rahul on 26-10-2018.
 */

public class ContactListFetch {

    @SerializedName("css_editor")
    @Expose
    private String cssEditor;
    @SerializedName("contact_list")
    @Expose
    private List<Contact> contactList = null;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("msg")
    @Expose
    private String msg;


    public String getCssEditor() {
        return cssEditor;
    }

    public void setCssEditor(String cssEditor) {
        this.cssEditor = cssEditor;
    }

    public List<Contact> getContactList() {
        return contactList;
    }

    public void setContactList(List<Contact> contactList) {
        this.contactList = contactList;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
