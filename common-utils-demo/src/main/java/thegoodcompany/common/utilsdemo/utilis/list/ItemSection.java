package thegoodcompany.common.utilsdemo.utilis.list;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

import thegoodcompany.common.utils.recyclerview.HeaderItem;
import thegoodcompany.common.utils.recyclerview.Section;

public class ItemSection extends Section<HeaderItem, Item> {
    public ItemSection(@Nullable HeaderItem sectionHeader, @NonNull List<Item> items) {
        super(sectionHeader, items);
    }
}
