package thegoodkid.common.utils.recyclerview;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

public class Section<H extends BaseHeaderItem, I extends BaseItem> {
    @Nullable private H mHeader;
    @NonNull private ArrayList<I> mItems;

    private boolean mHasHeader;

    public Section(@Nullable H sectionHeader, @NonNull ArrayList<I> items) {
        if (sectionHeader != null) {
            mHeader = sectionHeader;
            mHasHeader = true;
        }

        mItems = items;
    }

    @Nullable
    public H getSectionHeader() {
        return mHeader;
    }

    protected void addItem(I item) {
        mItems.add(item);
    }

    protected void addItem(int index, I item) {
        mItems.add(index, item);
    }

    protected void replaceItem(int index, I itemToReplaceWith) {
        mItems.set(index, itemToReplaceWith);
    }

    protected I removeItem(int index) {
        return mItems.remove(index);
    }

    public I getItem(int index) {
        return mItems.get(index);
    }

    public int getItemCount() {
        return mItems.size();
    }

    public boolean hasHeader() {
        return mHasHeader;
    }

    protected void clearItems() {
        mItems.clear();
    }
}
