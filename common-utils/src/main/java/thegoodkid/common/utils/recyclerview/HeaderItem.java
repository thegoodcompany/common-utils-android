package thegoodkid.common.utils.recyclerview;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class HeaderItem implements BaseHeaderItem {
    @NonNull
    private String mTitle;
    @Nullable
    private View mAccessoryView;

    public HeaderItem(@NonNull String title, @Nullable View accessoryView) {
        mTitle = title;
        mAccessoryView = accessoryView;
    }

    @NonNull
    public String getTitle() {
        return mTitle;
    }

    @Nullable
    public View getAccessoryView() {
        return mAccessoryView;
    }
}
