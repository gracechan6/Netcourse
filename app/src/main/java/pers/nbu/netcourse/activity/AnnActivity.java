package pers.nbu.netcourse.activity;

import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.TextView;

import pers.nbu.netcourse.R;

public class AnnActivity extends BaseActivity {

    private TextView title,time,content,attach,tcName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ann);
        initToolBar();
        setTitle("通知公告");

        initView();
    }

    /**
     * 初始化控件
     */
    private void initView(){
        title = (TextView) findViewById(R.id.title);
        time = (TextView) findViewById(R.id.time);
        content = (TextView) findViewById(R.id.content);
        attach = (TextView) findViewById(R.id.attach);
        tcName = (TextView) findViewById(R.id.tcName);

        title.setText(getIntent().getStringExtra(MainActivity.ANNTITLE));
        time.append(getIntent().getStringExtra(MainActivity.ANNTIME));
        content.setText(Html.fromHtml(getIntent().getStringExtra(MainActivity.ANNCON)));
        tcName.setText("发布人：" + getIntent().getStringExtra(MainActivity.TEACHNAME) + " 课程：" + getIntent().getStringExtra(MainActivity.COURNAME));
        if (getIntent().getStringExtra(MainActivity.ANNURL)!=null || (getIntent().getStringExtra(MainActivity.ANNURL)).length()>0) {

            attach.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //单击附件名后当如何如何;暂定下载下来再做本地预览。

                }
            });
        }
        else attach.setVisibility(View.GONE);
    }

}
