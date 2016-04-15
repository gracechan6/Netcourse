package pers.nbu.netcourse.db;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import pers.nbu.netcourse.BaseApplication;
import pers.nbu.netcourse.config.SystemConfig;
import pers.nbu.netcourse.entity.AnnEntity;
import pers.nbu.netcourse.entity.TaskEntity;
import pers.nbu.netcourse.entity.TaskManageEntity;

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
    public static final String TABLE_ANNSHOW = "tbAnnShow";
    public static final String TABLE_TASKSHOW = "tbTaskShow";
    public static final String TABLE_TASKMANAGESHOW = "tbTaskManageShow";


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

//公共方法
    /**
     * 判断表中是否存在数据
     * @param tableName
     */
    public int ifexistData(String tableName,String num){
        Cursor cursor=db.query(tableName,null,null,null,null,null,null);
        int count=1;
        int annnum=0;
        if (cursor.moveToFirst()){
            do {
                if (count == 2)
                    return cursor.getInt(cursor.getColumnIndex(num));
                annnum=cursor.getInt(cursor.getColumnIndex(num));
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
//start Ann DB
    /**
     * 将服务器端获取到的公告信息同步存储到本地数据
     * @param anns
     */
    public void saveAnnInfo(List<AnnEntity> anns){

        ContentValues values=null;
        db.beginTransaction();
        for (int i = 0; i < anns.size() ; i++) {
            values=new ContentValues();
            values.put(SystemConfig.ANNNUM, anns.get(i).getAnnNum());
            values.put(SystemConfig.ANNTITLE, anns.get(i).getAnnTitle());
            values.put(SystemConfig.ANNCON, anns.get(i).getAnnCon());
            values.put(SystemConfig.ANNURL, anns.get(i).getAnnUrl());
            values.put(SystemConfig.ANNTIME, anns.get(i).getAnnTime());
            values.put(SystemConfig.TEACHNAME, anns.get(i).getTeachName());
            values.put(SystemConfig.COURNAME, anns.get(i).getCourName());
            db.insert(TABLE_ANNSHOW, null, values);
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
        Cursor cursor=db.rawQuery("select * from "+TABLE_ANNSHOW+" order by AnnId desc limit 0,"+num,null);
        if (cursor.moveToFirst()){
            do {
                AnnEntity ann = new AnnEntity(cursor.getInt(cursor.getColumnIndex(SystemConfig.ANNID)),
                        cursor.getInt(cursor.getColumnIndex(SystemConfig.ANNNUM)),
                        cursor.getString(cursor.getColumnIndex(SystemConfig.ANNTITLE)),
                        cursor.getString(cursor.getColumnIndex(SystemConfig.ANNCON)),
                        cursor.getString(cursor.getColumnIndex(SystemConfig.ANNURL)),
                        cursor.getString(cursor.getColumnIndex(SystemConfig.ANNTIME)),
                        cursor.getString(cursor.getColumnIndex(SystemConfig.TEACHNAME)),
                        cursor.getString(cursor.getColumnIndex(SystemConfig.COURNAME)));
                anns.add(ann);
            }while (cursor.moveToNext());
        }
        return anns;
    }

//end Ann DB
//start Task DB
    /**
     * 将服务器端获取到的公告信息同步存储到本地数据
     * @param tasks
     */
    public void saveTaskShow(List<TaskEntity> tasks){

        ContentValues values=null;
        db.beginTransaction();
        for (int i = 0; i < tasks.size() ; i++) {
            values=new ContentValues();
            values.put(SystemConfig.TASKNUM, tasks.get(i).getTaskNum());
            values.put(SystemConfig.TASKTITLE, tasks.get(i).getTaskTitle());
            values.put(SystemConfig.TASKTIME, tasks.get(i).getTaskTime());
            values.put(SystemConfig.ENDTIME, tasks.get(i).getEndTime());
            values.put(SystemConfig.TEACHNAME, tasks.get(i).getTeachName());
            values.put(SystemConfig.COURNAME, tasks.get(i).getCourName());
            values.put(SystemConfig.TASKREQUIRE, tasks.get(i).getCourName());
            db.insert(TABLE_TASKSHOW, null, values);
        }
        db.setTransactionSuccessful();
        db.endTransaction();
    }

    /**
     * 从本地数据库获取num条数据
     * @param num
     * @return
     */
    public ArrayList<TaskEntity> getTaskShow(int num){
        ArrayList<TaskEntity> tasks= new ArrayList<>();
        Cursor cursor=db.rawQuery("select * from "+TABLE_TASKSHOW+" order by TaskId desc limit 0,"+num,null);
        if (cursor.moveToFirst()){
            do {
                TaskEntity task = new TaskEntity(cursor.getInt(cursor.getColumnIndex(SystemConfig.TASKID)),
                        cursor.getInt(cursor.getColumnIndex(SystemConfig.TASKNUM)),
                        cursor.getString(cursor.getColumnIndex(SystemConfig.TASKTITLE)),
                        cursor.getString(cursor.getColumnIndex(SystemConfig.COURNAME)),
                        cursor.getString(cursor.getColumnIndex(SystemConfig.TEACHNAME)),
                        cursor.getString(cursor.getColumnIndex(SystemConfig.TASKTIME)),
                        cursor.getString(cursor.getColumnIndex(SystemConfig.ENDTIME)),
                        cursor.getString(cursor.getColumnIndex(SystemConfig.TASKREQUIRE)));
                tasks.add(task);
            }while (cursor.moveToNext());
        }
        return tasks;
    }
//end Task DB

//start TaskManage DB
    /**
     * 将服务器端获取到的公告信息同步存储到本地数据
     * @param tasks
     */
    public void saveTaskManageShow(List<TaskManageEntity> tasks){

        ContentValues values=null;
        db.beginTransaction();
        for (int i = 0; i < tasks.size() ; i++) {
            values=new ContentValues();
            values.put(SystemConfig.TASKNUM, tasks.get(i).getTaskNum());
            values.put(SystemConfig.TREEID, tasks.get(i).getTreeid());
            values.put(SystemConfig.TEACHNAME, tasks.get(i).getTeachName());
            values.put(SystemConfig.TASKTITLE, tasks.get(i).getTaskTitle());
            values.put(SystemConfig.COURNAME, tasks.get(i).getCourName());
            values.put(SystemConfig.TASKTIME, tasks.get(i).getTaskTime());
            values.put(SystemConfig.ENDTIME, tasks.get(i).getEndTime());
            values.put(SystemConfig.OPUSNUM, tasks.get(i).getOpusNum());
            db.insert(TABLE_TASKMANAGESHOW, null, values);
        }
        db.setTransactionSuccessful();
        db.endTransaction();
    }

    /**
     * 从本地数据库获取num条数据
     * @param num
     * @return
     */
    public ArrayList<TaskManageEntity> getTaskManageShow(int num){
        ArrayList<TaskManageEntity> tasks= new ArrayList<>();
        Cursor cursor=db.rawQuery("select * from "+TABLE_TASKMANAGESHOW+" order by TaskId desc limit 0,"+num,null);
        if (cursor.moveToFirst()){
            do {
                TaskManageEntity task = new TaskManageEntity(cursor.getInt(cursor.getColumnIndex(SystemConfig.TASKID)),
                        cursor.getInt(cursor.getColumnIndex(SystemConfig.TASKNUM)),
                        cursor.getInt(cursor.getColumnIndex(SystemConfig.TREEID)),
                        cursor.getString(cursor.getColumnIndex(SystemConfig.TEACHNAME)),
                        cursor.getString(cursor.getColumnIndex(SystemConfig.TASKTITLE)),
                        cursor.getString(cursor.getColumnIndex(SystemConfig.COURNAME)),
                        cursor.getString(cursor.getColumnIndex(SystemConfig.TASKTIME)),
                        cursor.getString(cursor.getColumnIndex(SystemConfig.ENDTIME)),
                        cursor.getInt(cursor.getColumnIndex(SystemConfig.OPUSNUM)));
                tasks.add(task);
            }while (cursor.moveToNext());
        }
        return tasks;
    }

    /**
     * 从本地数据库数据
     * @param b    以此来区分是要取已交的部分还是未交的部分
     * @return
     */
    public ArrayList<TaskManageEntity> getTaskManageShowOver(Boolean b){
        ArrayList<TaskManageEntity> tasks= new ArrayList<>();
        String sql;
        if (b){
            sql="select TaskNum,TaskId,OpusNum from "+TABLE_TASKMANAGESHOW+" where OpusNum>0 order by TaskId";
        }else{
            sql="select TaskNum,TaskId,OpusNum from "+TABLE_TASKMANAGESHOW+" where OpusNum=0 order by TaskId";
        }
        Cursor cursor=db.rawQuery(sql,null);
        if (cursor.moveToFirst()){
            do {
                TaskManageEntity task = new TaskManageEntity(cursor.getInt(cursor.getColumnIndex(SystemConfig.TASKID)),
                        cursor.getInt(cursor.getColumnIndex(SystemConfig.TASKNUM)));
                tasks.add(task);
            }while (cursor.moveToNext());
        }
        return tasks;
    }

    /**
     * 从本地数据库数据
     * @param id     taskId
     * @param num   OptNum
     * @return
     */
    public int updateTaskManageShowOver(int id,int num){

        ContentValues value=new ContentValues();
        value.put(SystemConfig.OPUSNUM,num);

        String[] args = {String.valueOf(id)};
        return db.update(TABLE_TASKMANAGESHOW, value, "TaskNum=?" ,args);
    }

//end TaskManage DB

}
