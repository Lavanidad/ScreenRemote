package com.ljkj.screenremote.ui.settings;

import androidx.fragment.app.FragmentTransaction;

import com.ljkj.lib_common.base.activity.BaseActivity;
import com.ljkj.screenremote.R;
import com.ljkj.screenremote.databinding.ActivitySettingBinding;
import com.ljkj.screenremote.ui.settings.fragment.IntroduceFragment;
import com.ljkj.screenremote.ui.settings.fragment.LanguageFragment;
import com.ljkj.screenremote.ui.settings.fragment.LogFragment;
import com.ljkj.screenremote.ui.settings.fragment.UpdateFragment;

/**
 * 作者: fzy
 * 日期: 2024/9/19
 * 描述:
 */
public class SettingsActivity extends BaseActivity<SettingsPresenter, ActivitySettingBinding> implements SettingsContract.SettingsView {

    private final String TAG = SettingsActivity.class.getSimpleName();

    private LanguageFragment languageFragment;
    private LogFragment logFragment;
    private UpdateFragment updateFragment;
    private IntroduceFragment introduceFragment;

    @Override
    protected ActivitySettingBinding getBinding() {
        return ActivitySettingBinding.inflate(getLayoutInflater());
    }

    @Override
    protected void initView() {
        binding = getBinding();
        initAllFragment();
    }

    @Override
    protected void initData() {
        super.initData();
    }

    @Override
    protected void initListener() {
        binding.back.setOnClickListener(v -> finish());

        binding.rgSetting.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId == R.id.rb_language) {
                initFragmentLanguage();
            } else if (checkedId == R.id.rb_log) {
                initFragmentLog();
            } else if (checkedId == R.id.rb_update) {
                initFragmentUpdate();
            } else if (checkedId == R.id.rb_produce) {
                initFragmentIntroduce();
            }
        });
    }

    private void initAllFragment() {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        if (languageFragment == null) {
            languageFragment = new LanguageFragment();
            transaction.add(R.id.framelayout_right, languageFragment);
        }
        if (logFragment == null) {
            logFragment = new LogFragment();
            transaction.add(R.id.framelayout_right, logFragment);
        }
        if (updateFragment == null) {
            updateFragment = new UpdateFragment();
            transaction.add(R.id.framelayout_right, updateFragment);
        }
        if (introduceFragment == null) {
            introduceFragment = new IntroduceFragment();
            transaction.add(R.id.framelayout_right, introduceFragment);
        }

        hideFragment(transaction);
        transaction.show(languageFragment);
        transaction.commit();
    }

    private void initFragmentLanguage() {
        androidx.fragment.app.FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        hideFragment(transaction);
        transaction.show(languageFragment);
        transaction.commit();
    }

    private void initFragmentLog() {
        androidx.fragment.app.FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        hideFragment(transaction);
        transaction.show(logFragment);
        transaction.commit();
    }

    private void initFragmentUpdate() {
        androidx.fragment.app.FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        hideFragment(transaction);
        transaction.show(updateFragment);
        transaction.commit();
    }

    private void initFragmentIntroduce() {
        androidx.fragment.app.FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        hideFragment(transaction);
        transaction.show(introduceFragment);
        transaction.commit();
    }

    private void hideFragment(FragmentTransaction transaction) {
        if (languageFragment != null) {
            transaction.hide(languageFragment);
        }
        if (logFragment != null) {
            transaction.hide(logFragment);
        }
        if (updateFragment != null) {
            transaction.hide(updateFragment);
        }
        if (introduceFragment != null) {
            transaction.hide(introduceFragment);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
