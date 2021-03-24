/*
 * Copyright (c) The Good Company. All rights reserved.
 * Licensed under the MIT License.
 */

package thegoodcompany.common.utilsdemo.utilis.list;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.microsoft.fluentui.listitem.ListItemView;
import com.microsoft.fluentui.listitem.ListSubHeaderView;
import com.microsoft.fluentui.widget.Button;

import java.util.LinkedHashMap;

import thegoodcompany.common.utils.recyclerview.BaseItem;
import thegoodcompany.common.utils.recyclerview.BaseListAdapter;
import thegoodcompany.common.utils.recyclerview.ButtonItem;
import thegoodcompany.common.utils.recyclerview.HeaderItem;
import thegoodcompany.common.utilsdemo.R;
import thegoodcompany.common.utilsdemo.databinding.ViewButtonBinding;

public class ListItemAdapter<K extends Enum<K>> extends BaseListAdapter<K, ItemSection, HeaderItem, Item, RecyclerView.ViewHolder> {
    @NonNull
    private final Context mContext;

    public ListItemAdapter(@NonNull Context context, LinkedHashMap<K, ItemSection> sectionMap) {
        super(sectionMap);

        mContext = context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        switch (ViewType.values()[viewType]) {
            case ITEM:
                ListItemView itemView = new ListItemView(mContext);
                itemView.setLayoutParams(layoutParams);
                return new ListItemAdapter.ItemViewHolder(itemView);

            case HEADER:
                ListSubHeaderView subHeaderView = new ListSubHeaderView(mContext);
                subHeaderView.setLayoutParams(layoutParams);
                return new ListItemAdapter.HeaderViewHolder(subHeaderView);

            case BUTTON:
                return new ListItemAdapter.ButtonViewHolder(LayoutInflater.from(mContext).inflate(R.layout.view_button, parent, false));
        }

        throw new AssertionError("Invalid view type: " + viewType);
    }

    @Override
    protected void onBindViewHolder(@NonNull AfterWards runAfterwards, @NonNull RecyclerView.ViewHolder holder, int position) {
        BaseItem baseItem = getItem(position);

        if (holder instanceof ItemViewHolder) {
            ItemViewHolder itemHolder = (ItemViewHolder) holder;
            Item item = (Item) baseItem;

            String subtitle = item.getSubtitle();
            String footer = item.getFooter();
            ListItemView.CustomViewSize viewSize = item.getCustomViewSize();

            itemHolder.mItemView.setTitle(item.getTitle());
            itemHolder.mItemView.setOnClickListener(item.getOnClickListener());

            itemHolder.mItemView.setCustomView(item.getCustomView());
            itemHolder.mItemView.setCustomAccessoryView(item.getAccessoryView());

            itemHolder.mItemView.setSubtitle(subtitle == null ? "" : subtitle);
            itemHolder.mItemView.setFooter(footer == null ? "" : footer);
            itemHolder.mItemView.setCustomViewSize(viewSize == null ? ListItemView.Companion.getDEFAULT_CUSTOM_VIEW_SIZE() : viewSize);
        } else if (holder instanceof ListItemAdapter.HeaderViewHolder) {
            HeaderViewHolder itemHolder = (HeaderViewHolder) holder;
            HeaderItem headerItem = (HeaderItem) baseItem;

            itemHolder.mSubHeaderView.setTitle(headerItem.getTitle());
            itemHolder.mSubHeaderView.setCustomAccessoryView(headerItem.getAccessoryView());
        } else {
            ButtonViewHolder itemHolder = (ButtonViewHolder) holder;
            ButtonItem buttonItem = (ButtonItem) baseItem;

            itemHolder.mButton.setText(buttonItem.getTitle());
            itemHolder.mButton.setOnClickListener(buttonItem.getOnClickListener());
        }

        runAfterwards.run();
    }

    @Override
    public int getItemPosition(Item item) {
        return super.getItemPosition(item);
    }

    static class HeaderViewHolder extends RecyclerView.ViewHolder {
        private final ListSubHeaderView mSubHeaderView;

        HeaderViewHolder(@NonNull ListSubHeaderView itemView) {
            super(itemView);

            mSubHeaderView = itemView;
        }
    }

    static class ItemViewHolder extends RecyclerView.ViewHolder {
        private final ListItemView mItemView;

        ItemViewHolder(@NonNull ListItemView itemView) {
            super(itemView);

            mItemView = itemView;
        }
    }

    static class ButtonViewHolder extends RecyclerView.ViewHolder {
        private final Button mButton;

        ButtonViewHolder(@NonNull View itemView) {
            super(itemView);

            ViewButtonBinding binding = ViewButtonBinding.bind(itemView);
            mButton = binding.button;
        }
    }
}
