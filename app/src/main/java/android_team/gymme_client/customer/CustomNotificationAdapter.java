package android_team.gymme_client.customer;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import android_team.gymme_client.R;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

public class CustomNotificationAdapter extends ArrayAdapter<String> {
    private ArrayList<String> items;

    private Activity context;

   /* public CustomNotificationAdapter(){
        items = new ArrayList<>();
    }

    public void add(String s){
        items.add(s);
        notifyItemInserted(items.size());
    }*/


    public CustomNotificationAdapter(Activity context, ArrayList<String> items) {
        super(context, R.layout.notification_item, items);
        this.context = context;
        this.items = items;
      /*  items = new ArrayList<>();
        items = list;
       */
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
        viewHolder.notification_text.setText(items.get(position));
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

    /*@NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater li = LayoutInflater.from(context);

        View v = li.inflate(android.R.layout.simple_list_item_1, parent, false);
        return new Holder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {
        String value = items.get(position);
        holder.textView.setText(value);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    class Holder extends RecyclerView.ViewHolder {

        TextView textView;
        public Holder(@NonNull View itemView) {
            super(itemView);

            textView = (TextView) itemView.findViewById(android.R.id.text1);
        }
    }

     */
}
