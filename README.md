Android-Animation-View
======================

Android animation view library (Pivot, Translate, etc)

This library support android animatate view.

Features

- Image animation view
- Youtube view (Now don't use)
- Text animation view
- Text list animation view

Animations
 
- Translate (right to left, up to down)
- Pivot (right to left, up to down)

setAnimationType, and setAnimationDuration, and etc method support. 

Demo Application 
'mintBoard' http://bit.ly/1uUjO8U 
'Digital photo frame with picasa' bit.ly/1IxSOQK


Example :

        AnimationBean bean = new AnimationBean();
        bean.pictureOriginalUrl = "Image url";
        animationView.setData(bean);

        animationView.setAnimationTime(SLIDE_TIME);
        animationView.addListener(new AnimationViewListener()
        {
          @Override
          public void onYoutubeViewerStart()
          {
          }
          
          
          @Override
          public void onYoutubeViewerFinish()
          {
          }
          
          
          @Override
          public void onViewClick()
          {
          }
          
          
          @Override
          public void onLoaded()
          {
          }
          
          
          @Override
          public void onLoadFail()
          {
            
          }
          
          
          @Override
          public void onAnimationStart()
          {
          }
          
          
          @Override
          public void onAnimationFinish()
          {
            slideHandler.postDelayed(new Runnable()
            {
              @Override
              public void run()
              {
                animationView.startFinishAnimation(new Runnable()
                {
                  @Override
                  public void run()
                  {
                    //Next method
                  }
                });
              }
            }, DELAY_TIME);
          }
        });
        animationView.startAnimation();