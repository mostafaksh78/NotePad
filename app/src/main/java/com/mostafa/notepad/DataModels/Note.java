package com.mostafa.notepad.DataModels;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Date;

@Entity(tableName = "Notes")
public class Note {

    @NonNull
    private Date date;
    private String title;

    @NonNull
    private String text;

    @PrimaryKey(autoGenerate = true)
    private int index;
    public Note(String title, @NonNull String text,@NonNull Date date) {
        this.title = title;
        this.text = text;
        this.date = date;
    }

    public Note(String toString,  @NonNull String toString1, int noteIndex, @NonNull Date date) {
        this.title = toString;
        this.text = toString1;

        this.date = date;
        this.index = noteIndex;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
    @NonNull
    public String getText() {
        return text;
    }

    public void setText(@NonNull String text) {
        this.text = text;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }
    @NonNull
    public Date getDate() {
        return date;
    }

    public void setDate(@NonNull Date date) {
        this.date = date;
    }

    @Override
    public String toString() {
        return "Note{" +
                "text='" + text + '\'' +
                ", index=" + index +
                '}';
    }
}
