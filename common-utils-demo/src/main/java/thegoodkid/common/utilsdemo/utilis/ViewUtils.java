package thegoodkid.common.utilsdemo.utilis;

import android.content.Context;
import android.graphics.drawable.Drawable;

import com.microsoft.fluentui.util.ThemeUtil;
import com.microsoft.fluentui.util.ThemeUtilsKt;

import thegoodkid.common.utilsdemo.R;

public class ViewUtils {
    public static Drawable createNavigationBackDrawable(Context context) {
        return ThemeUtilsKt.getTintedDrawable(context, R.drawable.ic_fluent_arrow_left_24_regular,
                ThemeUtil.INSTANCE.getThemeAttrColor(context, R.attr.fluentuiToolbarIconColor));
    }
}
