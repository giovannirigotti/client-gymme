package android_team.gymme_client.trainer.manage_training_sheet;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

import android_team.gymme_client.R;

public class CustomCreateDayAdapter extends ArrayAdapter<Integer> {

    private static ArrayList<Integer> days;
    private static Integer sheet_id;
    private Activity context;


    public CustomCreateDayAdapter(Activity _context, ArrayList<Integer> days, Integer sheet_id) {
        super(_context, R.layout.create_day_item, days);
        this.context = _context;
        this.days = days;
        this.sheet_id = sheet_id;

    }

    @Override
    public int getCount() {
        return days.size();
    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View r = convertView;
        CustomCreateDayAdapter.ViewHolder viewHolder = null;
        if (r == null) {
            LayoutInflater layoutInflater = context.getLayoutInflater();
            r = layoutInflater.inflate(R.layout.create_day_item, null);
            viewHolder = new CustomCreateDayAdapter.ViewHolder(r);
            r.setTag(viewHolder);
        } else {
            viewHolder = (CustomCreateDayAdapter.ViewHolder) r.getTag();
        }

        viewHolder.tv_testo.setText("Imposta giorno: " + String.valueOf(days.get(position)));

        viewHolder.btn_add_day.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // AGGIORNO POSIZIONE
                TrainerCreateDaysActivity.updatePosition(position);

                //VADO A CREARE IL GIORNO
                Log.e("REDIRECT", "Trainer Training single day Activity");
                Intent i = new Intent(context, TrainerCreateSingleDayActivity.class);
                i.putExtra("sheet_id", sheet_id);
                i.putExtra("seq", days.get(position));
                i.putExtra("user_id", TrainerCreateDaysActivity.getUserId());
                context.startActivity(i);
            }
        });
        return r;
    }

    class ViewHolder {
        TextView tv_testo;
        ImageView btn_add_day;

        ViewHolder(View v) {
            tv_testo = v.findViewById(R.id.tv_create_day_text);
            btn_add_day = v.findViewById(R.id.btn_create_day_plus);
        }
    }

}
