package com.ljkj.screenremote.ui.settings;

import androidx.fragment.app.FragmentTransaction;

import com.ljkj.lib_common.base.activity.BaseActivity;
import com.ljkj.screenremote.R;
import com.ljkj.screenremote.databinding.ActivitySettingBinding;
import com.ljkj.screenremote.ui.settings.fragment.IntroduceFragment;
import com.ljkj.screenremote.ui.settings.fragment.LanguageFragment;
import com.ljkj.screenremote.ui.settings.fragment.LogFragment;

/**
 * 作者: fzy
 * 日期: 2024/9/19
 * 描述:
 */
public class SettingsActivity extends BaseActivity<SettingsPresenter, ActivitySettingBinding> implements SettingsContract.SettingsView {

    private final String TAG = SettingsActivity.class.getSimpleName();

    private LanguageFragment languageFragment;
    private LogFragment logFragment;
//    private FragmentOperation fragmentOperation;
//    private FragmentUpgrade fragmentUpgrade;
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
            }
            else if (checkedId == R.id.rb_log) {
                initFragmentLog();
            }
//            else if (checkedId == R.id.rb_upgrade) {
//                initFragmentUpgrade();
//            }
            else if (checkedId == R.id.rb_produce) {
                initFragmentIntroduce();
            }
//            else if (checkedId == R.id.rb_operation) {
//                initFragmentOperation();
//            }
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
        if (introduceFragment == null) {
            introduceFragment = new IntroduceFragment();
            transaction.add(R.id.framelayout_right, introduceFragment);
        }
//        if (fragmentUpgrade == null) {
//            fragmentUpgrade = new FragmentUpgrade();
//            transaction.add(R.id.framelayout_right, fragmentUpgrade);
//        }
        //        if (fragmentOperation == null) {
//            fragmentOperation = new FragmentOperation();
//            transaction.add(R.id.framelayout_right, fragmentOperation);
//        }

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
//
//    private void initFragmentOperation() {
//        androidx.fragment.app.FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
//        hideFragment(transaction);
//        transaction.show(fragmentOperation);
//        transaction.commit();
//    }
//
//    private void initFragmentUpgrade() {
//        androidx.fragment.app.FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
//        hideFragment(transaction);
//        transaction.show(fragmentUpgrade);
//        transaction.commit();
//    }
//
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
//        if (fragmentOperation != null) {
//            transaction.hide(fragmentOperation);
//        }
        if (introduceFragment != null) {
            transaction.hide(introduceFragment);
        }
//        if (fragmentUpgrade != null) {
//            transaction.hide(fragmentUpgrade);
//        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
