package pers.nbu.netcourse.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.widget.AdapterView;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;
import pers.nbu.netcourse.BaseApplication;
import pers.nbu.netcourse.JsonTransform;
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
    private JsonTransform jsonTransform=JsonTransform.getInstance();

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
            intent.putExtra(SystemConfig.ANNTITLE,annLists.get(position).getAnnTitle());
            intent.putExtra(SystemConfig.ANNCON,annLists.get(position).getAnnCon());
            intent.putExtra(SystemConfig.ANNTIME,annLists.get(position).getAnnTime());
            intent.putExtra(SystemConfig.ANNURL,annLists.get(position).getAnnUrl());
            intent.putExtra(SystemConfig.TEACHNAME,annLists.get(position).getTeachName());
            intent.putExtra(SystemConfig.COURNAME,annLists.get(position).getCourName());
            intent.putExtra(SystemConfig.ANNNUM,annLists.get(position).getAnnNum());
            startActivity(intent);
        }
    };

    /**
     * 公告下拉刷新
     */
    SwipeRefreshLayout.OnRefreshListener refreshListener = new SwipeRefreshLayout.OnRefreshListener() {
        @Override
        public void onRefresh() {
            //获取当前公告的最后一条消息的AnnNum，与服务器端比较看最新消息是否为这个id
            //是，则不操作,否，获取此id后的所有消息，然后保存到本地并更新进度
            ArrayList<AnnEntity> arrayList=db.getAnnInfo(1);
            if (arrayList!=null) {
                LogUtil.d("test", String.valueOf(arrayList.get(0).getAnnNum()));
                AsyncHttpClient client =((BaseApplication)getApplication()).getSharedHttpClient();
                RequestParams params = new RequestParams("annNum",arrayList.get(0).getAnnNum());
                client.post(SystemConfig.URL_UPDATEANN,params,new JsonHttpResponseHandler(SystemConfig.SERVER_CHAR_SET){
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        try {
                            if (response.getBoolean("success") && !response.isNull("returnData")) {
                                if (jsonTransform.turnToAnnLists(response)) {
                                    initAnn();
                                }
                                LogUtil.i("success", "true");
                            } else {
                                Toast.makeText(getApplicationContext(), "success：fail", Toast.LENGTH_LONG).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                        LogUtil.e(getClass().getSimpleName(), "刷新失败！");
                    }

                    @Override
                    public void onFinish() {
                        srlayout.setRefreshing(false);
                    }
                });
            }
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
            /*获取所有公告信息*/
            AsyncHttpClient client = ((BaseApplication)getApplication()).getSharedHttpClient();
            client.post(SystemConfig.URL_ALLANN, new JsonHttpResponseHandler(SystemConfig.SERVER_CHAR_SET) {
                @Override
                public void onStart() {
                    super.onStart();
                    dialog.show();
                }

                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    try {
                        if (response.getBoolean("success")) {
                            if (jsonTransform.turnToAnnLists(response)) {
                                annLists.clear();
                                annLists.addAll(db.getAnnInfo(showNum));
                                annAdapter.notifyDataSetChanged();
                            }
                            //JsonObj2Lists(response);
                            LogUtil.i("success", "true");
                        } else {
                            Toast.makeText(getApplicationContext(), "服务器端未有公告发布哦", Toast.LENGTH_LONG).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                    LogUtil.e(getClass().getSimpleName(), "连接失败了！");
                    Toast.makeText(getApplicationContext(), "连接失败", Toast.LENGTH_LONG).show();
                }

                @Override
                public void onFinish() {
                    dialog.dismiss();
                }
            });
        }
    }
}
