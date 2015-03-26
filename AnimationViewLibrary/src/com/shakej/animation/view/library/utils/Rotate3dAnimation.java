package com.shakej.animation.view.library.utils;

import com.shakej.animation.view.library.controllers.BaseAnimationView;

import android.graphics.Camera;
import android.graphics.Matrix;
import android.view.animation.Animation;
import android.view.animation.Transformation;

public class Rotate3dAnimation extends Animation
{
  private final float mFromDegrees;
  private final float mToDegrees;
  private final float mCenterX;
  private final float mCenterY;
  private final float mDepthZ;
  private Camera mCamera;
  private int direction = BaseAnimationView.PIVOT_RIGHT_TO_LEFT;
  
  
  public Rotate3dAnimation(float fromDegrees, float toDegrees, float centerX, float centerY, float depthZ, int direction)
  {
    mFromDegrees = fromDegrees;
    mToDegrees = toDegrees;
    mCenterX = centerX;
    mCenterY = centerY;
    mDepthZ = depthZ;
    this.direction = direction;
  }
  
  
  @Override
  public void initialize(int width, int height, int parentWidth, int parentHeight)
  {
    super.initialize(width, height, parentWidth, parentHeight);
    mCamera = new Camera();
  }
  
  
  @Override
  protected void applyTransformation(float interpolatedTime, Transformation t)
  {
    final float fromDegrees = mFromDegrees;
    float degrees = fromDegrees + ((mToDegrees - fromDegrees) * interpolatedTime);
    
    final float centerX = mCenterX;
    final float centerY = mCenterY;
    final Camera camera = mCamera;
    
    final Matrix matrix = t.getMatrix();
    
    camera.save();
    camera.translate(0.0f, 0.0f, mDepthZ * (1.0f - interpolatedTime));
    
    if (direction == BaseAnimationView.PIVOT_RIGHT_TO_LEFT)
      camera.rotateY(degrees);
    else
      camera.rotateX(degrees);
    
    camera.getMatrix(matrix);
    camera.restore();
    
    if (direction == BaseAnimationView.PIVOT_RIGHT_TO_LEFT)
    {
      matrix.preTranslate(-centerX, -centerY);
      matrix.postTranslate(centerX, centerY);
    }
    else
    {
      matrix.preTranslate(-centerX, 0);
      matrix.postTranslate(centerX, 0);
    }
  }
}