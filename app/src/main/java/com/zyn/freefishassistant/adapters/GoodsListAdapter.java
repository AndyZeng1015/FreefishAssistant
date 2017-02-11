package com.zyn.freefishassistant.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.zyn.freefishassistant.R;
import com.zyn.freefishassistant.beans.GoodsDetailBean;
import com.zyn.freefishassistant.utils.DensityUtil;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

/**
 * 文件名 GoodsListAdapter
 * 描述
 * 版本信息，版本号 v1.0
 * 创建日期 2017/2/9
 * 版权声明 Created by ZengYinan
 */

public class GoodsListAdapter extends RecyclerView.Adapter<GoodsListAdapter.GoodsListHolderView>{

    private List<GoodsDetailBean> mGoodsDetailBeanList = new ArrayList<GoodsDetailBean>();
    private Context mContext;
    private OnItemClickListener mOnItemClickListener;

    public GoodsListAdapter(Context context, List<GoodsDetailBean> mGoodsDetailBeanList){
        this.mGoodsDetailBeanList = mGoodsDetailBeanList;
        this.mContext = context;
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        this.mOnItemClickListener = listener;
    }

    @Override
    public GoodsListHolderView onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_goods_detail, null);
        return new GoodsListHolderView(view);
    }

    @Override
    public void onBindViewHolder(GoodsListHolderView holder, int position) {
        holder.setData(mGoodsDetailBeanList.get(position), position);
    }

    @Override
    public int getItemCount() {
        return mGoodsDetailBeanList.size();
    }

    /**
     * 刷新数据
     * @param goodsDetailBeen
     */
    public void updateListDataAndRefresh(List<GoodsDetailBean> goodsDetailBeen){
        this.mGoodsDetailBeanList = goodsDetailBeen;
        this.notifyDataSetChanged();
    }

    class GoodsListHolderView extends RecyclerView.ViewHolder{

        private ImageView iv_img;
        private TextView tv_title;
        private TextView tv_price;
        private TextView tv_desc;
        private View rootView;

        public GoodsListHolderView(View itemView) {
            super(itemView);
            rootView = itemView;
            iv_img = (ImageView) itemView.findViewById(R.id.iv_img);
            tv_title = (TextView) itemView.findViewById(R.id.tv_title);
            tv_price = (TextView) itemView.findViewById(R.id.tv_price);
            tv_desc = (TextView) itemView.findViewById(R.id.tv_desc);
        }

        public void setData(final GoodsDetailBean goodsDetailBean, final int position){
            Picasso.with(mContext).load("http://"+goodsDetailBean.getList_imgUrl()).placeholder(R.drawable.icon_default).resize(DensityUtil.dip2px(mContext, 100), DensityUtil.dip2px(mContext, 100)).centerCrop().into(iv_img);
            tv_title.setText(goodsDetailBean.getTitle());
            tv_price.setText("￥"+goodsDetailBean.getPrice());
            tv_desc.setText(goodsDetailBean.getList_desc());

            rootView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mOnItemClickListener.onClick(goodsDetailBean, position);
                }
            });
        }
    }

    public interface OnItemClickListener{
        public void onClick(GoodsDetailBean goodsDetailBean, int pos);
    }
}
