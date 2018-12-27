package lx.curriculumschedule.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lx.curriculumschedule.ui.CourseActivity;
import lx.curriculumschedule.ui.LoginActivity;
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

    public static void saveCookies(Context context) {
            Map<String, List<Cookie>> cookiestores = HttpUtils.MyCookiesJar.getCookiestores();
            List<Cookie> cookies = cookiestores.get("61.142.209.19");
            SPUtils spCookie = new SPUtils(context, "cookie");
            if (cookies.size()>=1){
                for (int i = 0; i < cookies.size(); i++) {
                    Cookie cookie = cookies.get(i);

                    String name = cookie.name();
                    String domain = cookie.domain();
                    String path = cookie.path();
                    String value = cookie.value();
                    long expiresAt = cookie.expiresAt();
                    spCookie.putString("name",name);
                    spCookie.putString("domain",domain);
                    spCookie.putString("path",path);
                    spCookie.putString("value",value);
                    spCookie.putLong("expiresAt",expiresAt);

                    //spUtils.putString();
                    LogUtils.Logi(cookie.toString());
                    // Toast.makeText(context, cookie.toString(), Toast.LENGTH_SHORT).show();
                }
            }

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

    public static  void localCookie(Activity activity) {
        SPUtils spCookie = new SPUtils(activity,"cookie");
        SPUtils spUtils = new SPUtils(activity,"info");

        if (spUtils.getBoolean("登录",false)){
            String name = spCookie.getString("name");
            String domain = spCookie.getString("domain");
            String path = spCookie.getString("path");
            String value = spCookie.getString("value");
            long expiresAt = spCookie.getLong("expiresAt", 0);

            if (name.length()>0&&domain.length()>0&&value.length()>0){
                Cookie cookie = new Cookie.Builder()
                        .name(name)
                        .domain(domain)
                        .expiresAt(expiresAt)
                        .path(path)
                        .value(value)
                        .build();
                HttpUtils.MyCookiesJar.addCookie(domain,cookie);
                Intent intent = new Intent(activity,CourseActivity.class);
                activity.startActivity(intent);
                activity.finish();
            }else {
                Toast.makeText(activity, "登录失效了", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(activity,LoginActivity.class);
                activity.startActivity(intent);
                activity.finish();
            }

        }else {
            Intent intent = new Intent(activity,LoginActivity.class);
            activity.startActivity(intent);
            activity.finish();
        }
    }
}
