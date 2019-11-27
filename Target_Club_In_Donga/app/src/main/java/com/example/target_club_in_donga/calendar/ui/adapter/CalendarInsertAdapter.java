package com.example.target_club_in_donga.calendar.ui.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.RecyclerView;

import com.example.target_club_in_donga.R;
import com.example.target_club_in_donga.calendar.CalendarDayInsertTodo;
import com.example.target_club_in_donga.calendar.room.Todo;
import com.example.target_club_in_donga.calendar.ui.viewmodel.CalendarDayToDoViewModel;
import com.example.target_club_in_donga.calendar.ui.viewmodel.CalendarInsertViewModel;
import com.example.target_club_in_donga.databinding.DayToDoBinding;

import java.util.List;

public class CalendarInsertAdapter extends RecyclerView.Adapter {
    private final int TODO_TYPE = 0;
    private final int SCHEDULE_TYPE = 1;
    private final int TIMELINE_TYPE = 2;

    private List<Todo> mTodosList;
    private Context context;
    private Activity activity;
    private long time;

    //생성자
    public CalendarInsertAdapter(List<Todo> todos, Context context, Activity activity, long time) {
        this.mTodosList = todos;
        this.context = context;
        this.activity = activity;
        this.time = time;
    }

    public void setTodosList(List<Todo> todos) {
        mTodosList = todos;
        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {  // ViewType 나누기
        Object item = mTodosList.get(position);
        if (item instanceof Todo) {
            return TODO_TYPE;   // 할일 타입
        } else if (item instanceof String) {
            return SCHEDULE_TYPE;
        } else {
            return TIMELINE_TYPE;
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == TODO_TYPE) {
            DayToDoBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.item_todo_calendar_day, parent, false);
            return new ToDoViewHolder(binding);
        } else {

        }
        return null;
    }


    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {
        int viewType = getItemViewType(position);

        if (viewType == TODO_TYPE) {
            ToDoViewHolder holder = (ToDoViewHolder) viewHolder;
            Todo item = mTodosList.get(position);
            CalendarDayToDoViewModel model = new CalendarDayToDoViewModel();
            if (item instanceof Todo) {
                boolean bool = item.getIsChecked();
                String title = item.getTitle();
                long time = item.getTimeStamp();

                model.setTodoTitleData(title);
                model.setIsWorkChecked(bool);
                model.setTimeStampString(time);

                // OnClick
                DayToDoBinding binding = ((ToDoViewHolder) viewHolder).binding;
                binding.itemTodoCalendarIsCheckedCheckBox.setOnClickListener(v -> {
                    model.setIsWorkChecked(!bool);

                    int[] index = {item.getId()};
                    CalendarInsertViewModel insertModel = ViewModelProviders.of((FragmentActivity) activity).get(CalendarInsertViewModel.class);
                    insertModel.update(index, title, !bool);

                });
                binding.itemTodoCalendarTitleEditText.setOnClickListener(v -> {
                    Intent intent = new Intent(v.getContext(), CalendarDayInsertTodo.class);
                    intent.putExtra("DB_id", item.getId());
                    intent.putExtra("title",title);
                    intent.putExtra("checked", bool);
                    intent.putExtra("timestamp", time);
                    activity.startActivity(intent);
                    activity.finish();
                });

                if (bool) {
                    binding.itemTodoCalendarTitleEditText.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG);
                    binding.itemTodoCalendarTitleEditText.setTextColor
                            (ContextCompat.getColor(binding.itemTodoCalendarTitleEditText.getContext(), R.color.colorGray));
                } else {
                    binding.itemTodoCalendarTitleEditText.setPaintFlags(0);
                    binding.itemTodoCalendarTitleEditText.setTextColor
                            (ContextCompat.getColor(binding.itemTodoCalendarTitleEditText.getContext(), R.color.colorBlack));
                }
            } // ViewType == TO DO
            holder.setViewModel(model);
        } else {

        }
    }

    private class ToDoViewHolder extends RecyclerView.ViewHolder {
        private DayToDoBinding binding;

        private ToDoViewHolder(@NonNull DayToDoBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        private void setViewModel(CalendarDayToDoViewModel model) {
            binding.setModel(model);
            binding.executePendingBindings();
        }
    }

    @Override
    public int getItemCount() {
        if (mTodosList != null) {
            return mTodosList.size();
        }
        return 0;
    }
}
