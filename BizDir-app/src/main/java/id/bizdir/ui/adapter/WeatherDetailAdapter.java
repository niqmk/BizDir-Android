package id.bizdir.ui.adapter;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import id.bizdir.R;
import id.bizdir.model.Weather;
import id.bizdir.util.Helpers;

/**
 * Created by Hendry on 01/06/2015.
 */
public class WeatherDetailAdapter extends ArrayAdapter<Weather> {
    private List<Weather> list;
    private Activity context;
    private int layoutId;

    public WeatherDetailAdapter(Activity context, int layoutId, List<Weather> list) {
        super(context, layoutId, list);
        this.layoutId = layoutId;
        this.context = context;
        this.list = list;
    }

    static class ViewHolder {
        protected TextView textDay;
        protected ImageView imageWeather;
        protected TextView textMax;
        protected TextView textMin;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view;
        if (convertView == null) {
            LayoutInflater inflator = context.getLayoutInflater();
            view = inflator.inflate(layoutId, null);
            final ViewHolder viewHolder = new ViewHolder();
            viewHolder.textDay = (TextView) view.findViewById(R.id.textDay);
            viewHolder.imageWeather = (ImageView) view.findViewById(R.id.imageWeather);
            viewHolder.textMax = (TextView) view.findViewById(R.id.textMax);
            viewHolder.textMin = (TextView) view.findViewById(R.id.textMin);
            view.setTag(viewHolder);
        } else {
            view = convertView;
        }
        final ViewHolder holder = (ViewHolder) view.getTag();
        final Weather obj = list.get(position);
        if (obj != null) {
            Integer max = obj.getWeatherMax();
            Integer min = obj.getWeatherMin();
            Drawable iconWeather = Helpers.getWeather(obj.getWeather());
            holder.textDay.setText(obj.getDay());
            holder.textMax.setText(max.toString() + "°C");
            holder.textMin.setText(min.toString() + "°C");
            holder.imageWeather.setImageDrawable(iconWeather);
        }

        return view;
    }

}
