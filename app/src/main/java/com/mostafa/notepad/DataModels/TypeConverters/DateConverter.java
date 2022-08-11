package com.mostafa.notepad.DataModels.TypeConverters;

import androidx.room.TypeConverter;

import java.util.Date;

public class DateConverter {
    @TypeConverter
    public long convertDateToLong(Date date) {
        return date.getTime();
    }
    @TypeConverter
    public Date convertLongToDate(long time) {
        return new Date(time);
    }

}
