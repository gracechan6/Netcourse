package pers.nbu.netcourse.activity;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.ScrollView;
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
import pers.nbu.netcourse.adapter.TaskAdapter;
import pers.nbu.netcourse.config.SystemConfig;
import pers.nbu.netcourse.db.DB;
import pers.nbu.netcourse.entity.AnnEntity;
import pers.nbu.netcourse.entity.TaskEntity;
import pers.nbu.netcourse.fragment.BottomFragment;
import pers.nbu.netcourse.util.LogUtil;
import pers.nbu.netcourse.util.PreferenceUtils;
import pers.nbu.netcourse.view.ListViewForScrollView;


public class MainActivity extends BaseActivity {

    /*公告任务栏目需要用到的控件及数据定义*/
    private SwipeRefreshLayout srlayout,taskLayout;
    private SwipeMenuListView annLsv,taskLsv;
    private AnnAdapter annAdapter,annShowAdapter;
    private TaskAdapter taskAdapter,taskShowAdapter;
    private List<AnnEntity> annLists;
    private List<TaskEntity> taskLists;

    //加载更多
    private TextView footer;
    /*首页*/
    private ScrollView index;
    private ListViewForScrollView annShowLsv,taskShowLsv;

    /*更多*/
    private LinearLayout more;

    //底部导航栏
    private BottomFragment bottomFragment;
    private RadioGroup radioGroup;

    private DB db=DB.getInstance();
    private JsonTransform jsonTransform=JsonTransform.getInstance();

    private static int showNum=8,taskShowNum=8;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        bottomFragment = (BottomFragment) getFragmentManager().
                findFragmentById(R.id.bottomFragment);
        radioGroup = (RadioGroup) findViewById(R.id.radioGroup);
        radioGroup.setOnCheckedChangeListener(checkedChangeListener);

        initToolBar();
//        setTitle(getString(R.string.index));

    }

    /**
     * 初始化控件
     */
    protected void initView(){
        footer= (TextView) getLayoutInflater().inflate(R.layout.listview_foot,null);
        annLists = new ArrayList<>();
        taskLists = new ArrayList<>();

        //index
        index = (ScrollView) findViewById(R.id.index);
        annShowLsv = (ListViewForScrollView) findViewById(R.id.annShowLsv);
        taskShowLsv = (ListViewForScrollView) findViewById(R.id.taskShowLsv);
        annShowAdapter = new AnnAdapter(annLists,getApplicationContext());
        taskShowAdapter = new TaskAdapter(taskLists,getApplicationContext());
        annShowLsv.setAdapter(annShowAdapter);
        taskShowLsv.setAdapter(taskShowAdapter);
        annShowLsv.setOnItemClickListener(annClickListener);
        taskShowLsv.setOnItemClickListener(annClickListener);

        //AnnShow
        srlayout = (SwipeRefreshLayout) findViewById(R.id.srlayout);
        srlayout.setOnRefreshListener(refreshListener);
        annLsv = (SwipeMenuListView) findViewById(R.id.announInfoLsv);
        annAdapter = new AnnAdapter(annLists,getApplicationContext());
        annLsv.setAdapter(annAdapter);
        annLsv.setOnItemClickListener(annClickListener);

        //TaskShow
        taskLayout = (SwipeRefreshLayout) findViewById(R.id.taskLayout);
        taskLayout.setOnRefreshListener(taskShowRefresh);
        taskLsv = (SwipeMenuListView) findViewById(R.id.taskLsv);
        taskAdapter = new TaskAdapter(taskLists,getApplicationContext());
        taskLsv.setAdapter(taskAdapter);
        taskLsv.setOnItemClickListener(annClickListener);

        //more
        more = (LinearLayout) findViewById(R.id.more);
    }

    //点击
    protected AdapterView.OnItemClickListener annClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
            if(adapterView.getId()==annLsv.getId() || adapterView.getId()== annShowLsv.getId() ){
                if (position+1==annLists.size()){
                    showNum +=7;
                    getAnnFromDB(2,showNum);
                }else {
                    Intent intent = new Intent(getApplicationContext(), AnnActivity.class);
                    intent.putExtra(SystemConfig.ANNTITLE, annLists.get(position).getAnnTitle());
                    intent.putExtra(SystemConfig.ANNCON, annLists.get(position).getAnnCon());
                    intent.putExtra(SystemConfig.ANNTIME, annLists.get(position).getAnnTime());
                    intent.putExtra(SystemConfig.ANNURL, annLists.get(position).getAnnUrl());
                    intent.putExtra(SystemConfig.TEACHNAME, annLists.get(position).getTeachName());
                    intent.putExtra(SystemConfig.COURNAME, annLists.get(position).getCourName());
                    intent.putExtra(SystemConfig.ANNNUM, annLists.get(position).getAnnNum());
                    startActivity(intent);
                }
            }else if (adapterView.getId()==taskLsv.getId() || adapterView.getId()==taskShowLsv.getId()){
                if (position+1==taskLists.size()) {
                    taskShowNum+=7;
                    getTaskFromDB(3,taskShowNum);
                }
                Intent intent = new Intent(getApplicationContext(),TaskShowActivity.class);
                intent.putExtra(SystemConfig.TASKTITLE, taskLists.get(position).getTaskTitle());
                intent.putExtra(SystemConfig.TASKREQUIRE,taskLists.get(position).getTaskRequire());
                intent.putExtra(SystemConfig.TASKTIME,taskLists.get(position).getTaskTime());
                intent.putExtra(SystemConfig.ENDTIME,taskLists.get(position).getEndTime());
                intent.putExtra(SystemConfig.TEACHNAME,taskLists.get(position).getTeachName());
                intent.putExtra(SystemConfig.COURNAME,taskLists.get(position).getCourName());
                intent.putExtra(SystemConfig.TASKNUM,taskLists.get(position).getTaskNum());
                intent.putExtra("flag","1");//代表此activity传入
                startActivity(intent);
            }
        }
    };

    /**
     * 底部标签栏操作
     */
    RadioGroup.OnCheckedChangeListener checkedChangeListener=new RadioGroup.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {
            int rbId = group.getCheckedRadioButtonId();
            switch (rbId) {
                case R.id.index:
                    showView(1);setTitle("高校云课堂");init(1,3);
                    break;
                case R.id.message:
                    showView(2);setTitle("通知公告");initAnn(2,showNum);
                    break;
                case R.id.task:
                    showView(3);setTitle("任务");initTask(3,taskShowNum);
                    break;
                case R.id.more:
                    if (PreferenceUtils.getLOGINVAL())
                    {showView(4);setTitle("更多");}
                    else
                        showLogin();
                    break;
                case R.id.me:
                    if (PreferenceUtils.getLOGINVAL())
                    {showView(5);setTitle("我");}
                    break;
                default:
                    break;
            }
        }
    };

    /**
     * 底部控件选择控制显隐
     * @param select
     */
    protected void showView(int select){
        switch (select){
            case 1: index.setVisibility(View.VISIBLE);
                    srlayout.setVisibility(View.GONE);
                    taskLayout.setVisibility(View.GONE);
                    more.setVisibility(View.GONE);
                    break;
            case 2: index.setVisibility(View.GONE);
                    srlayout.setVisibility(View.VISIBLE);
                    taskLayout.setVisibility(View.GONE);
                    more.setVisibility(View.GONE);
                    break;
            case 3: index.setVisibility(View.GONE);
                    srlayout.setVisibility(View.GONE);
                    taskLayout.setVisibility(View.VISIBLE);
                    more.setVisibility(View.GONE);
                    break;
            case 4: index.setVisibility(View.GONE);
                    srlayout.setVisibility(View.GONE);
                    taskLayout.setVisibility(View.GONE);
                    more.setVisibility(View.VISIBLE);
                    break;
            case 5: index.setVisibility(View.GONE);
                    srlayout.setVisibility(View.GONE);
                    taskLayout.setVisibility(View.GONE);
                    more.setVisibility(View.GONE);
                    break;
            default:break;
        }
    }
//首页========start
    protected void init(int flag,int num){
        initAnn(flag, num);
        initTask(flag, num);
    }
//首页========end


//公告========start
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
                                    getAnnFromDB(2, showNum);
                                }
                                LogUtil.i("success", "true");
                            } else {
                                Toast.makeText(getApplicationContext(), "已是最新", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {

                        LogUtil.e(getClass().getSimpleName(), "刷新失败！");
                        Toast.makeText(getApplicationContext(), "刷新失败！", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFinish() {
                        srlayout.setRefreshing(false);
                    }
                });
            }
            //srlayout.setRefreshing(false);
        }
    };

    /**
     * 更新本地公告显示数据
     * @param flag   区分 是首页 1 显示还是公告栏 2显示
     * @param show
     */
    protected void getAnnFromDB(int flag,int show){
        annLists.clear();
        annLists.addAll(db.getAnnInfo(show));
        if (annLists.size()>= 8 && annLists.size()<db.countData(db.TABLE_ANNSHOW)) {
            annLists.add(new AnnEntity("LOADINGMORE","..."));
        }
        if (flag==1) annShowAdapter.notifyDataSetChanged();
        else annAdapter.notifyDataSetChanged();
    }
    /**
     * 初始化公告信息
     * @param flag 区分首页显示还是公告栏显示
     * @param num 显示条数
     */
    protected void initAnn(int flag,int num){
        final int flags=flag,nums=num;
        if (db.ifexistData(DB.TABLE_ANNSHOW,SystemConfig.ANNNUM)>0){
            getAnnFromDB(flags,nums);
        }
        else{
            /*获取所有公告信息*/
            AsyncHttpClient client = ((BaseApplication)getApplication()).getSharedHttpClient();
            client.post(SystemConfig.URL_ALLANN, new JsonHttpResponseHandler(SystemConfig.SERVER_CHAR_SET) {
                @Override
                public void onStart() {
                    dialog.show();
                }

                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    try {
                        if (response.getBoolean("success") && !response.isNull("returnData")) {
                            if (jsonTransform.turnToAnnLists(response)) {
                                getAnnFromDB(flags, nums);
                            }
                            //JsonObj2Lists(response);
                            LogUtil.i("success", "true");
                        } else {
                            Toast.makeText(getApplicationContext(), "服务器端未有公告发布!", Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                    LogUtil.e("test", "连接失败！"+throwable.toString());
                    Toast.makeText(getApplicationContext(), "连接失败", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onFinish() {
                    dialog.dismiss();
                }
            });
        }
    }
//公告========end

//任务========start
    /**
     * 更新本地任务显示数据
     * @param flag  区分 是首页1显示还是任务栏3显示
     * @param show 显示数量
     */
    protected void getTaskFromDB(int flag,int show){
        taskLists.clear();
        taskLists.addAll(db.getTaskShow(show));
        if (taskLists.size()>= 8 && taskLists.size()<db.countData(db.TABLE_TASKSHOW)) {
            taskLists.add(new TaskEntity("LOADINGMORE"));
        }
        if (flag==1) taskShowAdapter.notifyDataSetChanged();
        else taskAdapter.notifyDataSetChanged();
    }
    /**
     * 初始化任务信息
     */
    protected void initTask(int flag,int num){
        final int flags=flag;
        if (db.ifexistData(DB.TABLE_TASKSHOW,SystemConfig.TASKNUM)>0){
            getTaskFromDB(flags, num);
        }
        else{
            /*获取任务信息*/
            getTask(1,0,num,flag);
        }
    }

    /**
     * 从服务器端获取数据
     */
    protected void getTask(final int flag,int taskNum, final int showTask, final int flags){
        AsyncHttpClient client = ((BaseApplication)getApplication()).getSharedHttpClient();
        RequestParams params = new RequestParams("TaskNum",taskNum);
        client.post(SystemConfig.URL_GETTASK,params, new JsonHttpResponseHandler(SystemConfig.SERVER_CHAR_SET) {
            @Override
            public void onStart() {
                super.onStart();
                if (flag==1){dialog.show();}
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    if (response.getBoolean("success") && !response.isNull("returnData")) {
                        if (jsonTransform.turnToTaskLists(response)) {
                            getTaskFromDB(flags, showTask);
                        }
                        LogUtil.i("success", "true");
                    } else {
                        if (flag==1) Toast.makeText(getApplicationContext(), "服务器端未有任务发布!", Toast.LENGTH_SHORT).show();
                        else Toast.makeText(getApplicationContext(), "已是最新", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                LogUtil.e("test", "连接失败！");
                if (flag==1)
                    Toast.makeText(getApplicationContext(), "连接失败", Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(getApplicationContext(), "刷新失败", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFinish() {
                if (flag==1){dialog.dismiss();}
                else taskLayout.setRefreshing(false);
            }
        });
    }

    /**
     * 任务下拉刷新
     */
    SwipeRefreshLayout.OnRefreshListener taskShowRefresh = new SwipeRefreshLayout.OnRefreshListener() {
        @Override
        public void onRefresh() {
            //获取当前任务的最后一条消息的AnnNum，与服务器端比较看最新消息是否为这个id
            //是，则不操作,否，获取此id后的所有消息，然后保存到本地并更新进度
            ArrayList<TaskEntity> arrayList = db.getTaskShow(1);
            if (arrayList != null) {
                LogUtil.d("test", String.valueOf(arrayList.get(0).getTaskNum()));
                getTask(2, arrayList.get(0).getTaskNum(),3,taskShowNum);
            }
        }
    };

    protected void showLogin(){
        AlertDialog.Builder builder  = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("未登录") ;
        builder.setMessage("是否登录？") ;
        builder.setPositiveButton("否", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                LogUtil.d("test", String.valueOf(i));
            }
        });
        builder.setNegativeButton("是", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                LogUtil.d("test",String.valueOf(i));
                startActivityForResult(new Intent(MainActivity.this, LoginActivity.class), 4);
            }
        });
        builder.show();
    }
//任务========end


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==4){
            if(resultCode == Activity.RESULT_OK) {
                LogUtil.i("test", "更多返回");
                showView(4);setTitle("更多");
            }
        }else if (requestCode==5){
            LogUtil.i("test","我  返回");
        }

    }

//更多========start

    /**
     * 任务管理
     * @param view
     */
    public void doTaskManage(View view){
        startActivity(new Intent(getApplicationContext(),TaskManageActivity.class));
    }

    /**
     * 出勤管理
     * @param view
     */
    public void doAttendManage(View view){
        startActivity(new Intent(getApplicationContext(),AttendManageActivity.class));
    }

//更多========end
}
