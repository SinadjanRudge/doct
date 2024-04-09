package com.triadss.doctrack2.dto;

import java.sql.Timestamp;

public class NotificationDTO {
    private String content, reciver, tile;
    private Timestamp dataSent;
    public String getContent(){return content;}
    public void setContent(String content){ this.content = content;}
    public Timestamp getDataSent() {
        return dataSent;
    }
    public void setDataSent(Timestamp dataSent) {this.dataSent = dataSent;}
    public String getReciver(){return reciver;}
    public void setReciver(String reciver){ this.reciver = reciver;}
    public String getTitle(){return tile;}
    public void setTitle(String tile){ this.tile = tile;}
}
