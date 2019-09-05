package com.example.target_club_in_donga;

import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.example.target_club_in_donga.Activity_Adapters.ScheduleActivity_Adapter;
import com.example.target_club_in_donga.Fragments.HomeActivity_Fragment;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class ScheduleActivity extends AppCompatActivity implements HomeActivity_Fragment.OnFragmentInteractionListener {
    private ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_pager);
        viewPager = (ViewPager)findViewById(R.id.view_pager);
        ScheduleActivity_Adapter fragmentAdapter = new ScheduleActivity_Adapter(getSupportFragmentManager());
        viewPager.setAdapter(fragmentAdapter);

        // 홈에서 일정을 눌었을 떄, viewPager에서 ViewPagerAdapter_Schedule로 일정화면이 나오고
        // 오른쪽에서 왼쪽으로 슬라이드를 하면 홈 화면이 나오도록 한다.

        RecyclerView Day_schedule = findViewById(R.id.activity_schedule_RecyclerView_day_schedule);

        Schedule_item_Adapter adapter = new Schedule_item_Adapter(new Schedule_item_Adapter(Schedule_item_Adapter.onScheduleClickListener(){
          @Override
          public void onScheduleClickListener(ScheduleActivity model){
              Toast.makeText(ScheduleActivity.this, model.getName(), Toast.LENGTH_SHORT).show();
          }
        });
    }


    @Override
    public void onFragmentInteraction(final Uri uri) {

    }
    
    private static class Schedule_item_Adapter extends RecyclerView.Adapter<Schedule_item_Adapter.Schedule_item_ViewHolder> {
        interface onScheduleClickListener {
            void onScheduleClickListener(ScheduleActivity model);
        }
        
        private onScheduleClickListener mListener;
        
        private List<ScheduleActivity> mItems = new ArrayList<>();
    
        public Schedule_item_Adapter() {}
    
        public Schedule_item_Adapter(onScheduleClickListener listener) {
            mListener = listener;
        }
    
        public void setItems(List<ScheduleActivity> items) {
            this.mItems = items;
            notifyDataSetChanged();
        }
    
        @NonNull
        @Override
        public Schedule_item_ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.schedule_item, parent, false);
            final Schedule_item_ViewHolder viewHolder = new Schedule_item_ViewHolder(view);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mListener != null) {
                        final ScheduleActivity item = mItems.get(viewHolder.getAdapterPosition());
                        mListener.onScheduleClickListener(item);
                    }
                }
            });
            return viewHolder;
        }
    
        @Override
        public void onBindViewHolder(@NonNull Schedule_item_ViewHolder holder, int position) {
            // TODO : 데이터를 뷰홀더에 표시하시오
            ScheduleActivity scheduleActivity = mItems.get(position);
            holder.name.setText(scheduleActivity.getName());
            holder.name.setText(scheduleActivity.getAge() + " ");
        }
    
        @Override
        public int getItemCount() {
            return mItems.size();
        }
    
        public static class Schedule_item_ViewHolder extends RecyclerView.ViewHolder {
            TextView name;
            TextView age;
            
            public Schedule_item_ViewHolder(@NonNull View itemView) {
                super(itemView);
                name = itemView.findViewById(R.id.schedule_item_TextView_name);
                age = itemView.findViewById(R.id.schedule_item_TextView_age);
            }
        }
    }
}