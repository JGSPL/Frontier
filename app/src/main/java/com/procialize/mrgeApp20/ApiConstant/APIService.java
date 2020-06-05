package com.procialize.mrgeApp20.ApiConstant;

/**
 * Created by Preet on 10/30/2017.
 */


import com.procialize.mrgeApp20.BuddyList.DataModel.FetchBuddyList;
import com.procialize.mrgeApp20.BuddyList.DataModel.FetchChatList;
import com.procialize.mrgeApp20.BuddyList.DataModel.FetchSendRequest;
import com.procialize.mrgeApp20.Engagement.Models.FetchEngagementData;
import com.procialize.mrgeApp20.GetterSetter.AddExhibitorBrochure;
import com.procialize.mrgeApp20.GetterSetter.Agenda;
import com.procialize.mrgeApp20.GetterSetter.Analytic;
import com.procialize.mrgeApp20.GetterSetter.CommentList;
import com.procialize.mrgeApp20.GetterSetter.ContactListFetch;
import com.procialize.mrgeApp20.GetterSetter.CurrencyConverterResponse;
import com.procialize.mrgeApp20.GetterSetter.CurrencyDropDown;
import com.procialize.mrgeApp20.GetterSetter.DeleteExhibitorBrochure;
import com.procialize.mrgeApp20.GetterSetter.DeleteNewsFeedComment;
import com.procialize.mrgeApp20.GetterSetter.DeletePost;
import com.procialize.mrgeApp20.GetterSetter.DeleteSelfie;
import com.procialize.mrgeApp20.GetterSetter.DocumentsListFetch;
import com.procialize.mrgeApp20.GetterSetter.EditNewsFeed;
import com.procialize.mrgeApp20.GetterSetter.EventInfoFetch;
import com.procialize.mrgeApp20.GetterSetter.EventListing;
import com.procialize.mrgeApp20.GetterSetter.ExhibitorDashboard;
import com.procialize.mrgeApp20.GetterSetter.ExhibitorList;
import com.procialize.mrgeApp20.GetterSetter.ExhibitorMeetingUserListing;
import com.procialize.mrgeApp20.GetterSetter.ExhibitorMsgUserListing;
import com.procialize.mrgeApp20.GetterSetter.ExhibitorViewUserListing;
import com.procialize.mrgeApp20.GetterSetter.ExhibitoreBrochureViewUserListing;
import com.procialize.mrgeApp20.GetterSetter.FetchAgenda;
import com.procialize.mrgeApp20.GetterSetter.FetchAttendee;
import com.procialize.mrgeApp20.GetterSetter.FetchFeed;
import com.procialize.mrgeApp20.Speaker.Models.FetchSpeaker;
import com.procialize.mrgeApp20.GetterSetter.Forgot;
import com.procialize.mrgeApp20.GetterSetter.GalleryListFetch;
import com.procialize.mrgeApp20.GetterSetter.GeneralInfoList;
import com.procialize.mrgeApp20.GetterSetter.LeaderBoardListFetch;
import com.procialize.mrgeApp20.GetterSetter.LikeListing;
import com.procialize.mrgeApp20.GetterSetter.LikePost;
import com.procialize.mrgeApp20.GetterSetter.LivePollFetch;
import com.procialize.mrgeApp20.GetterSetter.LivePollSubmitFetch;
import com.procialize.mrgeApp20.GetterSetter.Login;
import com.procialize.mrgeApp20.GetterSetter.NotificationListExhibitorFetch;
import com.procialize.mrgeApp20.GetterSetter.NotificationListFetch;
import com.procialize.mrgeApp20.GetterSetter.NotificationSend;
import com.procialize.mrgeApp20.GetterSetter.PostComment;
import com.procialize.mrgeApp20.GetterSetter.PostSelfie;
import com.procialize.mrgeApp20.GetterSetter.PostTextFeed;
import com.procialize.mrgeApp20.GetterSetter.PostVideoSelfie;
import com.procialize.mrgeApp20.GetterSetter.ProfileSave;
import com.procialize.mrgeApp20.GetterSetter.QADirectFetch;
import com.procialize.mrgeApp20.GetterSetter.QASessionFetch;
import com.procialize.mrgeApp20.GetterSetter.QASpeakerFetch;
import com.procialize.mrgeApp20.GetterSetter.QRPost;
import com.procialize.mrgeApp20.GetterSetter.QuizFetch;
import com.procialize.mrgeApp20.GetterSetter.QuizSubmitFetch;
import com.procialize.mrgeApp20.GetterSetter.RatingSessionPost;
import com.procialize.mrgeApp20.GetterSetter.RatingSpeakerPost;
import com.procialize.mrgeApp20.GetterSetter.ReportComment;
import com.procialize.mrgeApp20.GetterSetter.ReportCommentHide;
import com.procialize.mrgeApp20.GetterSetter.ReportPost;
import com.procialize.mrgeApp20.GetterSetter.ReportPostHide;
import com.procialize.mrgeApp20.GetterSetter.ReportSelfie;
import com.procialize.mrgeApp20.GetterSetter.ReportSelfieHide;
import com.procialize.mrgeApp20.GetterSetter.ReportUser;
import com.procialize.mrgeApp20.GetterSetter.ReportUserHide;
import com.procialize.mrgeApp20.GetterSetter.ReportVideoContest;
import com.procialize.mrgeApp20.GetterSetter.ReportVideoContestHide;
import com.procialize.mrgeApp20.GetterSetter.ResetPassword;
import com.procialize.mrgeApp20.GetterSetter.SelfieLike;
import com.procialize.mrgeApp20.GetterSetter.SelfieListFetch;
import com.procialize.mrgeApp20.GetterSetter.SendMessagePost;
import com.procialize.mrgeApp20.GetterSetter.SponsorsFetch;
import com.procialize.mrgeApp20.GetterSetter.SurveyListFetch;
import com.procialize.mrgeApp20.GetterSetter.TimeWeather;
import com.procialize.mrgeApp20.GetterSetter.TravelListFetch;
import com.procialize.mrgeApp20.GetterSetter.VideoContestLikes;
import com.procialize.mrgeApp20.GetterSetter.VideoContestListFetch;
import com.procialize.mrgeApp20.GetterSetter.VideoFetchListFetch;
import com.procialize.mrgeApp20.GetterSetter.Weather;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface APIService {

    @POST("SpecificEventLogin")
    @FormUrlEncoded
    Call<Login> LoginPost(/*@Field("email") String email,
                          @Field("password") String password,
                          @Field("api_access_token") String api_access_token,*/
                          @Field("event_id") String eventId,
                          @Field("registration_id") String registration_id,
                          @Field("platform") String platform,
                          @Field("device") String device,
                          @Field("os_version") String os_version,
                          @Field("api_access_token") String api_access_token,
                          @Field("app_version") String app_version);

    @POST("Login")
    @FormUrlEncoded
    Call<EventListing> EventListPost(@Field("email") String email,
                                     @Field("password") String password);

    @POST("EventListFetch")
    @FormUrlEncoded
    Call<EventListing> EventListMPost(@Field("api_access_token") String api_access_token);

    /*@POST("Login")
    @FormUrlEncoded
    Call<EventListing> EventListPost(@Field("mobile") String email,
                                     @Field("password") String password);*/


    @POST("LoginMobile")
    @FormUrlEncoded
    Call<EventListing> EventListPost(@Field("mobile") String mobile);

    @POST("ResendOtp")
    @FormUrlEncoded
    Call<EventListing> ResendOtp(@Field("mobile") String mobile);


    @POST("ExhibitorFetch")
    @FormUrlEncoded
    Call<ExhibitorList> ExhibitorFetch(@Field("event_id") String event_id,
                                       @Field("api_access_token") String api_access_token);

    @POST("ExhibitorAnalyticsSubmit")
    @FormUrlEncoded
    Call<DeleteExhibitorBrochure> ExhibitorAnalyticsSubmit(@Field("event_id") String event_id,
                                                           @Field("api_access_token") String api_access_token,
                                                           @Field("analytic_type") String analytic_type,
                                                           @Field("target_id") String target_id,
                                                           @Field("exhibitor_id") String exhibitor_id);

    @POST("ForgetPassword")
    @FormUrlEncoded
    Call<Forgot> ForgotPassword(@Field("email") String email);

    @POST("ResetPassword")
    @FormUrlEncoded
    Call<ResetPassword> ResetPassword(@Field("temp_password") String email,
                                      @Field("new_password") String new_password);


    @POST("CommentFetch")
    @FormUrlEncoded
    Call<CommentList> getComment(@Field("event_id") String eventid,
                                 @Field("post_id") String post_id);


    @POST("PostComment")
    @FormUrlEncoded
    Call<PostComment> postComment(@Field("event_id") String eventId,
                                  @Field("news_feed_id") String news_feed_id,
                                  @Field("comment_data") String comment_data,
                                  @Field("api_access_token") String api_access_token);

    @POST("SendNotification")
    @FormUrlEncoded
    Call<NotificationSend> SendNotification(@Field("api_access_token") String api_access_token,
                                            @Field("event_id") String event_id,
                                            @Field("message") String message,
                                            @Field("display_time") String display_time);

    @POST("NewsFeedLike")
    @FormUrlEncoded
    Call<LikePost> postLike(@Field("event_id") String eventId,
                            @Field("news_feed_id") String news_feed_id,
                            @Field("api_access_token") String api_access_token);


    @POST("NewsFeedLikeUser")
    @FormUrlEncoded
    Call<LikeListing> postLikeUserList(@Field("api_access_token") String api_access_token,
                                       @Field("news_feed_id") String notification_id,
                                       @Field("event_id") String eventId);

    @POST("DeleteExhibitorBrochure")
    @FormUrlEncoded
    Call<DeleteExhibitorBrochure> DeleteExhibitorBrochure(@Field("api_access_token") String api_access_token,
                                                          @Field("brochure_id") String brochure_id,
                                                          @Field("event_id") String eventId);

    @POST("AddExhibitorBrochure")
    @Multipart
    Call<AddExhibitorBrochure> AddExhibitorBrochure(@Part("brochure_type") RequestBody type,
                                                    @Part("api_access_token") RequestBody api_access_token,
                                                    @Part("event_id") RequestBody event_id,
                                                    @Part("exhibitor_id") RequestBody exhibitor_id,
                                                    @Part("brochure_title") RequestBody brochure_title,
                                                    @Part MultipartBody.Part brochure_file_name);

    @POST("PostNewsFeed")
    @Multipart
    Call<PostTextFeed> PostNewsFeed(@Part("type") RequestBody type,
                                    @Part("api_access_token") RequestBody api_access_token,
                                    @Part("event_id") RequestBody event_id,
                                    @Part("status") RequestBody status,
                                    @Part MultipartBody.Part filename);

    @POST("PostNewsFeed")
    @Multipart
    Call<PostTextFeed> PostNewsFeed(@Part("type") RequestBody type,
                                    @Part("api_access_token") RequestBody api_access_token,
                                    @Part("event_id") RequestBody event_id,
                                    @Part("status") RequestBody status,
                                    @Part MultipartBody.Part filename,
                                    @Part MultipartBody.Part thumbimage);


    @POST("EditNewsFeed")
    @Multipart
    Call<EditNewsFeed> EditNewsFeed(@Part("Type") RequestBody type,
                                    @Part("api_access_token") RequestBody api_access_token,
                                    @Part("event_id") RequestBody event_id,
                                    @Part("status") RequestBody status,
                                    @Part("news_feed_id") RequestBody news_feed_id,
                                    @Part MultipartBody.Part filename);


    @POST("EditProfileSubmit")
    @Multipart
    Call<ProfileSave> ProfileSave(
            @Part("api_access_token") RequestBody api_access_token,
            @Part("first_name") RequestBody first_name,
            @Part("last_name") RequestBody last_name,
            @Part("description") RequestBody description,
            @Part("city") RequestBody city,
            @Part("country") RequestBody country,
            @Part("mobile") RequestBody mobile,
            @Part("attendee_type") RequestBody attendee_type,
            @Part("designation") RequestBody designation,
            @Part("event_id") RequestBody event_id,
            @Part("company_name") RequestBody company_name);


    @POST("EditProfileSubmit")
    @Multipart
    Call<ProfileSave> ProfileSave(
            @Part("api_access_token") RequestBody api_access_token,
            @Part("first_name") RequestBody first_name,
            @Part("last_name") RequestBody last_name,
            @Part("description") RequestBody description,
            @Part("city") RequestBody city,
            @Part("country") RequestBody country,
            @Part("mobile") RequestBody mobile,
            @Part("attendee_type") RequestBody attendee_type,
            @Part("designation") RequestBody designation,
            @Part("event_id") RequestBody event_id,
            @Part("company_name") RequestBody company_name,
            @Part MultipartBody.Part filename);


    @POST("EditProfileFetch")
    @Multipart
    Call<ProfileSave> fetchProfileDetail(
            @Part("api_access_token") RequestBody api_access_token,
            @Part("event_id") RequestBody event_id);

    @POST("QRScanDataPost")
    @FormUrlEncoded
    Call<QRPost> QRScanDataPost(@Field("api_access_token") String api_access_token,
                                @Field("event_id") String event_id,
                                @Field("first_name") String first_name,
                                @Field("last_name") String last_name,
                                @Field("contact_number") String contact_number,
                                @Field("email") String email);

    @POST("SendExhibitorMeetingRequest")
    @FormUrlEncoded
    Call<QRPost> SendExhibitorMeetingRequest(@Field("api_access_token") String api_access_token,
                                             @Field("event_id") String event_id,
                                             @Field("exhibitor_id") String exhibitor_id,
                                             @Field("meeting_date_time") String meeting_date_time,
                                             @Field("description") String description);

    @POST("ExhibitorSendMessage")
    @FormUrlEncoded
    Call<SendMessagePost> ExhibitorSendMessage(@Field("api_access_token") String api_access_token,
                                               @Field("event_id") String event_id,
                                               @Field("exhibitor_id") String exhibitor_id,
                                               @Field("message_text") String message_text,
                                               @Field("target_attendee_id") String target_attendee_id,
                                               @Field("target_attendee_type") String target_attendee_type);

//
//    @POST("NewsFeedFetch")
//    @FormUrlEncoded
//    Call<FetchFeed> FeedFetchPost(@Field("api_access_token") String api_access_token,
//                                  @Field("event_id") String event_id);

    @POST("NewsFeedFetchMultiple")
    @FormUrlEncoded
    Call<FetchFeed> FeedFetchPost(@Field("api_access_token") String api_access_token,
                                  @Field("event_id") String event_id);

    @POST("NewsFeedReaction")
    @FormUrlEncoded
    Call<LikePost> NewsFeedReaction(@Field("reaction_type") String reaction_type, @Field("event_id") String eventId,
                                    @Field("news_feed_id") String news_feed_id,
                                    @Field("api_access_token") String api_access_token);


    @POST("GenInfoAll")
    @FormUrlEncoded
    Call<GeneralInfoList> FetchGeneralInfo(@Field("event_id") String event_id);

    @POST("GenInfoCurrencyDropdown")
    @FormUrlEncoded
    Call<CurrencyDropDown> FetchCurrencyDropDown(@Field("event_id") String event_id);

    @POST("GenInfoWeather")
    @FormUrlEncoded
    Call<TimeWeather> FetchTimeWeather(@Field("event_id") String event_id);

    @POST("GenInfoCurrencyConverter")
    @FormUrlEncoded
    Call<CurrencyConverterResponse> SubmitCurrencyConverter(@Field("event_id") String event_id,
                                                            @Field("from_currency") String from_currency,
                                                            @Field("to_currency") String to_currency,
                                                            @Field("amount") String amount);


    @POST("SpeakerFetch")
    @FormUrlEncoded
    Call<FetchSpeaker> SpeakerFetchPost(@Field("api_access_token") String api_access_token,
                                        @Field("event_id") String event_id);


    @POST("AttendeeFetch")
    @FormUrlEncoded
    Call<FetchAttendee> AttendeeFetchPost(@Field("api_access_token") String api_access_token,
                                          @Field("event_id") String event_id);


    @POST("AgendaSeprateFetch")
    @FormUrlEncoded
    Call<FetchAgenda> AgendaFetchPost(@Field("api_access_token") String api_access_token,
                                      @Field("event_id") String event_id);

    @POST("AgendaVacationSeprateFetch")
    @FormUrlEncoded
    Call<Agenda> AgendaFetchVacation(@Field("api_access_token") String api_access_token,
                                     @Field("event_id") String event_id);


    @POST("DeleteNewsFeedPost")
    @FormUrlEncoded
    Call<DeletePost> DeletePost(@Field("api_access_token") String api_access_token,
                                @Field("event_id") String event_id, @Field("news_feed_id") String news_feed_id);

    @POST("RateSpeaker")
    @FormUrlEncoded
    Call<RatingSpeakerPost> RatingSpeakerPost(@Field("api_access_token") String api_access_token,
                                              @Field("event_id") String event_id,
                                              @Field("target_id") String news_feed_id,
                                              @Field("rating") String rating,
                                              @Field("comment") String comment);


    @POST("RateSession")
    @FormUrlEncoded
    Call<RatingSessionPost> RatingSessionPost(@Field("api_access_token") String api_access_token,
                                              @Field("event_id") String event_id, @Field("target_id") String target_id, @Field("rating") String rating,
                                              @Field("comment") String comment,
                                              @Field("feedback_type") String feedback_type);


    @POST("SendMessage")
    @FormUrlEncoded
    Call<SendMessagePost> SendMessagePost(@Field("api_access_token") String api_access_token,
                                          @Field("event_id") String event_id,
                                          @Field("message_text") String message_text,
                                          @Field("target_attendee_id") String target_attendee_id,
                                          @Field("target_attendee_type") String target_attendee_type);





    @POST("SurveyFetch")
    @FormUrlEncoded
    Call<SurveyListFetch> SurveyListFetch(@Field("api_access_token") String api_access_token,
                                          @Field("event_id") String event_id);

    @POST("DocumentsFetch")
    @FormUrlEncoded
    Call<DocumentsListFetch> DocumentsListFetch(@Field("api_access_token") String api_access_token,
                                                @Field("event_id") String event_id);

    @POST("TravelGalleryFetch")
    @FormUrlEncoded
    Call<TravelListFetch> TravelListFetch(@Field("api_access_token") String api_access_token,
                                          @Field("event_id") String event_id);

    @POST("NotificationListFetch")
    @FormUrlEncoded
    Call<NotificationListFetch> NotificationListFetch(@Field("api_access_token") String api_access_token,
                                                      @Field("event_id") String event_id);

    @POST("leaderboard")
    @FormUrlEncoded
    Call<LeaderBoardListFetch> LeaderBoardListFetch(@Field("event_id") String event_id,
                                                    @Field("api_access_token") String api_access_token);

    @POST("GalleryFetch")
    @FormUrlEncoded
    Call<GalleryListFetch> GalleryListFetch(@Field("api_access_token") String api_access_token,
                                            @Field("event_id") String event_id);

    @POST("VideoFetch")
    @FormUrlEncoded
    Call<VideoFetchListFetch> VideoFetchListFetch(@Field("api_access_token") String api_access_token,
                                                  @Field("event_id") String event_id);

    @POST("LivePollFetch")
    @FormUrlEncoded
    Call<LivePollFetch> LivePollFetch(@Field("api_access_token") String api_access_token,
                                      @Field("event_id") String event_id);


    @POST("LivePollSubmit")
    @FormUrlEncoded
    Call<LivePollSubmitFetch> LivePollSubmitFetch(@Field("api_access_token") String api_access_token,
                                                  @Field("event_id") String event_id, @Field("live_poll_id") String live_poll_id,
                                                  @Field("live_poll_options_id") String live_poll_options_id);


    @POST("QuizFetch")
    @FormUrlEncoded
    Call<QuizFetch> QuizFetch(@Field("api_access_token") String api_access_token,
                              @Field("event_id") String event_id);


    @POST("QuizSubmit")
    @FormUrlEncoded
    Call<QuizSubmitFetch> QuizSubmitFetch(@Field("api_access_token") String api_access_token,
                                          @Field("event_id") String event_id, @Field("quiz_id") String live_poll_id,
                                          @Field("quiz_options_id") String live_poll_options_id);


    @POST("QASessionFetch")
    @FormUrlEncoded
    Call<QASessionFetch> QASessionFetch(@Field("api_access_token") String api_access_token,
                                        @Field("event_id") String event_id);

    @POST("QADirectFetch")
    @FormUrlEncoded
    Call<QADirectFetch> QADirectFetch(@Field("api_access_token") String api_access_token,
                                      @Field("event_id") String event_id);

    @POST("QADirectLike")
    @FormUrlEncoded
    Call<QADirectFetch> QADirectLike(@Field("api_access_token") String api_access_token,
                                     @Field("event_id") String event_id, @Field("question_id") String question_id
    );


    @POST("QASessionLike")
    @FormUrlEncoded
    Call<QASessionFetch> QASessionLike(@Field("api_access_token") String api_access_token,
                                       @Field("event_id") String event_id, @Field("question_id") String question_id,
                                       @Field("session_id") String session_id);

    @POST("QASessionPost")
    @FormUrlEncoded
    Call<QASessionFetch> QASessionPost(@Field("api_access_token") String api_access_token,
                                       @Field("event_id") String event_id, @Field("question") String question,
                                       @Field("speaker_name") String speaker_name, @Field("session_id") String session_id);


    @POST("QASpeakerFetch")
    @FormUrlEncoded
    Call<QASpeakerFetch> QASpeakerFetch(@Field("api_access_token") String api_access_token,
                                        @Field("event_id") String event_id);

    @POST("QASpeakerLike")
    @FormUrlEncoded
    Call<QASpeakerFetch> QASpeakerLike(@Field("api_access_token") String api_access_token,
                                       @Field("event_id") String event_id, @Field("question_id") String question_id,
                                       @Field("session_id") String session_id);

    @POST("QASpeakerPost")
    @FormUrlEncoded
    Call<QASpeakerFetch> QASpeakerPost(@Field("api_access_token") String api_access_token,
                                       @Field("event_id") String event_id, @Field("question") String question,
                                       @Field("speaker_id") String speaker_id);

    @POST("QADirectPost")
    @FormUrlEncoded
    Call<QADirectFetch> QADirectPost(@Field("api_access_token") String api_access_token,
                                     @Field("event_id") String event_id, @Field("question") String question);


    @POST("NewsFeedDetailFetch")
    @FormUrlEncoded
    Call<FetchFeed> NewsFeedDetailFetch(@Field("api_access_token") String api_access_token,
                                        @Field("event_id") String event_id, @Field("post_id") String post_id);

    @POST("SelfieListFetch")
    @FormUrlEncoded
    Call<SelfieListFetch> SelfieListFetch(@Field("api_access_token") String api_access_token,
                                          @Field("event_id") String event_id);


    @POST("SelfieLike")
    @FormUrlEncoded
    Call<SelfieLike> SelfieLike(@Field("api_access_token") String api_access_token,
                                @Field("event_id") String event_id,
                                @Field("selfie_gallery_id") String selfie_gallery_id);

    @POST("VideoContestListFetch")
    @FormUrlEncoded
    Call<VideoContestListFetch> VideoContestListFetch(@Field("api_access_token") String api_access_token,
                                                      @Field("event_id") String event_id);


    @POST("VideoContestLikes")
    @FormUrlEncoded
    Call<VideoContestLikes> VideoContestLikes(@Field("api_access_token") String api_access_token,
                                              @Field("event_id") String event_id,
                                              @Field("video_contest_id") String selfie_gallery_id);

    @POST("PostSelfie")
    @Multipart
    Call<PostSelfie> PostSelfie(@Part("api_access_token") RequestBody api_access_token,
                                @Part("event_id") RequestBody event_id,
                                @Part("title") RequestBody title,
                                @Part MultipartBody.Part filename);

    @POST("PostVideoContest")
    @Multipart
    Call<PostVideoSelfie> PostVideoContest(@Part("api_access_token") RequestBody api_access_token,
                                           @Part("event_id") RequestBody event_id,
                                           @Part("title") RequestBody title,
                                           @Part MultipartBody.Part thumb,
                                           @Part MultipartBody.Part filename);


    @POST("ReportPostHide")
    @FormUrlEncoded
    Call<ReportPostHide> ReportPostHide(@Field("api_access_token") String api_access_token,
                                        @Field("event_id") String event_id,
                                        @Field("post_id") String post_id);


    @POST("ReportPost")
    @FormUrlEncoded
    Call<ReportPost> ReportPost(@Field("api_access_token") String api_access_token,
                                @Field("event_id") String event_id,
                                @Field("post_id") String post_id,
                                @Field("text") String text);


    @POST("ReportUser")
    @FormUrlEncoded
    Call<ReportUser> ReportUser(@Field("api_access_token") String api_access_token,
                                @Field("event_id") String event_id,
                                @Field("target_attendee_id") String target_attendee_id,
                                @Field("text") String text);

    @POST("ReportUserHide")
    @FormUrlEncoded
    Call<ReportUserHide> ReportUserHide(@Field("api_access_token") String api_access_token,
                                        @Field("event_id") String event_id,
                                        @Field("target_attendee_id") String target_attendee_id);


    @POST("DeleteNewsFeedComment")
    @FormUrlEncoded
    Call<DeleteNewsFeedComment> DeleteNewsFeedComment(@Field("api_access_token") String api_access_token,
                                                      @Field("news_feed_id") String news_feed_id,
                                                      @Field("comment_id") String comment_id,
                                                      @Field("event_id") String event_id);


    @POST("ReportCommentHide")
    @FormUrlEncoded
    Call<ReportCommentHide> ReportCommentHide(@Field("api_access_token") String api_access_token,
                                              @Field("event_id") String event_id, @Field("comment_id") String comment_id);


    @POST("ReportComment")
    @FormUrlEncoded
    Call<ReportComment> ReportComment(@Field("api_access_token") String api_access_token,
                                      @Field("event_id") String event_id,
                                      @Field("comment_id") String post_id,
                                      @Field("text") String text);


    @POST("NotificationListExhibitorFetch")
    @FormUrlEncoded
    Call<NotificationListExhibitorFetch> NotificationListExhibitorFetch(@Field("api_access_token") String api_access_token,
                                                                        @Field("event_id") String event_id,
                                                                        @Field("exhibitor_id") String exhibitor_id);

    @POST("ApproveRejectMeetingRequest")
    @FormUrlEncoded
    Call<DeleteSelfie> ApproveRejectMeetingRequest(@Field("api_access_token") String api_access_token,
                                                   @Field("event_id") String event_id,
                                                   @Field("meeting_id") String meeting_id,
                                                   @Field("exhibitor_id") String exhibitor_id,
                                                   @Field("meeting_status") String meeting_status);

    @POST("ReportSelfieHide")
    @FormUrlEncoded
    Call<ReportSelfieHide> ReportSelfieHide(@Field("api_access_token") String api_access_token,
                                            @Field("event_id") String event_id,
                                            @Field("selfie_id") String selfie_id);

    @POST("DeleteSelfie")
    @FormUrlEncoded
    Call<DeleteSelfie> DeleteSelfie(@Field("api_access_token") String api_access_token,
                                    @Field("event_id") String event_id,
                                    @Field("selfie_id") String selfie_id);


    @POST("ReportSelfie")
    @FormUrlEncoded
    Call<ReportSelfie> ReportSelfie(@Field("api_access_token") String api_access_token,
                                    @Field("event_id") String event_id,
                                    @Field("selfie_id") String selfie_id,
                                    @Field("text") String text);


    @POST("ReportVideoContestHide")
    @FormUrlEncoded
    Call<ReportVideoContestHide> ReportVideoContestHide(@Field("api_access_token") String api_access_token,
                                                        @Field("event_id") String event_id,
                                                        @Field("video_contest_id") String video_contest_id);

    @POST("DeleteVideoContest")
    @FormUrlEncoded
    Call<ReportVideoContestHide> DeleteVideoContest(@Field("api_access_token") String api_access_token,
                                                    @Field("event_id") String event_id,
                                                    @Field("video_contest_id") String video_contest_id);


    @POST("ReportVideoContest")
    @FormUrlEncoded
    Call<ReportVideoContest> ReportVideoContest(@Field("api_access_token") String api_access_token,
                                                @Field("event_id") String event_id,
                                                @Field("selfie_id") String selfie_id,
                                                @Field("text") String text);

    @POST("AnalyticsSubmit")
    @FormUrlEncoded
    Call<Analytic> Analytic(@Field("api_access_token") String api_access_token,
                            @Field("event_id") String event_id,
                            @Field("target_attendee_id") String target_attendee_id,
                            @Field("target_attendee_type") String target_attendee_type,
                            @Field("analytic_type") String analytic_type);

    @POST("ExhibitorDashboard")
    @FormUrlEncoded
    Call<ExhibitorDashboard> ExhibitorDashboard(@Field("api_access_token") String api_access_token,
                                                @Field("event_id") String event_id,
                                                @Field("exhibitor_id") String exhibitor_id);

    @POST("ExhibitorViewUserListing")
    @FormUrlEncoded
    Call<ExhibitorViewUserListing> ExhibitorViewUserListing(@Field("api_access_token") String api_access_token,
                                                            @Field("event_id") String event_id,
                                                            @Field("exhibitor_id") String exhibitor_id);

    @POST("ExhibitorMeetingUserListing")
    @FormUrlEncoded
    Call<ExhibitorMeetingUserListing> ExhibitorMeetingUserListing(@Field("api_access_token") String api_access_token,
                                                                  @Field("event_id") String event_id,
                                                                  @Field("exhibitor_id") String exhibitor_id);

    @POST("ExhibitorMsgUserListing")
    @FormUrlEncoded
    Call<ExhibitorMsgUserListing> ExhibitorMsgUserListing(@Field("api_access_token") String api_access_token,
                                                          @Field("event_id") String event_id,
                                                          @Field("exhibitor_id") String exhibitor_id);

    @POST("ExhibitorBrochureViewUserListing")
    @FormUrlEncoded
    Call<ExhibitoreBrochureViewUserListing> ExhibitoreBrochureViewUserListing(@Field("api_access_token") String api_access_token,
                                                                              @Field("event_id") String event_id,
                                                                              @Field("exhibitor_id") String exhibitor_id,
                                                                              @Field("brochure_id") String brochure_id);


    @POST("AnalyticsSubmit")
    @FormUrlEncoded
    Call<Analytic> Analytic(@Field("api_access_token") String api_access_token,
                            @Field("event_id") String event_id,
                            @Field("target_attendee_id") String target_attendee_id,
                            @Field("target_attendee_type") String target_attendee_type,
                            @Field("analytic_type") String analytic_type,
                            @Field("video_details") String video_details);

    @POST("GenInfoWeatherYahooWeather")
    @FormUrlEncoded
    Call<Weather> WeatherListFetch(@Field("api_access_token") String api_access_token,
                                   @Field("event_id") String event_id);

    @POST("EditProfileSubmit")
    @Multipart
    Call<ProfileSave> ProfileSave1(
            @Part("api_access_token") RequestBody api_access_token,
            @Part("first_name") RequestBody first_name,
            @Part("last_name") RequestBody last_name,
            @Part("description") RequestBody description,
            @Part("city") RequestBody city,
            @Part("country") RequestBody country,
            @Part("mobile") RequestBody mobile,
            @Part("attendee_type") RequestBody attendee_type,
            @Part("designation") RequestBody designation,
            @Part("event_id") RequestBody event_id,
            @Part("company_name") RequestBody company_name);

    @POST("SponsorsFetch")
    @FormUrlEncoded
    Call<SponsorsFetch> SponsorsFetch(@Field("event_id") String event_id,
                                      @Field("api_access_token") String api_access_token);

    @POST("ContactListFetch")
    @FormUrlEncoded
    Call<ContactListFetch> ContactListFetch(@Field("event_id") String event_id,
                                            @Field("api_access_token") String api_access_token);
    @POST("ContactHeplDeskFetch")
    @FormUrlEncoded
    Call<ContactListFetch> ContactHeplDeskFetch(@Field("event_id") String event_id,
                                            @Field("api_access_token") String api_access_token);

    //buddy related api
    @POST("sendFriendRequest")
    @FormUrlEncoded
    Call<FetchSendRequest> sendFriendRequest(@Field("api_access_token") String api_access_token,
                                             @Field("event_id") String event_id,
                                             @Field("buddy_id") String buddy_id);

    @POST("cancelFriendRequest")
    @FormUrlEncoded
    Call<FetchSendRequest> cancelFriendRequest(@Field("event_id") String event_id,
                                             @Field("api_access_token") String api_access_token,
                                             @Field("buddy_id") String buddy_id);

    @POST("respondToFriendRequest")
    @FormUrlEncoded
    Call<FetchSendRequest> respondToFriendRequest(@Field("event_id") String event_id,
                                               @Field("api_access_token") String api_access_token,
                                               @Field("buddy_id") String buddy_id,
                                                  @Field("response") String response);

    @POST("getBuddyList")
    @FormUrlEncoded
    Call<FetchBuddyList> getBuddyList(@Field("event_id") String event_id,
                                      @Field("api_access_token") String api_access_token
                                                  );


    @POST("EngagementListFetch")
    @FormUrlEncoded
    Call<FetchEngagementData> getEngagementData(@Field("event_id") String event_id,
                                                @Field("api_access_token") String api_access_token
                                                  );
    @POST("LiveChatPostUser")
    @FormUrlEncoded
    Call<FetchChatList> LiveChatPostUser(@Field("event_id") String event_id,
                                         @Field("api_access_token") String api_access_token,
                                         @Field("buddy_id") String buddy_id,
                                         @Field("message") String message );



    @POST("EventInfoFetch")
    @FormUrlEncoded
    Call<EventInfoFetch> EventInfoFetch(@Field("api_access_token") String api_access_token,
                                        @Field("event_id") String event_id);



    @POST("getUserChathistory")
    @FormUrlEncoded
    Call<FetchChatList> UserChathistory(@Field("event_id") String event_id,
                                         @Field("api_access_token") String api_access_token,
                                         @Field("buddy_id") String buddy_id,
                                         @Field("chat_id") String message );

    @POST("SpotLivePollFetch")
    @FormUrlEncoded
    Call<LivePollFetch> SpotLivePollFetch(@Field("api_access_token") String api_access_token,
                                       @Field("event_id") String event_id);


    @POST("SpotLivePollFetch")
    @FormUrlEncoded
    Call<LivePollSubmitFetch> SpotLivePollSubmit(@Field("api_access_token") String api_access_token,
                                           @Field("event_id") String event_id,
                                           @Field("live_poll_id") String live_poll_id,
                                           @Field("live_poll_options_id") String live_poll_options_id
                                       );

}
