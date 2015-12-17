package pers.nbu.netcourse.activity;

import android.app.Activity;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import pers.nbu.netcourse.ActivityCollector;
import pers.nbu.netcourse.R;

public class BaseActivity extends Activity {

	protected Toolbar mToolBar;
	protected TextView mTitle;
	protected TextView mRight,mLeft;
	protected Drawable drawable;

	protected Toolbar.LayoutParams lp;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ActivityCollector.addActivity(this);
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		ActivityCollector.removeActivity(this);
	}

	/**
	 * 设置标题
	 * @param title
	 */
	protected void setTitle(String title)
	{
		this.mTitle.setText(title);
	}

	/**
	 * 初始化导航栏
	 */
	protected void initToolBar() {
		mToolBar = (Toolbar) findViewById(R.id.toolbar);
		mToolBar.setBackgroundColor(getResources().getColor(R.color.toolbar_bg));
		//setSupportActionBar(mToolBar);

		//设置标题
		lp = new Toolbar.LayoutParams(Toolbar.LayoutParams.WRAP_CONTENT, Toolbar.LayoutParams.WRAP_CONTENT);
		lp.gravity = Gravity.CENTER;
		mTitle = new TextView(this);
		mTitle.setTextColor(getResources().getColor(R.color.black));
		mTitle.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
		mTitle.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
		mToolBar.addView(mTitle, lp);
	}

	/**
	 * 设置导航栏左侧返回
	 * @param s
	 * @param icon
	 */
	protected void setLeftOfToolbar(String s,Drawable icon){
		if (mLeft == null){
			lp = new Toolbar.LayoutParams(Toolbar.LayoutParams.WRAP_CONTENT, Toolbar.LayoutParams.WRAP_CONTENT);
			lp.gravity = Gravity.LEFT;
			mLeft = new TextView(this);
			//drawable=getResources().getDrawable(R.drawable.ic_launcher_blue);
			//drawable.setBounds(0, 0, 50, 50);
			//mRight.setCompoundDrawables(drawable, null, null, null);
			mLeft.setText("");
			mLeft.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
			mLeft.setTextColor(getResources().getColor(R.color.bottom_text_selected));
			mLeft.setGravity(Gravity.CENTER);
			mLeft.setPadding(10, 0, 0, 0);
			mToolBar.addView(mLeft, lp);
		}
		if (s!=null)  mLeft.setText(s);
		else  mLeft.setText("");
		if (icon != null) {
			drawable = icon;
			drawable.setBounds(0, 0, 50, 50);
			mLeft.setCompoundDrawables(drawable, null, null, null);
		}
		else
			mLeft.setCompoundDrawables(null, null, null, null);
		mLeft.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
	}

	/**
	 * 设置导航栏右侧操作，flag作为区分不同操作的标志
	 * @param s
	 * @param icon
	 * @param flag
	 */
	protected void setRightOfToolbar(String s,Drawable icon,final String flag){
		if (mRight == null){
			lp = new Toolbar.LayoutParams(Toolbar.LayoutParams.WRAP_CONTENT, Toolbar.LayoutParams.WRAP_CONTENT);
			lp.gravity = Gravity.RIGHT;
			mRight = new TextView(this);
			//drawable=getResources().getDrawable(R.drawable.ic_launcher_blue);
			//drawable.setBounds(0, 0, 50, 50);
			//mRight.setCompoundDrawables(drawable, null, null, null);
			mRight.setText("");
			mRight.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
			mRight.setTextColor(getResources().getColor(R.color.bottom_text_selected));
			mRight.setPadding(0, 0, 10, 0);
			mRight.setGravity(Gravity.CENTER);
			mToolBar.addView(mRight, lp);
		}

		if (s!=null)  mRight.setText(s);
		else  mRight.setText("");
		if (icon != null) {
			drawable = icon;
			drawable.setBounds(0, 0, 50, 50);
			mRight.setCompoundDrawables(drawable, null, null, null);
		}
		else
			mRight.setCompoundDrawables(null, null, null, null);


	}

	/**
	 * 是否隐藏导航栏，默认开启
	 * 注意，有个操作隐藏后之后操作要记得开回来
	 * @param b
	 */
	protected void hideToolbar(Boolean b){
		if (b) mToolBar.setVisibility(View.GONE);
		else mToolBar.setVisibility(View.VISIBLE);
	}
	
}
