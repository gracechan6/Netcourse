package pers.nbu.netcourse.db;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import pers.nbu.netcourse.BaseApplication;
import pers.nbu.netcourse.entity.AnnEntity;

/**
 * Created by GraceChan on 2015/10/12.
 */
public class DB {
    /**
     * 数据库名
     */
    public static final String DB_NAME = "AIOC";

    /**
     * 数据库版本
     */
    public static final int VERSION = 1;

    private static DB DB;

    private SQLiteDatabase db;


    /**
     * 表名
     */
    public static final String TABLE_ANNINFO = "tbAnnInfo";
        //字段名
        public final String COL_ANNNUM = "AnnNum";
        public final String COL_ANNTITLE = "AnnTitle";
        public final String COL_ANNCON = "AnnCon";
        public final String COL_ANNURL = "AnnUrl";
        public final String COL_ANNTIME = "AnnTime";
        public final String COL_COURNAME = "CourName";
        public final String COL_TEACHNAME = "TeachName";
        public final String COL_ANNID = "AnnId";


    /**
     * 将构造方法私有化
     */
    private DB() {
        DBOpenHelper dbHelper = new DBOpenHelper(BaseApplication.getContext(),
                DB_NAME, null, VERSION);
        db = dbHelper.getWritableDatabase();
    }

    /**
     * 获取CabinetGridDB的实例。
     */
    public synchronized static DB getInstance() {
        if (DB == null) {
            DB = new DB();
        }
        return DB;
    }

    /**
     * 判断表中是否存在数据
     * @param tableName
     */
    public int ifexistData(String tableName){
        Cursor cursor=db.query(tableName,null,null,null,null,null,null);
        int count=1;
        int annnum=0;
        if (cursor.moveToFirst()){
            do {
                if (count == 2)
                    return cursor.getInt(cursor.getColumnIndex(COL_ANNNUM));
                annnum=cursor.getInt(cursor.getColumnIndex(COL_ANNNUM));
                count++;
            }while (cursor.moveToNext());
        }
        return annnum;
    }

    /**
     * 表中数据总条数
     * @param tableName
     */
    public int countData(String tableName){
        Cursor cursor=db.query(tableName,null,null,null,null,null,null);

        return cursor.getCount();//?not sure
    }



    /**
     * 将服务器端获取到的公告信息同步存储到本地数据
     * @param anns
     */
    public void saveAnnInfo(List<AnnEntity> anns){

        ContentValues values=null;
        db.beginTransaction();
        for (int i = 0; i < anns.size() ; i++) {
            values=new ContentValues();
            values.put(COL_ANNNUM, anns.get(i).getAnnNum());
            values.put(COL_ANNTITLE, anns.get(i).getAnnTitle());
            values.put(COL_ANNCON, anns.get(i).getAnnCon());
            values.put(COL_ANNURL, anns.get(i).getAnnUrl());
            values.put(COL_ANNTIME, anns.get(i).getAnnTime());
            values.put(COL_TEACHNAME, anns.get(i).getTeachName());
            values.put(COL_COURNAME, anns.get(i).getCourName());
            db.insert(TABLE_ANNINFO, null, values);
        }
        db.setTransactionSuccessful();
        db.endTransaction();
    }

    /**
     * 从本地数据库获取num条数据
     * @param num
     * @return
     */
    public ArrayList<AnnEntity> getAnnInfo(int num){
        ArrayList<AnnEntity> anns= new ArrayList<>();
        Cursor cursor=db.rawQuery("select * from "+TABLE_ANNINFO+" order by AnnId desc limit 0,"+num,null);
        if (cursor.moveToFirst()){
            do {
                AnnEntity ann = new AnnEntity(cursor.getInt(cursor.getColumnIndex(COL_ANNID)),
                        cursor.getInt(cursor.getColumnIndex(COL_ANNNUM)),
                        cursor.getString(cursor.getColumnIndex(COL_ANNTITLE)),
                        cursor.getString(cursor.getColumnIndex(COL_ANNCON)),
                        cursor.getString(cursor.getColumnIndex(COL_ANNURL)),
                        cursor.getString(cursor.getColumnIndex(COL_ANNTIME)),
                        cursor.getString(cursor.getColumnIndex(COL_TEACHNAME)),
                        cursor.getString(cursor.getColumnIndex(COL_COURNAME)));
                anns.add(ann);
            }while (cursor.moveToNext());
        }
        return anns;
    }

}
