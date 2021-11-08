package com.example.plantdroid.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class PlantDatabase extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1; // Database Version
    private static final String DATABASE_NAME = "contactsManager"; // Database Name
    private static final String TABLE_CONTACTS = "contacts"; // Contacts table name
    private static final String KEY_ID = "id"; // Contacts Table Columns names, so as below
    private static final String KEY_NAME = "name";
    private static final String KEY_PH_NO = "phone_number";

    public PlantDatabase(Context context) {
        // 增加这里的版本，将调用onUpgrade()，DATABASE_NAME用于创建database
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // 仅在数据库文件不存在且刚刚创建时运行
        String CREATE_CONTACTS_TABLE = "CREATE TABLE " + TABLE_CONTACTS + "("
                + KEY_ID + " INTEGER PRIMARY KEY," + KEY_NAME + " TEXT,"
                + KEY_PH_NO + " TEXT" + ")";
        db.execSQL(CREATE_CONTACTS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        // 仅当数据库文件存在但存储的版本号低于构造函数中请求的版本号时调用。
    }

    // public void addContact(Contact contact) {
    //     SQLiteDatabase db = this.getWritableDatabase();
    //     // ContentValues() class is used to store a set of
    //     // values that content resolver can handle
    // 	/* 不使用内容解析器，您可以在这里放一条sql语句，例如
    // 		String ROW1 = "INSERT INTO " + …;
    // 		db.execSQL(ROW1); */
    //     ContentValues values = new ContentValues();
    //     values.put(KEY_NAME, contact.getName()); // Contact Name
    //     values.put(KEY_PH_NO, contact.getPhoneNumber()); // Contact Phone Number
    //     // Inserting Row
    //     db.insert(TABLE_CONTACTS, null, values);
    //     db.close(); // Closing database connection
    // }
    //
    // public Contact getContact(int id) {
    //     SQLiteDatabase db = this.getReadableDatabase();
    //     Cursor cursor = db.query(TABLE_CONTACTS,
    //             new String[]{KEY_ID, KEY_NAME, KEY_PH_NO},
    //             KEY_ID + "=?", new String[]{String.valueOf(id)},
    //             null, null, null, null);
    //     if (cursor != null)
    //         cursor.moveToFirst();
    //     Contact contact = new Contact(Integer.parseInt(cursor.getString(0)), cursor.getString(1), cursor.getString(2));
    //     return contact;
    // }
}
