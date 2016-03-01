package metral.julien.channelmessaging.Database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;


public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "channelMessaging.db";
    private static final int DATABASE_VERSION = 3;
    private static Context context;

    private static DatabaseHelper dbHelper = null;
    private static SQLiteDatabase db = null;

    public static DatabaseHelper getInstance(Context context){
        if(dbHelper==null){
            dbHelper = new DatabaseHelper(context);
            openConnexion();
        }
        return dbHelper;
    }

    private DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        dbHelper = this;
    }

    @Override
    public void onCreate(SQLiteDatabase database) {
        FriendsDB.onCreate(database);
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion) {
        Log.wtf("DatabaseHelper","Upgrade database from "+oldVersion+" to "+newVersion);
        FriendsDB.onUpgrade(database, oldVersion, newVersion);
    }

    @Override
    public void onDowngrade(SQLiteDatabase database, int oldVersion, int newVersion) {
        Log.wtf("DatabaseHelper","Upgrade database from "+oldVersion+" to "+newVersion);
        super.onDowngrade(database, oldVersion, newVersion);
        FriendsDB.onUpgrade(database, oldVersion, newVersion);
    }

    // will be called only once when singleton is created
    private static void openConnexion(){
        if ( db == null ){
            db = dbHelper.getWritableDatabase();
        }
    }

    // onDestroy method of application
    public synchronized void closeConnecion() {
        if(dbHelper!=null){
            dbHelper.close();
            db.close();
            dbHelper = null;
            db = null;
        }
    }
}

