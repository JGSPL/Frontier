package com.procialize.mrgeApp20.NewsFeed.Views.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;
import com.procialize.mrgeApp20.GetterSetter.SelectedImages;
import com.procialize.mrgeApp20.R;

import java.util.List;

import cn.jzvd.JZVideoPlayer;
import cn.jzvd.JZVideoPlayerStandard;

public class ViewPagerMultimediaAdapter  extends PagerAdapter {

    private Context context;
    private LayoutInflater layoutInflater;
    List<SelectedImages> imagePathList;
    //final int mGridWidth;
    // private Integer [] images = {R.drawable.pic,R.drawable.abc,R.drawable.slide3};

    public ViewPagerMultimediaAdapter(Context context, List<SelectedImages> imagePathList) {
        this.context = context;
        this.imagePathList = imagePathList;
        /*WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        int width = 0;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            Point size = new Point();
            wm.getDefaultDisplay().getSize(size);
            width = size.x;
        } else {
            width = wm.getDefaultDisplay().getWidth();
        }
        mGridWidth = width ;*/
    }

    @Override
    public int getCount() {
        return imagePathList.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {

        JZVideoPlayerStandard videoview;
        TextView name;
        ImageView imgplay;
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.custom_layout, null);



        ImageView imageView = (ImageView) view.findViewById(R.id.imageView);
        videoview = view.findViewById(R.id.videoview);
        name = view.findViewById(R.id.name);
        imgplay = view.findViewById(R.id.imgplay);
        final ProgressBar progressBar = view.findViewById(R.id.progressbar);
        String albumFile = imagePathList.get(position).getmPath();



        if (albumFile.contains("png") || albumFile.contains("jpg") || albumFile.contains("jpeg") || albumFile.contains("gif")) {
            imageView.setVisibility(View.VISIBLE);
            videoview.setVisibility(View.GONE);

            imgplay.setVisibility(View.GONE);
            progressBar.setVisibility(View.GONE);
            JZVideoPlayer.goOnPlayOnPause();
            //imageView.setImageURI(Uri.parse(images.get(position)));
            Glide.with(videoview).load(imagePathList.get(position).getmPath()).into(imageView);


        } else if (albumFile.contains("mp4")) {
            imageView.setVisibility(View.GONE);
            videoview.setVisibility(View.VISIBLE);
            imgplay.setVisibility(View.GONE);
            progressBar.setVisibility(View.GONE);
            String videoPath = imagePathList.get(position).getmPath();
            videoview.setUp(videoPath
                    , JZVideoPlayerStandard.SCREEN_WINDOW_NORMAL, "");


            /*HttpProxyCacheServer proxy = getProxy(context);
            String proxyUrl = proxy.getProxyUrl(videoPath);
            videoview.setUp(proxyUrl , JZVideoPlayerStandard.SCREEN_WINDOW_NORMAL, "");*/

            Glide.with(videoview).load(videoPath).into(videoview.thumbImageView);


        }

        ViewPager vp = (ViewPager) container;
        vp.addView(view, 0);
        return view;
    }


    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        ViewPager vp = (ViewPager) container;
        View view = (View) object;
        JZVideoPlayer.releaseAllVideos();
        JZVideoPlayerStandard.releaseAllVideos();
        vp.removeView(view);
    }

}
