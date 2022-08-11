package com.mostafa.notepad.Database;

import androidx.room.Database;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.mostafa.notepad.DataModels.Note;
import com.mostafa.notepad.DataModels.TypeConverters.DateConverter;
import com.mostafa.notepad.Database.Daos.NoteDao;

@Database(entities = {Note.class}, version = 1)
@TypeConverters({DateConverter.class})
public abstract class NoteDatabase extends RoomDatabase {
    public abstract NoteDao getNoteDao();
}
