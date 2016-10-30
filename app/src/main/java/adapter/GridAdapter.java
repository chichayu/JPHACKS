package adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import entity.Baggage;
import jp.ac.u_tokyo.jphacks.R;

/**
 * Created by xixi on 10/21/16.
 */
public class GridAdapter extends BaseAdapter {

     /*
    private static final int[] iconItems = {
            android.R.drawable.ic_yamato,
            android.R.drawable.ic_sagawa,
            android.R.drawable.ic_nihon
    };
    */

    private LayoutInflater inflater;
    private int layoutId;
    private List<Baggage> list = new ArrayList<Baggage>();

    static class ViewHolder {
        TextView name;
        TextView date;
        TextView starttime;
        TextView endtime;
       // ImageView icon;
    }

    public GridAdapter(Context context, int layoutId, ArrayList<Baggage> list) {
        super();
        this.inflater = LayoutInflater.from(context);
        this.layoutId = layoutId;
        this.list = list;
    }

    public boolean add(Baggage baggage){
        boolean ress = list.add(baggage);
        if(ress){
            notifyDataSetChanged();
        }
        return ress;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            Log.d("convertView == null()", "position= " + position);
            // main.xml の <GridView .../> に grid_items.xml を inflate して convertView とする
            convertView = inflater.inflate(layoutId, null);
            // ViewHolder を生成
            holder = new ViewHolder();
            holder.name = (TextView) convertView.findViewById(R.id.nameView);
            holder.date = (TextView) convertView.findViewById(R.id.dateView);
            holder.starttime = (TextView) convertView.findViewById(R.id.startView);
            holder.endtime = (TextView) convertView.findViewById(R.id.endView);
          //  holder.icon = (ImageView) convertView.findViewById(R.id.iconItem);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        Baggage bag = (Baggage) getItem(position);
        holder.name.setText(bag.getDeliveryname());
        holder.date.setText(bag.getScheduledday());
        holder.starttime.setText(bag.getScheduletime());
        holder.endtime.setText(bag.getScheduletime());

        if (bag.getCompanyflag() == "1") {
            convertView.setBackgroundColor(Color.GREEN);
            //   holder.icon.setImageResource(iconItems[0]);
        } else if (bag.getCompanyflag() == "2") {
            convertView.setBackgroundColor(Color.BLUE);
            //   holder.icon.setImageResource(iconItems[1]);
        } else if (bag.getCompanyflag() == "3") {
            convertView.setBackgroundColor(Color.parseColor("#444444"));
         //   holder.icon.setImageResource(iconItems[2]);
        }

        return convertView;
    }

    @Override
    public int getCount() {
        // 全要素数を返す
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

}

// コードで指定する場合
// ImageView image = (ImageView)findViewById(R.id.icon_1);
// image.setImageResource(android.R.drawable.ic_input_add);