package com.wlp.myanjunote.jetpack.room;

import androidx.room.Database;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.wlp.myanjunote.jetpack.room.daos.BookDao;
import com.wlp.myanjunote.jetpack.room.entity.Book;


@Database(entities = {Book.class}, version = 3, exportSchema = false)
@TypeConverters({Converters.class})
public abstract class AppDatabase extends RoomDatabase {

    public abstract BookDao bookDao();

}