package com.mintshop.animation.view.library.utils;

import android.graphics.Bitmap;

import com.mintshop.animation.view.library.views.BaseAnimationView;

public class ImageUtil
{
  public static int caculateAnimationNumber(Bitmap bitmap)
  {
    if (bitmap == null)
      return BaseAnimationView.PIVOT_RIGHT_TO_LEFT;
    
    float compare = Float.intBitsToFloat(bitmap.getWidth()) / Float.intBitsToFloat(bitmap.getHeight());
    if (compare > 1.2f)
      return BaseAnimationView.PIVOT_UP_TO_DOWN;
    else if (compare > 0.5f)
      return BaseAnimationView.PIVOT_RIGHT_TO_LEFT;
    else
      return BaseAnimationView.PIVOT_UP_TO_DOWN;
  }
}
