package thegoodkid.common.utilsdemo;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.RecyclerView;

import com.microsoft.fluentui.listitem.ListItemDivider;
import com.microsoft.fluentui.listitem.ListItemView;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedList;

import thegoodkid.common.utils.BuildConfig;
import thegoodkid.common.utilsdemo.databinding.ActivityDemoListBinding;
import thegoodkid.common.utilsdemo.databinding.FragmentDemoSearchBinding;
import thegoodkid.common.utilsdemo.utilis.list.Item;
import thegoodkid.common.utilsdemo.utilis.list.ItemSection;
import thegoodkid.common.utilsdemo.utilis.list.ListItemAdapter;

public class DemoListActivity extends BaseAppActivity implements SearchView.OnQueryTextListener {
    private static final String SEARCH_FRAGMENT_TAG = "demo_search_fragment";
    private ActivityDemoListBinding binding;
    private DemoListAdapter adapter;
    private Handler queryHandler;
    private LinkedList<Runnable> queries = new LinkedList<>();

    private static void openDemoActivity(Context context, @NonNull Demo demo) {
        Intent intent = new Intent(context, demo.activityClass);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDemoListBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setupAppbar();

        adapter = new DemoListAdapter();
        binding.listContainer.addItemDecoration(new ListItemDivider(this, DividerItemDecoration.VERTICAL));
        binding.listContainer.setAdapter(adapter);
    }

    private void setupAppbar() {
        binding.appBar.getToolbar().setSubtitle(BuildConfig.VERSION_NAME);
        setSupportActionBar(binding.appBar.getToolbar());

        queryHandler = new Handler(getMainLooper());

        binding.searchbar.setOnCloseListener(() -> {
            FragmentManager manager = getSupportFragmentManager();
            Fragment fragment = manager.findFragmentByTag(SEARCH_FRAGMENT_TAG);
            if (fragment != null) {
                FragmentTransaction transaction = manager.beginTransaction();
                transaction.remove(fragment);
                transaction.commit();
            }

            return true;
        });

        binding.searchbar.setOnQueryTextListener(this);

        binding.searchbar.setOnQueryTextFocusChangeListener((view, b) -> {
            if (b) {
                FragmentManager manager = getSupportFragmentManager();
                if (manager.findFragmentByTag(SEARCH_FRAGMENT_TAG) != null) return;

                DemoSearchFragment fragment = new DemoSearchFragment();

                FragmentTransaction transaction = manager.beginTransaction();
                transaction.replace(binding.searchContainer.getId(), fragment, SEARCH_FRAGMENT_TAG);
                transaction.commit();
            }
        });
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        binding.searchbar.setShowSearchProgress(true);
        clearPendingQueries();

        Runnable query = () -> {
            Fragment fragment = getSupportFragmentManager().findFragmentByTag(SEARCH_FRAGMENT_TAG);
            if (!(fragment instanceof DemoSearchFragment)) return;

            ((DemoSearchFragment) fragment).setQueryText(newText);
            binding.searchbar.setShowSearchProgress(false);
        };

        queries.addLast(query);
        queryHandler.postDelayed(query, 500);

        return true;
    }

    private void clearPendingQueries() {
        int count = queries.size();

        for (; count > 0; count--) {
            Runnable r = queries.pollFirst();
            if (r != null) queryHandler.removeCallbacks(r);
        }
    }

    @Override
    public void onBackPressed() {
        FragmentManager manager = getSupportFragmentManager();
        Fragment fragment = manager.findFragmentByTag(SEARCH_FRAGMENT_TAG);

        if (fragment == null) super.onBackPressed();
        else manager.beginTransaction().remove(fragment).commit();
    }

    protected enum Demo {
        LIST_ITEM("List Item", R.layout.activity_list_item, ListItemActivity.class),
        NUMBER_UTILS("Number Utils", R.layout.activity_number_utils, NumberUtilsActivity.class),
        SYSTEM_UTILS("System Utils", R.layout.activity_system_utils, SystemUtilsActivity.class),
        CALENDAR_UTILS("Calendar Utils", R.layout.activity_calendar_utils, CalendarUtilsActivity.class);

        public final String title;
        public final int layout;
        protected final Class<? extends AppCompatActivity> activityClass;

        Demo(String title, @LayoutRes int layout, Class<? extends AppCompatActivity> activity) {
            this.title = title;
            this.layout = layout;
            activityClass = activity;
        }
    }

    private enum SectionKey {
        DEFAULT
    }

    public static class DemoSearchFragment extends Fragment {
        private static final String QUERY_TEXT = "query_text";

        private FragmentDemoSearchBinding binding;
        private ListItemAdapter<SectionKey> adapter;
        private String queryText;

        public DemoSearchFragment() {

        }

        @Override
        public void onCreate(@Nullable Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);

            LinkedHashMap<SectionKey, ItemSection> sectionMap = new LinkedHashMap<>();
            sectionMap.put(SectionKey.DEFAULT, new ItemSection(null, new ArrayList<>()));
            adapter = new ListItemAdapter<>(requireContext(), sectionMap);
        }

        @Nullable
        @Override
        public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            binding = FragmentDemoSearchBinding.inflate(inflater, container, false);
            binding.container.addItemDecoration(new ListItemDivider(requireContext(), DividerItemDecoration.VERTICAL));
            binding.container.setAdapter(adapter);

            if (savedInstanceState != null) {
                String query = savedInstanceState.getString(QUERY_TEXT);
                if (query != null) setQueryText(query);
            }

            return binding.getRoot();
        }

        @Override
        public void onSaveInstanceState(@NonNull Bundle outState) {
            super.onSaveInstanceState(outState);

            outState.putString(QUERY_TEXT, queryText);
        }

        public void setQueryText(@NonNull String query) {
            queryText = query;
            updateUi();
        }

        private void updateUi() {
            if (queryText == null || queryText.length() == 0) return;

            ArrayList<Item> items = new ArrayList<>();

            String regex = "[^a-z0-9]";
            String against = queryText.toLowerCase().replaceAll(regex, "");

            for (Demo demo : Demo.values()) {
                if (demo.title.toLowerCase().replaceAll(regex, "").contains(against)) {
                    items.add(new Item(demo.title)
                            .setOnClickListener(view -> openDemoActivity(requireContext(), demo)));
                }
            }

            adapter.clearSectionItems(SectionKey.DEFAULT);
            adapter.addItems(SectionKey.DEFAULT, items);
        }
    }

    private class DemoListAdapter extends RecyclerView.Adapter<DemoListAdapter.ViewHolder> {
        private final int ITEM_COUNT = Demo.values().length;

        @NonNull
        @Override
        public DemoListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new ViewHolder(new ListItemView(DemoListActivity.this));
        }

        @Override
        public void onBindViewHolder(@NonNull DemoListAdapter.ViewHolder holder, int position) {
            final Demo demo = Demo.values()[position];

            RecyclerView.LayoutParams layoutParams = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            holder.itemView.setLayoutParams(layoutParams);

            holder.itemView.setTitle(demo.title);
            holder.itemView.setOnClickListener(view -> openDemoActivity(DemoListActivity.this, demo));
        }

        @Override
        public int getItemCount() {
            return ITEM_COUNT;
        }

        private class ViewHolder extends RecyclerView.ViewHolder {
            ListItemView itemView;

            public ViewHolder(@NonNull ListItemView itemView) {
                super(itemView);

                this.itemView = itemView;
            }
        }
    }
}
