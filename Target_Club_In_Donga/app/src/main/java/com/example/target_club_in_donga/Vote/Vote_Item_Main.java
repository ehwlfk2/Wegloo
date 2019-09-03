package com.example.target_club_in_donga.Vote;

public class Vote_Item_Main {
    private String title;//보안을 위해서 private로
    //private String date;
    private Object timestamp;
    private String color;
    private int totalCount;

    public Vote_Item_Main(String title, Object timestamp, String color, int totalCount){//new생성자를 통해서 생성자가 만들어진다.
        this.title = title;
        this.timestamp = timestamp;
        this.color = color;
    }
    public String getTitle(){//외부로 id값을 리턴해서 내보내준다.
        return title;
    }
    public Object getTimestamp(){
        return timestamp;
    }
    public String getColor(){
        return color;
    }
    public int getTotalCount(){
        return totalCount;
    }

    public void setTitle(String title){//외부에서 받은 id를 내부로 넣어준다.
        this.title=title;
    }
    public void setTimestamp(Object timestamp){
        this.timestamp = timestamp;
    }
    public void setColor(String color){
        this.color = color;
    }
    public void setTotalCount(int totalCount){this.totalCount = totalCount;}
}
