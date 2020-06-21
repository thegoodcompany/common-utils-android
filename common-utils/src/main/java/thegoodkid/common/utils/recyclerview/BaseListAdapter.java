package thegoodkid.common.utils.recyclerview;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedHashMap;

public abstract class BaseListAdapter<K extends Enum<K>, V extends Section,
        VH extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<VH> {

    private LinkedHashMap<K, V> mSectionMap;
    private OnLoadMoreListener<K> mOnLoadMoreListener;

    private boolean mShouldRespectSectionOrder;

    public BaseListAdapter(LinkedHashMap<K, V> sectionMap) {
        mSectionMap = sectionMap;
    }

    @Override
    public final void onBindViewHolder(@NonNull VH holder, int position) {
        onBindViewHolder(() -> {
            if (mOnLoadMoreListener != null && position == getItemCount() - 1) {
                ArrayList<K> keys = new ArrayList<>(mSectionMap.keySet());
                mOnLoadMoreListener.onLoadMore(keys.get(keys.size() - 1));
            }
        }, holder, position);
    }

    /**
     * Override this method to update the contents of {@link androidx.recyclerview.widget.RecyclerView.ViewHolder#itemView}
     * Before the return statement, invoke the {@link AfterWards#run()} method from {@code runAfterWards}
     *
     * @param runAfterwards You must run the this prior to returning
     * @param holder        The ViewHolder which should be updated to represent the contents of the
     *                      item at the given position in the data set.
     * @param position      The position of the item within the adapter's data set.
     */
    protected abstract void onBindViewHolder(@NonNull AfterWards runAfterwards, @NonNull VH holder, int position);

    /**
     * Get view type of the item at {@code position}
     * The default implementation covers only types at {@link ViewType}
     * Consider overriding this method when more are required
     * It is also convenient to use the id resource to identify view types
     *
     * @param position Position to query
     * @return An int value representing the view type
     */
    @Override
    public int getItemViewType(int position) {
        int evaluatingPosition = 0;
        int containerSectionPosition = 0;

        for (V section : mSectionMap.values()) {
            boolean sectionHasHeader = section.hasHeader();

            evaluatingPosition += section.getItemCount() + (sectionHasHeader ? 1 : 0);

            if (evaluatingPosition > position) {
                if (containerSectionPosition == position && sectionHasHeader)
                    return ViewType.HEADER.ordinal();

                int indexAtSection = position - containerSectionPosition - (sectionHasHeader ? 1 : 0);
                BaseItem item = section.getItem(indexAtSection);

                if (item instanceof ButtonItem) return ViewType.BUTTON.ordinal();
                else return ViewType.ITEM.ordinal();
            }

            containerSectionPosition = evaluatingPosition;
        }

        throw new AssertionError("No view type found at position: " + position);
    }

    public boolean containSection(K sectionKey) {
        return mSectionMap.containsKey(sectionKey);
    }

    /**
     * Adds the given item item into the specified section
     *
     * @param key  Section key of the section where the specified item be added
     * @param item The item to be added
     * @throws NullPointerException When specified section is not present
     */
    public void addItem(K key, BaseItem item) {
        V section = mSectionMap.get(key);
        if (section != null) {
            section.addItem(item);

            notifyItemInserted(getSectionStartPosition(key) + getItemCount() - (section.hasHeader() ? 0 : 1));
        } else {
            throw generateSectionNotPresentException(key);
        }
    }

    /**
     * Add an item at the specified position of the specified section; If the specified
     * section does not exist, a log report would be sent to server when it is permitted.
     * <p>
     * Use the {@link BaseListAdapter#containSection(K)} method to know whether
     * the section exist or not
     *
     * @param key      Key of the section the provided item be added to
     * @param position The position in respect w/ the section at which the item should be
     *                 added at
     * @param item     The item to be added
     * @throws NullPointerException When specified section is not present
     */
    public void addItem(K key, int position, BaseItem item) {
        V section = mSectionMap.get(key);
        if (section != null) {
            section.addItem(position, item);

            notifyItemInserted(getSectionStartPosition(key) + position + (section.hasHeader() ? 1 : 0));
        } else {
            throw generateSectionNotPresentException(key);
        }
    }

    protected void replaceItemSilent(V section, int position, BaseItem itemToReplaceWith) {
        section.removeItem(position);
        section.addItem(position, itemToReplaceWith);
    }

    /**
     * Replaces {@code itemToReplace} with {@code itemToReplaceWith}
     *
     * @param sectionKey        Host section
     * @param itemToReplace     The item that will replace {@code itemToReplaceWith}
     * @param itemToReplaceWith The item that will be replaced by {@code itemToReplace}
     * @return Whether the item was successfully replaced or not
     * @throws NullPointerException When specified section is not present
     */
    public boolean replaceItem(K sectionKey, BaseItem itemToReplace, BaseItem itemToReplaceWith) {
        V section = mSectionMap.get(sectionKey);
        if (section == null) throw generateSectionNotPresentException(sectionKey);

        int startPos = getSectionStartPosition(sectionKey);
        int itemCount = section.getItemCount();
        for (int i = 0; i < itemCount; i++) {
            if (section.getItem(i).equals(itemToReplace)) {
                section.removeItem(i);
                section.addItem(i, itemToReplaceWith);

                notifyItemChanged(startPos + i + (section.hasHeader() ? 1 : 0));
                return true;
            }
        }

        return false;
    }

    /**
     * Replaces item at position {@code position} in the specified section
     *
     * @param sectionKey        Host section
     * @param position          Position of the old item at specified section
     * @param itemToReplaceWith The item which will replace the other
     * @throws NullPointerException When specified section is not present
     */
    public void replaceItem(K sectionKey, int position, BaseItem itemToReplaceWith) {
        V section = mSectionMap.get(sectionKey);
        if (section == null) throw generateSectionNotPresentException(sectionKey);

        replaceItemSilent(section, position, itemToReplaceWith);

        int startPos = getSectionStartPosition(sectionKey);
        int itemPos = startPos + position + (section.hasHeader() ? 1 : 0);

        notifyItemChanged(itemPos);
    }

    public void replaceAndMoveItem(K sectionKey, int oldPos, int newPos, BaseItem itemToReplaceWith) {
        V section = mSectionMap.get(sectionKey);
        if (section == null) return;

        replaceItemSilent(section, oldPos, itemToReplaceWith);

        int startPos = getSectionStartPosition(sectionKey) + (section.hasHeader() ? 1 : 0);
        int itemPosThen = startPos + oldPos;
        int itemPosNow = startPos + newPos;

        section.addItem(newPos, section.removeItem(oldPos));

        notifyItemChanged(itemPosThen);
        notifyItemMoved(itemPosThen, itemPosNow);
    }

    protected BaseItem getItem(int position) {
        int containerPosition = 0;
        int evaluatingPos = 0;

        for (Section section : mSectionMap.values()) {
            evaluatingPos += section.getItemCount() + (section.hasHeader() ? 1 : 0);

            if (evaluatingPos > position) {
                if (containerPosition == position && section.hasHeader())
                    return section.getSectionHeader();

                int itemIndex = position - containerPosition - (section.hasHeader() ? 1 : 0);
                return section.getItem(itemIndex);
            }

            containerPosition = evaluatingPos;
        }

        return null;
    }

    public void addSection(K sectionKey, V section) {
        mSectionMap.put(sectionKey, section);

        if (mShouldRespectSectionOrder) reorderSectionsSilent();

        notifyItemRangeInserted(getSectionStartPosition(sectionKey), section.getItemCount());
    }

    public void replaceSection(K sectionKey, V section) {
        V oldSection = mSectionMap.get(sectionKey);
        if (oldSection == null) throw generateSectionNotPresentException(sectionKey);

        boolean hadHeaderThen = oldSection.hasHeader();
        boolean hasHeaderNow = section.hasHeader();

        int startPos = getSectionStartPosition(sectionKey);
        int sectionItemCountThen = oldSection.getItemCount() + (hadHeaderThen ? 1 : 0);
        int sectionItemCountNow = section.getItemCount() + (hasHeaderNow ? 1 : 0);

        int itemCountThen = getItemCount();
        mSectionMap.put(sectionKey, section);
        int itemCountNow = itemCountThen + (sectionItemCountNow - sectionItemCountThen);

        if (itemCountNow > itemCountThen) {
            notifyItemRangeChanged(startPos, sectionItemCountThen);
            notifyItemRangeInserted(startPos + sectionItemCountThen, itemCountNow - itemCountThen);
        } else {
            notifyItemRangeChanged(startPos, sectionItemCountNow);
        }

        if (sectionItemCountThen > sectionItemCountNow)
            notifyItemRangeRemoved(startPos + sectionItemCountNow, sectionItemCountThen - sectionItemCountNow);
    }

    /**
     * Removes item at {@code positionAtSection} position from specified section.
     *
     * @param sectionKey        The key of the section the item should be removed from
     * @param positionAtSection The item's index at the section
     * @return Whether the item was successfully removed or not
     * @throws NullPointerException When specified section is not present
     */
    public boolean removeItemFromSection(K sectionKey, int positionAtSection) {
        V section = mSectionMap.get(sectionKey);
        if (section == null) throw generateSectionNotPresentException(sectionKey);

        if (positionAtSection < section.getItemCount()) {
            section.removeItem(positionAtSection);
            notifyItemRemoved(getSectionStartPosition(sectionKey) + positionAtSection + (section.hasHeader() ? 1 : 0));

            return true;
        }

        return false;
    }

    public boolean removeItemFromSection(K sectionKey, BaseItem item) {
        V section = mSectionMap.get(sectionKey);
        if (section == null) throw generateSectionNotPresentException(sectionKey);

        int itemCount = section.getItemCount();
        for (int i = 0; i < itemCount; i++) {
            if (section.getItem(i).equals(item)) {
                section.removeItem(i);
                notifyItemRemoved(getSectionStartPosition(sectionKey) + i + (section.hasHeader() ? 1 : 0));

                return true;
            }
        }

        return false;
    }

    public boolean removeSection(K sectionKey) {
        Section section = mSectionMap.get(sectionKey);
        if (section == null) return false;

        int startPos = getSectionStartPosition(sectionKey);
        int itemCount = getSectionItemCount(sectionKey) + (section.hasHeader() ? 1 : 0);

        mSectionMap.remove(sectionKey);

        notifyItemRangeRemoved(startPos, itemCount);
        return true;
    }

    public void clear() {
        int itemCount = getItemCount();

        mSectionMap.clear();
        notifyItemRangeRemoved(0, itemCount);
    }

    public void clearSectionItems(K sectionKey) {
        Section section = mSectionMap.get(sectionKey);
        if (section == null) throw generateSectionNotPresentException(sectionKey);

        int startPos = getSectionStartPosition(sectionKey);
        int itemCount = section.getItemCount() + (section.hasHeader() ? 1 : 0);

        section.clearItems();

        notifyItemRangeRemoved(startPos, itemCount);
    }

    /**
     * Get notified when a section needs for content
     *
     * @param listener Listener to get invoked
     */
    public void setNotifyForMoreItems(OnLoadMoreListener<K> listener) {
        mOnLoadMoreListener = listener;
    }

    public void removeNotifyForMoreItemsListener() {
        mOnLoadMoreListener = null;
    }

    protected void reorderSectionsSilent() {
        int size = mSectionMap.keySet().size();
        ArrayList<K> keyArray = new ArrayList<>(mSectionMap.keySet());
        ArrayList<V> sectionArray = new ArrayList<>(mSectionMap.values());

        boolean isAlreadySorted = true;
        int expectedOrdinal = 0;
        for (int i = 0; i < size; i++) {
            if (keyArray.get(i).ordinal() != expectedOrdinal) isAlreadySorted = false;
            expectedOrdinal++;
        }

        if (!isAlreadySorted) {
            ArrayList<Integer> currentOrder = new ArrayList<>(size);
            int[] sorted = new int[size];

            for (int i = 0; i < size; i++) {
                int ordinal = keyArray.get(i).ordinal();
                currentOrder.add(ordinal);
                sorted[i] = ordinal;
            }

            Arrays.sort(sorted);

            LinkedHashMap<K, V> sortedSectionMap = new LinkedHashMap<>();

            for (int i = 0; i < size; i++) {
                int oldIndex = currentOrder.indexOf(sorted[i]);

                K key = keyArray.get(oldIndex);
                sortedSectionMap.put(key, sectionArray.get(oldIndex));
            }

            mSectionMap = sortedSectionMap;
        }
    }

    /**
     * Specify whether the sections should be displayed as per their insertion order or
     * their enum key set ordinal
     *
     * @param shouldRespect Specify whether to respect enum key set ordinal or not
     */
    public void setRespectSectionOrder(boolean shouldRespect) {
        mShouldRespectSectionOrder = shouldRespect;

        if (shouldRespect) {
            reorderSectionsSilent();
            notifyDataSetChanged();
        }
    }

    /**
     * @return Total items including headers
     */
    @Override
    public int getItemCount() {
        int size = 0;
        for (Section section : mSectionMap.values()) {
            size += section.getItemCount() + (section.hasHeader() ? 1 : 0);
        }

        return size;
    }

    /**
     * @return Total items excluding headers but including other view types e.g button
     */
    public int getListItemCount() {
        int count = 0;
        for (Section section : mSectionMap.values()) {
            count += section.getItemCount();
        }

        return count;
    }

    public int getSectionCount() {
        return mSectionMap.size();
    }

    public int getSectionItemCount(K sectionKey) {
        Section section = mSectionMap.get(sectionKey);

        if (section == null) throw generateSectionNotPresentException(sectionKey);
        else return section.getItemCount();
    }

    protected int getSectionStartPosition(K sectionKey) {
        int containerPos = 0;
        Iterator<V> sections = mSectionMap.values().iterator();

        boolean sectionExists = false;
        for (K key : mSectionMap.keySet()) {
            if (key == sectionKey) {
                sectionExists = true;
                break;
            }

            Section section = sections.next();
            containerPos += section.getItemCount() + (section.hasHeader() ? 1 : 0);
        }

        if (sectionExists) return containerPos;
        else throw generateSectionNotPresentException(sectionKey);
    }

    protected LinkedHashMap<K, V> getSectionMap() {
        return mSectionMap;
    }

    private NullPointerException generateSectionNotPresentException(K providedKey) {
        return new NullPointerException("Section not present; couldn't complete action (provided section key: " + providedKey +
                "; present section keys: " + mSectionMap.keySet() + ")");
    }

    /**
     * ITEM view refers to regular list item view w/
     * title, subtitle, and custom view
     * SUB_HEADER represents section title and
     * BUTTON view type indicates button to trigger tasks like
     * loading more item from the section
     */
    protected enum ViewType {
        HEADER, ITEM, BUTTON
    }

    public interface OnLoadMoreListener<K> {
        void onLoadMore(K key);
    }

    protected interface AfterWards {
        void run();
    }

}