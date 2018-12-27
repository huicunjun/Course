package lx.curriculumschedule.ui;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

import lx.curriculumschedule.R;
import lx.curriculumschedule.bean.Course;
import lx.curriculumschedule.bean.User;
import lx.curriculumschedule.utils.DateUtils;
import lx.curriculumschedule.utils.HttpUtils;
import lx.curriculumschedule.utils.LogUtils;
import lx.curriculumschedule.utils.NetUtils;
import lx.curriculumschedule.utils.SPUtils;
import lx.curriculumschedule.utils.ToastUtils;
import okhttp3.Cookie;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class QCourseActivity extends AppCompatActivity implements View.OnClickListener {
    private RecyclerView mRecy;
    private OkHttpClient okHttpClient;
    private Context context = QCourseActivity.this;

    private String TAG = getClass().getSimpleName();

    private SPUtils spUtils;
    private User user;
    private List<Course> courses = new ArrayList<>();
    private Toolbar mToolbar;
    private DrawerLayout mDl;
    private int TYPE_KC = 0;
    private int TYPE_TIME = 1;
    private FloatingActionButton mFab;
    private IAdapter adapter;
    private List<TextView> viewLists = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.qactivity_course);

        okHttpClient = HttpUtils.getOkHttpClient();
        spUtils = new SPUtils(context, "info");
        adapter = new IAdapter();

        user = new User();
        user.setId(spUtils.getString("账号", "1711605043"));
        user.setName(spUtils.getString("name", "李涤威"));
        // init();
        initView();

        initToolBar();

        if (NetUtils.isConnectIsNomarl(context)) {
            if (spUtils.getBoolean("登录", false)) {
              new Thread(new Runnable() {
                  @Override
                  public void run() {
                     courses = initData();
                      runOnUiThread(new Runnable() {
                          @Override
                          public void run() {
                              if (courses==null){
                                  new ToastUtils(getWindow().getDecorView()).showSnackbar("获取课表数据为空\n");
                              }else {
                                  mRecy.setAdapter(adapter);
                              }
                          }
                      });
                  }
              }).start();
                if (spUtils.getString("姓名") != null) {
                    new ToastUtils(getWindow().getDecorView()).showSnackbar(DateUtils.getTip(spUtils.getString("姓名") + "\n"));
                }
            }else {
                new ToastUtils(getWindow().getDecorView()).showSnackbar("获取课表数据失败,可能当前账户登录已失效,重新登录试试!"+"\n");
            }

        } else {
            new ToastUtils(getWindow().getDecorView()).showSnackbar("网络已经断开!\n");
        }

        mingyan();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    protected void onDestroy() {
        HttpUtils.saveCookies(this);
        super.onDestroy();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.zhuxiao:
                spUtils.putBoolean("登录", false);
                new SPUtils(context, "cookie").getSp().edit().clear().apply();
                Intent intent = new Intent(context, LoginActivity.class);
                startActivity(intent);
                finish();
                break;
            case R.id.setting:
                new ToastUtils(getWindow().getDecorView()).showSnackbar("开发者正在夜以继日的开发中!\n");
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void initToolBar() {
        setSupportActionBar(mToolbar);
        @SuppressLint("ResourceType") ActionBarDrawerToggle drawerToggle = new ActionBarDrawerToggle(this, mDl, mToolbar, R.mipmap.back_android_light2, R.mipmap.pf_menu_light);
        drawerToggle.syncState();
        mDl.addDrawerListener(drawerToggle);

    }


    private  List<Course> initData() {
        List<Course> dataList = new ArrayList<>();
                Request request = new Request.Builder()
                        .get()
                        .addHeader("Host", "61.142.209.19:81")
                        .addHeader("Referer", "http://61.142.209.19:81/xs_main.aspx?xh=" + user.getId() + "")
                        .url("http://61.142.209.19:81/xskbcx.aspx?xh=" + user.getId() + "&xm=" + user.getName() + "&gnmkdm=N121603")
                        .build();
                Response response = null;//Referer http://61.142.209.19:81/xs_main.aspx?xh=1711605043
                String string = null;
                try {
                    response = okHttpClient.newCall(request).execute();
                    string = response.body().string();
                    Log.i(TAG, string);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                if (string!=null&&string.contains("周")){
                    final Document document = Jsoup.parse(string);
                    final Element element = document.select("#Table1 > tbody:nth-child(1)").get(0);//#Table1 > tbody:nth-child(1)

                    Elements elements = document.getElementById("Table1").getElementsByTag("tbody").get(0).getElementsByTag("tr");
                    for (int i = 2; i < elements.size(); i++) {//12
                        Elements tds = elements.get(i).getElementsByTag("td");
                        int flag = 0;
                        for (int j = 0; j < tds.size(); j++) {
                            Element td = tds.get(j);
                            String text = td.text();
                            String rowspan = td.attr("rowspan");
                            String align = td.attr("align");
                            if (align.equals("center")) {
                                Course course = new Course();
                                if (text.contains("周") && text.contains("}")) {
                                    String time = text.substring(text.indexOf("周") - 1, text.indexOf("}") + 1);
                                    course.setTime(time);
                                    String name = text.substring(0, text.indexOf("周"));
                                    course.setName(name);
                                    /*String teacher = text.substring(text.indexOf("} "), text.indexOf(" "));
                                    course.setTeacher(teacher);*/
                                    String address = text.substring(text.indexOf("} ")+1, text.length());
                                    course.setAddress(address);
                                }
                                if (flag < 5) {
                                    dataList.add(course);
                                    flag++;
                                }
                            }
                            if (j == tds.size() - 1) {
                                i++;
                            }
                        }
                    }
                    return  dataList;
                }else {
                    return null;
                }
            }

    private void initView() {
        mRecy = (RecyclerView) findViewById(R.id.recy);
        mRecy.setLayoutManager(new GridLayoutManager(context, 5));

        // mRecy.setOnClickListener(this);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mDl = (DrawerLayout) findViewById(R.id.dl);
        mFab = (FloatingActionButton) findViewById(R.id.fab);
        mFab.setOnClickListener(this);
    }

    private void loadCookies() {
        if (spUtils.getBoolean("登录", false)) {
           // initData();
            /*new Thread(new Runnable() {
                @Override
                public void run() {
                   courses = initData();
                   runOnUiThread(new Runnable() {
                       @Override
                       public void run() {
                           mRecy.setAdapter(adapter);
                       }
                   });
                }
            }).start();*/
        } else {

        }
    }

    private void saveCookies() {
        Map<String, List<Cookie>> cookiestores = HttpUtils.MyCookiesJar.getCookiestores();
        List<Cookie> cookies = cookiestores.get("http://61.142.209.19:81/default2.aspx");
        for (int i = 0; i < cookies.size(); i++) {
            Cookie cookie = cookies.get(i);
            LogUtils.Logi(cookie.toString());
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            default:
                break;
            case R.id.recy:
                break;
            case R.id.fab:
                adapter.reSetColor();
                int color = getRandomColor();
                getWindow().setStatusBarColor(color);
                mToolbar.setBackgroundColor(color);


               // mFab.setBackgroundColor(color);
                break;
        }
    }

    class IAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

     /*   @NonNull
        @Override
        public CourseViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            if (i == 0) {
                //课程
                View view = LayoutInflater.from(context).inflate(R.layout.item_kb, viewGroup, false);
                CourseViewHolder courseViewHolder = new CourseViewHolder(view);
                return courseViewHolder;
            } else {
                View view = LayoutInflater.from(context).inflate(R.layout.item_time, viewGroup, false);
                TimeViewHolder timeViewHolder = new TimeViewHolder(view);
                return timeViewHolder;
            }


        }

        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder courseViewHolder, int i) {

            courseViewHolder.mTv.setText(courses.get(i).getName());

            if (courses.get(i).getName().length() > 2) {
                courseViewHolder.mTv.setBackgroundColor(getRandomColor());
            }
        }*/

        @Override
        public int getItemCount() {
            return courses.size();
        }

        @NonNull
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int type) {
            View view = LayoutInflater.from(context).inflate(R.layout.item_kb, viewGroup, false);
            return new CourseViewHolder(view);
           /* if (type == 0) {
                //课程
                View view = LayoutInflater.from(context).inflate(R.layout.item_kb, viewGroup, false);
                return new CourseViewHolder(view);
            } else {
                View view = LayoutInflater.from(context).inflate(R.layout.item_time, viewGroup, false);
                return new TimeViewHolder(view);
            }*/
        }

        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
            if (viewHolder instanceof CourseViewHolder) {
                CourseViewHolder courseViewHolder = ((CourseViewHolder) viewHolder);
                courseViewHolder.mTv.setText(courses.get(i).getName()+courses.get(i).getAddress()+courses.get(i).getTeacher());
                if (courses.get(i).getName().length() > 2) {
                    courseViewHolder.mTv.setBackgroundColor(getRandomColor());
                    viewLists.add(courseViewHolder.mTv);
                }
          /*  if (viewHolder instanceof CourseViewHolder) {
                CourseViewHolder courseViewHolder = ((CourseViewHolder) viewHolder);
                i = i - 1 - i / 6;
                courseViewHolder.mTv.setText(courses.get(i).getName());
                if (courses.get(i).getName().length() > 2) {
                    courseViewHolder.mTv.setBackgroundColor(getRandomColor());
                    viewLists.add(courseViewHolder.mTv);
                }
            } else {
                TimeViewHolder timeViewHolder = ((TimeViewHolder) viewHolder);
               *//* timeViewHolder.mTv1.setText();
                timeViewHolder.mTv2.setText();*//*
                switch (i) {
                    case 0:
                        timeViewHolder.mTv1.setText("1");
                        timeViewHolder.mTv2.setText("2");
                        break;
                    case 6:
                        timeViewHolder.mTv1.setText("3");
                        timeViewHolder.mTv2.setText("4");
                        break;
                    case 12:
                        timeViewHolder.mTv1.setText("5");
                        timeViewHolder.mTv2.setText("6");
                        break;
                    case 18:
                        timeViewHolder.mTv1.setText("7");
                        timeViewHolder.mTv2.setText("8");
                        break;
                    case 24:
                        timeViewHolder.mTv1.setText("9");
                        timeViewHolder.mTv2.setText("10");
                        break;
                    case 30:
                        timeViewHolder.mTv1.setText("11");
                        timeViewHolder.mTv2.setText("12");
                        break;
                }*/

            }
        }

/*        @Override
        public int getItemViewType(int position) {
            return position % 6 == 0 ? TYPE_TIME : TYPE_KC;
            //return courses.get(position).getType();
        }*/

        public void reSetColor() {
            for (int i = 0; i < viewLists.size(); i++) {
                viewLists.get(i).setBackgroundColor(getRandomColor());
            }
        }

        class CourseViewHolder extends RecyclerView.ViewHolder {
            View view;
            TextView mTv;

            CourseViewHolder(View view) {
                super(view);
                this.view = view;
                this.mTv = (TextView) view.findViewById(R.id.tv);
            }
        }

        class TimeViewHolder extends RecyclerView.ViewHolder

        {
            View view;
            TextView mTv1;
            TextView mTv2;

            TimeViewHolder(View view) {
                super(view);
                this.view = view;
                this.mTv1 = (TextView) view.findViewById(R.id.tv1);
                this.mTv2 = (TextView) view.findViewById(R.id.tv2);
            }
        }
    }

    private void mingyan() {
        final String[] my = new String[]{
                "养心莫善于诚。",
                "诚信者，天下之结也。",
                "生活有度，人生添寿。",
                "人生若只如初见，何事秋风悲画扇。",
                "寂寞空庭春欲晚，梨花满地不开门。",
                "冠盖满京华，斯人独憔悴。",
        };
        //  toolbar.setSubtitle(my[0]);
        final Random random = new Random();
        int i = random.nextInt(my.length);
        mToolbar.setSubtitle(my[i]);
    }

    private int getRandomColor() {
        int colors[] = {getResources().getColor(R.color.color3F51B5),
                getResources().getColor(R.color.colorff5722),
                getResources().getColor(R.color.color8bc34a),
                getResources().getColor(R.color.colorba68c8),
                getResources().getColor(R.color.color5c6bc0),
                getResources().getColor(R.color.color536dfe),
                getResources().getColor(R.color.color795548),
                getResources().getColor(R.color.color607d8b),

                getResources().getColor(R.color.color53add9),
                getResources().getColor(R.color.colorb442ca),
                getResources().getColor(R.color.colordcd862),
                getResources().getColor(R.color.colord9795c),
                getResources().getColor(R.color.colorb3f16d),
                getResources().getColor(R.color.colorbca3de),
        };
        final Random random = new Random();
        int i = random.nextInt(colors.length);

        return colors[i];
    }
}
