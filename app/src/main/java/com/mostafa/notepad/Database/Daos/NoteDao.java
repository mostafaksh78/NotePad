package com.mostafa.notepad.Database.Daos;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.mostafa.notepad.DataModels.Note;

import java.util.List;

@Dao
public abstract class NoteDao {
    @Insert
    public abstract void insert(Note... notes);
    @Update
    public abstract void update(Note... notes);
    @Delete
    public abstract void delete(Note notes);
    @Query("SELECT * FROM NOTES")
    public abstract List<Note> getAll();
    @Query("SELECT * FROM NOTES WHERE `index` = :index")
    public abstract Note getNote(int index);

    public void scrollIndex(int from, int to) {
        // TODO
        updateIndex(from,-1);
        if (from < to){ // kam be ziad
            for (int i = from + 1; i <to + 1 ; i++) {
                updateIndex(i,i -1);
            }
        }else{ // ziad be kam
            for (int i = from - 1; i >to -1 ; i--) {
                updateIndex(i,i +1);
            }
        }
        updateIndex(-1,to);
    }
    @Query("UPDATE NOTES SET `index` =  :number WHERE `index` = :index")
    public abstract void updateIndex(int index,int number);

}
