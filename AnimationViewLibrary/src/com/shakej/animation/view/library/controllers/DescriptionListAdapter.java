package com.shakej.animation.view.library.controllers;

import java.util.ArrayList;

import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.shakej.animation.view.library.R;
import com.shakej.animation.view.library.beans.DescriptionBean;
import com.shakej.animation.view.library.utils.ImageDownloader;
import com.shakej.animation.view.library.utils.SystemManager;

public class DescriptionListAdapter extends BaseAdapter
{
  private ArrayList<DescriptionBean> beans;
  private Context context;
  
  
  public DescriptionListAdapter(Context context, ArrayList<DescriptionBean> beans)
  {
    this.context = context;
    this.beans = beans;
  }
  
  
  @Override
  public int getCount()
  {
    return beans.size();
  }
  
  
  @Override
  public Object getItem(int position)
  {
    return beans.get(position);
  }
  
  
  @Override
  public long getItemId(int position)
  {
    return position;
  }
  
  
  @SuppressLint({ "ViewHolder", "InflateParams" })
  @Override
  public View getView(int position, View convertView, ViewGroup parent)
  {
    DescriptionBean bean = (DescriptionBean) getItem(position);
    final View replyView = LayoutInflater.from(context).inflate(R.layout.view_description_list_item, null);
    
    ImageView profileImage = (ImageView) replyView.findViewById(R.id.image_profile);
    TextView nickName = (TextView) replyView.findViewById(R.id.content_nickname);
    TextView name = (TextView) replyView.findViewById(R.id.content_name);
    TextView time = (TextView) replyView.findViewById(R.id.content_time);
    TextView desctiption = (TextView) replyView.findViewById(R.id.content_description);
    
    Bitmap bm = ImageDownloader.getImageBitmap(context, bean.profileImageUrl);
    nickName.setText(bean.title);
    name.setVisibility(View.GONE);
    time.setVisibility(View.GONE);
    nickName.setText(bean.subtitle);
    time.setText(bean.subDescription);
    desctiption.setText(bean.description);
    profileImage.setImageBitmap(bm);
    
    if (position != 0)
      ((LinearLayout) replyView.findViewById(R.id.empty_container)).setVisibility(View.VISIBLE);
    
    if (position == 0)
      ObjectAnimator.ofFloat(replyView, "alpha", 0, 1).setDuration(500).start();
    else
    {
      replyView.setVisibility(View.GONE);
      final ObjectAnimator ani = ObjectAnimator.ofFloat(replyView, "translationX", SystemManager.getInstance(context).screenSize().x * 2, 0).setDuration(500);
      Handler animationHandler = new Handler();
      Runnable aniRunn = new Runnable()
      {
        @Override
        public void run()
        {
          //왼쪽 편에 마진을 주기 위해 빈 레이아웃을 넣음 
          //리스트뷰 안의 Row는 setMargins를 제공하지 않음 
          ((LinearLayout) replyView.findViewById(R.id.empty_container)).setVisibility(View.VISIBLE);
          replyView.setVisibility(View.VISIBLE);
          ani.start();
        }
      };
      
      animationHandler.postDelayed(aniRunn, position * 100);
    }
    
    return replyView;
  }
}
