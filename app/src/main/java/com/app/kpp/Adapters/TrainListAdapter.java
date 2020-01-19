package com.app.kpp.Adapters;
import android.content.Context;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.app.kpp.Models.Train;
import com.app.kpp.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by User on 3/14/2017.
 */

public class TrainListAdapter extends ArrayAdapter<Train> {

    private static final String TAG = "PersonListAdapter";

    private Context mContext;
    private int mResource;
    private int lastPosition = -1;

    /**
     * Holds variables in a View
     */
    private static class ViewHolder {
        TextView start_st;
        TextView end_st;
        TextView start_date;
        TextView end_date;
        TextView start_time;
        TextView end_time;

    }

    /**
     * Default constructor for the PersonListAdapter
     * @param context
     * @param resource
     * @param objects
     */
    public TrainListAdapter(Context context, int resource, ArrayList<Train> objects) {
        super(context, resource, objects);
        mContext = context;
        mResource = resource;
    }

    //@NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        //get the persons information
        String start_st = getItem(position).getStarting_sration();
        String end_st = getItem(position).getEnd_station();
        String start_date = getItem(position).getDate_arival();
        String end_date = getItem(position).getDate_departure();
        String start_time = getItem(position).getTime_start();
        String end_time = getItem(position).getTime_end();


        //Create the person object with the information
        Train train = new Train(start_st,end_st,start_date,end_date,start_time,end_time);

        //create the view result for showing the animation
        final View result;

        //ViewHolder object
        ViewHolder holder;


        if(convertView == null){
            LayoutInflater inflater = LayoutInflater.from(mContext);
            convertView = inflater.inflate(mResource, parent, false);
            holder= new ViewHolder();
            holder.start_st = (TextView) convertView.findViewById(R.id.textView2);
            holder.end_st = (TextView) convertView.findViewById(R.id.textView3);
            holder.start_date = (TextView) convertView.findViewById(R.id.textView4);
            holder.end_date = (TextView) convertView.findViewById(R.id.textView5);
            holder.start_time = (TextView) convertView.findViewById(R.id.textView6);
            holder.end_time = (TextView) convertView.findViewById(R.id.textView7);

            result = convertView;

            convertView.setTag(holder);
        }
        else{
            holder = (ViewHolder) convertView.getTag();
            result = convertView;
        }



        holder.start_st.setText(train.getStarting_sration());
        holder.end_st.setText(train.getEnd_station());
        holder.start_date.setText(train.getDate_arival());
        holder.end_date.setText(train.getDate_departure());
        holder.start_time.setText(train.getTime_start());
        holder.end_time.setText(train.getTime_end());


        return convertView;
    }
}