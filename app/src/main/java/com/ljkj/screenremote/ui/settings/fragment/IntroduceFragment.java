package com.ljkj.screenremote.ui.settings.fragment;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.ljkj.lib_common.base.fragment.BaseFragment;
import com.ljkj.screenremote.databinding.FragmentIntroduceBinding;
import com.ljkj.screenremote.ui.settings.contarct.IntroduceFragmentContract;
import com.ljkj.screenremote.ui.settings.presenter.IntroduceFragmentPresenter;

/**
 * 作者: fzy
 * 日期: 2024/9/19
 * 描述:
 */
public class IntroduceFragment extends BaseFragment<IntroduceFragmentPresenter, FragmentIntroduceBinding> implements IntroduceFragmentContract.IntroduceView {

    public static final String TAG = LogFragment.class.getSimpleName();

    @Override
    protected FragmentIntroduceBinding getViewBinding(@NonNull LayoutInflater inflater, @Nullable ViewGroup container) {
        return FragmentIntroduceBinding.inflate(inflater, container, false);
    }

    @Override
    protected void initView() {
        super.initView();
    }

    @Override
    protected void initData() {
        super.initData();
    }

    @Override
    protected void initListener() {
        super.initListener();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}