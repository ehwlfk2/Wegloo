package com.example.target_club_in_donga.PushMessages;

public class NotificationModel {

    //public Notification notification = new Notification();
    public Data data = new Data();
    /*public static class Notification{
        public String title;
        public String text;
        public String clickAction;
    }*/
    public String to;
    public static class Data{
        public String title;
        public String text;
        public String click_action;
        public String clickAction;
    }
}
