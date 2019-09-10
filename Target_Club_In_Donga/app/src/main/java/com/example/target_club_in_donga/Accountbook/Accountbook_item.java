package com.example.target_club_in_donga.Accountbook;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class Accountbook_item {
        private String id;
        private String price;
        private String detail;
        private String today;


        public void setId(String id) {
                this.id = id;
        }

        public String getId() {
                return id;
        }

        public void setToday(String today) {
                this.today = today;
        }

        public String getToday() {
                return today;
        }

        public void setPrice(String price) {
                this.price = price;
        }

        public int getPrice() {
                return  Integer.valueOf(price);
        }

        public void setDetail(String detail) {
                this.detail = detail;
        }

        public String getDetail() {
                return detail;
        }

        public Accountbook_item(String id, String price, String detail) {
                this.id = id;
                this.price = price;
                this.detail = detail;
        }

        public Accountbook_item(String id,String price,String detail,String today) {
                this.id = id;
                this.price=price;
                this.detail=detail;
                this.today=today;
        }
}