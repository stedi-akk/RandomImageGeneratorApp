package com.stedi.randomimagegenerator.app.other;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.DimenRes;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.util.TypedValue;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.NumberPicker;
import android.widget.Toast;

import com.stedi.randomimagegenerator.app.di.RootSavePath;

import java.io.File;
import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.Date;

public final class Utils {
    private static SimpleDateFormat dateFormat;

    private Utils() {
    }

    public static int dp2pxi(@NonNull Context context, @DimenRes int id) {
        return (int) dp2px(context, id);
    }

    public static float dp2px(@NonNull Context context, @DimenRes int id) {
        Resources res = context.getResources();
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_PX, res.getDimensionPixelOffset(id), res.getDisplayMetrics());
    }

    public static int dp2pxi(@NonNull Context context, float dp) {
        return (int) dp2px(context, dp);
    }

    public static float dp2px(@NonNull Context context, float dp) {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, context.getResources().getDisplayMetrics());
    }

    public static void toastLong(@NonNull Context context, @StringRes int id) {
        toastLong(context, context.getString(id));
    }

    public static void toastLong(@NonNull Context context, @NonNull String message) {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show();
    }

    public static void toastShort(@NonNull Context context, @StringRes int id) {
        toastShort(context, context.getString(id));
    }

    public static void toastShort(@NonNull Context context, @NonNull String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

    public static void hideInput(@NonNull Activity activity) {
        View view = activity.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    @NonNull
    public static String formatTime(long millis) {
        if (dateFormat == null)
            dateFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm");
        return dateFormat.format(new Date(millis));
    }

    @NonNull
    public static String formatSavePath(@RootSavePath @NonNull String rootSavePath, @NonNull String path) {
        return path.replace(rootSavePath + File.separator, "sdcard/Pictures/RIG/");
    }

    // https://stackoverflow.com/questions/24233556/changing-numberpicker-divider-color
    public static void setDividerColor(@NonNull NumberPicker picker, int color) {
        Field[] pickerFields = NumberPicker.class.getDeclaredFields();
        for (Field pf : pickerFields) {
            if (pf.getName().equals("mSelectionDivider")) {
                pf.setAccessible(true);
                try {
                    pf.set(picker, new ColorDrawable(color));
                } catch (Exception e) {
                    // ignore
                }
                break;
            }
        }
    }
}
