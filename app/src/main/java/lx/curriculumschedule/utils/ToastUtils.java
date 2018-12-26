package lx.curriculumschedule.utils;

import android.graphics.Color;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.widget.Button;

import lx.curriculumschedule.R;

public class ToastUtils {
    private View view;

    public ToastUtils(View view) {
        this.view = view;
    }

    public void showSnackbar(String s) {
         Snackbar snackbar  = Snackbar.make(view, s, Snackbar.LENGTH_SHORT).setAction("Ok", new View.OnClickListener() {
             @Override
             public void onClick(View v) {

             }
         });
         snackbar.show();
    }
    public void showSnackbar(String s, int snackBarViewBackgroundColor) {
        Snackbar snackbar  = Snackbar.make(view, s, Snackbar.LENGTH_LONG).setAction("ok", new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        //获取Snackbar的view
        View snackbarview = snackbar.getView();
        //文字的颜色
        // ((TextView) snackbarview.findViewById(R.id.snackbar_text)).setTextColor(Color.parseColor("#FF0000"));
        //按钮颜色
        //按钮的颜色
        ((Button)snackbarview.findViewById(R.id.snackbar_action)).setTextColor(Color.WHITE);
        //设置背景色
        snackbarview.setBackgroundColor(snackBarViewBackgroundColor);
        snackbar.show();
        //Toast.makeText(context, day + "", Toast.LENGTH_SHORT).show();
    }
    public static void showSnackbar(String s, int snackBarViewBackgroundColor, View view) {
        Snackbar snackbar  = Snackbar.make(view, s, Snackbar.LENGTH_LONG).setAction("ok", new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        //获取Snackbar的view
        View snackbarview = snackbar.getView();
        //文字的颜色
        // ((TextView) snackbarview.findViewById(R.id.snackbar_text)).setTextColor(Color.parseColor("#FF0000"));
        //按钮颜色
        //按钮的颜色
        ((Button)snackbarview.findViewById(R.id.snackbar_action)).setTextColor(Color.WHITE);
        //设置背景色
        snackbarview.setBackgroundColor(snackBarViewBackgroundColor);
        snackbar.show();
        //Toast.makeText(context, day + "", Toast.LENGTH_SHORT).show();
    }
}
