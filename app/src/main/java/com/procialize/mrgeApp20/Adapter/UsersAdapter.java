package com.procialize.mrgeApp20.Adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.Spannable;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.procialize.mrgeApp20.ApiConstant.ApiConstant;
import com.procialize.mrgeApp20.CustomTools.PicassoTrustAll;
import com.procialize.mrgeApp20.GetterSetter.AttendeeList;
import com.procialize.mrgeApp20.R;

import org.apache.commons.lang3.StringUtils;

import java.util.Locale;

import static android.content.Context.MODE_PRIVATE;
import static com.procialize.mrgeApp20.Session.ImagePathConstants.KEY_ATTENDEE_PIC_PATH;

/**
 * Adapter to the mentions list shown to display the result of an '@' mention.
 */
public class UsersAdapter extends RecyclerArrayAdapter<AttendeeList, UsersAdapter.UserViewHolder> {

    /**
     * {@link Context}.
     */
    private final Context context;

    /**
     * Current search string typed by the user.  It is used highlight the query in the
     * search results.  Ex: @bill.
     */
    private String currentQuery;

    /**
     * {@link ForegroundColorSpan}.
     */
    private final ForegroundColorSpan colorSpan;
    ApiConstant constant = new ApiConstant();
    String MY_PREFS_NAME = "ProcializeInfo";
    public UsersAdapter(final Context context) {
        this.context = context;
        final int orange = ContextCompat.getColor(context, R.color.mentions_default_color);
        this.colorSpan = new ForegroundColorSpan(orange);
    }

    /**
     * Setter for what user has queried.
     */
    public void setCurrentQuery(final String currentQuery) {
        if (StringUtils.isNotBlank(currentQuery)) {
            this.currentQuery = currentQuery.toLowerCase(Locale.US);
        }
    }

    /**
     * Create UI with views for user name and picture.
     */
    @Override
    public UserViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(context).inflate(R.layout.item_user, parent, false);
        return new UserViewHolder(view);
    }

    /**
     * Display user name and picture.
     */
    @Override
    public void onBindViewHolder(UserViewHolder holder, int position) {
        final AttendeeList mentionsUser = getItem(position);

        if (mentionsUser != null && StringUtils.isNotBlank(mentionsUser.getFirstName())) {

            SharedPreferences prefs1 = context.getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
            String picPath =  prefs1.getString(KEY_ATTENDEE_PIC_PATH,"");
            holder.image_url = /*constant.profilepic*/picPath + mentionsUser.getProfilePic();
            holder.name.setText(mentionsUser.getFirstName() + " " + mentionsUser.getLastName(), TextView.BufferType.SPANNABLE);
            highlightSearchQueryInUserName(holder.name.getText());

            PicassoTrustAll.getInstance(context).load(holder.image_url)
                    .placeholder(R.drawable.profilepic_placeholder)
                    .into(holder.imageView);


        }
    }

    /**
     * Highlights the current search text in the mentions list.
     */
    private void highlightSearchQueryInUserName(CharSequence userName) {
        if (StringUtils.isNotBlank(currentQuery)) {
            int searchQueryLocation = userName.toString().toLowerCase(Locale.US).indexOf(currentQuery);

            if (searchQueryLocation != -1) {
                Spannable userNameSpannable = (Spannable) userName;
                userNameSpannable.setSpan(
                        colorSpan,
                        searchQueryLocation,
                        searchQueryLocation + currentQuery.length(),
                        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
        }
    }

    /**
     * View holder for user.
     */
    static class UserViewHolder extends RecyclerView.ViewHolder {
        final TextView name;
        final ImageView imageView;
        String image_url;

        UserViewHolder(View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.user_full_name);
            imageView = itemView.findViewById(R.id.user_picture);
        }
    }
}

