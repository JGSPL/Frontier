package com.procialize.mrgeApp20.Speaker.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class PdfList implements Serializable {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("pdf_name")
    @Expose
    private String pdf_name;
    @SerializedName("pdf_file")
    @Expose
    private String pdf_file;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPdf_name() {
        return pdf_name;
    }

    public void setPdf_name(String pdf_name) {
        this.pdf_name = pdf_name;
    }

    public String getPdf_file() {
        return pdf_file;
    }

    public void setPdf_file(String pdf_file) {
        this.pdf_file = pdf_file;
    }
}
