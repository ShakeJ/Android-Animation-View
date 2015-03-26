package com.shakej.animation.view.library.controllers;

import android.animation.Animator;
import android.animation.Animator.AnimatorListener;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.TranslateAnimation;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.shakej.animation.view.library.R;
import com.shakej.animation.view.library.listeners.AnimationViewListener;
import com.shakej.animation.view.library.utils.Rotate3dAnimation;
import com.shakej.animation.view.library.utils.SystemManager;

public class BaseAnimationView extends LinearLayout
{
  public final static int DIRECTION_RIGHT = 0;
  public final static int DIRECTION_UP = 1;
  public final static int DIRECTION_FADE_IN = 2;
  public final static int PIVOT_RIGHT_TO_LEFT = 3;
  public final static int PIVOT_UP_TO_DOWN = 4;
  
  //Animation Attrs
  public int direction = PIVOT_UP_TO_DOWN;
  protected final static int DELAY_TIME = 1000;
  public int slideShowTime = 1000;
  public int delayTime = DELAY_TIME;
  
  private Context context;
  public AnimationViewListener listener;
  
  public RelativeLayout imageContainer;
  public DescriptionView descriptionContainer;
  
  
  public BaseAnimationView(Context context)
  {
    super(context);
    this.context = context;
  }
  
  
  public BaseAnimationView(Context context, AttributeSet attrs)
  {
    super(context, attrs);
    this.context = context;
  }
  
  
  public BaseAnimationView(Context context, AttributeSet attrs, int defStyle)
  {
    super(context, attrs, defStyle);
    this.context = context;
  }
  
  
  public void setContentImageLayout(RelativeLayout imageLayout)
  {
    this.imageContainer = imageLayout;
  }
  
  
  public void setDescriptionView(DescriptionView view)
  {
    this.descriptionContainer = view;
  }
  
  
  public void addListener(AnimationViewListener listener)
  {
    this.listener = listener;
  }
  
  
  public void setAnimationDirection(int direction)
  {
    this.direction = direction;
  }
  
  
  public void setAnimationTime(int slideShowTime)
  {
    this.slideShowTime = slideShowTime;
  }
  
  
  public void startViewAnimation()
  {
    Log.w("WARN", "Start Animation");
    boolean isGoogleTv = context.getPackageManager().hasSystemFeature("com.google.android.tv");
    if (isGoogleTv)
      animateViewForGoogleTV();
    else
      animateViewForAndroid();
  }
  
  
  public void startViewFinishAnimation(final Runnable run)
  {
    boolean isGoogleTv = context.getPackageManager().hasSystemFeature("com.google.android.tv");
    if (isGoogleTv)
      animateFinishForGoogleTV(run);
    else
      animateFinishForAndroid(run);
  }
  
  
  //Animation for android
  private void animateViewForAndroid()
  {
    //For Android 
    ObjectAnimator imageAnimation;
    ObjectAnimator descriptionAnimation = null;
    
    if (direction == DIRECTION_RIGHT)
      imageAnimation = ObjectAnimator.ofFloat(imageContainer, "translationX", -SystemManager.getInstance(context).screenSize().x, 0);
    else if (direction == DIRECTION_UP)
      imageAnimation = ObjectAnimator.ofFloat(imageContainer, "translationY", -SystemManager.getInstance(context).screenSize().y, 0);
    else if (direction == PIVOT_UP_TO_DOWN)
    {
      descriptionAnimation = ObjectAnimator.ofFloat(descriptionContainer, "translationY", descriptionContainer.getHeight(), 0);
      imageAnimation = ObjectAnimator.ofFloat(imageContainer, "rotationX", 270, 370, 360);
      imageContainer.setPivotX(imageContainer.findViewById(R.id.content_image).getWidth() / 2);
      imageContainer.setPivotY(0);
    }
    else if (direction == PIVOT_RIGHT_TO_LEFT)
    {
      descriptionAnimation = ObjectAnimator.ofFloat(descriptionContainer, "translationX", -descriptionContainer.getWidth(), 0);
      imageAnimation = ObjectAnimator.ofFloat(imageContainer, "rotationY", -90, 3, 0);
      imageContainer.setPivotX(imageContainer.getWidth());
      imageContainer.setPivotY(imageContainer.getHeight() / 2);
    }
    else
    {
      descriptionAnimation = ObjectAnimator.ofFloat(descriptionContainer, "translationY", -SystemManager.getInstance(context).screenSize().y, 0);
      imageAnimation = ObjectAnimator.ofFloat(imageContainer, "alpha", 0, 1);
    }
    
    imageAnimation.setDuration(slideShowTime);
    imageAnimation.addListener(new AnimatorListener()
    {
      @Override
      public void onAnimationStart(Animator arg0)
      {
        if (descriptionContainer != null)
          descriptionContainer.setVisibility(View.VISIBLE);
        imageContainer.setVisibility(View.VISIBLE);
        if (listener != null)
          listener.onAnimationStart();
      }
      
      
      @Override
      public void onAnimationRepeat(Animator arg0)
      {
      }
      
      
      @Override
      public void onAnimationEnd(Animator arg0)
      {
        if (listener != null)
          listener.onAnimationFinish();
      }
      
      
      @Override
      public void onAnimationCancel(Animator arg0)
      {
      }
    });
    imageAnimation.start();
    
    if (descriptionAnimation != null)
    {
      descriptionAnimation.setDuration(slideShowTime);
      descriptionAnimation.start();
    }
  }
  
  
  //Animation For Google Tv
  private void animateViewForGoogleTV()
  {
    Animation imageAnimation = null;
    Animation descriptionAnimation = null;
    
    if (direction == DIRECTION_RIGHT || direction == PIVOT_RIGHT_TO_LEFT)
    {
      imageAnimation = getRotateAnimation(270, 360, direction);
      descriptionAnimation = new TranslateAnimation(-descriptionContainer.getWidth(), 0, 0, 0);
    }
    else
    {
      imageAnimation = getRotateAnimation(-90, 0, direction);
      descriptionAnimation = new TranslateAnimation(0, 0, descriptionContainer.getHeight(), 0);
    }
    
    imageAnimation.setDuration(slideShowTime);
    
    imageAnimation.setAnimationListener(new AnimationListener()
    {
      @Override
      public void onAnimationStart(Animation animation)
      {
        if (descriptionContainer != null)
          descriptionContainer.setVisibility(View.VISIBLE);
        imageContainer.setVisibility(View.VISIBLE);
        if (listener != null)
          listener.onAnimationStart();
        
        if (listener != null)
          listener.onAnimationFinish();
      }
      
      
      @Override
      public void onAnimationRepeat(Animation animation)
      {
        
      }
      
      
      @Override
      public void onAnimationEnd(Animation animation)
      {
        
      }
    });
    imageContainer.startAnimation(imageAnimation);
    
    if (descriptionAnimation != null)
    {
      descriptionAnimation.setDuration(slideShowTime);
      descriptionContainer.startAnimation(descriptionAnimation);
    }
  }
  
  
  //Finish Animation for android 
  private void animateFinishForAndroid(final Runnable run)
  {
    //For Android
    ObjectAnimator imageAnimation;
    ObjectAnimator descriptionAnimation = null;
    
    if (direction == DIRECTION_RIGHT)
      imageAnimation = ObjectAnimator.ofFloat(imageContainer, "translationX", 0, -SystemManager.getInstance(context).screenSize().x);
    else if (direction == DIRECTION_UP)
      imageAnimation = ObjectAnimator.ofFloat(imageContainer, "translationY", 0, -SystemManager.getInstance(context).screenSize().y);
    else if (direction == PIVOT_UP_TO_DOWN)
    {
      descriptionAnimation = ObjectAnimator.ofFloat(descriptionContainer, "translationY", 0, descriptionContainer.getHeight());
      imageAnimation = ObjectAnimator.ofFloat(imageContainer, "rotationX", 360, 370, 270);
      imageContainer.setPivotX(imageContainer.findViewById(R.id.content_image).getWidth() / 2);
      imageContainer.setPivotY(0);
    }
    else if (direction == PIVOT_RIGHT_TO_LEFT)
    {
      descriptionAnimation = ObjectAnimator.ofFloat(descriptionContainer, "translationX", 0, -descriptionContainer.getWidth());
      imageAnimation = ObjectAnimator.ofFloat(imageContainer, "rotationY", 0, 3, -90);
      imageContainer.setPivotX(imageContainer.findViewById(R.id.content_image).getWidth());
      imageContainer.setPivotY(imageContainer.findViewById(R.id.content_image).getHeight() / 2);
    }
    else
    {
      descriptionAnimation = ObjectAnimator.ofFloat(descriptionContainer, "translationY", -SystemManager.getInstance(context).screenSize().y, 0);
      imageAnimation = ObjectAnimator.ofFloat(imageContainer, "alpha", 1, 0);
    }
    
    imageAnimation.setDuration(slideShowTime);
    imageAnimation.addListener(new AnimatorListener()
    {
      @Override
      public void onAnimationStart(Animator arg0)
      {
      }
      
      
      @Override
      public void onAnimationRepeat(Animator arg0)
      {
      }
      
      
      @Override
      public void onAnimationEnd(Animator arg0)
      {
        run.run();
      }
      
      
      @Override
      public void onAnimationCancel(Animator arg0)
      {
      }
    });
    imageAnimation.start();
    
    if (descriptionAnimation != null)
    {
      descriptionAnimation.setDuration(slideShowTime);
      descriptionAnimation.start();
    }
  }
  
  
  //Finish Animation for GoogleTV 
  private void animateFinishForGoogleTV(final Runnable run)
  {
    Animation imageAnimation;
    Animation descriptionAnimation = null;
    
    if (direction == DIRECTION_RIGHT || direction == PIVOT_RIGHT_TO_LEFT)
    {
      imageAnimation = getRotateAnimation(360, 270, direction);
      descriptionAnimation = new TranslateAnimation(0, -descriptionContainer.getWidth(), 0, 0);
    }
    else
    {
      imageAnimation = getRotateAnimation(0, -90, direction);
      descriptionAnimation = new TranslateAnimation(0, 0, 0, descriptionContainer.getHeight());
    }
    
    imageAnimation.setDuration(slideShowTime);
    imageAnimation.setAnimationListener(new AnimationListener()
    {
      @Override
      public void onAnimationStart(Animation animation)
      {
      }
      
      
      @Override
      public void onAnimationRepeat(Animation animation)
      {
        
      }
      
      
      @Override
      public void onAnimationEnd(Animation animation)
      {
        run.run();
      }
    });
    
    imageContainer.startAnimation(imageAnimation);
    if (descriptionAnimation != null)
    {
      descriptionAnimation.setDuration(slideShowTime);
      descriptionContainer.startAnimation(descriptionAnimation);
    }
  }
  
  
  private Animation getRotateAnimation(float start, float end, int direction)
  {
    Rotate3dAnimation rotation = null;
    
    if (direction == PIVOT_RIGHT_TO_LEFT)
      rotation = new Rotate3dAnimation(start, end, imageContainer.getWidth(), imageContainer.getHeight() / 2, 0.0f, direction);
    else
      rotation = new Rotate3dAnimation(start, end, imageContainer.getWidth() / 2, 0, 0.0f, direction);
    
    rotation.setDuration(slideShowTime);
    rotation.setFillAfter(true);
    rotation.setInterpolator(new AccelerateInterpolator());
    return rotation;
  }
}
