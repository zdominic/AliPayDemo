package com.example.dominic.alipaydemo;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import com.alipay.sdk.app.PayTask;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

public class MainActivity extends AppCompatActivity implements Response.Listener<String>, Response.ErrorListener {
    
    //支付宝支付协议接口
    private  String url = "https://buy.tmall.com/auction/confirm_order.htm?x-itemid=533040575438&x-uid=671771801&spm=a220l.1.a22016.d011001001001.RxX726&submitref=0a67f6";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


    }

    public void aliPay(View view){
        //1.发起网络请求,提交参数
        StringRequest request = new StringRequest(url,this,this);
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(request);

    }

    @Override
    public void onResponse(String s) {
        //2.解析服务器返回的json
        Gson gson = new Gson();
        final AliPayInfo aliPayInfo = gson.fromJson(s, AliPayInfo.class);

        //3.调用第三方支付SDK的支付方法，传入“支付串码”
        new Thread() {
            public void run() {
                PayTask payTask = new PayTask(MainActivity.this);
                //同步返回
                String pay = payTask.pay(aliPayInfo.getPayInfo(), true);//true显示加载进度
                Message message = mHandler.obtainMessage();
                message.obj = pay;



            }
        }.start();

    }

    //4.处理支付结果
    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            String result = (String) msg.obj;

            PayResult payResult = new PayResult(result);
            /**
             * 同步返回的结果必须放置到服务端进行验证（验证的规则请看https://doc.open.alipay.com/doc2/
             * detail.htm?spm=0.0.0.0.xdvAU6&treeId=59&articleId=103665&
             * docType=1) 建议商户依赖异步通知
             */
            String resultInfo = payResult.getResult();// 同步返回需要验证的信息

            String resultStatus = payResult.getResultStatus();
            // 判断resultStatus 为“9000”则代表支付成功，具体状态码代表含义可参考接口文档
            if (TextUtils.equals(resultStatus, "9000")) {
                Toast.makeText(MainActivity.this, "支付成功", Toast.LENGTH_SHORT).show();
            } else {
                // 判断resultStatus 为非"9000"则代表可能支付失败
                // "8000"代表支付结果因为支付渠道原因或者系统原因还在等待支付结果确认，最终交易是否成功以服务端异步通知为准（小概率状态）
                if (TextUtils.equals(resultStatus, "8000")) {
                    Toast.makeText(MainActivity.this, "支付结果确认中", Toast.LENGTH_SHORT).show();

                } else {
                    // 其他值就可以判断为支付失败，包括用户主动取消支付，或者系统返回的错误
                    Toast.makeText(MainActivity.this, "支付失败", Toast.LENGTH_SHORT).show();

                }
            }
        }
    };

    @Override
    public void onErrorResponse(VolleyError volleyError) {

    }
}
