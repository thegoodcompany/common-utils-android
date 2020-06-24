package thegoodkid.common.utilsdemo.utilis.list;

import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.microsoft.fluentui.listitem.ListItemView;

import java.util.Objects;

import thegoodkid.common.utils.recyclerview.BaseItem;

public class Item implements BaseItem {
    @NonNull
    private String mTitle;
    @Nullable
    private String mSubtitle;
    @Nullable
    private String mFooter;
    @Nullable
    private View mCustomView;
    @Nullable
    private ListItemView.CustomViewSize mCustomViewSize;
    @Nullable
    private View.OnClickListener mOnClickListener;
    @Nullable
    private View mAccessoryView;

    public Item(@NonNull String title) {
        mTitle = title;
    }

    @NonNull
    public String getTitle() {
        return mTitle;
    }

    public void replaceTitle(String newTitle) {
        Log.d("Temp", "Replacing title from " + mTitle + " to " + newTitle);
        mTitle = newTitle;
    }

    @Nullable
    public String getSubtitle() {
        return mSubtitle;
    }

    public Item setSubtitle(String subtitle) {
        Log.d("Temp", "Title: " + mTitle);
        Log.d("Temp", "Setting subtitle from " + mSubtitle + " to " + subtitle);

        mSubtitle = subtitle;

        return this;
    }

    @Nullable
    public String getFooter() {
        return mFooter;
    }

    public Item setFooter(String footer) {
        Log.d("Temp", "Title: " + mTitle);
        Log.d("Temp", "Setting footer from " + mFooter + " to " + footer);

        mFooter = footer;

        return this;
    }

    public Item setCustomView(View customView, ListItemView.CustomViewSize size) {
        mCustomView = customView;
        mCustomViewSize = size;

        return this;
    }

    @Nullable
    public View getCustomView() {
        return mCustomView;
    }

    @Nullable
    public ListItemView.CustomViewSize getCustomViewSize() {
        return mCustomViewSize;
    }

    public View getAccessoryView() {
        return mAccessoryView;
    }

    public Item setAccessoryView(View accessoryView) {
        mAccessoryView = accessoryView;

        return this;
    }

    View.OnClickListener getOnClickListener() {
        return mOnClickListener;
    }

    public Item setOnClickListener(View.OnClickListener onClickListener) {
        mOnClickListener = onClickListener;

        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Item)) return false;
        Item item = (Item) o;

        return mTitle.equals(item.mTitle) &&
                Objects.equals(mSubtitle, item.mSubtitle) &&
                Objects.equals(mFooter, item.mFooter) &&
                Objects.equals(mCustomView, item.mCustomView) &&
                mCustomViewSize == item.mCustomViewSize &&
                Objects.equals(mOnClickListener, item.mOnClickListener) &&
                Objects.equals(mAccessoryView, item.mAccessoryView);
    }

    @Override
    public int hashCode() {
        return Objects.hash(mTitle, mSubtitle, mFooter, mCustomView, mCustomViewSize, mOnClickListener, mAccessoryView);
    }
}