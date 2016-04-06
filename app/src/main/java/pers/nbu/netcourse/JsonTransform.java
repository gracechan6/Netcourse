package pers.nbu.netcourse;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import pers.nbu.netcourse.db.DB;
import pers.nbu.netcourse.entity.AnnEntity;

/**
 * Created by GraceChan on 2016/4/6.
 */
public class JsonTransform {
    private static JsonTransform jsonTransform;
    private DB db= DB.getInstance();

    private JsonTransform(){

    }

    public synchronized static JsonTransform getInstance(){
        if (jsonTransform==null){
            jsonTransform=new JsonTransform();
        }
        return jsonTransform;
    }

    /**
     * 将获取到的json格式公告信息转换成公告格式
     * @param jsonObject
     * @return
     */
    public Boolean turnToAnnLists(JSONObject jsonObject) {
        ArrayList<AnnEntity> lists = new ArrayList<>();
        int annNum = 0;
        //boolean find = false;
        //annNum = db.ifexistData(DB.TABLE_ANNINFO);
        //解析获取到的json数据串（所有公告信息）
        try {
            JSONArray jsonArray = jsonObject.getJSONArray("returnData");
            JSONObject jobj;
            AnnEntity entity;
            for (int i = 0; i < jsonArray.length(); i++) {
                jobj = jsonArray.getJSONObject(i);
//                if (annNum > 0) {
//                    if (annNum == jobj.getInt("annNum")) {
//                        entity = new AnnEntity(jobj.getInt("annNum"), jobj.getString("annTitle"),
//                                jobj.getString("annCon"), jobj.getString("annUrl"),
//                                jobj.getString("annTime"), jobj.getString("teachName"),
//                                jobj.getString("courName"));
//                        lists.add(entity);
//                        find = true;
//                    } else if (find) {
//                        entity = new AnnEntity(jobj.getInt("annNum"), jobj.getString("annTitle"),
//                                jobj.getString("annCon"), jobj.getString("annUrl"),
//                                jobj.getString("annTime"), jobj.getString("teachName"),
//                                jobj.getString("courName"));
//                        lists.add(entity);
//                    }
//                } else {
                    entity = new AnnEntity(jobj.getInt("annNum"),
                            jobj.getString("annTitle"),
                            jobj.getString("annCon"),
                            jobj.getString("annUrl"),
                            jobj.getString("annTime"),
                            jobj.getString("teachName"),
                            jobj.getString("courName")
                    );
                    lists.add(entity);
//                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (lists.size() > 0) {
            db.saveAnnInfo(lists);
            return true;
        }
        return false;
    }
}
