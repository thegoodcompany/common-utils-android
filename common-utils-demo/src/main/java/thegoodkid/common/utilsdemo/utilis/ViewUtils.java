package thegoodkid.common.utilsdemo.utilis;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;

import androidx.annotation.AttrRes;
import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.core.widget.ImageViewCompat;

import com.microsoft.fluentui.util.ThemeUtil;
import com.microsoft.fluentui.util.ThemeUtilsKt;

import org.jetbrains.annotations.NotNull;

import thegoodkid.common.utilsdemo.R;

public class ViewUtils {
    public static Drawable createNavigationBackDrawable(@NonNull Context context) {
        return ThemeUtilsKt.getTintedDrawable(context, R.drawable.ic_fluent_arrow_left_24_regular,
                ThemeUtil.INSTANCE.getThemeAttrColor(context, R.attr.fluentuiToolbarIconColor));
    }

    public static Drawable getTintedDrawable(@NonNull Context context, @DrawableRes int drawableId, @AttrRes int tint) {
        int themedColor = ThemeUtil.INSTANCE.getThemeAttrColor(context, tint);
        return ThemeUtilsKt.getTintedDrawable(context, drawableId, themedColor);
    }

    @NotNull
    public static ImageView createTintedIcon(@NonNull Context context, @DrawableRes int imageId, @AttrRes int tint) {
        ImageView imageView = new ImageView(context);
        Drawable drawable = context.getDrawable(imageId);
        imageView.setImageDrawable(drawable);

        ImageViewCompat.setImageTintList(imageView, ThemeUtil.INSTANCE.getThemeAttrColorStateList(context, tint));

        return imageView;
    }
}
