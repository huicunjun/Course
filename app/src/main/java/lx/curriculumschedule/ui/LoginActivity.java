package lx.curriculumschedule.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatEditText;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import lx.curriculumschedule.utils.HttpUtils;
import lx.curriculumschedule.utils.LogUtils;
import lx.curriculumschedule.R;
import lx.curriculumschedule.utils.MessageUtils;
import lx.curriculumschedule.utils.NetUtils;
import lx.curriculumschedule.utils.SPUtils;
import lx.curriculumschedule.utils.ToastUtils;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Cookie;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private OkHttpClient okHttpClient;
    /**
     * 用户名:
     */
    private AppCompatEditText mUsername;
    /**
     * 密码:
     */
    private AppCompatEditText mPassword;
    /**
     * 登陆
     */
    private AppCompatButton mLogin;
    /**
     * 使用协议
     */
    private TextView mXieyi;
    /**
     * 新用户注册
     */
    private TextView mZhuce;
    private String Tag = getClass().getSimpleName();
    private ImageView mImgYzm;
    private Context context = LoginActivity.this;
    /**
     * 验证码:
     */
    private AppCompatEditText mEdYzm;
   // private String cookie;
    private String VIEWSTATE;
   // private HashMap<String,List<Cookie>> cookieStore = new HashMap<>();
    private SPUtils spUtils;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initView();
        spUtils = new SPUtils(context,"info");

        okHttpClient = HttpUtils.getOkHttpClient();
        getVIEWSTATE();

       //刷新验证码
        mImgYzm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                verificationCode();
            }
        });

        //loadCookies();

    }

    private void saveCookies() {
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
    @Override
    protected void onDestroy() {
        Map<String, List<Cookie>> cookiestores = HttpUtils.MyCookiesJar.getCookiestores();
        super.onDestroy();
    }

    private void initView() {
        mUsername = (AppCompatEditText) findViewById(R.id.username);
        mPassword = (AppCompatEditText) findViewById(R.id.password);
        mLogin = (AppCompatButton) findViewById(R.id.login);
        mLogin.setOnClickListener(this);
        mXieyi = (TextView) findViewById(R.id.xieyi);
        mZhuce = (TextView) findViewById(R.id.zhuce);
        mImgYzm = (ImageView) findViewById(R.id.img_yzm);
        mEdYzm = (AppCompatEditText) findViewById(R.id.ed_yzm);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            default:
                break;
            case R.id.login:
                if (NetUtils.isConnectIsNomarl(context)){
                    Login();
                }else {
                    new ToastUtils(getWindow().getDecorView()).showSnackbar("当前网络已断开!\n");
                }
                Login();
                break;
        }
    }

    /**
     * 请求VIEWSTATE
     */
    private void getVIEWSTATE() {
     new Thread(new Runnable() {
         @Override
         public void run() {
             String url = "http://61.142.209.19:81/default2.aspx";
             final Request request = new Request.Builder().get().url(url).build();
             try {
                 Response response = okHttpClient.newCall(request).execute();
                 String string = response.body().string();
                 Document document = Jsoup.parse(string);
                 VIEWSTATE  = document.getElementById("__VIEWSTATE").val();
                 verificationCode();
                 Logs("__VIEWSTATE"+response.headers().toString()+"\n"+VIEWSTATE);
             } catch (IOException e) {
                 e.printStackTrace();
             }

         }
     }).start();
    }
    /**
     * 请求验证码
     */
    private void verificationCode() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                String url = "http://61.142.209.19:81/checkcode.aspx";
                final Request request = new Request.Builder()
                        .get().url(url)
                        // .header("Set-Cookie",cookie)
                        .build();
                try {
                    Response response = okHttpClient.newCall(request).execute();
                    final byte[] bytes = response.body().bytes();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            //  Log.i(Tag,cookie);
                            Glide.with(context).load(bytes).into(mImgYzm);
                        }
                    });
                    Logs("验证码"+response.headers().toString());
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }).start();

    }

    private void Login() {

        //__VIEWSTATE	/wEPDwUKMTE3MDU2MjIzN2Rkiy1hzWgYwfRxijjPTveFsoqegds=
        //TextBox1	1711605043
        //TextBox2	440981199906226832
        //TextBox3	k0uw
        //RadioButtonList1	%D1%A7%C9%FA
        //Button1
       new Thread(new Runnable() {
           @Override
           public void run() {
               String url = "http://61.142.209.19:81/default2.aspx";
               final String id = mUsername.getText().toString();
               String pass = mPassword.getText().toString();
               String yzm = Objects.requireNonNull(mEdYzm.getText()).toString();
               FormBody formBody  = new FormBody.Builder()
                       .add("__VIEWSTATE",VIEWSTATE)
                       .add("TextBox1", id)
                       .add("TextBox2", pass)
                       .add("TextBox3", yzm)
                       .add("RadioButtonList1", "学生")//%D1%A7%C9%FA
                       .add("Button1", "")
                       .add("__EVENTVALIDATION","/wEWCwL1uJaxBgLs0bLrBgLs0fbZDALs0Yq1BQK/wuqQDgKAqenNDQLN7c0VAuaMg+INAveMotMNAoznisYGArursYYIr0kwaQ40D9n5Y1VNLkE/gRRL/BY=")
                       .build();

               Request request = new Request
                       .Builder()
                       .url(url)
                       .post(formBody)
                       .build();
               okHttpClient.newCall(request).enqueue(new Callback() {
                   @Override
                   public void onFailure(Call call, IOException e) {

                   }

                   @Override
                   public void onResponse(Call call, Response response) throws IOException {
                       final String string = response.body().string();
                      Log.i(Tag,"登录成功后"+string);
                      if (string.contains("欢迎您")){
                          Document document = Jsoup.parse(string);
                          String s = document.select("li.top:nth-child(1) > ul:nth-child(2) > li:nth-child(1) > a:nth-child(1)").toString();
                          Log.i(Tag,s);
                          runOnUiThread(new Runnable() {
                              @Override
                              public void run() {

                                  Document document = Jsoup.parse(string);
                                  String name = document.getElementById("xhxm").text();
                                 // Toast.makeText(context, "欢迎回来,"+name, Toast.LENGTH_SHORT).show();
                                  MessageUtils.setName(name);
                                  spUtils.putString("name",name);
                                  spUtils.putString("id",id);

                                  spUtils.putBoolean("登录",true);
                                  spUtils.putString("姓名",name);
                                  saveCookies();
                                  Intent intent = new Intent(context,CourseActivity.class);
                                  startActivity(intent);
                                  finish();
                              }
                          });
                      }else {
                          Snackbar.make(getWindow().getDecorView(),"抱歉,服务器开了小差,登录失败!+\n",Snackbar.LENGTH_SHORT).setAction("OK", new View.OnClickListener() {
                              @Override
                              public void onClick(View v) {

                              }
                          }).show();
                      }

                       Logs("登录"+response.headers().toString());

                   }
               });
           }
       }).start();
    }

    private void Logs(String s) {
        Log.i(Tag,s);
    }
}
