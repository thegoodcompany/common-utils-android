package thegoodkid.common.utils;

import android.content.Context;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class SystemUtils {
    /**
     * Gives focus on {@param view}, then shows the soft keyboard.
     *
     * @param view A view on which the user should type something
     * @return Whether keyboard was successfully shown or not
     */
    public static boolean showSoftKeyboard(@NonNull Context context, @NonNull View view) {
        if (view.requestFocus()) {
            InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
            if (imm != null) return imm.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT);
        }

        return false;
    }

    public static void showSoftKeyboard(@NonNull Window window, @Nullable View view) {
        if (view != null) view.requestFocus();
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
    }

    public static boolean hideSoftKeyboard(@NonNull Context context, @NonNull View view) {
        view.clearFocus();

        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null)
            return imm.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);

        return false;
    }
}
