package pers.nbu.netcourse;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;
import pers.nbu.netcourse.adapter.AnnAdapter;
import pers.nbu.netcourse.entity.AnnEntity;


public class MainActivity extends ActionBarActivity {

    //private TextView tv;
    private ListView annLsv;
    private AnnAdapter annAdapter;
    private List<AnnEntity> annLists;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //tv= (TextView) findViewById(R.id.touchback);
        initView();

    }

    protected void initView(){
        annLsv = (ListView) findViewById(R.id.announInfoLsv);
        annLists = new ArrayList<>();
        annAdapter = new AnnAdapter(annLists,getApplicationContext());
        annLsv.setAdapter(annAdapter);
    }

    public void doTouch(View view){
        /*获取所有公告信息*/
        AsyncHttpClient client = new AsyncHttpClient();
        //10.0.2.2
        //10.22.152.114
        String url="http://10.22.152.114:8080/netcourse/getAllAnnounInfo.action";
        client.post(url,new JsonHttpResponseHandler("utf-8"){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                //super.onSuccess(statusCode, headers, response);
                try {
                    if (response.getBoolean("success")){
                        annLists.clear();
                        annLists.addAll(JsonObj2Lists(response));
                        annAdapter.notifyDataSetChanged();
                        Log.i("success","true");
                    }
                    else{
                        Toast.makeText(getApplicationContext(),"success：fail",Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                Toast.makeText(getApplicationContext(),"连接失败",Toast.LENGTH_LONG).show();
            }

        });
    }

    protected ArrayList<AnnEntity> JsonObj2Lists(JSONObject jsonObject){
        ArrayList<AnnEntity> lists=new ArrayList<>();
        try {
            JSONArray jsonArray = jsonObject.getJSONArray("returnData");
            JSONObject jobj;
            AnnEntity entity;
            for (int i = 0; i < jsonArray.length() ; i++) {
                jobj = jsonArray.getJSONObject(i);
                entity = new AnnEntity(jobj.getString("annTitle"),
                                            jobj.getString("annCon")
                );
                lists.add(entity);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return lists;
    }


}
