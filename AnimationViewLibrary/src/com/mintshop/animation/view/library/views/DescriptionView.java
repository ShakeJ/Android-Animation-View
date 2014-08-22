package com.mintshop.animation.view.library.views;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mintshop.animation.view.library.R;
import com.mintshop.animation.view.library.beans.DescriptionBean;
import com.mintshop.animation.view.library.utils.ImageDownloader;

@SuppressLint("InflateParams")
public class DescriptionView extends BaseAnimationView
{
  
  private DescriptionBean bean;
  private View view;
  private RoundedImageView profileImageView;
  private Bitmap imageBitmap = null;
  private Context context;
  private TextView textDescription;
  
  
  public DescriptionView(Context context, DescriptionBean bean)
  {
    super(context);
    this.bean = bean;
    this.context = context;
    init(context);
  }
  
  
  public DescriptionView(Context context, AttributeSet attrs)
  {
    super(context, attrs);
    this.context = context;
    init(context);
  }
  
  
  public DescriptionView(Context context, AttributeSet attrs, int defStyle)
  {
    super(context, attrs, defStyle);
    this.context = context;
    init(context);
  }
  
  
  public void setData(Context context, DescriptionBean bean)
  {
    this.bean = bean;
    init(context);
  }
  
  
  private void init(final Context context)
  {
    removeAllViews();
    this.setOrientation(LinearLayout.VERTICAL);
    LayoutInflater inflater = LayoutInflater.from(context);
    view = inflater.inflate(R.layout.view_description, null);
    
    TextView name = (TextView) view.findViewById(R.id.content_title);
    TextView subTitle = (TextView) view.findViewById(R.id.content_subtitle);
    TextView leftTitle = (TextView) view.findViewById(R.id.content_left_title);
    textDescription = (TextView) view.findViewById(R.id.content_description);
    profileImageView = ((RoundedImageView) view.findViewById(R.id.image_user_profile));
    
    View line = (View) view.findViewById(R.id.line_detail);
    
    if (bean != null)
    {
      if (TextUtils.isEmpty(bean.profileImageUrl))
        profileImageView.setVisibility(View.GONE);
      else
        new ImageDownloaderTask().execute(bean.profileImageUrl);
      
      name.setText(bean.title);
      
      if (!TextUtils.isEmpty(bean.subtitle))
        subTitle.setText(bean.subtitle);
      else
        subTitle.setVisibility(View.GONE);
      
      if (!TextUtils.isEmpty(bean.subDescription))
        leftTitle.setText(bean.subDescription);
      else
        leftTitle.setVisibility(View.GONE);
      
      if (!TextUtils.isEmpty(bean.description))
        textDescription.setText(bean.description);
      else
      {
        textDescription.setVisibility(View.GONE);
        line.setVisibility(View.GONE);
      }
      addView(view);
    }
  }
  
  
  public void setContentVisible(boolean isVisible)
  {
    if (isVisible)
      textDescription.setVisibility(View.VISIBLE);
    else
      textDescription.setVisibility(View.GONE);
  }
  
  
  public void removeDisplayView()
  {
    profileImageView.setImageBitmap(null);
    if (imageBitmap != null)
    {
      imageBitmap.recycle();
      imageBitmap = null;
    }
  }
  
  class ImageDownloaderTask extends AsyncTask<String, Integer, Bitmap>
  {
    @Override
    protected Bitmap doInBackground(String... params)
    {
      try
      {
        imageBitmap = ImageDownloader.getProfileImageBitmap(context, params[0]);
      }
      catch (Exception e)
      {
      }
      
      return null;
    }
    
    
    @Override
    protected void onPostExecute(Bitmap result)
    {
      super.onPostExecute(result);
      if (imageBitmap != null)
        profileImageView.setImageBitmap(imageBitmap);
      if (listener != null)
        listener.onLoaded();
    }
  }
  
}
