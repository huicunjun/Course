package lx.curriculumschedule.ui;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import java.util.ArrayList;
import java.util.List;

import lx.curriculumschedule.Fragment1;
import lx.curriculumschedule.R;
import lx.curriculumschedule.bean.Course;
import lx.curriculumschedule.bean.User;
import lx.curriculumschedule.utils.SPUtils;
import lx.curriculumschedule.utils.ToastUtils;
import okhttp3.OkHttpClient;

public class HomeActivity extends AppCompatActivity {

    private Toolbar mToolbar;
    private TabLayout mTab;
    private ViewPager mVp;
    private OkHttpClient okHttpClient;
    private String TAG = getClass().getSimpleName();

    private User user;
    private List<List<Course>> dataList = new ArrayList<>();

    private SPUtils spUtils;
    private Context context = HomeActivity.this;
   //
    // private WebView mWeb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);


        initView();
    }


    private void initView() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mTab = (TabLayout) findViewById(R.id.tab);
        mVp = findViewById(R.id.vp);
       // mWeb = (WebView) findViewById(R.id.web);
        final List<Fragment> fragments = new ArrayList<>();
        fragments.add(new Fragment1());
       // fragments.add(new Fragment1());
        //fragments.add(new Fragment1());
        mTab.setupWithViewPager(mVp);
        //Objects.requireNonNull(mTab.getTabAt(0)).setText("课程表");
        mVp.setAdapter(new FragmentStatePagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int i) {
                return fragments.get(i);
            }

            @Override
            public int getCount() {
                return fragments.size();
            }
        });
    }





    //http://61.142.209.19:81/xskbcx.aspx?xh=1711605043&xm=李涤威&gnmkdm=N121603
    //http://61.142.209.19:81/xskbcx.aspx?xh=1711605043&xm=%C0%EE%B5%D3%CD%FE&gnmkdm=N121603
    //xf_xsqxxxk.aspx?xh=1711605043&amp;xm=李涤威&amp;gnmkdm=N121203
}
