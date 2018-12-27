package lx.curriculumschedule.ui;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;

import com.qmuiteam.qmui.widget.dialog.QMUIDialog;

import lx.curriculumschedule.R;
import lx.curriculumschedule.fragment.Fragment_book;
import lx.curriculumschedule.fragment.Fragment_course;
import lx.curriculumschedule.utils.SPUtils;
import lx.curriculumschedule.utils.ToastUtils;

public class HomeActivity extends AppCompatActivity implements View.OnClickListener {


    private NavigationView mNavView;
    private Toolbar mToolbar;
    private DrawerLayout mDl;
    private FloatingActionButton mFab;
    private Context context = HomeActivity.this;
    private FrameLayout mContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        initView();
        initToolBar();

        getSupportFragmentManager().beginTransaction().replace(R.id.content, new Fragment_course()).commit();

        mNavView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()){
                    case R.id.nav_about:
                         getSupportFragmentManager().beginTransaction().replace(R.id.content,new Fragment_book()).commit();
                        break;
                }
                return false;
            }
        });

    }

    private void initToolBar() {
        setSupportActionBar(mToolbar);
        @SuppressLint("ResourceType") ActionBarDrawerToggle drawerToggle = new ActionBarDrawerToggle(this, mDl, mToolbar, R.mipmap.back_android_light2, R.mipmap.pf_menu_light);
        drawerToggle.syncState();
        mDl.addDrawerListener(drawerToggle);

    }

    private void initView() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mDl = (DrawerLayout) findViewById(R.id.dl);
      /*  mFab = (FloatingActionButton) findViewById(R.id.fab);
        mFab.setOnClickListener(this);*/
        mNavView = (NavigationView) findViewById(R.id.nav_view);
        mToolbar.setOnClickListener(this);
        mContent = (FrameLayout) findViewById(R.id.content);
        mFab = (FloatingActionButton) findViewById(R.id.fab);
        mFab.setOnClickListener(this);
        mNavView.setOnClickListener(this);
        mDl.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.zhuxiao:
                new QMUIDialog.MenuDialogBuilder(context).addItem("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        SPUtils spUtils = new SPUtils(context, "info");
                        spUtils.putBoolean("登录", false);
                        new SPUtils(context, "cookie").getSp().edit().clear().apply();
                        Intent intent = new Intent(context, LoginActivity.class);
                        startActivity(intent);
                        finish();
                    }
                }).addItem("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                }).show();


                break;
            case R.id.setting:
                new ToastUtils(getWindow().getDecorView()).showSnackbar("开发者正在夜以继日的开发中!\n");
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
