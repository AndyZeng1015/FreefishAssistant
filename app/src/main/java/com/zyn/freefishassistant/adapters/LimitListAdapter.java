package com.zyn.freefishassistant.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SwitchCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.zyn.freefishassistant.R;
import com.zyn.freefishassistant.beans.LimitMapBean;
import com.zyn.freefishassistant.utils.Contast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * 文件名 LimitListAdapter
 * 描述
 * 版本信息，版本号 v1.0
 * 创建日期 2017/2/11
 * 版权声明 Created by ZengYinan
 */

public class LimitListAdapter extends RecyclerView.Adapter<LimitListAdapter.LimitViewHolder> {

    private List<HashMap<String, String>> searchData = new ArrayList<HashMap<String, String>>();
    private Context mContext;

    private OnClickListener mOnClickListener;
    private OnLongClickListener mOnLongClickListener;


    public void updateData(List<HashMap<String, String>> searchData){
        this.searchData = searchData;
        this.notifyDataSetChanged();
    }

    public void setOnClickListener(OnClickListener onClickListener) {
        mOnClickListener = onClickListener;
    }

    public void setOnLongClickListener(OnLongClickListener onLongClickListener) {
        mOnLongClickListener = onLongClickListener;
    }

    public LimitListAdapter(Context context, List<HashMap<String, String>> data) {
        this.searchData = data;
        mContext = context;
    }

    @Override
    public LimitViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_limit_manager, null);
        return new LimitViewHolder(view);
    }

    @Override
    public void onBindViewHolder(LimitViewHolder holder, int position) {
        holder.setData(position, searchData.get(position));
    }

    @Override
    public int getItemCount() {
        return searchData.size();
    }

    class LimitViewHolder extends RecyclerView.ViewHolder {
        private TextView tv_keyword;
        private TextView tv_price;
        private TextView tv_city;
        private SwitchCompat sc_is_show;

        public LimitViewHolder(View itemView) {
            super(itemView);
            tv_keyword = (TextView) itemView.findViewById(R.id.tv_keyword);
            tv_price = (TextView) itemView.findViewById(R.id.tv_price);
            tv_city = (TextView) itemView.findViewById(R.id.tv_city);
            sc_is_show = (SwitchCompat) itemView.findViewById(R.id.sc_is_show);

        }

        public void setData(final int position, HashMap<String, String> stringStringHashMap) {
            //关键字
            tv_keyword.setText(stringStringHashMap.get("q"));

            //价格
            String start = "";
            String end = "";
            if(stringStringHashMap.get("start") == null || stringStringHashMap.get("start").equals("")){
                start = "0";
            }else{
                start = stringStringHashMap.get("start");
            }

            if(stringStringHashMap.get("end") == null || stringStringHashMap.get("end").equals("")){
                end = "~";
            }else{
                end = stringStringHashMap.get("end");
            }

            tv_price.setText("价格区间："+start+"-"+end);

            //城市
            if(stringStringHashMap.get("divisionId") == null || stringStringHashMap.get("divisionId").equals("")){
                tv_city.setVisibility(View.GONE);
            }else{
                tv_city.setText(stringStringHashMap.get("divisionId"));
            }

            //是否开启
            if(stringStringHashMap.get("is_Show").equals("显示")){
                sc_is_show.setChecked(true);
            }else if(stringStringHashMap.get("is_Show").equals("不显示")){
                sc_is_show.setChecked(false);
            }

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnClickListener.onClick(position);
                }
            });

            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    mOnLongClickListener.onLongClick(position);
                    return true;
                }
            });

            sc_is_show.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    for(LimitMapBean limitMapBean : Contast.searchData.get(position).getLimitMapBeen()){
                        if(limitMapBean.getKey().equals("is_Show")){
                            if(isChecked){
                                limitMapBean.setValue("显示");
                            }else{
                                limitMapBean.setValue("不显示");
                            }
                        }
                    }
                }
            });
        }
    }

    public interface OnClickListener{
        void onClick(int pos);
    }

    public interface OnLongClickListener{
        void onLongClick(int pos);
    }
}
