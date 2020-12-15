package com.procialize.frontier.GetterSetter;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Naushad on 12/11/2017.
 */

public class DocumentsListFetch {

    @SerializedName("document_list")
    @Expose
    private List<DocumentList> documentList = null;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("msg")
    @Expose
    private String msg;
    @SerializedName("document_url_path")
    @Expose
    private String document_url_path;

    public String getDocument_url_path() {
        return document_url_path;
    }

    public void setDocument_url_path(String document_url_path) {
        this.document_url_path = document_url_path;
    }

    public List<DocumentList> getDocumentList() {
        return documentList;
    }

    public void setDocumentList(List<DocumentList> documentList) {
        this.documentList = documentList;
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
