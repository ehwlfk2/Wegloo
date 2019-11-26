package com.example.target_club_in_donga.calendar.ui.adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.example.target_club_in_donga.R;
import com.example.target_club_in_donga.calendar.CalendarDay;
import com.example.target_club_in_donga.calendar.ui.viewmodel.CalendarHeaderViewModel;
import com.example.target_club_in_donga.calendar.ui.viewmodel.CalendarViewModel;
import com.example.target_club_in_donga.calendar.ui.viewmodel.EmptyViewModel;
import com.example.target_club_in_donga.databinding.CalendarHeaderBinding;
import com.example.target_club_in_donga.databinding.DayItemBinding;
import com.example.target_club_in_donga.databinding.EmptyDayBinding;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


public class CalendarAdapter extends RecyclerView.Adapter {
    private final int HEADER_TYPE = 0;
    private final int EMPTY_TYPE = 1;
    private final int DAY_TYPE = 2;

    private List<Object> mCalendarList;
    private Context context;

    // 생성자
    public CalendarAdapter(ArrayList<Object> calendarList, Context context) {
        this.mCalendarList = calendarList;
        this.context = context; // Toast 를 위해서
    }


    public void setCalendarList(ArrayList<Object> calendarList) {
        mCalendarList = calendarList;
        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) { //뷰타입 나누기
        Object item = mCalendarList.get(position);
        if (item instanceof Long) {
            return HEADER_TYPE; //날짜 타입
        } else if (item instanceof String) {
            return EMPTY_TYPE; // 비어있는 일자 타입
        } else {
            return DAY_TYPE;
        }
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == HEADER_TYPE) { // 날짜 타입
            CalendarHeaderBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.item_calendar_header, parent, false);
            StaggeredGridLayoutManager.LayoutParams params = (StaggeredGridLayoutManager.LayoutParams) binding.getRoot().getLayoutParams();
            params.setFullSpan(true); //Span을 하나로 통합하기
            binding.getRoot().setLayoutParams(params);
            return new HeaderViewHolder(binding);
        } else if (viewType == EMPTY_TYPE) { //비어있는 일자 타입
            EmptyDayBinding binding =
                    DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.item_day_empty, parent, false);
            return new EmptyViewHolder(binding);
        } else { //(viewType == DAY_TYPE)
            DayItemBinding binding =
                    DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.item_day, parent, false);// 일자 타입
            return new DayViewHolder(binding);
        }

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {
        int viewType = getItemViewType(position);

        if (viewType == HEADER_TYPE) { // 날짜 타입 꾸미기
            HeaderViewHolder holder = (HeaderViewHolder) viewHolder;
            Object item = mCalendarList.get(position);
            CalendarHeaderViewModel model = new CalendarHeaderViewModel();
            if (item instanceof Long) {
                model.setHeaderDate((Long) item);
            }
            holder.setViewModel(model);
        } else if (viewType == EMPTY_TYPE) { // 비어있는 날짜 타입 꾸미기(요일)
            EmptyViewHolder holder = (EmptyViewHolder) viewHolder;
            Object item = mCalendarList.get(position);
            EmptyViewModel model = new EmptyViewModel();
            if (item instanceof String) {
                model.setDayOfWeekData((String) item);
            }
            holder.setViewModel(model);
        } else if (viewType == DAY_TYPE) { // 일자 타입 꾸미기
            DayViewHolder holder = (DayViewHolder) viewHolder;
            Object item = mCalendarList.get(position);
            final CalendarViewModel model = new CalendarViewModel();
            if (item instanceof Calendar) {
                model.setCalendar((Calendar) item);
                model.setClickCheck(false);

                //click event
                viewHolder.itemView.setOnClickListener(v -> {
                    model.setClickCheck(!model.getClickCheck());
                    if (model.getClickCheck()) {
                        Log.v("develop_Log_v", "model.getCalendar" + model.getCalendar());
                        Toast.makeText(context, "Click check: True", Toast.LENGTH_SHORT).show();
                        //v.setBackgroundResource(R.drawable.calendar_circle);
                    } else {
                        Toast.makeText(context, "Click check: False", Toast.LENGTH_SHORT).show();
                        //v.setBackground(null);
                    }

                    // move activity screen
                    Intent intent = new Intent(v.getContext(), CalendarDay.class);
                    intent.putExtra("timestamp", model.getTimeStamp());
                    context.startActivity(intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                });
            }
            holder.setViewModel(model);
        }

        /*
        // click event
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Log.v("develop_Log", "Click check!!!");
                Toast.makeText(context
                        , "Click check", Toast.LENGTH_SHORT).show();
                if (v.getBackground() == null) {
                    v.setBackgroundResource(R.drawable.calendar_circle);
                } else {
                    v.setBackground(null);
                }

                //v.getParent().
            }
        });
        */
    }

    @Override
    public int getItemCount() {
        if (mCalendarList != null) {
            return mCalendarList.size();
        }
        return 0;
    }


    private class HeaderViewHolder extends RecyclerView.ViewHolder { //헤더 타입 ViewHolder
        private CalendarHeaderBinding binding;

        private HeaderViewHolder(@NonNull CalendarHeaderBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        private void setViewModel(CalendarHeaderViewModel model) {
            binding.setModel(model);
            binding.executePendingBindings();
        }
    }


    private class EmptyViewHolder extends RecyclerView.ViewHolder { // 비어있는 날짜 타입 ViewHolder
        private EmptyDayBinding binding;

        private EmptyViewHolder(@NonNull EmptyDayBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        private void setViewModel(EmptyViewModel model) {
            binding.setModel(model);
            binding.executePendingBindings();
        }
    }

    private class DayViewHolder extends RecyclerView.ViewHolder {// 날짜 타입 ViewHolder
        private DayItemBinding binding;

        private DayViewHolder(@NonNull DayItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        private void setViewModel(CalendarViewModel model) {
            binding.setModel(model);
            binding.executePendingBindings();
        }
    }
}

