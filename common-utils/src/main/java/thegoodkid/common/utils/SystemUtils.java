package thegoodkid.common.utils;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class SystemUtils {
    public static boolean showSoftKeyboard(@NonNull Activity activity, View view) {
        if (view != null) view.requestFocus();

        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) return imm.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT);

        return false;
    }

    public static void showSoftKeyboard(@NonNull Window window, @Nullable View view) {
        if (view != null) view.requestFocus();
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
    }

    public static boolean hideSoftKeyboard(@NonNull Activity activity, @NonNull View view) {
        view.clearFocus();

        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) return imm.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);

        return false;
    }
}
