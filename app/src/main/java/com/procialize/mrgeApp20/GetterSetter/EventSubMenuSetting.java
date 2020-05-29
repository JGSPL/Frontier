package com.procialize.mrgeApp20.GetterSetter;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class EventSubMenuSetting {

    @SerializedName("field_name")
    @Expose
    private String fieldName;
    @SerializedName("field_value")
    @Expose
    private String fieldValue;

    @SerializedName("is_parent_id")
    @Expose
    private String is_parent_id;

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

    public String getIs_parent_id() {
        return is_parent_id;
    }

    public void setIs_parent_id(String is_parent_id) {
        this.is_parent_id = is_parent_id;
    }
}
