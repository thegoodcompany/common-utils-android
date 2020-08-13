package thegoodcompany.common.utilsdemo;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;

import androidx.coordinatorlayout.widget.CoordinatorLayout;

import com.google.android.material.appbar.AppBarLayout;

import thegoodcompany.common.utilsdemo.databinding.ActivityDemoBinding;
import thegoodcompany.common.utilsdemo.utilis.ViewUtils;

public abstract class DemoActivity extends BaseAppActivity {
    private ActivityDemoBinding binding;
    private View contentView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDemoBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setupAppbar();

        CoordinatorLayout.LayoutParams params = new CoordinatorLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        params.setBehavior(new AppBarLayout.ScrollingViewBehavior());

        contentView = getLayoutInflater().inflate(getDemo().layout, binding.getRoot(), false);
        binding.getRoot().addView(contentView, params);
    }

    private void setupAppbar() {
        setSupportActionBar(binding.appBar.getToolbar());

        binding.appBar.getToolbar().setTitle(getDemo().title);
        binding.appBar.getToolbar().setNavigationIcon(ViewUtils.createNavigationBackDrawable(this));
        binding.appBar.getToolbar().setNavigationOnClickListener(view -> onBackPressed());
    }

    protected void expandAppbar() {
        binding.appBar.setExpanded(true, true);
    }

    protected void collapseAppbar() {
        binding.appBar.setExpanded(false, true);
    }

    protected View getContentView() {
        return contentView;
    }

    protected abstract DemoListActivity.Demo getDemo();
}