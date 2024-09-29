package com.ljkj.screenremote.adapter;


import android.content.Context;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.ljkj.lib_common.bean.WarnDataBean;
import com.ljkj.screenremote.R;

import java.text.SimpleDateFormat;


/**
 *
 */
public class WarningAdapter extends BaseQuickAdapter<WarnDataBean, BaseViewHolder> {

    Context mcontext;

    public WarningAdapter(Context context) {
        super(R.layout.item_warning);
        this.mcontext = context;
    }

    @Override
    protected void convert(@NonNull BaseViewHolder baseViewHolder, WarnDataBean warnDataBean) {
        TextView tvWarn = baseViewHolder.getView(R.id.tv_warn_text);
        TextView tvTime = baseViewHolder.getView(R.id.tv_time);
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        tvWarn.setText(warnDataBean.getText());
        tvTime.setText(format.format(warnDataBean.getTime()));
    }
}
