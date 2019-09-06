package com.example.target_club_in_donga.Schedule;

import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ScheduleAdapter extends RecyclerView.Adapter {
    private final int HEADER_TYPE = 0;
    // 현재 제거한 상태 --> 이전 달의 기록을 불러올 겁니다
    private final int EMPTY_TYPE = 1;
    private final int DAY_TYPE = 2;

    private List<Object> mScheduleList;

    public  ScheduleAdapter(List<Object> ScheduleList){ mScheduleList = ScheduleList; }

    public void setScheduleList(List<Object> scheduleList){
        mScheduleList = scheduleList;
        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position){   // 뷰타입 나누기

        return DAY_TYPE;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }
}
