package com.ljkj.screenremote.adapter;

import android.content.Context;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.ljkj.lib_common.bean.LogListBean;
import com.ljkj.screenremote.R;
import com.ljkj.screenremote.ui.settings.fragment.LogFragment;

public class LogListAdapter extends BaseQuickAdapter<LogListBean, BaseViewHolder> {

    private LogFragment logFragment;

    public LogListAdapter(LogFragment fragment) {
        super(R.layout.item_log_upload, null);
        this.logFragment = fragment;
    }

    @Override
    protected void convert(BaseViewHolder helper, LogListBean item) {
        TextView tv_log_name = helper.getView(R.id.tv_log_name);
        ImageView iv_select = helper.getView(R.id.iv_select);

        updateSelectIcon(iv_select, item.isSelect());

        tv_log_name.setText(item.getName());

        helper.itemView.setOnClickListener(v -> toggleSelection(item, iv_select));
        iv_select.setOnClickListener(v -> toggleSelection(item, iv_select));
    }


    private void toggleSelection(LogListBean item, ImageView iv_select) {
        item.setSelect(!item.isSelect());

        updateSelectIcon(iv_select, item.isSelect());

        logFragment.operationLogFiles(item);
    }

    private void updateSelectIcon(ImageView iv_select, boolean isSelected) {
        if (isSelected) {
            iv_select.setImageResource(R.drawable.ic_cb_selected);
        } else {
            iv_select.setImageResource(R.drawable.ic_cb_unselected);
        }
    }

}
