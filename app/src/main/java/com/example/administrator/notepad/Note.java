package com.example.administrator.notepad;

import org.w3c.dom.Node;

/**
 * Created by Administrator on 2017/8/25.
 */

public class Note {
    private Long milliSeconds;
    private String content;
    public Note(String content){
        milliSeconds = System.currentTimeMillis();
        this.content = content;
    }

    public Note(Long milliSeconds, String content){
        this.milliSeconds = milliSeconds;
        this.content = content;
    }
    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Long getMilliSeconds() {
        return milliSeconds;
    }

    public void setMilliSeconds(Long milliSeconds) {
        this.milliSeconds = milliSeconds;
    }
}
