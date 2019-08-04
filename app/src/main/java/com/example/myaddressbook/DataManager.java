package com.example.myaddressbook;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;
import android.util.Log;

public class DataManager {

    private SQLiteDatabase db;

    /**
     * DataManager constructor which initializes the database.
     *
     * @param context Reference to the calling activity.
     */
    public DataManager(Context context) {
        MySQLiteOpenHelper helper = new MySQLiteOpenHelper(context);
        db = helper.getWritableDatabase();

    }

    /**
     * Executes select query sorted by name.
     *
     * @return cursor to the result table.
     */
    public Cursor selectAll() {

        Cursor cursor = null;

        try {
            final String SELECT_ALL_CONTACTS = "select * from contact order by name";
            cursor = db.rawQuery(SELECT_ALL_CONTACTS, null);
        } catch (Exception e) {
            Log.i("selectAll", e.getMessage());
        }

        return cursor;
    }

    /**
     * Insert a new row into the database
     *
     * @param name         Name of the contact
     * @param phone        Phone number of the contact
     * @param phoneType    Phone type of the contact. Possible values are Cell, Home, and Work.
     * @param email        Email address of the contact
     * @param street       Street address of the contact
     * @param city         City of the contact
     * @param state        State of the contact
     * @param zip          Zip code of the contact
     * @param profileImage Profile image of the contact
     */
    public void insert(String name, String phone, String phoneType, String email, String street,
                       String city, String state, String zip, byte[] profileImage) {

        String insert_query = "insert into contact values(NULL, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        SQLiteStatement st = db.compileStatement(insert_query);
        st.clearBindings();

        st.bindString(1, name);
        st.bindString(2, phone);
        st.bindString(3, phoneType);
        st.bindString(4, email);
        st.bindString(5, street);
        st.bindString(6, city);
        st.bindString(7, state);
        st.bindString(8, zip);
        if (profileImage != null)
            st.bindBlob(9, profileImage);
        st.executeInsert();


    }

    public void delete(Contact contact) {
        String name = contact.getFullName();
        PhoneNumber phone = contact.getPrimaryPhoneNumber();

        String delete_query = "delete from contact where name=?";

        SQLiteStatement st = db.compileStatement(delete_query);
        st.bindString(1, name);

        int count = st.executeUpdateDelete();


//        try {
//            final String DELETE_QUERY = "delete from contact where name= '" + name + "';";
//
//            db.execSQL(DELETE_QUERY);
//
//        } catch (SQLException e) {
//            Log.i("info", "In DataManager delete method");
//            Log.i("info", e.getMessage());
//        }
        Log.i("info", "Deleted contact " + count);

    }

    public void update(Contact c, String oldName) {

        String name = c.getFullName();
        PhoneNumber p = c.getPrimaryPhoneNumber();
        Address a = c.getPrimaryAddress();

        String update_query = "update contact set  name=?, phone=?, phoneType=?, email=?, street=?, city=?, state=?, zip=?, profileImage=? where name=?";

        SQLiteStatement st = db.compileStatement(update_query);

        st.bindString(1, name);
        st.bindString(2, p.getNumber());
        st.bindString(3, Integer.toString(p.getPhoneNumberType()));
        st.bindString(4, c.getEmail());
        st.bindString(5, a.getStreet());
        st.bindString(6, a.getCity());
        st.bindString(7, a.getState());
        st.bindString(8, a.getZipCode());
        st.bindBlob(9, c.getProfileImage());
        st.bindString(10, oldName);

        int count = st.executeUpdateDelete();
    }


    public boolean findContactByName(String name) {
        Cursor cursor = null;
        boolean found = false;

        try {
            final String SELECT__CONTACT = "select * from contact where name='" + name + "';";
            cursor = db.rawQuery(SELECT__CONTACT, null);
        } catch (Exception e) {
            Log.i("findContactByName", e.getMessage());
        }

        if (cursor.getCount() > 0)
            found = true;

        return found;
    }

    /**
     * MySQLiteOpenHelper class extends the SQLiteOpenHelper class. It is used for
     * initializing and updating the database as needed.
     */
    private class MySQLiteOpenHelper extends SQLiteOpenHelper {

        /**
         * Constructor os used to initialize the database name, and version number
         *
         * @param context
         */
        public MySQLiteOpenHelper(Context context) {
            super(context, "address_book.db", null, 1);

        }

        /**
         * Called when database is accessed for the first time. It creates the structure of the database
         *
         * @param db Reference to the SQLiteDatabase object.
         */
        public void onCreate(SQLiteDatabase db) {

            try {
                final String CREATE_TABLE_QUERY = "create table contact ("
                        + "_id integer primary key autoincrement not null, "
                        + "name text not null, "
                        + "phone text, "
                        + "phoneType text, "
                        + "email text, "
                        + "street text, "
                        + "city text, "
                        + "state text, "
                        + "zip text, "
                        + "profileImage blob)";


                db.execSQL(CREATE_TABLE_QUERY);
            } catch (SQLException e) {
                Log.i("In DataManager onCreate", e.getMessage());
            }

        }


        public void onUpgrade(SQLiteDatabase db, int oldVErsion, int newVersion) {

        }
    }
}
