package com.bracu.thesis.medqueue;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Tahmid on 22-Aug-15.
 */
public class PrescriptionAdapter extends ArrayAdapter<PrescriptionModel>{

    LayoutInflater inflater;

    public PrescriptionAdapter(Context context, List<PrescriptionModel> objects) {
        super(context, 0, objects);
        inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        PrescriptionModel aPrescription = getItem(position);
        ViewHolder holder;
        if(convertView==null){
            convertView = inflater.inflate(R.layout.item_pres,null);
            holder = new ViewHolder();
            holder.tvMed = (TextView)convertView.findViewById(R.id.tvMed);
            holder.tvDose = (TextView)convertView.findViewById(R.id.tvDose);
            holder.tvFreq = (TextView)convertView.findViewById(R.id.tvFreq);
            convertView.setTag(holder);
        }
        holder = (ViewHolder)convertView.getTag();
        holder.tvMed.setText(aPrescription.getMedName());
        holder.tvFreq.setText(aPrescription.getDose());
        holder.tvDose.setText(aPrescription.getFreq());
        return convertView;
    }
    static class ViewHolder{
        public TextView tvMed;
        public TextView tvDose;
        public TextView tvFreq;
    }
}
