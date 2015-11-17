package pers.nbu.netcourse.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import pers.nbu.netcourse.R;
import pers.nbu.netcourse.entity.AnnounInfo;

/**
 * Created by gracechan on 2015/11/9.
 */
public class AnnounInfoAdapter extends BaseAdapter{

    class ViewHolder{
        public View unread;
        public TextView title,content;
    }
    private List<AnnounInfo> announInfoList;
    private ViewHolder viewHolder;
    private Context context;
    private LayoutInflater inflater;


    @Override
    public int getCount() {
        return announInfoList.size();
    }

    @Override
    public Object getItem(int position) {
        return announInfoList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView==null){
            viewHolder = new ViewHolder();
            convertView = inflater.inflate(R.layout.layout_announinfo,null);
            viewHolder.unread = convertView.findViewById(R.id.unread);
            viewHolder.title = (TextView) convertView.findViewById(R.id.title);
            viewHolder.content = (TextView) convertView.findViewById(R.id.content);
            convertView.setTag(viewHolder);
        }

        return convertView;
    }
}
