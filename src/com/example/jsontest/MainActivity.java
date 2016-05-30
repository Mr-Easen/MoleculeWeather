package com.example.jsontest;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Looper;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.LocationClientOption.LocationMode;

public class MainActivity extends Activity
{

	private GridView gridView;
	private EditText edittext;
	private TextView city, temp, weather, air;
	private static String url = "https://api.heweather.com/x3/weather?cityid=CN101280102&key=65326eeb6eba49ccbf8abaadc3119e8b";
	private String string = "https://api.heweather.com/x3/citylist?search=allchina&key=65326eeb6eba49ccbf8abaadc3119e8b";
	private String location_city = "";
	public LocationClient mLocationClient = null;
	public MyLocationListenner myListener = new MyLocationListenner();
	boolean flag = true;
	boolean location = true;
	boolean isFirstLoc = true;
	String city_id = null;
	private ProgressBar mProgressBar;
	private FrameLayout mRootFrameLayout;
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		mProgressBar = new ProgressBar(this);
		gridView = (GridView) findViewById(R.id.grid);
		edittext = (EditText) findViewById(R.id.edittext);
		city = (TextView) findViewById(R.id.city);
		temp = (TextView) findViewById(R.id.tem);
		weather = (TextView) findViewById(R.id.weather);
		// air = (TextView) findViewById(R.id.air);
		LBS();
		showProgressBar();
		// new MyAsyncTask().execute(url);
	}

	public void LBS()
	{

		mLocationClient = new LocationClient(getApplicationContext());
		mLocationClient.registerLocationListener(myListener);
		// mLocationClient.setAccessKey("8LurGy8Gt38hBVsG80ofhleT2Ffflhop");
		setLocationOption();
		mLocationClient.start();
	}

	public class MyLocationListenner implements BDLocationListener
	{
		@Override
		public void onReceiveLocation(BDLocation arg0)
		{
			// TODO Auto-generated method stub
			location_city = arg0.getCity().substring(0,
					arg0.getCity().length() - 1);
			if (isFirstLoc)
			{
				Toast.makeText(MainActivity.this,
						"您现在正在" + arg0.getDistrict() + arg0.getStreet(), 1)
						.show();
				isFirstLoc = false;
				try
				{
					setUI(string);
				} catch (MalformedURLException e)
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e)
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

		}
	}

	private void setLocationOption()
	{
		// TODO Auto-generated method stub
		LocationClientOption option = new LocationClientOption();
		option.setLocationMode(LocationMode.Hight_Accuracy);
		// 可选，默认高精度，设置定位模式，高精度，低功耗，仅设备
		option.setCoorType("bd09ll");
		// 可选，默认gcj02，设置返回的定位结果坐标系
		int span = 1000;
		option.setScanSpan(span);
		// 可选，默认0，即仅定位一次，设置发起定位请求的间隔需要大于等于1000ms才是有效的
		option.setIsNeedAddress(true);
		// 可选，设置是否需要地址信息，默认不需要
		option.setOpenGps(true);
		// 可选，默认false,设置是否使用gps
		option.setLocationNotify(true);
		// 可选，默认false，设置是否当gps有效时按照1S1次频率输出GPS结果
		option.setIsNeedLocationDescribe(true);
		// 可选，默认false，设置是否需要位置语义化结果，可以在BDLocation.getLocationDescribe里得到，结果类似于“在北京天安门附近”
		option.setIsNeedLocationPoiList(true);
		// 可选，默认false，设置是否需要POI结果，可以在BDLocation.getPoiList里得到
		option.setIgnoreKillProcess(false);
		// 可选，默认true，定位SDK内部是一个SERVICE，并放到了独立进程，设置是否在stop的时候杀死这个进程，默认不杀死
		option.SetIgnoreCacheException(false);
		// 可选，默认false，设置是否收集CRASH信息，默认收集
		option.setEnableSimulateGps(false);
		// 可选，默认false，设置是否需要过滤gps仿真结果，默认需要
		mLocationClient.setLocOption(option);
	}

	public void search(View v) throws MalformedURLException, IOException
	{
		showProgressBar();
		if (edittext.getText().equals(""))
			return;
		else
		{
			setUI(string);
			if (flag)
			{
				Toast.makeText(getApplicationContext(), "请准确输入城市名称", 0).show();
				dismissProgressBar();
			}
			
		}

		InputMethodManager inputMethodManager =(InputMethodManager)this.getApplicationContext().
				getSystemService(Context.INPUT_METHOD_SERVICE); 
			
	inputMethodManager.hideSoftInputFromWindow(edittext.getWindowToken(), 0); //隐藏
	}

	private void setUI(final String string) throws MalformedURLException,
			IOException
	{
		// TODO Auto-generated method stub
		new Thread(new Runnable()
		{

			@Override
			public void run()
			{
				InputStream is = null;
				try
				{
					is = new URL(string).openStream();
				} catch (MalformedURLException e1)
				{
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (IOException e1)
				{
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}

				InputStreamReader isr;
				String result = "";
				String line = "";
				try
				{
					isr = new InputStreamReader(is, "utf-8");
					BufferedReader br = new BufferedReader(isr);
					while ((line = br.readLine()) != null)
					{
						result += line;
					}
				} catch (UnsupportedEncodingException e)
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e)
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				//System.out.println(result);
				try
				{
					JSONObject root = new JSONObject(result);
					JSONArray city_info = root.getJSONArray("city_info");
					String city_name = edittext.getText().toString();
					if (location)
					{
						city_name = location_city;
						location = false;
					}
					

					JSONObject rows;
					for (int i = 0; i < city_info.length(); i++)
					{
						flag = true;
						rows = city_info.getJSONObject(i);
						if (city_name.equals(rows.getString("city")))
						{
							city_id = rows.getString("id");
							flag = false;
							break;
						}
					}
					/*if (flag)
					{
						return;
					}*/

					url = "https://api.heweather.com/x3/weather?cityid="
							+ city_id + "&key=65326eeb6eba49ccbf8abaadc3119e8b";
					new MyAsyncTask().execute(url);
				} catch (JSONException e)
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
		}).start();

	}

	@Override
	protected void onDestroy()
	{
		// TODO Auto-generated method stub
		mLocationClient.stop();
		super.onDestroy();
	}

	class MyAsyncTask extends AsyncTask<String, Integer, List<MessageBean>>
	{

		@Override
		protected void onPreExecute()
		{
			// TODO Auto-generated method stub
			super.onPreExecute();
		}
		@Override
		protected List<MessageBean> doInBackground(String... params)
		{
			// TODO Auto-generated method stub
			try
			{
				return getData(params[0]);
			} catch (MalformedURLException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return null;
		}

		@Override
		protected void onProgressUpdate(Integer... values)
		{
			// TODO Auto-generated method stub
			super.onProgressUpdate(values);
		}
		@Override
		protected void onPostExecute(List<MessageBean> result)
		{
			// TODO Auto-generated method stub
			super.onPostExecute(result);

			int size = result.size();
			int length = 100;
			DisplayMetrics dm = new DisplayMetrics();
			getWindowManager().getDefaultDisplay().getMetrics(dm);
			float density = dm.density;
			int gridviewWidth = (int) (size * (length + 8) * density);
			int itemWidth = (int) (length * density * 1.2);

			LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
					gridviewWidth, LinearLayout.LayoutParams.FILL_PARENT);
			gridView.setLayoutParams(params); // 设置GirdView布局参数,横向布局的关键
			gridView.setColumnWidth(itemWidth); // 设置列表项宽
			gridView.setHorizontalSpacing(20); // 设置列表项水平间距
			gridView.setStretchMode(GridView.NO_STRETCH);
			gridView.setNumColumns(size); // 设置列数量=列表集合数
			dismissProgressBar();
			MyAdapter adapter = new MyAdapter(MainActivity.this, result);
			city.setText(result.get(0).city);
			weather.setText(result.get(0).txt);
			temp.setText(result.get(0).tem + "℃");

			gridView.setAdapter(adapter);

			
		}

	}

	private List<MessageBean> getData(String string)
			throws MalformedURLException, IOException
	{
		// TODO Auto-generated method stub
		List<MessageBean> listbean = new ArrayList<MessageBean>();
		// String jsonstring = request(string);
		String jsonstring = readStream(new URL(string).openStream());
		MessageBean mb;
		try
		{
			JSONObject root = new JSONObject(jsonstring);
			JSONArray hefeng = root.getJSONArray("HeWeather data service 3.0");
			JSONObject data = hefeng.getJSONObject(0);
			JSONObject basic = data.getJSONObject("basic");
			JSONObject now = data.getJSONObject("now");
			JSONObject cond = now.getJSONObject("cond");
			JSONArray forecast = data.getJSONArray("daily_forecast");
			JSONObject rows;
			JSONObject fore_cond;
			JSONObject tem;
			mb = new MessageBean();
			mb.city = basic.getString("city");
			mb.tem = now.getString("tmp");
			mb.txt = cond.getString("txt");
			listbean.add(mb);
			for (int i = 0; i < forecast.length(); i++)
			{

				mb = new MessageBean();

				rows = forecast.getJSONObject(i);
				fore_cond = rows.getJSONObject("cond");
				tem = rows.getJSONObject("tmp");
				mb.txt_d = fore_cond.getString("txt_d");
				mb.txt_n = fore_cond.getString("txt_n");
				mb.date = rows.getString("date");
				mb.max = tem.getString("max");
				mb.min = tem.getString("min");
				listbean.add(mb);

			}
		} catch (JSONException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return listbean;
	}

	private String readStream(InputStream is)
	{
		// TODO Auto-generated method stub
		InputStreamReader isr;
		String result = "";
		String line = "";
		try
		{
			isr = new InputStreamReader(is, "utf-8");
			BufferedReader br = new BufferedReader(isr);
			while ((line = br.readLine()) != null)
			{
				result += line;
			}
		} catch (UnsupportedEncodingException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//System.out.println(result);

		return result;
	}

	private String request(String string)
	{
		BufferedReader reader = null;
		String result = null;
		StringBuffer sbf = new StringBuffer();
		try
		{
			URL url = new URL(string);
			HttpsURLConnection connection = (HttpsURLConnection) url
					.openConnection();
			connection.setRequestMethod("GET");
			connection.connect();
			InputStream ism = connection.getInputStream();
			reader = new BufferedReader(new InputStreamReader(ism, "UTF-8"));
			String strRead = null;
			while ((strRead = reader.readLine()) != null)
			{
				sbf.append(strRead);
				sbf.append("\r\n");
			}
			reader.close();
			result = sbf.toString();
		} catch (Exception e)
		{
			e.printStackTrace();
		}
		//Log.i("xys", result);
		return result;

	}

	class Today
	{
		public String city;
		public String tmp;
		public String weather;
	}
	
	private void showProgressBar()
	{
		mRootFrameLayout = (FrameLayout) findViewById(android.R.id.content);
		FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(
				FrameLayout.LayoutParams.WRAP_CONTENT,
				FrameLayout.LayoutParams.WRAP_CONTENT);
		layoutParams.gravity = Gravity.CENTER;
		mProgressBar = new ProgressBar(getApplicationContext());
		mProgressBar.setLayoutParams(layoutParams);
		mProgressBar.setVisibility(View.VISIBLE);
		mRootFrameLayout.addView(mProgressBar);
	}

	/**
	 * 隐藏风火轮
	 */
	private void dismissProgressBar()
	{
		if (null != mProgressBar && null != mRootFrameLayout)
		{
			mRootFrameLayout.removeView(mProgressBar);
		}
	}
}
