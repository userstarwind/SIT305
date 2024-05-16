package com.example.intellicareer;


import android.widget.TextView;

import com.zhpan.bannerview.BaseBannerAdapter;
import com.zhpan.bannerview.BaseViewHolder;

public class BannerAdapter extends BaseBannerAdapter<CustomBean> {
    @Override
    public void bindData(BaseViewHolder<CustomBean> holder, CustomBean data, int position, int pageSize) {
        holder.setImageResource(R.id.banner_image, data.getImageRes());
        TextView textView=holder.findViewById(R.id.tv_describe);
        textView.setText(data.getDescribe());
    }

    @Override
    public int getLayoutId(int viewType) {
        return R.layout.banner_item;
    }
}
