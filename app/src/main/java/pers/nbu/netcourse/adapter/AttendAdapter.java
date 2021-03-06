package pers.nbu.netcourse.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import pers.nbu.netcourse.R;
import pers.nbu.netcourse.entity.AttendEntity;

/**
 * Created by gracechan on 2015/11/9.
 */
public class AttendAdapter extends BaseAdapter{

    class ViewHolder{
        public TextView staname,title,status,relTime;
    }
    private List<AttendEntity> infoEntityList;
    private AttendEntity entity;
    private ViewHolder viewHolder;
    private Context context;
    private LayoutInflater inflater;

    public AttendAdapter(List<AttendEntity> infoEntityList, Context context) {
        this.infoEntityList = infoEntityList;
        this.context = context;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return infoEntityList.size();
    }

    @Override
    public Object getItem(int position) {
        return infoEntityList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView==null){
            viewHolder = new ViewHolder();
            convertView = inflater.inflate(R.layout.layout_attend, null);
            viewHolder.title = (TextView) convertView.findViewById(R.id.title);
            viewHolder.staname = (TextView) convertView.findViewById(R.id.staname);
            viewHolder.status = (TextView) convertView.findViewById(R.id.status);
            viewHolder.relTime = (TextView) convertView.findViewById(R.id.relTime);
            convertView.setTag(viewHolder);
        }
        else
            viewHolder = (ViewHolder) convertView.getTag();

        entity=infoEntityList.get(position);
        if (entity.getStatus().equals("LOADINGMORE")){
            viewHolder.title.setText("加载更多");
            Drawable img_back=context.getResources().getDrawable(R.drawable.icon_load);
            img_back.setBounds(0, 0, img_back.getMinimumWidth(), img_back.getMinimumHeight());
            viewHolder.title.setCompoundDrawables(img_back, null, null, null);
            viewHolder.title.setGravity(Gravity.CENTER);

            viewHolder.status.setVisibility(View.GONE);
            viewHolder.staname.setVisibility(View.GONE);
            viewHolder.relTime.setVisibility(View.GONE);
        }
        else {
            viewHolder.title.setText(entity.getCourName());
            viewHolder.staname.setText("[" + entity.getStaName() + "]");
            if (entity.getStaName().equals("正在考勤中"))
                viewHolder.staname.setTextColor(context.getResources().getColor(R.color.btn_unClick));
            else
                viewHolder.staname.setTextColor(context.getResources().getColor(R.color.announ_title));

            if (entity.getStatus().equals("缺课"))
                viewHolder.status.setTextColor(context.getResources().getColor(R.color.red_select));
            else
                viewHolder.status.setTextColor(context.getResources().getColor(R.color.announ_content));
            viewHolder.status.setText("状态： " + entity.getStatus());
            viewHolder.relTime.setText("时间： " + entity.getStatusTime().substring(0, 10));

            viewHolder.title.setCompoundDrawables(null, null, null, null);
            viewHolder.title.setGravity(Gravity.LEFT);

            viewHolder.status.setVisibility(View.VISIBLE);
            viewHolder.staname.setVisibility(View.VISIBLE);
            viewHolder.relTime.setVisibility(View.VISIBLE);

        }
        return convertView;
    }
}
