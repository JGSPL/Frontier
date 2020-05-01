package com.procialize.mrgeApp20.util;



import com.procialize.mrgeApp20.model.Feed;

import java.util.Arrays;
import java.util.List;

/**
 * Created by KenZira on 2/1/17.
 */

public class Constants {

  public static int HORIZONTAL_SPACING = DisplayUtil.dpToPx(4);

  public static int VERTICAL_SPACING = DisplayUtil.dpToPx(8);

  public static int HEIGHT_VIEW_REACTION = DisplayUtil.dpToPx(200);
//  public static int HEIGHT_VIEW_REACTION = DisplayUtil.dpToPx(300);

  public static final int MAX_ALPHA = 255;

  public static final List<Feed> feeds = Arrays.asList(new Feed(), new Feed());
}
