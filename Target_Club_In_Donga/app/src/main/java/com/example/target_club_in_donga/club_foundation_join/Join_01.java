package com.example.target_club_in_donga.club_foundation_join;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Filter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.target_club_in_donga.R;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class Join_01 extends AppCompatActivity implements View.OnClickListener {
    private List<AutoCompleteItem> list = new ArrayList<>();
    private CustomAutoCompleteTextView joinclube_autoCompleteSearch;
    private ImageButton foundation_02_button_back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join_01);
        joinclube_autoCompleteSearch = findViewById(R.id.joinclube_autoCompleteSearch);
        foundation_02_button_back = findViewById(R.id.joinclube_00_imagebutton_back);
        foundation_02_button_back.setOnClickListener(this);
        /*Bitmap bm = BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher_foreground);
        ByteArrayOutputStream  byteArray = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.PNG, 100, byteArray);
        byte[] bytes = byteArray.toByteArray();*/

        //byte Pic[];
        //Pic = yourImageByte; //get the image in the form of byte[] from db
        //ByteArrayInputStream imageStream = new ByteArrayInputStream(Pic);

        //category_icon2 = findViewById(R.id.category_icon2);
        //category_icon2.setImageBitmap(theImage);

        //System.out.println(myBitmap);
        //category_icon2.setImageBitmap(myBitmap);
        // Add the country details
        Bitmap theImage = BitmapFactory.decodeResource(getResources(),R.drawable.bb);
        AutoCompleteItem entry1= new AutoCompleteItem();
        entry1.title = "하이안녕";
        entry1.image = theImage;
        list.add(entry1);
        AutoCompleteItem entry2= new AutoCompleteItem();
        Bitmap theImage2 = BitmapFactory.decodeResource(getResources(),R.drawable.aa);
        entry2.title = "바이안녕";
        entry2.image = theImage2;
        list.add(entry2);
        AutoCompleteItem entry3= new AutoCompleteItem();
        Bitmap theImage3 = BitmapFactory.decodeResource(getResources(),R.drawable.cc);
        entry3.title = "이안하";
        entry3.image = theImage3;
        list.add(entry3);
        list.add(entry1);
        list.add(entry2);
        list.add(entry3);
        list.add(entry1);
        list.add(entry2);
        list.add(entry3);
        list.add(entry1);
        list.add(entry2);
        list.add(entry3);


        SearchItemArrayAdapter adapter = new SearchItemArrayAdapter(this, R.layout.search_listitem_icon, list);
        joinclube_autoCompleteSearch.setAdapter(adapter);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.joinclube_00_imagebutton_back:
                finish();
                break;
        }
    }

    public class SearchItemArrayAdapter extends ArrayAdapter<AutoCompleteItem> {
        private static final String tag = "SearchItemArrayAdapter";
        private AutoCompleteItem listEntry;
        private TextView autoItem;
        private ImageView categoryIcon;
        private List<AutoCompleteItem> AutoCompleteItemList;
        private List<AutoCompleteItem> tempItems;
        private List<AutoCompleteItem> suggestions;
        /**
         *
         * @param context
         * @param textViewResourceId
         * @param objects
         */
        public SearchItemArrayAdapter(Context context, int textViewResourceId, List<AutoCompleteItem> objects) {
            super(context, textViewResourceId, objects);
            AutoCompleteItemList = objects;
            suggestions = new ArrayList<>();
            tempItems = new ArrayList<>(objects);
            Log.d(tag, "Search List -> journalEntryList := " + AutoCompleteItemList.toString());
        }

        @Override
        public int getCount() {
            return this.AutoCompleteItemList.size();
        }

        @Override
        public AutoCompleteItem getItem(int position) {
            AutoCompleteItem journalEntry = this.AutoCompleteItemList.get(position);
            Log.d(tag, "*-> Retrieving JournalEntry @ position: " + String.valueOf(position) + " : " + journalEntry.toString());
            return journalEntry;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View row = convertView;
            LayoutInflater inflater = LayoutInflater.from(getContext());

            if (row == null) {
                row = inflater.inflate(R.layout.search_listitem_icon, parent, false);
            }

            listEntry = this.AutoCompleteItemList.get(position);
            String searchItem = listEntry.title;
            autoItem = row.findViewById(R.id.search_auto_item);
            autoItem.setText(searchItem);

            // Get a reference to ImageView holder
            categoryIcon = row.findViewById(R.id.category_icon);
            categoryIcon.setImageBitmap(listEntry.image);

            return row;
        }


        @Override
        public Filter getFilter() {
            return nameFilter;
        }

        Filter nameFilter = new Filter() {
            @Override
            public CharSequence convertResultToString(Object resultValue) {
                String str = resultValue.toString();
                return str;
            }

            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                if (constraint != null) {
                    suggestions.clear();
                    for (int i=0;i<tempItems.size();i++) {
                        if (tempItems.get(i).title.toLowerCase().contains(constraint.toString().toLowerCase())) {
                            AutoCompleteItem item = new AutoCompleteItem();
                            item.title = tempItems.get(i).title;
                            item.image = tempItems.get(i).image;
                            suggestions.add(item);
                        }
                    }
                    FilterResults filterResults = new FilterResults();
                    filterResults.values = suggestions;
                    filterResults.count = suggestions.size();
                    return filterResults;
                }
                else {
                    return new FilterResults();
                }
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                List<AutoCompleteItem> filterList = (ArrayList<AutoCompleteItem>) results.values;
                if (results != null && results.count > 0)
                {
                    clear();
                    for (int i=0;i<filterList.size();i++) {
                        AutoCompleteItem item = new AutoCompleteItem();
                        item.title = tempItems.get(i).title;
                        item.image = tempItems.get(i).image;
                        add(item);
                        notifyDataSetChanged();
                    }
                }
            }
        };
    }
}
