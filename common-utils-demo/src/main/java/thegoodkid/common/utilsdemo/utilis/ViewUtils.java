package thegoodkid.common.utilsdemo.utilis;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;

import androidx.annotation.AttrRes;
import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.core.widget.ImageViewCompat;

import com.microsoft.fluentui.util.ThemeUtil;

import org.jetbrains.annotations.NotNull;

import thegoodkid.common.utilsdemo.R;

public class ViewUtils {
    public static Drawable createNavigationBackDrawable(@NonNull Context context) {
        return createTintedDrawable(context, R.drawable.ic_fluent_arrow_left_24_selector, R.attr.fluentuiToolbarIconColor);
    }

    public static Drawable tintAndGetDrawable(@NonNull Context context, @DrawableRes int drawableId, @AttrRes int tint) {
        int themedColor = ThemeUtil.INSTANCE.getThemeAttrColor(context, tint);
        Drawable drawable = context.getDrawable(drawableId);
        if (drawable != null) drawable.setTint(themedColor);

        return drawable;
    }

    public static Drawable createTintedDrawable(@NonNull Context context, @DrawableRes int drawableId, @AttrRes int tint) {
        int themedColor = ThemeUtil.INSTANCE.getThemeAttrColor(context, tint);
        Drawable drawable = ContextCompat.getDrawable(context, drawableId);

        if (drawable != null) {
            drawable = DrawableCompat.wrap(drawable);
            DrawableCompat.setTint(drawable.mutate(), themedColor);
        }

        return drawable;
    }

    @NotNull
    public static ImageView createTintedIcon(@NonNull Context context, @DrawableRes int imageId, @AttrRes int tint) {
        ImageView imageView = new ImageView(context);
        Drawable drawable = context.getDrawable(imageId);
        imageView.setImageDrawable(drawable);

        ImageViewCompat.setImageTintList(imageView, ThemeUtil.INSTANCE.getThemeAttrColorStateList(context, tint));

        return imageView;
    }

    @NonNull
    public static ImageView createIcon(@NonNull Context context, @DrawableRes int imageId) {
        ImageView imageView = new ImageView(context);
        Drawable drawable = context.getDrawable(imageId);
        imageView.setImageDrawable(drawable);

        return imageView;
    }
}
