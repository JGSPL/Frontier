package com.procialize.mrgeApp20.widget;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import androidx.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.procialize.mrgeApp20.ApiConstant.APIService;
import com.procialize.mrgeApp20.ApiConstant.ApiUtils;
import com.procialize.mrgeApp20.Fragments.WallFragment_POST;
import com.procialize.mrgeApp20.GetterSetter.LikePost;
import com.procialize.mrgeApp20.GetterSetter.NewsFeedList;
import com.procialize.mrgeApp20.R;
import com.procialize.mrgeApp20.Session.SessionManager;
import com.procialize.mrgeApp20.util.Constants;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.MODE_PRIVATE;


/**
 * Created by KenZira on 3/10/17.
 */

public class ReactionView extends View {
    String MY_PREFS_NAME = "ProcializeInfo";
    private APIService mAPIService;
    SessionManager sessionManager;
    private static final int SCALE_DURATION = 200;
    private static final int TRANSLATION_DURATION = 1000;
    private static final int CHILD_TRANSLATION_DURATION = 300;
    private static final int CHILD_DELAY = 100;
    private static final int DRAW_DELAY = 50;
    private RoundedBoard board;
    private List<Emotion> emotions;
    private int selectedIndex = -1;
    NewsFeedList feed;
    int position;
    TextView likeimage;
    TextView liketext;
    Context context;
    String eventid, colorActive;
    private HashMap<String, String> user;
    RelativeLayout relative;
    String token;
    FrameLayout root;

    private SelectingAnimation selectingAnimation;
    private DeselectAnimation deselectAnimation;

    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            invalidate();
        }
    };

    public ReactionView(Context context) {
        super(context);
        init();
    }

    public ReactionView(Context context, NewsFeedList _feed, int _position, TextView _likeimage, TextView _liketext, FrameLayout _root, RelativeLayout _relative) {
        super(context);
        this.context = context;
        feed = _feed;
        position = _position;
        root = _root;
        likeimage = _likeimage;
        liketext = _liketext;
        relative = _relative;
        init();
    }

    public ReactionView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ReactionView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        board = new RoundedBoard();
        setLayerType(LAYER_TYPE_SOFTWARE, null);

        /*emotions = Arrays.asList(
                new Emotion(getContext(), "Like", "Like.json"),
                new Emotion(getContext(), "Love", "Love.json"),
                new Emotion(getContext(), "Haha", "Haha.json"),
                new Emotion(getContext(), "Wow", "Wow.json"),
                new Emotion(getContext(), "Sorry", "Sorry.json"),
                new Emotion(getContext(), "Angry", "Anger.json")
        );*/

        emotions = Arrays.asList(
                new Emotion(getContext(), "Like", getResources().getDrawable(R.drawable.like_0)),
                new Emotion(getContext(), "Love", getResources().getDrawable(R.drawable.love_1)),
                new Emotion(getContext(), "Smile", getResources().getDrawable(R.drawable.smile_2)),
                new Emotion(getContext(), "Haha", getResources().getDrawable(R.drawable.haha_3)),
                new Emotion(getContext(), "Wow", getResources().getDrawable(R.drawable.wow_4)),
                new Emotion(getContext(), "Sorry", getResources().getDrawable(R.drawable.sad_5)),
                new Emotion(getContext(), "Angry", getResources().getDrawable(R.drawable.angry_6))
        );
        mAPIService = ApiUtils.getAPIService();
        selectingAnimation = new SelectingAnimation();
        deselectAnimation = new DeselectAnimation();
        deselectAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                selectedIndex = -1;
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });

        startAnimation(new TranslationAnimation());
    }



    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                return true;
            case MotionEvent.ACTION_MOVE:
                for (int i = 0; i < emotions.size(); i++) {
                    if (event.getX() > emotions.get(i).x &&
                            event.getX() < emotions.get(i).x + emotions.get(i).size) {
                        onSelect(i);
                        break;
                    }
                }
                return true;
            case MotionEvent.ACTION_UP:
                onDeselect();
                return true;
            case MotionEvent.ACTION_CANCEL:

                return true;
        }
        return super.onTouchEvent(event);
    }

    @Override
    public void setOnClickListener(@Nullable OnClickListener l) {
        //super.setOnClickListener(l);
        for (int i = 0; i < emotions.size(); i++) {
            if (getX() > emotions.get(i).x &&
                    getX() < emotions.get(i).x + emotions.get(i).size) {
                onSelect(i);
                break;
            }
        }
    }


    private void onSelect(int index) {
        Bitmap bitmap;
        Bitmap bitmap2 = null;
        if (selectedIndex == index) {
            return;
        }
        selectedIndex = index;
        selectingAnimation.prepare();
        startAnimation(selectingAnimation);
        //submitData(selectedIndex);

        int count = Integer.parseInt(feed.getTotalLikes());
        SharedPreferences prefs = context.getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        eventid = prefs.getString("eventid", "1");
        colorActive = prefs.getString("colorActive", "");
        sessionManager = new SessionManager(getContext());
        user = sessionManager.getUserDetails();
        // token
        token = user.get(SessionManager.KEY_TOKEN);

        Drawable[] drawables = likeimage.getCompoundDrawables();
        bitmap = ((BitmapDrawable) drawables[2]).getBitmap();

        feed.setLikeFlag(String.valueOf(selectedIndex));

        if (selectedIndex == 0) {
            likeimage.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.like_0, 0);
        }
        if (selectedIndex == 1) {
            likeimage.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.love_1, 0);
        } else if (selectedIndex == 2) {
            likeimage.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.smile_2, 0);
        }else if (selectedIndex == 3) {
            likeimage.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.haha_3, 0);

        } else if (selectedIndex == 4) {
            likeimage.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.wow_4, 0);
        } else if (selectedIndex == 5) {
            likeimage.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.sad_5, 0);

        } else if (selectedIndex == 6) {
            likeimage.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.angry_6, 0);
        }


        PostLike(String.valueOf(selectedIndex), eventid, feed.getNewsFeedId(), token);

        return;

    }

    private void onDeselect() {
        deselectAnimation.prepare();
        startAnimation(deselectAnimation);
        root.removeView(this);
        relative.removeView(this);
    }


    @Override
    protected void onDraw(final Canvas canvas) {
        board.draw(canvas);
        for (Emotion emotion : emotions) {
            emotion.draw(canvas);
        }

        postDelayed(runnable, DRAW_DELAY);
    }



    /*private void animateEmotions(float interpolatedTime) {

        for (Emotion emotion : emotions) {
            animateEmotionSize(emotion, interpolatedTime);
            animateEmotionPosition(emotion);
        }
    }*/

    private void animateEmotionPosition(Emotion emotion) {
        emotion.y = RoundedBoard.BASE_LINE - emotion.size;

        emotions.get(0).x = RoundedBoard.LEFT + Constants.HORIZONTAL_SPACING;
        emotions.get(emotions.size() - 1).x =
                RoundedBoard.LEFT + RoundedBoard.WIDTH - Constants.HORIZONTAL_SPACING
                        - emotions.get(emotions.size() - 1).size;

        for (int i = 1; i < selectedIndex; i++) {
            emotions.get(i).x = emotions.get(i - 1).x + emotions.get(i - 1).size
                    + Constants.HORIZONTAL_SPACING;
        }

        for (int i = emotions.size() - 2; i > selectedIndex; i--) {
            emotions.get(i).x = emotions.get(i + 1).x - emotions.get(i).size
                    - Constants.HORIZONTAL_SPACING;
        }

        if (selectedIndex > 0) {
            emotions.get(selectedIndex).x = emotions.get(selectedIndex - 1).x
                    + emotions.get(selectedIndex - 1).size + Constants.HORIZONTAL_SPACING;
        }
    }

   /* private void animateEmotionSize(Emotion emotion, float interpolatedTime) {
        emotion.setCurrentSize(emotion.startAnimatedSize +
                (int) (interpolatedTime * (emotion.endAnimatedSize - emotion.startAnimatedSize)));
    }*/

    private void animateRoundedBoard(float interpolatedTime) {
        board.setCurrentHeight(board.startAnimatedHeight + (interpolatedTime *
                (board.endAnimatedHeight - board.startAnimatedHeight)));
    }

    private class SelectingAnimation extends Animation {

        SelectingAnimation() {
            setDuration(SCALE_DURATION);
        }

        void prepare() {
            prepareEmotions();
            prepareRoundedBoard();
        }

        private void prepareEmotions() {
            for (int i = 0; i < emotions.size(); i++) {
                emotions.get(i).startAnimatedSize = emotions.get(i).size;

                if (i == selectedIndex) {
                    emotions.get(i).endAnimatedSize = Emotion.LARGE_SIZE;
                } else {
                    emotions.get(i).endAnimatedSize = Emotion.SMALL_SIZE;
                }
            }
        }

        private void prepareRoundedBoard() {
            board.startAnimatedHeight = board.height;
            board.endAnimatedHeight = RoundedBoard.SCALED_DOWN_HEIGHT;
        }

        @Override
        protected void applyTransformation(float interpolatedTime, Transformation t) {
            animateRoundedBoard(interpolatedTime);
            //animateEmotions(interpolatedTime);
        }
    }

    private class DeselectAnimation extends Animation {

        DeselectAnimation() {
            setDuration(SCALE_DURATION);
        }

        void prepare() {
            prepareRoundedBoard();
            prepareEmotions();
        }

        private void prepareEmotions() {
            for (Emotion emotion : emotions) {
                emotion.startAnimatedSize = emotion.size;
                emotion.endAnimatedSize = Emotion.MEDIUM_SIZE;
            }
        }

        private void prepareRoundedBoard() {
            board.startAnimatedHeight = board.height;
            board.endAnimatedHeight =RoundedBoard.HEIGHT;
        }

        @Override
        protected void applyTransformation(float interpolatedTime, Transformation t) {
            animateRoundedBoard(interpolatedTime);
            //animateEmotions(interpolatedTime);
        }
    }

    private class TranslationAnimation extends Animation {

        private static final int TRANSLATION_DISTANCE = 180;
        private final int EMOTION_RADIUS = Emotion.MEDIUM_SIZE / 2;

        TranslationAnimation() {
            setDuration(TRANSLATION_DURATION);
            prepareRoundedBoard();
            prepareEmotions();
        }

        private void prepareEmotions() {
            for (int i = 0; i < emotions.size(); i++) {
                emotions.get(i).endAnimatedY = RoundedBoard.TOP + Constants.VERTICAL_SPACING;
                emotions.get(i).startAnimatedY =
                        emotions.get(i).y = RoundedBoard.BOTTOM + TRANSLATION_DISTANCE;

                emotions.get(i).startAnimatedX
                        = emotions.get(i).x = i == 0 ? RoundedBoard.LEFT
                        + Constants.HORIZONTAL_SPACING + (Emotion.MEDIUM_SIZE / 2)
                        : emotions.get(i - 1).x + Emotion.MEDIUM_SIZE + Constants.HORIZONTAL_SPACING;
            }
        }

        private void prepareRoundedBoard() {
            board.startAnimatedY = board.y = RoundedBoard.BOTTOM + TRANSLATION_DISTANCE;
            board.endAnimatedY = RoundedBoard.TOP;
        }

        @Override
        protected void applyTransformation(float interpolatedTime, Transformation t) {
            translateEmotions(interpolatedTime);
            translateRoundedBoard();
        }

        private void translateEmotions(float interpolatedTime) {
            float currentTime = interpolatedTime * TRANSLATION_DURATION;
            for (int i = 0; i < emotions.size(); i++) {

                int delayOfChild = CHILD_DELAY * i;

                Emotion view = emotions.get(i);
                if ((currentTime > delayOfChild)) {
                    if ((currentTime - delayOfChild) <= CHILD_TRANSLATION_DURATION) {

                        float progressOfChild = ((currentTime - delayOfChild) / CHILD_TRANSLATION_DURATION);
                        view.y = view.startAnimatedY +
                                progressOfChild * (view.endAnimatedY - view.startAnimatedY);

                        view.x = view.startAnimatedX - progressOfChild * EMOTION_RADIUS;

                        view.setCurrentSize((int) (progressOfChild * Emotion.MEDIUM_SIZE));
                    } else {
                        view.x = view.startAnimatedX - EMOTION_RADIUS;
                        view.y = view.endAnimatedY;
                        view.setCurrentSize(Emotion.MEDIUM_SIZE);
                    }
                }
            }
        }

        private void translateRoundedBoard() {
            Emotion firstEmoticon = emotions.get(0);
            float d =
                    (firstEmoticon.y - firstEmoticon.startAnimatedY) / (firstEmoticon.endAnimatedY
                            - firstEmoticon.startAnimatedY) * (board.endAnimatedY - board.startAnimatedY);

            board.y = board.startAnimatedY + d;
        }

    }


    public void PostLike(String reaction_type, String eventid, String feedid, String token) {

//        showProgress();
        mAPIService.NewsFeedReaction(reaction_type, eventid, feedid, token).enqueue(new Callback<LikePost>() {
            @Override
            public void onResponse(Call<LikePost> call, Response<LikePost> response) {

                if (response.isSuccessful()) {
                    Log.i("hit", "post submitted to API." + response.toString());
//                    dismissProgress();
                    showPostlikeresponse(response);
                } else {
//                    dismissProgress();
                    Toast.makeText(getContext(), response.body().getMsg(), Toast.LENGTH_SHORT).show();

                    // Toast.makeText(getContext(), "Unable to process", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<LikePost> call, Throwable t) {
                Log.e("hit", "Unable to submit post to API.");
//                dismissProgress();
                // Toast.makeText(getContext(), "Unable to process", Toast.LENGTH_SHORT).show();

            }
        });
    }

    private void showPostlikeresponse(Response<LikePost> response) {

        if (response.body().getStatus().equals("Success")) {
            String count = String.valueOf(response.body().getLikeCount());
            liketext.setText(count + " Likes");
            feed.setTotalLikes(String.valueOf(count));

           // String likeFlag = String.valueOf(response.body().getLikeFlag());
            WallFragment_POST.newsfeedList.set(position, feed);
            Log.e("post", "success");
/*
            if (device.equalsIgnoreCase("vivo V3")) {
                fetchFeed(token, eventid);
            }*/
//            fetchFeed(token, eventid);
        } else {
            Log.e("post", "fail");
            Toast.makeText(getContext(), response.body().getMsg(), Toast.LENGTH_SHORT).show();
        }
    }


}
