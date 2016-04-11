package pers.nbu.netcourse.adapter;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import pers.nbu.netcourse.R;
import pers.nbu.netcourse.entity.TaskEntity;

/**
 * Created by gracechan on 2015/11/9.
 */
public class TaskAdapter extends BaseAdapter{

    class ViewHolder{
        public TextView course,title,teacher,relTime;
    }
    private List<TaskEntity> infoEntityList;
    private TaskEntity entity;
    private ViewHolder viewHolder;
    private Context context;
    private LayoutInflater inflater;

    public TaskAdapter(List<TaskEntity> infoEntityList, Context context) {
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
            convertView = inflater.inflate(R.layout.layout_taskshow, null);
            viewHolder.title = (TextView) convertView.findViewById(R.id.title);
            viewHolder.course = (TextView) convertView.findViewById(R.id.course);
            viewHolder.teacher = (TextView) convertView.findViewById(R.id.teacher);
            viewHolder.relTime = (TextView) convertView.findViewById(R.id.relTime);
            convertView.setTag(viewHolder);
        }
        else
            viewHolder = (ViewHolder) convertView.getTag();

        entity=infoEntityList.get(position);
        viewHolder.title.setText(entity.getTaskTitle());
        viewHolder.course.setText("["+entity.getCourName()+"]");
        viewHolder.teacher.setText("教师： "+entity.getTeachName());
        viewHolder.relTime.setText("时间： " + entity.getTaskTime().substring(0, 10));
        return convertView;
    }
}
