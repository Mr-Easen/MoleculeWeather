package com.example.jsontest;

import java.util.List;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class MyAdapter extends BaseAdapter
{

	private List<MessageBean> mlist;
	private LayoutInflater inflater;
	public MyAdapter(Context context,List<MessageBean> list)
	{
		mlist = list;
		inflater = LayoutInflater.from(context);
	}
	@Override
	public int getCount()
	{
		// TODO Auto-generated method stub
		return mlist.size() - 1;
	}

	@Override
	public Object getItem(int position)
	{
		// TODO Auto-generated method stub
		return mlist.get(position + 1);
	}

	@Override
	public long getItemId(int position)
	{
		// TODO Auto-generated method stub
		return position + 1;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent)
	{
		// TODO Auto-generated method stub
		ViewHolder viewholder = null;
		if(convertView == null)
		{
			viewholder = new ViewHolder();
			convertView = inflater.inflate(R.layout.buttom, null);
			viewholder.f_date = (TextView) convertView.findViewById(R.id.f_date);
			viewholder.f_weather = (TextView) convertView.findViewById(R.id.f_weather);
			viewholder.f_temp = (TextView) convertView.findViewById(R.id.f_temp);
			convertView.setTag(viewholder);
		}
		else
		{
			viewholder = (ViewHolder) convertView.getTag();
		}
		viewholder.f_date.setText(mlist.get(position + 1).date.substring(5, 10));
		if(mlist.get(position + 1).txt_d.equals(mlist.get(position + 1).txt_n))
		{
			viewholder.f_weather.setText(mlist.get(position + 1).txt_d);
		}
		else
			viewholder.f_weather.setText(mlist.get(position + 1).txt_d + "转" + mlist.get(position + 1).txt_n);
		viewholder.f_temp.setText(mlist.get(position + 1).min + "~" + mlist.get(position + 1).max + "℃");
		return convertView;
	}
    
	
	class ViewHolder
	{
		//public TextView city,tem,weather;
		public TextView f_date,f_weather,f_temp;
	}
}
