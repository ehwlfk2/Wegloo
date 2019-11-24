package com.example.target_club_in_donga.Notice;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.text.SpannableStringBuilder;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.target_club_in_donga.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.FirebaseDatabase;
import com.melnykov.fab.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

import static com.example.target_club_in_donga.Notice.NoticeActivity_Main.noticeDbKey;
import static com.example.target_club_in_donga.MainActivity.clubName;
import static com.example.target_club_in_donga.home_viewpager.HomeFragment0.userAdmin;

public class ExpandableListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private FirebaseDatabase database;
    private Activity activity;
    public static final int HEADER = 0;
    public static final int CHILD = 1;

    private List<Item> data;

    public ExpandableListAdapter(Activity activity, List<Item> data) {
        this.data = data;
        this.activity = activity;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int type) {
        View view = null;
        Context context = parent.getContext();
        float dp = context.getResources().getDisplayMetrics().density;
        int subItemPaddingLeft = (int) (18 * dp);
        int subItemPaddingTopAndBottom = (int) (5 * dp);
        switch (type) {
            case HEADER:
                LayoutInflater inflater = (LayoutInflater) parent.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                view = inflater.inflate(R.layout.notice_main_recyclerview_item, parent, false);
                ListHeaderViewHolder header = new ListHeaderViewHolder(view);
                return header;
            case CHILD:
                TextView itemTextView = new TextView(context);
                itemTextView.setPadding(subItemPaddingLeft, subItemPaddingTopAndBottom, 0, subItemPaddingTopAndBottom);
                itemTextView.setTextColor(0x88000000);
                itemTextView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                return new RecyclerView.ViewHolder(itemTextView) {
                };
        }
        return null;
    }

    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        final Item item = data.get(position);
        switch (item.type) {
            case HEADER:
                final ListHeaderViewHolder itemController = (ListHeaderViewHolder) holder;
                itemController.refferalItem = item;
                itemController.header_title.setText(item.title);
                itemController.header_writer.setText(item.writer);
                itemController.header_timedate.setText(item.timedate);

                if (item.invisibleChildren == null) {
                    itemController.btn_expand_toggle.setImageResource(R.drawable.ic_remove_circle_outline_24px);
                } else {
                    itemController.btn_expand_toggle.setImageResource(R.drawable.ic_add_circle_outline_24px);
                }
                itemController.btn_expand_toggle.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (item.invisibleChildren == null) {
                            item.invisibleChildren = new ArrayList<Item>();
                            int count = 0;
                            int pos = data.indexOf(itemController.refferalItem);
                            while (data.size() > pos + 1 && data.get(pos + 1).type == CHILD) {
                                item.invisibleChildren.add(data.remove(pos + 1));
                                count++;
                            }
                            notifyItemRangeRemoved(pos + 1, count);
                            itemController.btn_expand_toggle.setImageResource(R.drawable.ic_add_circle_outline_24px);
                        } else {
                            int pos = data.indexOf(itemController.refferalItem);
                            int index = pos + 1;
                            for (Item i : item.invisibleChildren) {
                                data.add(index, i);
                                index++;
                            }
                            notifyItemRangeInserted(pos + 1, index - pos - 1);
                            itemController.btn_expand_toggle.setImageResource(R.drawable.ic_remove_circle_outline_24px);
                            item.invisibleChildren = null;
                        }
                    }
                });

                if(userAdmin < 2){
                    itemController.linearLayout.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            PopupMenu popup = new PopupMenu(view.getContext(), view);

                            popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                                @Override
                                public boolean onMenuItemClick(MenuItem item) {
                                    //int x = item.getItemId();
                                    switch (item.getItemId()){
                                        case R.id.notice_update:
                                            Intent intent = new Intent(activity,NoticeActivity_Insert.class);
                                            intent.putExtra("type","update");
                                            //Log.e("position",position+"");
                                            intent.putExtra("updateKey",noticeDbKey.get(position));
                                            activity.startActivity(intent);

                                            return true;
                                        case R.id.notice_delete:
                                            delete_item(position);
                                            return true;

                                        default:
                                            return false;
                                    }
                                    //return false;
                                }
                            });
                            popup.inflate(R.menu.notice_main_popup);
                            popup.setGravity(Gravity.RIGHT); //오른쪽 끝에 뜨게
                            popup.show();
                        }
                    });
                }

                break;
            case CHILD:
                TextView itemTextView = (TextView) holder.itemView;
                itemTextView.setText(data.get(position).content);
                break;
        }
    }
    @Override
    public int getItemViewType(int position) {
        return data.get(position).type;
    }


    @Override
    public int getItemCount() {
        return data.size();
    }

    private static class ListHeaderViewHolder extends RecyclerView.ViewHolder {
        public TextView header_title;
        public TextView header_writer;
        public TextView header_timedate;
        public ImageView btn_expand_toggle;
        public Item refferalItem;
        public LinearLayout linearLayout;

        public ListHeaderViewHolder(View itemView) {
            super(itemView);
            header_title = itemView.findViewById(R.id.notice_main_recyclerview_item_textview_title);
            header_writer = itemView.findViewById(R.id.notice_main_recyclerview_item_textview_writer);
            header_timedate = itemView.findViewById(R.id.notice_main_recyclerview_item_textview_date);
            linearLayout = itemView.findViewById(R.id.notice_main_recyclerview_item_linarlayout);
            btn_expand_toggle = itemView.findViewById(R.id.notice_main_recyclerview_item_plusBtn);

        }
    }

    public static class Item {
        public int type;
        public SpannableStringBuilder title;
        public String content;
        public String writer;
        public String timedate;
        public List<Item> invisibleChildren;

        public Item(int type, SpannableStringBuilder title, String content, String writer, String timedate) {
            this.type = type;
            this.title = title;
            this.writer = writer;
            this.timedate = timedate;
            this.content = content;
        }
    }

    public void delete_item(final int position){
        //ArrayList<String> dbKey = new ArrayList<String>();

        database = FirebaseDatabase.getInstance();
        database.getReference().child("EveryClub").child(clubName).child("Notice").child(noticeDbKey.get(position)).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                //Toast.makeText(, "", Toast.LENGTH_SHORT).show();
                Toast.makeText(activity, "삭제성공", Toast.LENGTH_SHORT).show();

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(activity, "삭제실패", Toast.LENGTH_SHORT).show();
            }
        });
    }
}

