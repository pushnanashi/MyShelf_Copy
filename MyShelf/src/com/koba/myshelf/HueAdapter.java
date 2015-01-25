package com.koba.myshelf;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;



public class HueAdapter extends BaseAdapter {

  private Context mContext;
  private Bitmap    bitmapper[] = null;
  private String[] bbb = null; 
  private LayoutInflater mLayoutInflater;
  private String[]  header = null; 
  

  private static class ViewHolder {
	public TextView  TextView2;
	public ImageView hueImageView;
    public TextView  hueTextView;
 
  }

  public HueAdapter(Context context,String[] aaa,String[] head,Bitmap[] bitmap) {
    mContext = context;
    mLayoutInflater = LayoutInflater.from(context);
    bbb = aaa;
    bitmapper = bitmap;
    header = head;
  }

  public int getCount() {
    return bbb.length;
  }

  public Object getItem(int position) {
    return bbb[position];
  }

  public long getItemId(int position) {
    return position;
  }

  public View getView(int position, View convertView, ViewGroup parent) {

    ViewHolder holder;
    if (convertView == null) {
      convertView = mLayoutInflater.inflate(R.layout.aaaaaaa, null);
      holder = new ViewHolder();
     
      holder.hueImageView = (ImageView)convertView.findViewById(R.id.hue_imageview);
      holder.hueTextView = (TextView)convertView.findViewById(R.id.hue_textview);

      convertView.setTag(holder);
    } else {
      holder = (ViewHolder)convertView.getTag();
    }
    holder.TextView2.setText(header[position]);
    holder.hueImageView.setImageBitmap(bitmapper[position]);
    holder.hueTextView.setText(bbb[position]);
    
    return convertView;
  }
  
  
}