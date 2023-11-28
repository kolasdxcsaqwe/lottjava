package com.example.tt.utils;

import com.example.tt.Bean.PostParamBean;
import com.google.gson.Gson;
import okhttp3.*;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class HttpRequest {

    static  HttpRequest httpRequest;
    static OkHttpClient client;

    public static HttpRequest getInstance()
    {

        if(httpRequest==null)
        {
            httpRequest=new HttpRequest();
        }

        if(client==null)
        {
            client=new OkHttpClient();
        }
        return httpRequest;
    }


    public void post(String baseUrl, List<PostParamBean> params) {
        System.err.println(baseUrl+"--->"+new Gson().toJson(params));
        OkHttpClient client = new OkHttpClient.Builder()
                .readTimeout(20, TimeUnit.SECONDS)
                .connectTimeout(10, TimeUnit.SECONDS)
                .build();

        FormBody.Builder builder=  new FormBody.Builder();
        for (int i = 0; i < params.size(); i++) {
            PostParamBean postParamBean= params.get(i);
            builder.add(postParamBean.getKey(),postParamBean.getValue());
        }
        RequestBody requestBody=builder.build();

        Request request=new Request.Builder().addHeader("content-type","application/json")
                .url(baseUrl).post(requestBody).build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                System.err.println(baseUrl+"--->"+e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                System.err.println(baseUrl+"--->"+response.body().string());
            }
        });
    }

}