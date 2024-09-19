package com.ljkj.screenremote.ui.settings.fragment;

import android.content.Intent;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.blankj.utilcode.util.AppUtils;
import com.blankj.utilcode.util.ClickUtils;
import com.blankj.utilcode.util.SPUtils;
import com.hjq.toast.Toaster;
import com.ljkj.lib_common.base.fragment.BaseFragment;
import com.ljkj.lib_common.common.Constants;
import com.ljkj.screenremote.R;
import com.ljkj.screenremote.databinding.FragmentLanguageBinding;
import com.ljkj.screenremote.ui.settings.contarct.LanguageFragmentContract;
import com.ljkj.screenremote.ui.settings.presenter.LanguageFragmentPresenter;

/**
 * 作者: fzy
 * 日期: 2024/9/19
 * 描述:
 */
public class LanguageFragment extends BaseFragment<LanguageFragmentPresenter, FragmentLanguageBinding> implements LanguageFragmentContract.LanguageView {

    public static final String TAG = LanguageFragment.class.getSimpleName();

    //默认跟随系统
    private String selectedLanguage = "FS";

    @Override
    protected FragmentLanguageBinding getViewBinding(@NonNull LayoutInflater inflater, @Nullable ViewGroup container) {
        return FragmentLanguageBinding.inflate(inflater, container, false);
    }

    @Override
    protected void initView() {
        super.initView();
        String language = SPUtils.getInstance().getString(Constants.SP_KEY_LANGUAGE, Constants.SP_DEFAULT_LANGUAGE);
        updateLanguageSelection(language);
    }

    @Override
    protected void initData() {
        super.initData();
    }

    @Override
    protected void initListener() {
        setLanguageClickListener(binding.relCn, "CN");
        setLanguageClickListener(binding.relEn, "EN");
        setLanguageClickListener(binding.relWy, "UG");
        setLanguageClickListener(binding.relKo, "KO");
        setLanguageClickListener(binding.relThai, "TH");
        setLanguageClickListener(binding.relFrench, "FRENCH");
        setLanguageClickListener(binding.relPt, "PT");
        setLanguageClickListener(binding.relFollowSystem, "FS");
        setLanguageClickListener(binding.relTr, "TR");

        binding.btnSave.setOnClickListener(new ClickUtils.OnDebouncingClickListener() {
            @Override
            public void onDebouncingClick(View v) {
                SPUtils.getInstance().put(Constants.SP_KEY_LANGUAGE, selectedLanguage);
                restartApp();
            }
        });
    }

    private void setLanguageClickListener(View view, String language) {
        view.setOnClickListener(v -> {
            updateLanguageSelection(language);
            selectedLanguage = language;
        });
    }

    private void updateLanguageSelection(String language) {

        binding.cbFollowSystem.setChecked(false);
        binding.cbCn.setChecked(false);
        binding.cbEn.setChecked(false);
        binding.cbWy.setChecked(false);
        binding.cbKo.setChecked(false);
        binding.cbThai.setChecked(false);
        binding.cbFrench.setChecked(false);
        binding.cbPt.setChecked(false);
        binding.cbTr.setChecked(false);
        binding.cbEs.setChecked(false);
        binding.cbIt.setChecked(false);
        binding.cbRu.setChecked(false);

        switch (language) {
            case "FS":
                binding.cbFollowSystem.setChecked(true);
                break;
            case "CN":
                binding.cbCn.setChecked(true);
                break;
            case "EN":
                binding.cbEn.setChecked(true);
                break;
            case "UG":
                binding.cbWy.setChecked(true);
                break;
            case "KO":
                binding.cbKo.setChecked(true);
                break;
            case "TH":
                binding.cbThai.setChecked(true);
                break;
            case "FRENCH":
                binding.cbFrench.setChecked(true);
                break;
            case "PT":
                binding.cbPt.setChecked(true);
                break;
            case "TR":
                binding.cbTr.setChecked(true);
                break;
            case "ES":
                binding.cbEs.setChecked(true);
                break;
            case "IT":
                binding.cbIt.setChecked(true);
                break;
            case "RU":
                binding.cbRu.setChecked(true);
                break;
        }
    }

    Handler handler = new Handler();

    public void restartApp() {
        Toaster.show(getString(R.string.reStartApp));
        //重启应用
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                AppUtils.relaunchApp();
            }
        }, 2000);
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (handler != null) {
            handler = null;
        }
    }
}

