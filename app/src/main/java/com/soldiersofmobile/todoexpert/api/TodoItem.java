package com.soldiersofmobile.todoexpert.api;

public class TodoItem {

    public String content;
    public boolean done;

    @Override
    public String toString() {
        return "TodoItem{" +
                "content='" + content + '\'' +
                ", done=" + done +
                '}';
    }
}
