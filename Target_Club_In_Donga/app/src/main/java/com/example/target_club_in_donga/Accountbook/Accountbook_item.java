package com.example.target_club_in_donga.Accountbook;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Accountbook_item {
        private String id;
        private String pay;
        private String detail;

        public String getPaytotal(String Pay, int oldPay) {
                int i = Integer.valueOf(Pay);
                int j = oldPay;
                return "" + (i + j);
        }

        public void setId(String id) {
                this.id = id;
        }

        public String getId() {
                return id;
        }


        public void setPay(String pay) {
                this.pay = pay;
        }

        public String getPay() {
                return pay;
        }

        public void setDetail(String detail) {
                this.detail = detail;
        }

        public String getDetail() {
                return detail;
        }

        public Accountbook_item(String id, String pay, String detail) {
                this.id = id;
                this.pay = pay;
                this.detail = detail;
        }
}