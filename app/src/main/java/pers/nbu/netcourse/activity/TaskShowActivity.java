package pers.nbu.netcourse.activity;

import android.os.Bundle;
import android.widget.TextView;

import pers.nbu.netcourse.R;
import pers.nbu.netcourse.config.SystemConfig;

public class TaskShowActivity extends BaseActivity {

    private TextView title,time,require,endTime,tcName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_show);
        initToolBar();
        setTitle("任务");
        setRightOfToolbar(true);
        initView();
    }

    /**
     * 初始化控件
     */
    private void initView(){
        title = (TextView) findViewById(R.id.title);
        time = (TextView) findViewById(R.id.time);
        require = (TextView) findViewById(R.id.require);
        endTime = (TextView) findViewById(R.id.endTime);
        tcName = (TextView) findViewById(R.id.tcName);

        title.setText(getIntent().getStringExtra(SystemConfig.TASKTITLE));
        time.append(getIntent().getStringExtra(SystemConfig.TASKTIME));
        require.append(getIntent().getStringExtra(SystemConfig.TASKREQUIRE));
        tcName.setText("发布人：" + getIntent().getStringExtra(SystemConfig.TEACHNAME) + " 课程：" + getIntent().getStringExtra(SystemConfig.COURNAME));
        endTime.append(getIntent().getStringExtra(SystemConfig.ENDTIME));

    }
}
