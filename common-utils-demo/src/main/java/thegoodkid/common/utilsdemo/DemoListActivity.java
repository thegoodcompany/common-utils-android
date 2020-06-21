package thegoodkid.common.utilsdemo;

import android.content.Intent;
import android.os.Bundle;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.RecyclerView;

import com.microsoft.fluentui.listitem.ListItemDivider;
import com.microsoft.fluentui.listitem.ListItemView;

import thegoodkid.common.utilsdemo.databinding.ActivityDemoListBinding;

public class DemoListActivity extends AppCompatActivity {
    private ActivityDemoListBinding binding;
    private DemoListAdapter adapter;

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
        setSupportActionBar(binding.appBar.getToolbar());
    }

    protected enum Demos {
        NUMBER_UTILS("Number Utils", NumberUtilsActivity.class),
        SYSTEM_UTILS("System Utils", SystemUtilsActivity.class),
        LIST_ITEM("List Item", ListItemActivity.class);

        protected final String title;
        protected final Class<? extends AppCompatActivity> activityClass;

        Demos(String title, Class<? extends AppCompatActivity> activity) {
            this.title = title;
            activityClass = activity;
        }
    }

    private class DemoListAdapter extends RecyclerView.Adapter<DemoListAdapter.ViewHolder> {
        private final int ITEM_COUNT = Demos.values().length;

        @NonNull
        @Override
        public DemoListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new ViewHolder(new ListItemView(DemoListActivity.this));
        }

        @Override
        public void onBindViewHolder(@NonNull DemoListAdapter.ViewHolder holder, int position) {
            final Demos demo = Demos.values()[position];

            RecyclerView.LayoutParams layoutParams = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            holder.itemView.setLayoutParams(layoutParams);

            holder.itemView.setTitle(demo.title);
            holder.itemView.setOnClickListener(view -> {
                Intent intent = new Intent(DemoListActivity.this, demo.activityClass);
                startActivity(intent);
            });
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
