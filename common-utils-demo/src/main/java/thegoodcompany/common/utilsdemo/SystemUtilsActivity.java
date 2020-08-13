package thegoodcompany.common.utilsdemo;

import android.os.Bundle;
import android.view.View;

import thegoodcompany.common.utils.SystemUtils;
import thegoodcompany.common.utilsdemo.databinding.ActivitySystemUtilsBinding;

public class SystemUtilsActivity extends DemoActivity {
    private ActivitySystemUtilsBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySystemUtilsBinding.bind(getContentView());

        binding.showKeyboardButton.setOnClickListener(this::showKeyboard);
        binding.hideKeyboardButton.setOnClickListener(this::hideKeyboard);
    }

    @Override
    protected DemoListActivity.Demo getDemo() {
        return DemoListActivity.Demo.SYSTEM_UTILS;
    }

    public void showKeyboard(View v) {
        SystemUtils.showSoftKeyboard(this, binding.simpleEditText);
    }

    public void hideKeyboard(View v) {
        SystemUtils.hideSoftKeyboard(this, binding.simpleEditText);
    }
}