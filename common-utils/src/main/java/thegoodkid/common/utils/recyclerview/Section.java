package thegoodkid.common.utils.recyclerview;

import java.util.ArrayList;

public class Section {
    private HeaderItem mHeader;
    private ArrayList<BaseItem> mItems;

    private boolean mHasHeader;

    public Section(HeaderItem sectionHeader, ArrayList<BaseItem> items) {
        if (sectionHeader != null) {
            mHeader = sectionHeader;
            mHasHeader = true;
        }

        mItems = items;
    }

    HeaderItem getSectionHeader() {
        return mHeader;
    }

    void addItem(BaseItem item) {
        mItems.add(item);
    }

    void addItem(int index, BaseItem item) {
        mItems.add(index, item);
    }

    protected BaseItem removeItem(int index) {
        return mItems.remove(index);
    }

    public BaseItem getItem(int index) {
        return mItems.get(index);
    }

    public int getItemCount() {
        return mItems.size();
    }

    public boolean hasHeader() {
        return mHasHeader;
    }

    public void clearItems() {
        mItems.clear();
    }
}
