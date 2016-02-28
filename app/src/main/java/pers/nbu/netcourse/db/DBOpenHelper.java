package pers.nbu.netcourse.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Chenss on 2015/10/9.
 */
public class DBOpenHelper extends SQLiteOpenHelper {
    public final String CREATE_BOOK_ANNINFO = "create table tbAnnInfo("
            + "AnnId integer primary key autoincrement ,"
            + "AnnNum integer ,"
            + "AnnTitle varchar(100) ,"
            + "AnnCon text ,"
            + "AnnTime varchar(20) ,"
            + "AnnUrl varchar(200) ,"
            + "TeachName varchar(20) ,"
            + "CourName varchar(50))";

    private Context context;

    public DBOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
        this.context=context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_BOOK_ANNINFO);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
