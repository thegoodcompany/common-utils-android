package thegoodkid.common.utilsdemo;

import android.os.Bundle;
import android.view.View;

import thegoodkid.common.utils.SystemUtils;
import thegoodkid.common.utilsdemo.databinding.ActivitySystemUtilsBinding;

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