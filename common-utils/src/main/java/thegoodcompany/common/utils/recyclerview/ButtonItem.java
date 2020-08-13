package thegoodcompany.common.utils.recyclerview;

import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class ButtonItem implements BaseButtonItem {
    @NonNull
    private String mTitle;
    @Nullable
    private Button.OnClickListener mOnClickListener;

    public ButtonItem(@NonNull String title) {
        mTitle = title;
    }

    public ButtonItem setOnClickListener(Button.OnClickListener listener) {
        mOnClickListener = listener;

        return this;
    }

    @NonNull
    public String getTitle() {
        return mTitle;
    }

    @Nullable
    public Button.OnClickListener getOnClickListener() {
        return mOnClickListener;
    }
}
