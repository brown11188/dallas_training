package training.com.database;

import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by enclaveit on 2/1/16.
 */
public class DatabaseHelper extends SQLiteOpenHelper {

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

    private static final String CREATE_TABLE_USERS = "CREATE table " + TABLE_USERS + "("
            + USER_ID + " INTEGER PRIMARY KEY,"
            + USERNAME + " varchar(50) not null,"
            + PASSWORD + " varchar(50) not null,"
            + REGISTRATION_ID + " varchar(200)" + ")";

    private static final String CREATE_TABLE_CHAT_CONTENT = "CREATE table " + TABLE_CHAT_CONTENT + "("
            + MESSAGE + " text,"
            + EXPIRES_TIME + " datatime,"
            + USER_ID + " integer, foreign key (" + USER_ID +") references " + TABLE_USERS + "(" + USER_ID + "))";

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


}
