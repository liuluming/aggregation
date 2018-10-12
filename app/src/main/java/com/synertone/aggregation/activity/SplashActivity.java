package com.synertone.aggregation.activity;


import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.synertone.aggregation.BaseActivity;
import com.synertone.aggregation.R;
import com.synertone.aggregation.bean.AccountModel;
import com.synertone.aggregation.utils.AppData;
import com.synertone.aggregation.utils.SharedPreferenceManager;
import com.synertone.aggregation.utils.XTHttpUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;

public class SplashActivity extends BaseActivity {
    private RequestQueue mRequestQueue;

    public static Bitmap readBitMap(Context context, int resId) {
        BitmapFactory.Options opt = new BitmapFactory.Options();
        opt.inPreferredConfig = Bitmap.Config.ARGB_8888;
        opt.inPurgeable = true;
        opt.inInputShareable = true;
        opt.inJustDecodeBounds = false;
        InputStream is = context.getResources().openRawResource(resId);
        return BitmapFactory.decodeStream(is, null, opt);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_activity);
        mRequestQueue = Volley.newRequestQueue(getApplicationContext());
        screenOritention();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                // TODO Auto-generated method stub
                   String mToken = SharedPreferenceManager.getString(mContext, "mToken");
                    if (mToken == null) {
                        goToLoginActivity();
                    } else {
                        queryStatus(mToken);
                    }

            }
        }, 500);

    }
    private void goToLoginActivity() {
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(mContext, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        }, 2000);

    }
    private void queryStatus(final String mToken) {
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
                                    goToHomeActivity(mToken);
                                } else if ("-1".equals(code)) {
                                    goToLoginActivity();
                                } else {
                                    goToLoginActivity();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    goToLoginActivity();
                }
            });
            request.setRetryPolicy(new DefaultRetryPolicy(10 * 1000, 0, 0f));
            mRequestQueue.add(request);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void goToHomeActivity(final String mToken) {
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                AccountModel accountModel = new AccountModel();
                accountModel.setSessionToken(mToken);
                accountModel.setUser(SharedPreferenceManager.getString(mContext, "user"));
                accountModel.setPasswd(SharedPreferenceManager.getString(mContext, "passwd"));
                AppData.accountModel = accountModel;
                Intent intent = new Intent(mContext, MainActivity.class);
                startActivity(intent);
                finish();
            }
        }, 1500);


    }
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        // TODO Auto-generated method stub
        super.onConfigurationChanged(newConfig);
    }

    private void screenOritention() {
        if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            Log.i("info", "landscape");
            ImageView splash_img = (ImageView) findViewById(R.id.spalsh_img);
            splash_img.setScaleType(ScaleType.FIT_XY);
            Bitmap bgBitmap = readBitMap(this, R.drawable.luancher_land);
            Drawable drawable = new BitmapDrawable(bgBitmap);
            splash_img.setBackgroundDrawable(drawable);
        } else if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            Log.i("info", "portrait");
            ImageView splash_img = (ImageView) findViewById(R.id.spalsh_img);
            splash_img.setScaleType(ScaleType.FIT_XY);
            Bitmap bgBitmap = readBitMap(this, R.drawable.qidong);
            Drawable drawable = new BitmapDrawable(bgBitmap);
            splash_img.setBackgroundDrawable(drawable);

        }

    }


}
