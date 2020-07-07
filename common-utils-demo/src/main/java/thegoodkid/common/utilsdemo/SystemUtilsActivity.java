package thegoodkid.common.utilsdemo;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import thegoodkid.common.utils.SystemUtils;
import thegoodkid.common.utilsdemo.databinding.ActivitySystemUtilsBinding;
import thegoodkid.common.utilsdemo.utilis.ViewUtils;

public class SystemUtilsActivity extends AppCompatActivity {
    ActivitySystemUtilsBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySystemUtilsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setupAppbar();

        binding.showKeyboardButton.setOnClickListener(this::showKeyboard);
        binding.hideKeyboardButton.setOnClickListener(this::hideKeyboard);
    }

    private void setupAppbar() {
        setSupportActionBar(binding.appBar.getToolbar());
        binding.appBar.getToolbar().setTitle(DemoListActivity.Demo.SYSTEM_UTILS.title);

        binding.appBar.getToolbar().setNavigationIcon(ViewUtils.createNavigationBackDrawable(this));
        binding.appBar.getToolbar().setNavigationOnClickListener(view -> onBackPressed());
    }

    public void showKeyboard(View v) {
        SystemUtils.showSoftKeyboard(this, binding.simpleEditText);
    }

    public void hideKeyboard(View v) {
        SystemUtils.hideSoftKeyboard(this, binding.simpleEditText);
    }
}