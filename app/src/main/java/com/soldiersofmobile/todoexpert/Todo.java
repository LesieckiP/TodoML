package com.soldiersofmobile.todoexpert;

import java.io.Serializable;

import android.os.Parcel;
import android.os.Parcelable;

public class Todo implements Parcelable {

    private String content;
    private boolean done;

    public Todo(
            final String content,
            final boolean done
    ) {
        this.content = content;
        this.done = done;
    }

    protected Todo(Parcel in) {
        content = in.readString();
        done = in.readByte() != 0;
    }

    public static final Creator<Todo> CREATOR = new Creator<Todo>() {
        @Override
        public Todo createFromParcel(Parcel in) {
            return new Todo(in);
        }

        @Override
        public Todo[] newArray(int size) {
            return new Todo[size];
        }
    };

    public String getContent() {
        return content;
    }

    public void setContent(final String content) {
        this.content = content;
    }

    public boolean isDone() {
        return done;
    }

    public void setDone(final boolean done) {
        this.done = done;
    }

    @Override
    public String toString() {
        return "Todo{" +
                "content='" + content + '\'' +
                ", done=" + done +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(
            final Parcel dest,
            final int flags
    ) {

        dest.writeString(content);
        dest.writeByte((byte) (done ? 1 : 0));
    }
}
