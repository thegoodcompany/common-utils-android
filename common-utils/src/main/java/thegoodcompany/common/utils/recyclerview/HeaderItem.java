/*
 * Copyright (c) The Good Company. All rights reserved.
 * Licensed under the MIT License.
 */

package thegoodcompany.common.utils.recyclerview;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class HeaderItem implements BaseHeaderItem {
    @NonNull
    private String mTitle;
    @Nullable
    private View mAccessoryView;

    public HeaderItem(@NonNull String title) {
        mTitle = title;
    }

    public HeaderItem setAccessoryView(View accessoryView) {
        mAccessoryView = accessoryView;

        return this;
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
