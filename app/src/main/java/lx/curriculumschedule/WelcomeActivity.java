package lx.curriculumschedule;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import lx.curriculumschedule.ui.CourseActivity;
import lx.curriculumschedule.ui.LoginActivity;
import lx.curriculumschedule.utils.HttpUtils;
import lx.curriculumschedule.utils.SPUtils;
import okhttp3.Cookie;

public class WelcomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        HttpUtils.localCookie(WelcomeActivity.this);
       /* new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(2000);
                    HttpUtils.localCookie(WelcomeActivity.this);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();*/

    }

   /* private void localCookie() {
        SPUtils spCookie = new SPUtils(getApplicationContext(),"cookie");
        SPUtils spUtils = new SPUtils(getApplicationContext(),"info");

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
                Intent intent = new Intent(getApplicationContext(),CourseActivity.class);
                startActivity(intent);
                finish();
            }else {
                Toast.makeText(this, "登录失效了", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getApplicationContext(),LoginActivity.class);
                startActivity(intent);
                finish();
            }

        }else {
            Intent intent = new Intent(getApplicationContext(),LoginActivity.class);
            startActivity(intent);
            finish();
        }
    }*/
}
