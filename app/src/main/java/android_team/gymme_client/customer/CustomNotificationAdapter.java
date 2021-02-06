package android_team.gymme_client.customer;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;


import android_team.gymme_client.R;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class CustomNotificationAdapter extends ArrayAdapter<String> {
    private ArrayList<String> items;
    String idString;
    private Activity context;

    public CustomNotificationAdapter(Activity context, ArrayList<String> items) {
        super(context, R.layout.notification_item, items);
        this.context = context;
        this.items = items;
    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View r = convertView;
        ViewHolder viewHolder = null;
        if (r == null) {
            LayoutInflater layoutInflater = context.getLayoutInflater();
            r = layoutInflater.inflate(R.layout.notification_item, null);
            viewHolder = new ViewHolder(r);
            r.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) r.getTag();
        }
        idString = items.get(position).split(",")[0];
        String text = items.get(position).split(",")[1];
        viewHolder.notification_text.setText(text);
        viewHolder.notification_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                CustomerNotificationActivity.deleteNotification(position, context);
            }
        });
        return r;
    }

    class ViewHolder {
        TextView notification_text;
        ImageView notification_close;

        ViewHolder(View v) {
            notification_text = v.findViewById(R.id.list_text);
            notification_close = v.findViewById(R.id.list_image);
        }
    }

}
