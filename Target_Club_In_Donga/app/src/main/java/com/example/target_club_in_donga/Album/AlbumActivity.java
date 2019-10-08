package com.example.target_club_in_donga.Album;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.target_club_in_donga.R;

import java.util.ArrayList;

public class AlbumActivity extends AppCompatActivity {

    private ArrayList<Data> a1;
    private ListView listview;
    private TextView empty;
    private GroupAdapter adapter;

    @Override
    protected void onCreate(@Nullable final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_album);

        listview.setEmptyView(this.empty);

        a1 = new ArrayList<Data>();

        Data a1 = new Data("이름1", 1);
        Data a2 = new Data("이름2", 2);
        Data a3 = new Data("이름3", 3);

    }

    private class GroupAdapter extends ArrayAdapter<Object>  {
        private ArrayList<Data> item;
        private Data temp;

        public GroupAdapter(Context context, int resourceID, ArrayList item) {
            super(context, resourceID, item);
            this.item = item;
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            View view = convertView;
            if(view == null) {
                LayoutInflater v1 = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                view = v1.inflate(R.layout.activity_album_group, null);
            }
            temp = item.get(position);

            if(temp != null) {
                TextView textView = (TextView) view.findViewById(R.id.group_text);
                textView.setText(temp.getName() + " ( " + temp.getAdd() + " ) ");
            }
            return view;
        }
    }

}

class Data{
    String name;
    int add;
    Data(String name, int add) {
        this.name = name;
        this.add = add;
    }
    public String getName() {
        return name;
    }
    public int getAdd() {
        return add;
    }
}
