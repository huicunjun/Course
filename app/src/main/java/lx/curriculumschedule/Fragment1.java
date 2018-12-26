package lx.curriculumschedule;


import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
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

import lx.curriculumschedule.bean.Course;
import lx.curriculumschedule.bean.User;
import lx.curriculumschedule.utils.HttpUtils;
import lx.curriculumschedule.utils.LogUtils;
import lx.curriculumschedule.utils.SPUtils;
import okhttp3.Cookie;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


/**
 * A simple {@link Fragment} subclass.
 */
public class Fragment1 extends Fragment implements View.OnClickListener {


    private View view;
    private RecyclerView mRecy;
    private String id = "1711605043";
    private OkHttpClient okHttpClient;
    private Activity activity;
    private String TAG = getClass().getSimpleName();
    private SPUtils spUtils;
    private User user;
    private List<Course> dataList = new ArrayList<>();

    public Fragment1() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment1, container, false);
        okHttpClient = HttpUtils.getOkHttpClient();
        activity = getActivity();
        spUtils = new SPUtils(activity, "info");

        user = new User();
        user.setId(spUtils.getString("id", "1711605043"));
        user.setName(spUtils.getString("name", "李涤威"));
        // init();

        initView(view);
        loadCookies();

        return view;
    }

    private void initData() {
        final String url = "http://61.142.209.19:81/xskbcx.aspx";

        new Thread(new Runnable() {
            @Override
            public void run() {
                Request request = new Request.Builder().url("http://61.142.209.19:81/xs_main.aspx?xh=" + user.getId() + "").get().build();
                try {
                    Response response = okHttpClient.newCall(request).execute();
                    String string = response.body().string();
                    Log.i(TAG, string);
                    LogUtils.Logi(string);

                    Document parse = Jsoup.parse(string);
                    String href = parse.select("li.top:nth-child(5) > ul:nth-child(2) > li:nth-child(1) > a:nth-child(1)").attr("href").toString();
                    Request request1 = new Request.Builder()
                            .get()
                            .addHeader("Host", "61.142.209.19:81")
                            .addHeader("Referer", "http://61.142.209.19:81/xs_main.aspx?xh=" + user.getId() + "")
                            .url("http://61.142.209.19:81/xskbcx.aspx?xh=" + user.getId() + "&xm=" + user.getName() + "&gnmkdm=N121603")
                            // .removeHeader("User-Agent")
                            //.addHeader("User-Agent","Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:64.0) Gecko/20100101 Firefox/64.0")
                            .build();
                    Response response1 = okHttpClient.newCall(request1).execute();//Referer http://61.142.209.19:81/xs_main.aspx?xh=1711605043
                    String string1 = response1.body().string();
                    Log.i(TAG, string1);

                    final Document document1 = Jsoup.parse(string1);
                    final Element element = document1.select("#Table1 > tbody:nth-child(1)").get(0);//#Table1 > tbody:nth-child(1)

                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            Elements elements = document1.getElementById("Table1").getElementsByTag("tbody").get(0).getElementsByTag("tr");
                            for (int i = 2; i < elements.size(); i++) {//12
                                Elements tds = elements.get(i).getElementsByTag("td");
                                // if (tds.get(0).text().contains("上"))
                               // List<Course> tempDataList = new ArrayList<>();
                                int flag = 0;
                                for (int j = 0; j < tds.size(); j++) {
                                    Element td = tds.get(j);
                                    String text = td.text();
                                    String rowspan = td.attr("rowspan");
                                    String align = td.attr("align");
                                    if (align.equals("center")) {
                                        Course course = new Course();
                                        if (text.contains("周")&&text.contains("}")){
                                            String time = text.substring(text.indexOf("周") - 1, text.indexOf("}") + 1);
                                            course.setTime(time);
                                            String name = text.substring(0,text.indexOf("周"));
                                            course.setName(name);
                                        }
                                      //  String name = text.substring(text.indexOf("周"),(text.indexOf("}"))+1);

                                       // tempDataList.add(course);

                                        if (flag<5){
                                            dataList.add(course);
                                            flag++;
                                        }

                                    }
                                    if (j == tds.size() - 1) {
                                        i++;
                                    }
                                }

                            }
                            //append(dataList.toString()+"\n");
                            mRecy.setAdapter(new IAdapter());
                        }

                    });


                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private void initView(View view) {
        mRecy = (RecyclerView) view.findViewById(R.id.recy);
        mRecy.setLayoutManager(new GridLayoutManager(activity, 5));

       // mRecy.setOnClickListener(this);
    }
    private void loadCookies() {
        if (spUtils.getBoolean("登录",false)){
            initData();
        }else {

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
        }
    }

    class IAdapter extends RecyclerView.Adapter<IAdapter.ViewHolder> {

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            View view = LayoutInflater.from(activity).inflate(R.layout.item_kb, viewGroup, false);
            ViewHolder viewHolder = new ViewHolder(view);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {

            viewHolder.mTv.setText(dataList.get(i).getName());
        }

        @Override
        public int getItemCount() {
            return dataList.size();
        }


        class ViewHolder extends RecyclerView.ViewHolder {
            View view;
            TextView mTv;

            ViewHolder(View view) {
                super(view);
                this.view = view;
                this.mTv = (TextView) view.findViewById(R.id.tv);
            }
        }
    }
}
