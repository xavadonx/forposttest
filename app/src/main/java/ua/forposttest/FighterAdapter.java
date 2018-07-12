package ua.forposttest;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class FighterAdapter extends ArrayAdapter<Fighter> {
    public FighterAdapter(@NonNull Context context, List<Fighter> fighters) {
        super(context, R.layout.list_item, fighters);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder;
        View rowView = convertView;

        if (rowView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            rowView = layoutInflater.inflate(R.layout.list_item, parent, false);

            holder = new ViewHolder();
            holder.team = rowView.findViewById(R.id.li_team);
            holder.health = rowView.findViewById(R.id.li_health);
            holder.clips = rowView.findViewById(R.id.li_clips);
            holder.ammo = rowView.findViewById(R.id.li_ammo);

            rowView.setTag(holder);
        } else {
            holder = (ViewHolder) rowView.getTag();
        }

        holder.team.setText(getItem(position).team);
        holder.health.setText(String.valueOf(getItem(position).health));
        holder.clips.setText(String.valueOf(getItem(position).clips));
        holder.ammo.setText(String.valueOf(getItem(position).ammo));
//        holder.image.setImageDrawable(ContextCompat.getDrawable(getContext(), getItem(position).getImage()));
//        holder.name.setText(getItem(position).getName());
//        holder.email.setText(getItem(position).getEmail());

        return rowView;
    }

    private static class ViewHolder {
        public TextView team;
        public TextView health;
        public TextView clips;
        public TextView ammo;
    }
}
