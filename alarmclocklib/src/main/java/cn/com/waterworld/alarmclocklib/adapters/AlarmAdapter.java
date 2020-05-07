package cn.com.waterworld.alarmclocklib.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ToggleButton;

import java.util.List;

import cn.com.waterworld.alarmclocklib.R;
import cn.com.waterworld.alarmclocklib.interfaces.OnSwithChangeListener;
import cn.com.waterworld.alarmclocklib.model.AlarmBean;
import cn.com.waterworld.alarmclocklib.util.LogUtil;
import cn.com.waterworld.alarmclocklib.util.StringUtils;
import cn.com.waterworld.alarmclocklib.util.Unit;

/**
 * Created by wangfeng on 2018/6/11.
 */
public class AlarmAdapter extends RecyclerView.Adapter<AlarmAdapter.AlarmViewHolder> {

    private Context mContext;
    private List<AlarmBean> mDatas;
    private OnItemClickListener onItemListener;

    private OnSwithChangeListener swithChangeListener;

    public AlarmAdapter(Context context, List<AlarmBean> mDatas) {
        this.mContext = context;
        this.mDatas = mDatas;
    }


    public void addData(List<AlarmBean> mList) {
        if (mList.size() > 0) {
            mDatas.clear();
            mDatas.addAll(mList);
        }
    }

    public interface OnItemClickListener {
        void OnItemClick(AlarmBean alarmBean);
    }

    public void setOnItemClickListener(OnItemClickListener onItemListener) {
        this.onItemListener = onItemListener;
    }

    public void addItem(AlarmBean alarmBean) {
        if (alarmBean != null) {
            mDatas.add(alarmBean);
        }
    }

    public void setSwithChangeListener(OnSwithChangeListener swithChangeListener) {
        this.swithChangeListener = swithChangeListener;
    }

    @Override
    public AlarmAdapter.AlarmViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new AlarmAdapter.AlarmViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item_alarm_list, parent, false));
    }

    @Override
    public void onBindViewHolder(final AlarmAdapter.AlarmViewHolder holder, final int position) {
        final AlarmBean bean = mDatas.get(position);
        int Tmin = bean.getAlarmTime();
        String hour = "";
        String min = "";
        if (Tmin != 0) {
            hour = Tmin / 60 < 10 ? "0" + (Tmin / 60) : Tmin / 60 + "";
            min = Tmin % 60 < 10 ? "0" + (Tmin % 60) : Tmin % 60 + "";
        }
        holder.time_value.setText(Tmin == 0 ? "--" : hour + ":" + min);

        String weekValue = bean.getWeekValue();
        //设置选中星期几
        // TODO: 2018/6/11
        if (!StringUtils.isEmpty(weekValue)) {
            String strValue = Unit.toBinaryString2(weekValue);
            LogUtil.e("AlarmBean>>>:" + bean.toString());
            String[] str = strValue.split("");
            if (strValue.equals("00000000")) {
                holder.tvWeekTip.setText(mContext.getText(R.string.str_week_onetime));
                holder.tvWeek1.setVisibility(View.GONE);
                holder.tvWeek2.setVisibility(View.GONE);
                holder.tvWeek3.setVisibility(View.GONE);
                holder.tvWeek4.setVisibility(View.GONE);
                holder.tvWeek5.setVisibility(View.GONE);
                holder.tvWeek6.setVisibility(View.GONE);
                holder.tvWeek7.setVisibility(View.GONE);
            } else {
                holder.tvWeekTip.setText(mContext.getText(R.string.str_week_tip));
                holder.tvWeek1.setVisibility(View.VISIBLE);
                holder.tvWeek2.setVisibility(View.VISIBLE);
                holder.tvWeek3.setVisibility(View.VISIBLE);
                holder.tvWeek4.setVisibility(View.VISIBLE);
                holder.tvWeek5.setVisibility(View.VISIBLE);
                holder.tvWeek6.setVisibility(View.VISIBLE);
                holder.tvWeek7.setVisibility(View.VISIBLE);
                if (str.length > 8) {
                    if (str[2].equals("1")) {
                        holder.tvWeek1.setTextColor(mContext.getResources().getColor(R.color.item_bg_color));
                    } else {
                        holder.tvWeek1.setTextColor(mContext.getResources().getColor(R.color.deep_white));
                    }
                    if (str[3].equals("1")) {
                        holder.tvWeek2.setTextColor(mContext.getResources().getColor(R.color.item_bg_color));
                    } else {
                        holder.tvWeek2.setTextColor(mContext.getResources().getColor(R.color.deep_white));
                    }
                    if (str[4].equals("1")) {
                        holder.tvWeek3.setTextColor(mContext.getResources().getColor(R.color.item_bg_color));
                    } else {
                        holder.tvWeek3.setTextColor(mContext.getResources().getColor(R.color.deep_white));
                    }
                    if (str[5].equals("1")) {
                        holder.tvWeek4.setTextColor(mContext.getResources().getColor(R.color.item_bg_color));
                    } else {
                        holder.tvWeek4.setTextColor(mContext.getResources().getColor(R.color.deep_white));
                    }
                    if (str[6].equals("1")) {
                        holder.tvWeek5.setTextColor(mContext.getResources().getColor(R.color.item_bg_color));
                    } else {
                        holder.tvWeek5.setTextColor(mContext.getResources().getColor(R.color.deep_white));
                    }
                    if (str[7].equals("1")) {
                        holder.tvWeek6.setTextColor(mContext.getResources().getColor(R.color.item_bg_color));
                    } else {
                        holder.tvWeek6.setTextColor(mContext.getResources().getColor(R.color.deep_white));
                    }
                    if (str[8].equals("1")) {
                        holder.tvWeek7.setTextColor(mContext.getResources().getColor(R.color.item_bg_color));
                    } else {
                        holder.tvWeek7.setTextColor(mContext.getResources().getColor(R.color.deep_white));
                    }
                }
            }
        }

        holder.switch_alarm.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isSwitchOn) {
                if (swithChangeListener != null) {
                    if (isSwitchOn) {
                        if (bean.getIsOff() != 0) {
                            bean.setIsOff(0);
                            holder.item_container.setBackgroundColor(mContext.getResources().getColor(R.color.gray));
                            swithChangeListener.OnChange(bean, isSwitchOn);
                        }
                    } else {
                        if (bean.getIsOff() != 1) {
                            bean.setIsOff(1);
                            holder.item_container.setBackgroundColor(mContext.getResources().getColor(R.color.gray));
                            swithChangeListener.OnChange(bean, isSwitchOn);
                        }
                    }
                }
            }
        });

        holder.item_container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemListener.OnItemClick(bean);
            }
        });
        if (mDatas.get(position).getIsOff() == 0) {
            holder.switch_alarm.setChecked(true);
            holder.item_container.setBackgroundColor(mContext.getResources().getColor(R.color.gray));
        } else {
            holder.switch_alarm.setChecked(false);
            holder.item_container.setBackgroundColor(mContext.getResources().getColor(R.color.gray));
        }

    }

    public List<AlarmBean> getListData() {
        if (mDatas != null && mDatas.size() > 0) {
            return mDatas;
        }
        return null;
    }

    @Override
    public int getItemCount() {
        return mDatas == null ? 0 : mDatas.size();
    }


    class AlarmViewHolder extends RecyclerView.ViewHolder {

        RelativeLayout item_container;
        ToggleButton switch_alarm;
        TextView time_value;
        TextView tvWeekTip;
        TextView tvWeek1;
        TextView tvWeek2;
        TextView tvWeek3;
        TextView tvWeek4;
        TextView tvWeek5;
        TextView tvWeek6;
        TextView tvWeek7;

        public AlarmViewHolder(View view) {
            super(view);
            item_container = (RelativeLayout) itemView.findViewById(R.id.item_container);
            switch_alarm = (ToggleButton) itemView.findViewById(R.id.alarm_enable_view);
            time_value = (TextView) itemView.findViewById(R.id.tv_alarm_time);
            tvWeekTip = (TextView) itemView.findViewById(R.id.tv_week);
            tvWeek1 = (TextView) itemView.findViewById(R.id.tv_week1);
            tvWeek2 = (TextView) itemView.findViewById(R.id.tv_week2);
            tvWeek3 = (TextView) itemView.findViewById(R.id.tv_week3);
            tvWeek4 = (TextView) itemView.findViewById(R.id.tv_week4);
            tvWeek5 = (TextView) itemView.findViewById(R.id.tv_week5);
            tvWeek6 = (TextView) itemView.findViewById(R.id.tv_week6);
            tvWeek7 = (TextView) itemView.findViewById(R.id.tv_week7);
        }
    }

}
