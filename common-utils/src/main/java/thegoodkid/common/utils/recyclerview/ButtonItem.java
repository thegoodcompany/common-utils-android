package thegoodkid.common.utils.recyclerview;

import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class ButtonItem implements BaseItem {
    @NonNull
    private String mTitle;
    @Nullable
    private Button.OnClickListener mOnClickListener;

    public ButtonItem(@NonNull String title, @Nullable Button.OnClickListener onClickListener) {
        mTitle = title;
        mOnClickListener = onClickListener;
    }

    @NonNull
    @Override
    public String getTitle() {
        return mTitle;
    }

    @Nullable
    public Button.OnClickListener getOnClickListener() {
        return mOnClickListener;
    }
}
