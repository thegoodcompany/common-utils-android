/*
 * Copyright (c) The Good Company. All rights reserved.
 * Licensed under the MIT License.
 */

package thegoodcompany.common.utils.recyclerview;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class Item implements BaseItem {
    @NonNull private String mTitle;
    @Nullable private String mSubtitle;
    @Nullable private String mFooter;
    @Nullable private View mCustomView;
    @Nullable
    private View.OnClickListener mOnClickListener;

    public Item(@NonNull String title) {
        this.mTitle = title;
    }

    @NonNull
    public String getTitle() {
        return mTitle;
    }

    @NonNull
    public Item replaceTitle(@NonNull String title) {
        mTitle = title;

        return this;
    }

    @Nullable
    public String getSubtitle() {
        return mSubtitle;
    }

    @NonNull
    public Item setSubtitle(String subtitle) {
        mSubtitle = subtitle;

        return this;
    }

    @Nullable
    public String getFooter() {
        return mFooter;
    }

    @NonNull
    public Item setFooter(String footer) {
        mFooter = footer;

        return this;
    }

    @Nullable
    public View getCustomView() {
        return mCustomView;
    }

    @NonNull
    public Item setCustomView(View v) {
        mCustomView = v;

        return this;
    }

    @Nullable
    public View.OnClickListener getOnClickListener() {
        return mOnClickListener;
    }

    @NonNull
    public Item setOnClickListener(@Nullable View.OnClickListener onClickListener) {
        mOnClickListener = onClickListener;

        return this;
    }
}
