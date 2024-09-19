package com.ljkj.screenremote.ui.settings;

import androidx.fragment.app.FragmentTransaction;

import com.ljkj.lib_common.base.activity.BaseActivity;
import com.ljkj.screenremote.R;
import com.ljkj.screenremote.databinding.ActivitySettingBinding;
import com.ljkj.screenremote.ui.settings.fragment.LanguageFragment;

/**
 * 作者: fzy
 * 日期: 2024/9/19
 * 描述:
 */
public class SettingsActivity extends BaseActivity<SettingsPresenter, ActivitySettingBinding> implements SettingsContract.SettingsView {

    private final String TAG = SettingsActivity.class.getSimpleName();

    private LanguageFragment languageFragment;
//    private FragmentLog fragmentLog;
//    private FragmentOperation fragmentOperation;
//    private FragmentUpgrade fragmentUpgrade;
//    private FragmentProduce fragmentProduce;

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
//            else if (checkedId == R.id.rb_log) {
//                initFragmentLog();
//            } else if (checkedId == R.id.rb_upgrade) {
//                initFragmentUpgrade();
//            } else if (checkedId == R.id.rb_produce) {
//                initFragmentProduce();
//            } else if (checkedId == R.id.rb_operation) {
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
//        if (fragmentLog == null) {
//            fragmentLog = new FragmentLog();
//            transaction.add(R.id.framelayout_right, fragmentLog);
//        }
//        if (fragmentOperation == null) {
//            fragmentOperation = new FragmentOperation();
//            transaction.add(R.id.framelayout_right, fragmentOperation);
//        }
//        if (fragmentUpgrade == null) {
//            fragmentUpgrade = new FragmentUpgrade();
//            transaction.add(R.id.framelayout_right, fragmentUpgrade);
//        }
//        if (fragmentProduce == null) {
//            fragmentProduce = new FragmentProduce();
//            transaction.add(R.id.framelayout_right, fragmentProduce);
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

//    private void initFragmentLog() {
//        androidx.fragment.app.FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
//        hideFragment(transaction);
//        transaction.show(fragmentLog);
//        transaction.commit();
//    }
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
//    private void initFragmentProduce() {
//        androidx.fragment.app.FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
//        hideFragment(transaction);
//        transaction.show(fragmentProduce);
//        transaction.commit();
//    }

    private void hideFragment(FragmentTransaction transaction) {
        if (languageFragment != null) {
            transaction.hide(languageFragment);
        }
//        if (fragmentLog != null) {
//            transaction.hide(fragmentLog);
//        }
//        if (fragmentOperation != null) {
//            transaction.hide(fragmentOperation);
//        }
//        if (fragmentProduce != null) {
//            transaction.hide(fragmentProduce);
//        }
//        if (fragmentUpgrade != null) {
//            transaction.hide(fragmentUpgrade);
//        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
