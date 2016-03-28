package pers.nbu.netcourse.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;
import pers.nbu.netcourse.BaseApplication;
import pers.nbu.netcourse.R;
import pers.nbu.netcourse.adapter.AnnAdapter;
import pers.nbu.netcourse.config.SystemConfig;
import pers.nbu.netcourse.db.DB;
import pers.nbu.netcourse.entity.AnnEntity;
import pers.nbu.netcourse.fragment.BottomFragment;
import pers.nbu.netcourse.util.LogUtil;


public class MainActivity extends BaseActivity {

    /*公告栏目需要用到的控件及数据定义*/
    private SwipeRefreshLayout srlayout;
    private SwipeMenuListView annLsv;
    private AnnAdapter annAdapter;
    private List<AnnEntity> annLists;

    private TextView temporary;

    //底部导航栏
    private BottomFragment bottomFragment;
    private RadioGroup radioGroup;

    private DB db=DB.getInstance();

    private static int showNum=10;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        bottomFragment = (BottomFragment) getFragmentManager().
                findFragmentById(R.id.bottomFragment);
        radioGroup = (RadioGroup) findViewById(R.id.radioGroup);
        radioGroup.setOnCheckedChangeListener(checkedChangeListener);

        temporary= (TextView) findViewById(R.id.temporary);

        initToolBar();
        setTitle(getString(R.string.index));

    }

    public static final String ANNTITLE="annTitle";
    public static final String ANNCON="annCon";
    public static final String ANNTIME="annTime";
    public static final String ANNURL="annUrl";
    public static final String TEACHNAME="teachName";
    public static final String COURNAME="courName";
    public static final String ANNNUM="annNum";

    protected void initView(){
        srlayout = (SwipeRefreshLayout) findViewById(R.id.srlayout);
        srlayout.setOnRefreshListener(refreshListener);

        annLsv = (SwipeMenuListView) findViewById(R.id.announInfoLsv);
        annLists = new ArrayList<>();
        annAdapter = new AnnAdapter(annLists,getApplicationContext());
        annLsv.setAdapter(annAdapter);
        annLsv.setOnItemClickListener(annClickListener);
    }

    protected AdapterView.OnItemClickListener annClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
            Intent intent = new Intent(getApplicationContext(),AnnActivity.class);
            intent.putExtra(ANNTITLE,annLists.get(position).getAnnTitle());
            intent.putExtra(ANNCON,annLists.get(position).getAnnCon());
            intent.putExtra(ANNTIME,annLists.get(position).getAnnTime());
            intent.putExtra(ANNURL,annLists.get(position).getAnnUrl());
            intent.putExtra(TEACHNAME,annLists.get(position).getTeachName());
            intent.putExtra(COURNAME,annLists.get(position).getCourName());
            intent.putExtra(ANNNUM,annLists.get(position).getAnnNum());
            startActivity(intent);
        }
    };

    /**
     * 公告下拉刷新
     */
    SwipeRefreshLayout.OnRefreshListener refreshListener = new SwipeRefreshLayout.OnRefreshListener() {
        @Override
        public void onRefresh() {
            LogUtil.i(getApplication().getClass().getSimpleName().toString(), "下拉刷新");
            //获取当前公告的最后一条消息的AnnNum，与服务器端比较看最新消息是否为这个id
            //是，则不操作
            //否，获取此id后的所有消息，然后保存到本地并更新进度
            srlayout.setRefreshing(false);
        }
    };


    /**
     * 底部标签栏操作
     */
    RadioGroup.OnCheckedChangeListener checkedChangeListener=new RadioGroup.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {
            int rbId = group.getCheckedRadioButtonId();

            //hideToolbar(false);
            switch (rbId) {
                case R.id.index:
                    srlayout.setVisibility(View.GONE);
                    break;
                case R.id.message:
                    srlayout.setVisibility(View.VISIBLE);
                    setTitle("通知公告");
                    initAnn();
                    break;
                case R.id.task:
                    srlayout.setVisibility(View.GONE);
                    break;
                case R.id.more:
                    srlayout.setVisibility(View.GONE);
                    break;
                case R.id.me:
                    srlayout.setVisibility(View.GONE);
                    break;
                default:
                    break;
            }
        }
    };


    /**
     * 初始化公告信息
     */
    protected void initAnn(){
        if (db.ifexistData(DB.TABLE_ANNINFO)>0){
            annLists.clear();
            annLists.addAll(db.getAnnInfo(showNum));
            annAdapter.notifyDataSetChanged();
        }
        else{
            getAnn();
        }
    }

    /**
     * 从服务器端下载公告信息
     */
    protected void getAnn(){
        /*获取所有公告信息*/
        AsyncHttpClient client = ((BaseApplication)getApplication()).getSharedHttpClient();
        client.post(SystemConfig.URL_ALLANN,new JsonHttpResponseHandler("utf-8"){
            @Override
            public void onStart() {
                super.onStart();
                dialog.show();
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                try {
                    if (response.getBoolean("success")){
                        JsonObj2Lists(response);
                        LogUtil.i("success", "true");
                    }
                    else{
                        Toast.makeText(getApplicationContext(),"success：fail",Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {e.printStackTrace();}
            }
            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                Toast.makeText(getApplicationContext(),"连接失败",Toast.LENGTH_LONG).show();
            }

            @Override
            public void onFinish() {
                super.onFinish();
                dialog.dismiss();
            }
        });
    }

    /**
     * 将获取到的json格式公告信息转换成相应格式
     * @param jsonObject
     * @return
     */
    protected void JsonObj2Lists(JSONObject jsonObject){
        ArrayList<AnnEntity> lists=new ArrayList<>();
        int annNum=0;
        boolean find=false;
        annNum=db.ifexistData(DB.TABLE_ANNINFO);
        //解析获取到的json数据串（所有公告信息）
        try {
            JSONArray jsonArray = jsonObject.getJSONArray("returnData");
            JSONObject jobj;
            AnnEntity entity;
            for (int i = 0; i < jsonArray.length() ; i++) {
                jobj = jsonArray.getJSONObject(i);
                if (annNum>0){
                    if (annNum == jobj.getInt("annNum"))
                    {
                        entity = new AnnEntity(jobj.getInt("annNum"),jobj.getString("annTitle"),
                                jobj.getString("annCon"),jobj.getString("annUrl"),
                                jobj.getString("annTime"),jobj.getString("teachName"),
                                jobj.getString("courName"));
                        lists.add(entity);
                        find=true;
                    }else if (find){
                        entity = new AnnEntity(jobj.getInt("annNum"),jobj.getString("annTitle"),
                                jobj.getString("annCon"),jobj.getString("annUrl"),
                                jobj.getString("annTime"),jobj.getString("teachName"),
                                jobj.getString("courName"));
                        lists.add(entity);
                    }
                }
                else {
                    entity = new AnnEntity(jobj.getInt("annNum"),
                            jobj.getString("annTitle"),
                            jobj.getString("annCon"),
                            jobj.getString("annUrl"),
                            jobj.getString("annTime"),
                            jobj.getString("teachName"),
                            jobj.getString("courName")
                    );
                    lists.add(entity);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (lists.size()>0){
            db.saveAnnInfo(lists);
            annLists.clear();
            annLists.addAll(db.getAnnInfo(showNum));
            annAdapter.notifyDataSetChanged();
        }
    }
}
