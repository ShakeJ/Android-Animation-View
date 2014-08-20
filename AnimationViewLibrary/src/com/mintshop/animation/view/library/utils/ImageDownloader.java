package com.mintshop.animation.view.library.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Handler;
import android.widget.ImageView;

import com.mintshop.animation.view.library.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

public class ImageDownloader
{
  private static ImageLoader imageLoader;
  
  
  public static ImageLoader getInstance(Context context)
  {
    if (imageLoader == null)
    {
      imageLoader = ImageLoader.getInstance();
      ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(context).build();
      imageLoader.init(config);
    }
    
    return imageLoader;
  }
  
  
  public static void cleanMemory()
  {
    try
    {
      imageLoader.clearDiskCache();
      imageLoader.clearMemoryCache();
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }
  }
  
  
  public static Bitmap getImageBitmap(Context context, String url)
  {
    if (imageLoader == null)
    {
      imageLoader = ImageLoader.getInstance();
      ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(context).build();
      imageLoader.init(config);
    }
    
    DisplayImageOptions options = new DisplayImageOptions.Builder().showImageOnLoading(null).bitmapConfig(Bitmap.Config.RGB_565).showImageForEmptyUri(null).showImageOnFail(null).imageScaleType(
        ImageScaleType.EXACTLY).build();
    return ImageDownloader.getInstance(context).loadImageSync(url, options);
  }
  
  
  public static Bitmap getProfileImageBitmap(Context context, String url)
  {
    if (imageLoader == null)
    {
      imageLoader = ImageLoader.getInstance();
      ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(context).build();
      imageLoader.init(config);
    }
    DisplayImageOptions options = new DisplayImageOptions.Builder().imageScaleType(ImageScaleType.IN_SAMPLE_POWER_OF_2).build();
    return ImageDownloader.getInstance(context).loadImageSync(url, options);
  }
}
