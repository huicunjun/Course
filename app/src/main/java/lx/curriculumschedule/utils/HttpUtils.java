package lx.curriculumschedule.utils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class HttpUtils {
    //private static OkHttpClient okHttpClient = new OkHttpClient.Builder().cookieJar(new MyCookiesJar()).build();
    private static OkHttpClient okHttpClient;
    public static OkHttpClient getOkHttpClient() {
        if (okHttpClient==null){
            synchronized (HttpUtils.class){
                if (okHttpClient!=null){
                    return okHttpClient;
                }else {
                    okHttpClient = new OkHttpClient.Builder().cookieJar(new MyCookiesJar()).build();
                    return  okHttpClient;
                }
            }
        }else {
            return okHttpClient;
        }

    }

    public static  String http_get(String url) throws IOException {
        Request request = new Request.Builder().url(url).build();
        Response response = okHttpClient.newCall(request).execute();
        return  response.body().string();
    }

    public static  String http_post(String url) throws IOException {
        Request request = new Request.Builder().url(url).build();
        Response response = okHttpClient.newCall(request).execute();
        return  response.body().string();
    }

    public static class MyCookiesJar implements CookieJar {

        private static  Map<String, List<Cookie>> cookiestores = new HashMap<>();

        public static void setCookiestores(Map<String, List<Cookie>> cookiestores) {
            MyCookiesJar.cookiestores = cookiestores;
        }
        public static void addCookie(String host,Cookie cookie) {
            List<Cookie> cookieList = new ArrayList<>();
            cookieList.add(cookie);
            cookiestores.put(host,cookieList);
        }
        public static Map<String, List<Cookie>> getCookiestores() {
            return cookiestores;
        }

        @Override
        public void saveFromResponse(HttpUrl url, List<Cookie> cookies) {
            cookiestores.put(url.host(), cookies);
        }

        @Override
        public List<Cookie> loadForRequest(HttpUrl url) {
            List<Cookie> cookiesList = cookiestores.get(url.host());
            //注：这里不能返回null，否则会报NULLException的错误。
            //原因：当Request 连接到网络的时候，OkHttp会调用loadForRequest()
            return cookiesList != null ? cookiesList : new ArrayList<Cookie>();

        }
    }
}
