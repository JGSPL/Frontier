package com.procialize.frontier.GetterSetter;

import android.widget.EditText;

import com.percolate.mentions.Mentionable;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * A mention inserted into the {@link EditText}. All mentions inserted into the
 * {@link EditText} must implement the {@link Mentionable} interface.
 */
public class Mention implements Mentionable {

    private String mentionName;
    private String mentionid;


    private int offset;

    private int length;

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }

    @Override
    public int getMentionOffset() {
        return offset;
    }

    @Override
    public int getMentionLength() {
        return length;
    }

    @Override
    public String getMentionName() {
        return mentionName;
    }

    @Override
    public String getMentionid() {
        return mentionid;
    }

    @Override
    public void setMentionOffset(int offset) {
        this.offset = offset;
    }

    @Override
    public void setMentionLength(int length) {
        this.length = length;
    }

    @Override
    public void setMentionName(String mentionName) {
        this.mentionName = mentionName;
    }

    @Override
    public void setMentionid(String mentionid) {
        this.mentionid = mentionid;
    }
}
