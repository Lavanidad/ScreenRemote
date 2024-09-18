package com.ljkj.lib_common.base.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewbinding.ViewBinding;


/**
 * 作者: fzy
 * 日期: 2024/9/5
 */
public abstract class AbstractBaseFragment<VB extends ViewBinding> extends Fragment {

    protected VB binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = getViewBinding(inflater, container);
        initView();
        initData();
        initListener();
        return binding.getRoot();
    }

    protected abstract VB getViewBinding(@NonNull LayoutInflater inflater, @Nullable ViewGroup container);

    protected abstract void initView();

    protected abstract void initData();

    protected abstract void initListener();

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
