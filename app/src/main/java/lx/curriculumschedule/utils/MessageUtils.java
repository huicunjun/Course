package lx.curriculumschedule.utils;

import android.content.Context;

import lx.curriculumschedule.bean.User;

public class MessageUtils {
    static String name;

    public static void setName(String name) {
        MessageUtils.name = name;
    }

    public static String getName() {
        return name;
    }

    public static User getUser(Context context) {
        User user = new User();
        SPUtils spUtils = new SPUtils(context, "info");
        user.setId(spUtils.getString("账号", ""));
        user.setName(spUtils.getString("name", ""));
        return user;
    }
}
