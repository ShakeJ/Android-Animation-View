package com.shakej.animation.view.library.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Point;
import android.os.Build;
import android.os.PowerManager;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

public class SystemManager
{
  private static SystemManager _instance;
  private Context context;
  
  
  public static SystemManager getInstance(Context context)
  {
    if (_instance == null)
      _instance = new SystemManager(context);
    return _instance;
  }
  
  private static PowerManager.WakeLock wakeLock;
  
  
  public SystemManager(Context context)
  {
    super();
    this.context = context;
  }
  
  
  public Point screenSize()
  {
    Point kPoint = new Point();
    ((WindowManager) context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getSize(kPoint);
    return kPoint;
  }
  
  
  @SuppressWarnings("deprecation")
  public void displayAlwaysWakeUp()
  {
    final PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
    wakeLock = pm.newWakeLock(PowerManager.FULL_WAKE_LOCK, "");
    wakeLock.acquire();
  }
  
  
  public void releaseWakeLock()
  {
    wakeLock.release();
  }
  
  
  @SuppressLint("InlinedApi")
  public void setImmersiveMode(Window window)
  {
    if (Build.VERSION.SDK_INT == Build.VERSION_CODES.KITKAT)
      window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
    else
      window.getDecorView().setSystemUiVisibility(View.GONE);
  }
  
}
