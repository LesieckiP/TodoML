package com.soldiersofmobile.todoexpert.api;

public class TodoItem {

    public String objectId;
    public String content;
    public boolean done;

    @Override
    public String toString() {
        return "TodoItem{" +
                "objectId='" + objectId + '\'' +
                ", content='" + content + '\'' +
                ", done=" + done +
                '}';
    }
}
