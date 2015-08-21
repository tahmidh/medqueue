package com.bracu.thesis.medqueue;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;


import java.util.List;

public class AppointmentAdapter extends ArrayAdapter<AppointmentModel>{
    LayoutInflater inflater;

    public AppointmentAdapter(Context context, List<AppointmentModel> objects) {
        super(context, 0, objects);
        inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        AppointmentModel aAppoint = getItem(position);
        ViewHolder holder;
        if(convertView == null){
            convertView = inflater.inflate(R.layout.item, null);
            holder = new ViewHolder();
            holder.tvDate =(TextView)convertView.findViewById(R.id.tvDate);
            holder.tvTime =(TextView)convertView.findViewById(R.id.tvTime);
            holder.tvService = (TextView)convertView.findViewById(R.id.tvService);
            holder.tvPatient = (TextView)convertView.findViewById(R.id.tvPatient);
            convertView.setTag(holder);
        }
        holder=(ViewHolder)convertView.getTag();
        holder.tvDate.setText(aAppoint.getDate());
        holder.tvTime.setText("At "+ aAppoint.getTime());
        holder.tvService.setText("Apt for "+aAppoint.getService_name());
        holder.tvPatient.setText(aAppoint.getPatient());
        return convertView;
    }
    static class ViewHolder{
        public TextView tvDate;
        public TextView tvTime;
        public TextView tvService;
        public TextView tvPatient;
    }
}
