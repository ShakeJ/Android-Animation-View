package com.shakej.animation.view.library.controllers;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayer.ErrorReason;
import com.google.android.youtube.player.YouTubePlayer.OnInitializedListener;
import com.google.android.youtube.player.YouTubePlayer.PlaybackEventListener;
import com.google.android.youtube.player.YouTubePlayer.PlayerStateChangeListener;
import com.google.android.youtube.player.YouTubePlayer.PlayerStyle;
import com.google.android.youtube.player.YouTubePlayer.Provider;
import com.google.android.youtube.player.YouTubePlayerView;
import com.shakej.animation.view.library.R;
import com.shakej.animation.view.library.listeners.AnimationViewListener;

public class YoutubeViewer extends RelativeLayout
{
  private YouTubePlayerView youtubeView;
  private LinearLayout btnNext;
  private String videoId;
  private AnimationViewListener listener;
  private String youtubeDeveloperKey;
  private Context context;
  
  
  public YoutubeViewer(Context context)
  {
    super(context);
    this.context = context;
    init(context);
  }
  
  
  public YoutubeViewer(Context context, AttributeSet attrs)
  {
    super(context, attrs);
    this.context = context;
    init(context);
  }
  
  
  public YoutubeViewer(Context context, AttributeSet attrs, int defStyle)
  {
    super(context, attrs, defStyle);
    this.context = context;
    init(context);
  }
  
  
  @SuppressLint("InflateParams")
  private void init(Context context)
  {
    removeAllViews();
    LayoutInflater inflate = LayoutInflater.from(context);
    View view = inflate.inflate(R.layout.view_youtube, null);
    
//    youtubeView = (YouTubePlayerView) view.findViewById(R.id.youtube_view);
    btnNext = (LinearLayout) view.findViewById(R.id.btn_next);
    if (context.getPackageManager().hasSystemFeature("com.google.android.tv"))
      btnNext.setVisibility(View.GONE);
    addView(view);
  }
  
  
  public void addListener(AnimationViewListener listener)
  {
    this.listener = listener;
  }
  
  
  public void setData(String youtubeDeveloperKey, String videoId)
  {
    this.videoId = videoId;
    this.youtubeDeveloperKey = youtubeDeveloperKey;
  }
  
  
  public void initialize()
  {
    youtubeView.initialize(youtubeDeveloperKey, initListener);
  }
  
  private OnInitializedListener initListener = new OnInitializedListener()
  {
    @Override
    public void onInitializationSuccess(Provider provider, final YouTubePlayer player, boolean wasRestored)
    {
      player.loadVideo(videoId);
      if (context.getPackageManager().hasSystemFeature("com.google.android.tv"))
        player.setPlayerStyle(PlayerStyle.CHROMELESS);
      Handler mHandler = new Handler();
      mHandler.postDelayed(new Runnable()
      {
        @Override
        public void run()
        {
          try
          {
            player.play();
          }
          catch (Exception e)
          {
            e.printStackTrace();
          }
        }
      }, 2000);
      
      youtubeView.setOnClickListener(new OnClickListener()
      {
        @Override
        public void onClick(View v)
        {
          showNextButton(true);
        }
      });
      
      player.setPlaybackEventListener(new PlaybackEventListener()
      {
        @Override
        public void onStopped()
        {
        }
        
        
        @Override
        public void onSeekTo(int arg0)
        {
        }
        
        
        @Override
        public void onPlaying()
        {
          new Handler().postDelayed(new Runnable()
          {
            
            @Override
            public void run()
            {
              showNextButton(false);
            }
          }, 1000);
        }
        
        
        @Override
        public void onPaused()
        {
        }
        
        
        @Override
        public void onBuffering(boolean arg0)
        {
        }
      });
      
      player.setPlayerStateChangeListener(new PlayerStateChangeListener()
      {
        @Override
        public void onVideoStarted()
        {
          if (listener != null)
            listener.onYoutubeViewerStart();
          
          new Handler().postDelayed(new Runnable()
          {
            @Override
            public void run()
            {
              showNextButton(false);
            }
          }, 1000);
        }
        
        
        @Override
        public void onVideoEnded()
        {
          if (listener != null)
            listener.onYoutubeViewerFinish();
          player.release();
        }
        
        
        @Override
        public void onLoading()
        {
        }
        
        
        @Override
        public void onLoaded(String arg0)
        {
          Handler mHandler = new Handler();
          mHandler.postDelayed(new Runnable()
          {
            @Override
            public void run()
            {
              try
              {
                //try catch를 쓴 이유는 사용자가 화면을 나가서 player가 릴리즈가 된 경우가 있어서 
                player.play();
              }
              catch (Exception e)
              {
                
              }
            }
          }, 2000);
        }
        
        
        @Override
        public void onError(ErrorReason s)
        {
          if (!s.name().contains("UNAUTHORIZED_OVERLAY"))
            setNextMovie();
        }
        
        
        @Override
        public void onAdStarted()
        {
        }
      });
    }
    
    
    @Override
    public void onInitializationFailure(Provider provider, YouTubeInitializationResult result)
    {
    }
  };
  
  
  public void setNextMovie()
  {
    if (listener != null)
      listener.onYoutubeViewerFinish();
    YoutubeViewer.this.setVisibility(View.GONE);
  }
  
  
  private void showNextButton(boolean isShow)
  {
    if (!context.getPackageManager().hasSystemFeature("com.google.android.tv"))
    {
      if (isShow)
      {
        btnNext.bringToFront();
        btnNext.setVisibility(View.VISIBLE);
        btnNext.setOnClickListener(new OnClickListener()
        {
          @Override
          public void onClick(View v)
          {
            Log.w("WARN", "Next button click : " + listener);
            if (listener != null)
              listener.onYoutubeViewerFinish();
            YoutubeViewer.this.setVisibility(View.GONE);
          }
        });
      }
      else
        btnNext.setVisibility(View.GONE);
    }
  }
}
