package com.synertone.aggregation;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.lidroid.xutils.HttpUtils;
import com.synertone.aggregation.utils.AppData;
import com.synertone.aggregation.utils.SharedPreferenceManager;
import com.synertone.aggregation.utils.XTHttpUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.Unbinder;

public class BaseActivity extends FragmentActivity {
    protected Context mContext;
    protected HttpUtils http;
    private android.support.v4.app.FragmentManager supportFragmentManager;
    private boolean isVisible;
    private ProgressDialog pd;
    private static ArrayList<String> lastWarnList;
    protected AppData application;
    private Unbinder unbinder;
    private RequestQueue mRequestQueue;
    public boolean isVisible() {
        return isVisible;
    }

    @Override
    protected void onCreate(Bundle arg0) {
        // TODO Auto-generated method stub
        LayoutInflater.from(this).setFactory2(new LayoutInflater.Factory2() {
            @Override
            public View onCreateView(View parent, String name, Context context, AttributeSet attrs) {
                if("EditText".equals(name)){
                    EditText editText=new EditText(context,attrs);
                    editText.setImeOptions(EditorInfo.IME_FLAG_NO_EXTRACT_UI);
                    return  editText;
                }
                return null;
            }

            @Override
            public View onCreateView(String name, Context context, AttributeSet attrs) {
                if("EditText".equals(name)){
                    EditText editText=new EditText(context,attrs);
                    editText.setImeOptions(EditorInfo.IME_FLAG_NO_EXTRACT_UI);
                    return  editText;
                }
                return null;
            }
        });
        super.onCreate(arg0);
        mContext = this;
        application = (AppData) getApplication();
        mRequestQueue = Volley.newRequestQueue(getApplicationContext());
        application.addActivity(new WeakReference<Activity>(this));
        http = new HttpUtils();
        http.configTimeout(30 * 1000);
        http.configSoTimeout(30 * 1000);
        http.configCurrentHttpCacheExpiry(500);
        supportFragmentManager = getSupportFragmentManager();
        pd = new ProgressDialog(mContext);
        pd.setCanceledOnTouchOutside(false);
        isVisible = true;
    }
    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);
         unbinder = ButterKnife.bind(this);
    }
    @Override
    public void setContentView(View view) {
        super.setContentView(view);
        unbinder = ButterKnife.bind(this);
    }

    @Override
    public void setContentView(View view, ViewGroup.LayoutParams params) {
        super.setContentView(view, params);
        unbinder =ButterKnife.bind(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(unbinder!=null){
            unbinder.unbind();
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }




    public void querySessionStatus(final OnSessionStatusListener onSessionStatusListener) {
        final String mToken = SharedPreferenceManager.getString(mContext, "mToken");
        String queryStatusUrl = XTHttpUtil.QUERY_STATUS;
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("sessionToken", mToken);
            JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST,
                    queryStatusUrl, jsonObject,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                JSONObject jsonObject = new JSONObject(response.toString());
                                String code = jsonObject.getString("code");
                                if ("0".equals(code)) {
                                    //Session未失效
                                    onSessionStatusListener.sessionSuccess();
                                } else if ("-1".equals(code)) {
                                    //Session失效
                                   //showLoginDialog();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    error.printStackTrace();
                    onSessionStatusListener.sessionErrorResponse();
                }
            });
            request.setRetryPolicy(new DefaultRetryPolicy(10 * 1000, 0, 0f));
            mRequestQueue.add(request);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public abstract class OnSessionStatusListener{
        public abstract void sessionSuccess();
        public void sessionErrorResponse(){

        }
    }
    protected void showDia(String... msg) {
        if (msg != null && msg.length > 0) {
            pd.setMessage("\"" + msg[0] + "....." + "\"");
        } else {
            pd.setMessage("正在加载数据.....");
        }
        if(isVisible){
            pd.show();
        }

    }

    protected void dismissDia() {
        if (pd != null&&isVisible) {
            pd.dismiss();
        }
    }
}

