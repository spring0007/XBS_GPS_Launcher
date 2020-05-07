package com.sczn.wearlauncher.task.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.sczn.wearlauncher.R;
import com.sczn.wearlauncher.task.bean.TaskInfo;
import com.sczn.wearlauncher.util.TimeUtil;

import java.util.List;

/**
 * Created by Bingo on 2019/2/21.
 */

public class TaskInfoAdapter extends RecyclerView.Adapter<TaskInfoAdapter.MyViewHolder> {
    private Context context;
    private List<TaskInfo> taskInfos;

    public TaskInfoAdapter(Context context) {
        this.context = context;
    }

    /**
     * 设置数据
     *
     * @param taskInfos
     */
    public void setData(List<TaskInfo> taskInfos) {
        if (taskInfos != null) {
            this.taskInfos = taskInfos;
            notifyDataSetChanged();
        }
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.item_taskinfo_list, parent, false));
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        holder.tvWeek.setText(TimeUtil.getWeekString(context));
        holder.tvTime.setText(taskInfos.get(position).getStartTime() + "-" + taskInfos.get(position).getEndTime());
        holder.tvContent.setText(taskInfos.get(position).getTask());
    }

    @Override
    public int getItemCount() {
        return taskInfos == null ? 0 : taskInfos.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView tvTime;
        private TextView tvWeek;
        private TextView tvContent;


        public MyViewHolder(View itemView) {
            super(itemView);
            tvTime = itemView.findViewById(R.id.tv_time);
            tvWeek = itemView.findViewById(R.id.tv_week);
            tvContent = itemView.findViewById(R.id.tv_content);
        }
    }
}
