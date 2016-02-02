package training.com.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;

import training.com.dao.DatabaseDAO;
import training.com.model.Message;
import training.com.model.Users;

/**
 * Created by enclaveit on 2/1/16.
 */
public class DatabaseHelper extends SQLiteOpenHelper implements DatabaseDAO {

    private SQLiteDatabase database;
    private static final String TAG = "Database Helper";

    private static final int DB_VERSION = 1;

    private static final String DB_NAME = "db_chat";

    private static final String TABLE_USERS = "tbl_users";
    private static final String TABLE_CHAT_CONTENT = "tbl_chat_content";

    //Column name of tbl_users table
    private static final String USER_ID = "user_id";
    private static final String USERNAME = "username";
    private static final String PASSWORD = "password";
    private static final String REGISTRATION_ID = "registration_id";

    //Column name of tbl_chat_content table
    private static final String MESSAGE = "message";
    private static final String EXPIRES_TIME = "expires_time";

    private static final String[] USER_INFO = {USER_ID,USERNAME,REGISTRATION_ID};

    private static final String CREATE_TABLE_USERS = "CREATE table " + TABLE_USERS + "("
            + USER_ID + " integer PRIMARY KEY AUTOINCREMENT,"
            + USERNAME + " varchar(50) not null,"
            + PASSWORD + " varchar(50) not null,"
            + REGISTRATION_ID + " varchar(200)" + ")";

    private static final String CREATE_TABLE_CHAT_CONTENT = "CREATE table " + TABLE_CHAT_CONTENT + "("
            + MESSAGE + " text,"
            + EXPIRES_TIME + " datetime,"
            + USER_ID + " integer, foreign key (" + USER_ID +") references " + TABLE_USERS + "(" + USER_ID + "))";
    private static final String SELECT_ALL_USER = "SELECT * FROM "+TABLE_USERS;

    public DatabaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
        database = getWritableDatabase();
    }



    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_USERS);
        db.execSQL(CREATE_TABLE_CHAT_CONTENT);
        Log.i("DATABASE", "Create successfully");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CHAT_CONTENT);
        Log.i("DATABASE", "Update successfully");
        onCreate(db);
    }


    @Override
    public void addUser(Users user) {
        SQLiteDatabase database =  this.getWritableDatabase();

        ContentValues values =  new ContentValues();
        values.put(USERNAME, user.getUserName());
        values.put(PASSWORD, user.getPassword());
        values.put(REGISTRATION_ID, user.getRegistrationId());
        database.insert(TABLE_USERS, null, values);
        database.close();
    }

    @Override
    public void addMessage(String message, String expires_date, int user_id) {
        SQLiteDatabase database = this.getWritableDatabase();

        ContentValues values =  new ContentValues();
        values.put(MESSAGE, message);
        values.put(EXPIRES_TIME, expires_date);
        values.put(USER_ID, user_id);
        database.insert(TABLE_CHAT_CONTENT, null, values);
        database.close();
    }
    @Override
    public ArrayList<Users> getUsers(){
        ArrayList<Users> userList = new ArrayList<Users>();

        SQLiteDatabase database = this.getWritableDatabase();


        Cursor cursor = database.rawQuery(SELECT_ALL_USER, null);

        Users users = null;

        if(cursor.moveToFirst()){
            do{
                users = new Users();
                users.setUserId(cursor.getColumnIndex(USER_ID));
                users.setUserName(cursor.getString(cursor.getColumnIndex(USERNAME)));
                users.setRegistrationId(cursor.getString(cursor.getColumnIndex(REGISTRATION_ID)));
                userList.add(users);
            }while (cursor.moveToNext());
        }

        return userList;
    }
    @Override
    public Users getUser(int userId){
        SQLiteDatabase database = this.getWritableDatabase();
        Cursor cursor = database.query(TABLE_USERS,USER_INFO,USER_ID+"= ?",
                new String[]{String.valueOf(userId)},null,null,null,null);
        if(cursor!= null)cursor.moveToFirst();
        Users user = new Users();
        user.setUserId(Integer.parseInt(cursor.getString(0)));
        user.setUserName(cursor.getString(1));
        user.setRegistrationId(cursor.getString(2));

        return user;
    }

}
