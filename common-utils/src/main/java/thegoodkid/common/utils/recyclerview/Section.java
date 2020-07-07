package thegoodkid.common.utils.recyclerview;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

public class Section<H extends BaseHeaderItem, I extends BaseItem> {
    @Nullable
    private H mHeader;
    @NonNull
    private List<I> mItems;

    private boolean mHasHeader;

    public Section(@Nullable H sectionHeader, @NonNull List<I> items) {
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

    protected void addItems(List<I> items) {
        mItems.addAll(items);
    }

    protected void moveItem(int oldIndex, int newIndex) {
        mItems.add(newIndex, mItems.remove(oldIndex));
    }

    protected void replaceItem(int index, I itemToReplaceWith) {
        mItems.set(index, itemToReplaceWith);
    }

    protected I removeItem(int index) {
        return mItems.remove(index);
    }

    protected boolean removeItem(I item) {
        return mItems.remove(item);
    }

    public I getItem(int index) {
        return mItems.get(index);
    }

    public int getItemIndex(I item) {
        return mItems.indexOf(item);
    }

    public int getItemCount() {
        return mItems.size();
    }

    public boolean hasHeader() {
        return mHasHeader;
    }

    public boolean hasItem(I item) {
        return mItems.contains(item);
    }

    protected void clearItems() {
        mItems.clear();
    }
}
