package thegoodkid.common.utilsdemo;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;

import androidx.appcompat.app.AppCompatActivity;

import thegoodkid.common.utils.NumberUtils;
import thegoodkid.common.utils.SystemUtils;
import thegoodkid.common.utilsdemo.databinding.ActivityNumberUtilsBinding;
import thegoodkid.common.utilsdemo.utilis.ViewUtils;

public class NumberUtilsActivity extends AppCompatActivity {
    ActivityNumberUtilsBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityNumberUtilsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setupAppbar();
        init();
    }

    private void setupAppbar() {
        binding.appBar.getToolbar().setTitle(DemoListActivity.Demos.NUMBER_UTILS.title);
        setSupportActionBar(binding.appBar.getToolbar());

        binding.appBar.getToolbar().setNavigationIcon(ViewUtils.createNavigationBackDrawable(this));
        binding.appBar.getToolbar().setNavigationOnClickListener(view -> onBackPressed());
    }

    private void init() {
        binding.extractionNumberEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                Double number = NumberUtils.extractNumbers(editable.toString(), null);
                binding.extractedNumberTextView.setText(number == null ? getString(R.string.result_string_with_number) : number.toString());
            }
        });

        SystemUtils.showSoftKeyboard(this, binding.extractionNumberEditText);
    }
}