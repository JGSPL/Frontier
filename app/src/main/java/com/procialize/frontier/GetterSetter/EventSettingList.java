package com.procialize.frontier.GetterSetter;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Naushad on 12/22/2017.
 */

public class EventSettingList {
    @SerializedName("field_name")
    @Expose
    private String fieldName;
    @SerializedName("field_value")
    @Expose
    private String fieldValue;

    @SerializedName("is_parent_id")
    @Expose
    private String is_parent_id;

    @SerializedName("sub_menu")
    @Expose
    private List<EventSubMenuSetting> sub_menuList = null;

    public String getIs_parent_id() {
        return is_parent_id;
    }

    public void setIs_parent_id(String is_parent_id) {
        this.is_parent_id = is_parent_id;
    }

    public List<EventSubMenuSetting> getSub_menuList() {
        return sub_menuList;
    }

    public void setSub_menuList(List<EventSubMenuSetting> sub_menuList) {
        this.sub_menuList = sub_menuList;
    }

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public String getFieldValue() {
        return fieldValue;
    }

    public void setFieldValue(String fieldValue) {
        this.fieldValue = fieldValue;
    }
}
