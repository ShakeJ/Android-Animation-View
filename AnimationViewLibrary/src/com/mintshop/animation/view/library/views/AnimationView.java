package com.mintshop.animation.view.library.views;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Handler;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.mintshop.animation.view.library.R;
import com.mintshop.animation.view.library.beans.AnimationBean;
import com.mintshop.animation.view.library.beans.DescriptionBean;
import com.mintshop.animation.view.library.listeners.AnimationViewListener;
import com.mintshop.animation.view.library.utils.ImageDownloader;
import com.mintshop.animation.view.library.utils.ImageUtil;
import com.mintshop.animation.view.library.utils.SystemManager;

public class AnimationView extends BaseAnimationView
{
  private Context context;
  
  public View inflatedView;
  public DescriptionView descriptionView;
  public RelativeLayout imageContainer;
  private TextView textTitle;
  
  public AnimationBean bean = new AnimationBean();
  
  public ImageView contentImageView;
  private TextView contentTextView;
  private RelativeLayout customViewContainer;
  private YoutubeViewer youtubeViewer;
  
  private Bitmap imageBitmap = null;
  
  private WebView readabilityWebView;
  private boolean isYoutubeMode = false;
  
  
  public AnimationView(Context context, AnimationViewListener listener)
  {
    super(context);
    this.context = context;
    super.listener = listener;
    init(context);
  }
  
  
  public AnimationView(Context context, AttributeSet attrs)
  {
    super(context, attrs);
    this.context = context;
    init(context);
  }
  
  
  public AnimationView(Context context, AttributeSet attrs, int defStyle)
  {
    super(context, attrs, defStyle);
    this.context = context;
    init(context);
  }
  
  
  @SuppressLint({ "InflateParams", "ClickableViewAccessibility" })
  private void init(final Context context)
  {
    removeAllViews();
    this.setOrientation(LinearLayout.VERTICAL);
    LayoutInflater inflater = LayoutInflater.from(context);
    inflatedView = inflater.inflate(R.layout.view_animation, null);
    
    textTitle = (TextView) inflatedView.findViewById(R.id.text_title);
    
    descriptionView = (DescriptionView) inflatedView.findViewById(R.id.description_view);
    contentTextView = (TextView) inflatedView.findViewById(R.id.center_description_view);
    customViewContainer = (RelativeLayout) inflatedView.findViewById(R.id.container_custom_view);
    readabilityWebView = (WebView) inflatedView.findViewById(R.id.readability_web_view);
    youtubeViewer = (YoutubeViewer) inflatedView.findViewById(R.id.youtube_viewer);
    
    imageContainer = (RelativeLayout) inflatedView.findViewById(R.id.container_image);
    contentImageView = (ImageView) inflatedView.findViewById(R.id.content_image);
    
    ImageView imageScrollView = (ImageView) imageContainer.findViewById(R.id.content_image);
    RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, SystemManager.getInstance(context).screenSize().y);
    imageScrollView.setLayoutParams(params);
    
    super.setContentImageLayout(imageContainer);
    descriptionView.setVisibility(View.INVISIBLE);
    imageContainer.setVisibility(View.INVISIBLE);
    
    contentImageView.setOnClickListener(new OnClickListener()
    {
      @Override
      public void onClick(View v)
      {
        if (listener != null)
          listener.onViewClick();
      }
    });
    descriptionView.setOnClickListener(new OnClickListener()
    {
      @Override
      public void onClick(View v)
      {
        if (listener != null)
          listener.onViewClick();
      }
    });
    
    super.setDescriptionView(descriptionView);
    addView(inflatedView);
  }
  
  
  @Override
  protected void onConfigurationChanged(Configuration newConfig)
  {
    super.onConfigurationChanged(newConfig);
    ImageView imageScrollView = (ImageView) imageContainer.findViewById(R.id.content_image);
    RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, SystemManager.getInstance(context).screenSize().y);
    imageScrollView.setLayoutParams(params);
  }
  
  
  public void addListener(AnimationViewListener listener)
  {
    super.listener = listener;
    super.addListener(listener);
  }
  
  
  //기본형 
  public void setData(final AnimationBean bean)
  {
    this.bean = bean;
    
    contentTextView.setVisibility(View.GONE);
    customViewContainer.setVisibility(View.GONE);
    contentImageView.setVisibility(View.VISIBLE);
    
    if (!TextUtils.isEmpty(bean.title))
    {
      textTitle.setText(bean.title);
      textTitle.setVisibility(View.VISIBLE);
      textTitle.bringToFront();
    }
    else
      textTitle.setVisibility(View.GONE);
    
    if (!TextUtils.isEmpty(bean.pictureOriginalUrl))
    {
      // 이미지인 경우
      Log.w("WARN", "AnimationView setData - Image");
      new ImageDownloaderTask().execute(bean.pictureOriginalUrl);
    }
    else
    {
      // 텍스트만 있는 경우
      if (!TextUtils.isEmpty(bean.readabilityUrl))
      {
        Log.w("WARN", "AnimationView setData - Readability");
        contentImageView.setVisibility(View.GONE);
        customViewContainer.setVisibility(View.GONE);
        textTitle.setVisibility(View.GONE);
        
        readabilityWebView.setVisibility(View.VISIBLE);
        loadReadability(bean.readabilityUrl);
      }
      else
      {
        Log.w("WARN", "AnimationView setData - Center Text");
        contentImageView.setVisibility(View.GONE);
        customViewContainer.setVisibility(View.GONE);
        textTitle.setVisibility(View.GONE);
        contentTextView.setVisibility(View.VISIBLE);
        
        contentTextView.setText(bean.title);
        contentTextView.bringToFront();
        
        Handler h = new Handler();
        h.postDelayed(new Runnable()
        {
          @Override
          public void run()
          {
            if (listener != null)
              listener.onLoaded();
          }
        }, 2000);
      }
    }
  }
  
  
  //커스텀한 뷰를 만들어서 붙이면 애니메이션
  public void setView(View view)
  {
    Log.w("WARN", "AnimationView set CustomView");
    
    customViewContainer.addView(view);
    contentImageView.setVisibility(View.GONE);
    contentTextView.setVisibility(View.GONE);
    textTitle.setVisibility(View.GONE);
    youtubeViewer.setVisibility(View.GONE);
    
    customViewContainer.setVisibility(View.VISIBLE);
    customViewContainer.getLayoutParams().height = SystemManager.getInstance(context).screenSize().y;
    customViewContainer.getLayoutParams().width = SystemManager.getInstance(context).screenSize().x;
    
    RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) view.getLayoutParams();
    params.addRule(RelativeLayout.CENTER_IN_PARENT);
    
    view.setLayoutParams(params);
    view.setOnClickListener(new OnClickListener()
    {
      
      @Override
      public void onClick(View v)
      {
        if (listener != null)
          listener.onViewClick();
      }
    });
    
    Handler h = new Handler();
    h.postDelayed(new Runnable()
    {
      @Override
      public void run()
      {
        if (listener != null)
          listener.onLoaded();
      }
    }, 1000);
  }
  
  
  //디스크립션 상자들이 리스트로 나타남 
  public void setStringArray(ArrayList<DescriptionBean> descriptions)
  {
    Log.w("WARN", "AnimationView set String Array");
    bean.descriptions.addAll(descriptions);
    
    contentImageView.setVisibility(View.GONE);
    contentTextView.setVisibility(View.GONE);
    textTitle.setVisibility(View.GONE);
    customViewContainer.setVisibility(View.GONE);
    
    final ListView replyListView = (ListView) inflatedView.findViewById(R.id.content_retweet);
    replyListView.setAdapter(new DescriptionListAdapter(context, bean.descriptions));
    replyListView.setVisibility(View.VISIBLE);
    
    Handler mHandler = new Handler();
    mHandler.postDelayed(new Runnable()
    {
      @Override
      public void run()
      {
        if (listener != null)
          listener.onLoaded();
      }
    }, 1000);
  }
  
  
  //유튜브 
  public void setMovie(String youtubeDeveloperKey, String videoId)
  {
    isYoutubeMode = true;
    youtubeViewer.setData(youtubeDeveloperKey, videoId);
    contentImageView.setVisibility(View.GONE);
    contentTextView.setVisibility(View.GONE);
    textTitle.setVisibility(View.GONE);
    customViewContainer.setVisibility(View.GONE);
    youtubeViewer.setVisibility(View.VISIBLE);
    youtubeViewer.addListener(listener);
    
    Handler mHandler = new Handler();
    mHandler.postDelayed(new Runnable()
    {
      @Override
      public void run()
      {
        if (listener != null)
          listener.onLoaded();
      }
    }, 1000);
  }
  
  class ImageDownloaderTask extends AsyncTask<String, Integer, Bitmap>
  {
    @Override
    protected Bitmap doInBackground(String... params)
    {
      try
      {
        imageBitmap = ImageDownloader.getImageBitmap(context, params[0]);
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
      
      new CalculateBitmapAnimation().execute(imageBitmap);
      contentImageView.setImageBitmap(imageBitmap);
      if (listener != null)
        listener.onLoaded();
    }
  }
  
  
  public void removeDisplayView()
  {
    contentImageView.setImageBitmap(null);
    if (imageBitmap != null)
    {
      imageBitmap.recycle();
      imageBitmap = null;
    }
    descriptionView.removeDisplayView();
    readabilityWebView = null;
  }
  
  
  public void setAnimationDirection(int direction)
  {
    super.direction = direction;
  }
  
  
  public void setAnimationTime(int slideShowTime)
  {
    super.slideShowTime = slideShowTime;
  }
  
  
  public void addDescriptionView(DescriptionBean descriptionBean)
  {
    descriptionView.setData(context, descriptionBean);
  }
  
  
  public void startAnimation()
  {
    if (isYoutubeMode)
    {
      Log.w("WARN", "Start Youtube");
      youtubeViewer.initialize();
    }
    else
    {
      startViewAnimation();
    }
  }
  
  
  public void startFinishAnimation(Runnable run)
  {
    startViewFinishAnimation(run);
  }
  
  private class CalculateBitmapAnimation extends AsyncTask<Bitmap, Integer, Integer>
  {
    @Override
    protected Integer doInBackground(Bitmap... params)
    {
      return ImageUtil.caculateAnimationNumber(params[0]);
    }
    
    
    @Override
    protected void onPostExecute(Integer animationNumber)
    {
      super.onPostExecute(animationNumber);
      setAnimationDirection(animationNumber);
    }
  }
  
  
  //Load Readability
  @SuppressLint("SetJavaScriptEnabled")
  private void loadReadability(String url)
  {
    readabilityWebView.setWebChromeClient(new CaptureWebViewClient());
    readabilityWebView.setWebViewClient(new WebViewClient()
    {
      @Override
      public boolean shouldOverrideUrlLoading(WebView view, String url)
      {
        Log.w("WARN", "View url : " + url);
        view.loadUrl(url);
        return true;
      }
      
      
      @Override
      public void onPageFinished(WebView view, String url)
      {
        super.onPageFinished(view, url);
        try
        {
          if (readabilityWebView != null)
          {
            readabilityWebView.setEnabled(false);
          }
        }
        catch (Exception e)
        {
          e.printStackTrace();
        }
        if (listener != null)
          listener.onLoaded();
      }
      
      
      @Override
      public void onReceivedError(WebView view, int errorCode, String description, String failingUrl)
      {
        if (listener != null)
          listener.onLoadFail();
      }
    });
    readabilityWebView.getSettings().setJavaScriptEnabled(true);
    readabilityWebView.getSettings().setSupportMultipleWindows(false);
    readabilityWebView.getSettings().setJavaScriptCanOpenWindowsAutomatically(false);
    readabilityWebView.loadUrl(url);
  }
  
  //Readability
  public class CaptureWebViewClient extends WebChromeClient
  {
    @Override
    public boolean onJsAlert(WebView view, String url, String message, JsResult result)
    {
      return true;
    }
    
    
    @SuppressLint("SetJavaScriptEnabled")
    public void onProgressChanged(final WebView view, int newProgress)
    {
    };
    
  }
}
