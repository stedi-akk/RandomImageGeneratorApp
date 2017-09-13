package com.stedi.randomimagegenerator.app.other;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.DimenRes;
import android.support.annotation.NonNull;
import android.util.SparseArray;
import android.util.TypedValue;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public final class Utils {
    private Utils() {
    }

    public static int dimen2pxi(@NonNull Context context, @DimenRes int id) {
        return dp2pxi(context, context.getResources().getDimensionPixelSize(id));
    }

    public static float dimen2px(@NonNull Context context, @DimenRes int id) {
        return dp2px(context, context.getResources().getDimensionPixelSize(id));
    }

    public static int dp2pxi(@NonNull Context context, float dp) {
        return (int) dp2px(context, dp);
    }

    public static float dp2px(@NonNull Context context, float dp) {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, context.getResources().getDisplayMetrics());
    }

    public static void sleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @NonNull
    public static <T> List<T> sparseArrayToList(@NonNull SparseArray<T> array) {
        List<T> result = new ArrayList<>();
        for (int i = 0; i < array.size(); i++)
            result.add(array.valueAt(i));
        return result;
    }

    public static void toastLong(@NonNull Context context, @NonNull String message) {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show();
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
}
