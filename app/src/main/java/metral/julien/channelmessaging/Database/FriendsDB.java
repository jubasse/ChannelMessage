package metral.julien.channelmessaging.Database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Environment;

import java.util.ArrayList;
import java.util.UUID;

import metral.julien.channelmessaging.Model.User;

/**
 * Created by Julien on 29/02/2016.
 */
public class FriendsDB {

    public static final String FRIENDS_TABLE_NAME = "Friends";
    public static final String KEY_ID = "id";
    public static final String KEY_USERNAME = "username";
    public static final String KEY_IMAGE_URL = "imageUrl";
    private static final String FRIENDS_TABLE_CREATE = "CREATE TABLE " + FRIENDS_TABLE_NAME + " (" + KEY_ID + " TEXT, " +
            KEY_USERNAME + " TEXT, " + KEY_IMAGE_URL + " TEXT);";



    public static void onCreate(SQLiteDatabase db) {
        db.execSQL(FRIENDS_TABLE_CREATE);
    }


    public static void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + FRIENDS_TABLE_NAME);
        onCreate(db);
    }

    public static ArrayList<User> all(Context context){
        SQLiteDatabase db = DatabaseHelper.getInstance(context).getWritableDatabase();
        ArrayList<User> friends = new ArrayList<>();
        Cursor cursor = db.query(FRIENDS_TABLE_NAME,
                new String[]{
                        KEY_ID,KEY_USERNAME,KEY_IMAGE_URL
                }, null, null, null, null, null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            User friend = cursorToFriend(cursor);
            friends.add(friend);
            cursor.moveToNext();
        }
        cursor.close();
        return friends;
    }

    private static User cursorToFriend(Cursor cursor) {
        User friend = new User();
        friend.setIdentifiant(cursor.getString(0));
        friend.setUsername(cursor.getString(1));
        friend.setImageUrl(cursor.getString(2));
        return friend;
    }
}
