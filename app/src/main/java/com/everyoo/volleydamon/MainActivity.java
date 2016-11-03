package com.everyoo.volleydamon;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.JsonRequest;
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.everyoo.volleydamon.modle.Weather;
import com.everyoo.volleydamon.modle.WeatherInfo;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private Context mContext;
    private RequestQueue mQueue;
    private StringRequest stringRequest;
    private JsonObjectRequest jsonObjectRequest;
    private JsonArrayRequest jsonArrayRequest;
    private GsonRequest<Weather> gsonRequest;
    private ImageRequest imageRequest;
    private ImageLoader imageLoader;
    private NetworkImageView networkImageView;


    private ImageView imageView;
    private TextView textView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textView = (TextView) findViewById(R.id.textView);
        imageView = (ImageView) findViewById(R.id.image);
        mContext = this;
        mQueue = Volley.newRequestQueue(mContext);

        /**
         * 网络数据请求的用法
         */
        /*stringRequestMethod();   //请求的网络数据是一条普通的文本数据或一个String 格式的数据，这个至少暂时我不常用
        mQueue.add(stringRequest);*/

        /*JsonObjectRequestMethod();  //请求的网络数据是一个json格式的数据，这个比较常用
        mQueue.add(jsonObjectRequest);*/


        /*JsonArrayRequestMethod();  //请求的网络数据是一个json数组格式的数据，对这个用法还不是很理解
        mQueue.add(jsonArrayRequest);*/


        /**
         * 图片加载的用法：
         * 三种方法都是请求加载网络图片，只不过后两个方法有图片的缓存机制
         *
         * 总结：第三种方法，相对会较好一些
         */
        /*ImageRequestMethod();
        mQueue.add(imageRequest);*/


        //不过ImageLoader明显要比ImageRequest更加高效，因为它不仅可以帮我们对图片进行缓存，还可以过滤掉重复的链接，避免重复发送请求。
        // ImageLoaderMethod();


       /* networkImageView = (NetworkImageView) findViewById(R.id.image_volley);
        NetworkImageViewMethod();*/


        /**
         * 定制自己的Request
         *
         *
         *
         *
         */

        GsonRequestMethod();
        mQueue.add(gsonRequest);

    }


    private void stringRequestMethod() {
        stringRequest = new StringRequest(Request.Method.GET, "http://192.168.22.214/zhbj/categories.json",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        Log.i(TAG, "请求成功");
                        Log.i(TAG, "onResponse: s=" + s);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        Log.i(TAG, "请求失败");
                        Log.i(TAG, "onErrorResponse: volleyError=" + volleyError);
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<String, String>();
                map.put("params1", "value1");
                return map;
            }
        };
    }


    private void JsonObjectRequestMethod() {

        jsonObjectRequest = new JsonObjectRequest("http://192.168.22.214/zhbj/categories.json", null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject jsonObject) {
                        Log.i(TAG, "请求成功");
                        Log.i(TAG, "onResponse: jsonObject=" + jsonObject.toString());
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        Log.i(TAG, "请求失败");
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                return super.getParams();
            }
        };


    }


    private void JsonArrayRequestMethod() {

        jsonArrayRequest = new JsonArrayRequest("",
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray jsonArray) {

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {

                    }
                }

        );


    }

    private void ImageRequestMethod() {

        imageRequest = new ImageRequest("http://192.168.22.214/myTest/tupian/46.jpg",
                new Response.Listener<Bitmap>() {
                    @Override
                    public void onResponse(Bitmap bitmap) {
                        Log.i(TAG, "onResponse: sucessful!");
                        imageView.setImageBitmap(bitmap);
                    }
                }, 0, 0, ImageView.ScaleType.FIT_XY, Bitmap.Config.RGB_565,
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        imageView.setImageResource(R.mipmap.ic_launcher);
                    }
                }
        );


    }

    private void ImageLoaderMethod() {
        ImageLoader.ImageListener imageListener = ImageLoader.getImageListener(imageView, R.mipmap.ic_launcher, R.mipmap.ic_launcher);
        imageLoader = new ImageLoader(mQueue, new BitmapCache());
        imageLoader.get("http://192.168.22.214/myTest/tupian/4L.jpg", imageListener);
    }

    private void NetworkImageViewMethod() {
        imageLoader = new ImageLoader(mQueue, new BitmapCache());

        networkImageView.setDefaultImageResId(R.mipmap.ic_launcher);
        networkImageView.setErrorImageResId(R.mipmap.ic_launcher);
        networkImageView.setImageUrl("http://192.168.22.214/myTest/tupian/4L.jpg", imageLoader);

    }


    private void GsonRequestMethod() {
        gsonRequest = new GsonRequest<Weather>(
                Request.Method.GET, "http://192.168.22.214/myTest/tianqi.json", Weather.class,
                new Response.Listener<Weather>() {
                    @Override
                    public void onResponse(Weather weather) {
                        Log.i(TAG, "onResponse: sueccessful!");

                        WeatherInfo weatherInfo = weather.getWeatherinfo();
                        Log.i(TAG, "city is " + weatherInfo.getCity());
                        Log.i(TAG, "temp is " + weatherInfo.getTemp());
                        Log.i(TAG, "time is " + weatherInfo.getTime());
                        textView.setText(weatherInfo.getCity());

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        Log.i(TAG, "onErrorResponse: ");
                    }
                }
        );


    }

}
