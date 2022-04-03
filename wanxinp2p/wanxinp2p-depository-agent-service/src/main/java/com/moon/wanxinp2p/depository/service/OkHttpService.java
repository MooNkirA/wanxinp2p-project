package com.moon.wanxinp2p.depository.service;

import com.moon.wanxinp2p.depository.interceptor.SignatureInterceptor;
import lombok.extern.log4j.Log4j2;
import lombok.extern.slf4j.Slf4j;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;

/**
 * okHttp3请求工具类
 */
@Log4j2
@Service
public class OkHttpService {

    @Autowired
    private SignatureInterceptor signatureInterceptor;

    /**
     * okHttp 同步 GET 请求(加入请求拦截，用于数据签名及验签)
     *
     * @param url
     * @return
     */
    public String doSyncGet(String url) {
        OkHttpClient okHttpClient = new OkHttpClient().newBuilder().addInterceptor(signatureInterceptor).build();
        Request request = new Request.Builder().url(url).build();
        try (Response response = okHttpClient.newCall(request).execute()) {
            return response.body().string();
        } catch (IOException e) {
            log.warn("请求出现异常: ", e);
        }
        return "";
    }

    /**
     * okHttp 同步 GET 请求(无拦截)
     *
     * @param url
     * @return
     */
    public String doGet(String url) {
        String responseBody = "";
        OkHttpClient okHttpClient = new OkHttpClient().newBuilder().build();
        Request request = new Request.Builder().url(url).build();

        try (Response response = okHttpClient.newCall(request).execute()) {
            if (response.isSuccessful() && response.body() != null) {
                responseBody = response.body().string();
            }
        } catch (IOException e) {
            log.warn("请求出现异常: ", e);
        }

        return responseBody;
    }

}
