package android_team.gymme_client.trainer.menage_trainig_sheet;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonObject;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import android_team.gymme_client.R;
import android_team.gymme_client.customer.CustomerSmallObject;
import android_team.gymme_client.gym.menage_customer.GymCustomersActivity;
import android_team.gymme_client.gym.menage_worker.GymMenageWorkerActivity;
import android_team.gymme_client.support.MyApplication;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class CustomTrainerTrainingSheetCustomerAdapter extends ArrayAdapter<TrainingSheetObject>  {

    private static ArrayList<TrainingSheetObject> sheets;
    private Activity context;

    public CustomTrainerTrainingSheetCustomerAdapter(Activity _context, ArrayList<TrainingSheetObject> sheets) {
        super(_context, R.layout.training_sheet_item, sheets);
        this.context = _context;
        this.sheets = sheets;
    }

    @Override
    public int getCount() {
        return sheets.size();
    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View r = convertView;
        CustomTrainerTrainingSheetCustomerAdapter.ViewHolder viewHolder = null;
        if (r == null) {
            LayoutInflater layoutInflater = context.getLayoutInflater();
            r = layoutInflater.inflate(R.layout.training_sheet_item, null);
            viewHolder = new CustomTrainerTrainingSheetCustomerAdapter.ViewHolder(r);
            r.setTag(viewHolder);
        } else {
            viewHolder = (CustomTrainerTrainingSheetCustomerAdapter.ViewHolder) r.getTag();
        }

        final String training_sheet_id = sheets.get(position).getTraining_sheet_id();
        final String customer_id = sheets.get(position).getCustomer_id();
        final String trainer_id = sheets.get(position).getTrainer_id();
        final String creation_date = sheets.get(position).getCreation_date();
        final String title = sheets.get(position).getTitle();
        final String description = sheets.get(position).getDescription();
        final String number_of_days = sheets.get(position).getNumber_of_days();
        final String strength = sheets.get(position).getStrength();
        final String name = sheets.get(position).getName();
        final String lastname = sheets.get(position).getLastname();

        viewHolder.tv_title.setText(title);
        viewHolder.tv_date.setText(creation_date);
        viewHolder.tv_days.setText("Numero giorni: " + number_of_days);

        viewHolder.btn_sheet_info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                InfoSheet(context, training_sheet_id, customer_id, trainer_id, creation_date, title, description, number_of_days, strength, name, lastname,  position);
            }
        });
        return r;
    }

    class ViewHolder {
        TextView tv_title, tv_date, tv_days;
        ImageView btn_sheet_info;

        ViewHolder(View v) {
            tv_title = v.findViewById(R.id.tv_sheet_item_title);
            tv_date = v.findViewById(R.id.tv_sheet_item_date);
            tv_days = v.findViewById(R.id.tv_sheet_item_days);
            btn_sheet_info = v.findViewById(R.id.btn_sheet_item_info);
        }
    }


    public void InfoSheet(Activity a, String training_sheet_id, String customer_id, String trainer_id, String creation_date, String title, String description, String number_of_days, String strength, String name, String lastname, Integer position) {
        CustomTrainerTrainingSheetCustomerAdapter.CustomDialogSheetInfo cdd = new CustomTrainerTrainingSheetCustomerAdapter.CustomDialogSheetInfo(a, training_sheet_id, customer_id, trainer_id, creation_date, title, description, number_of_days, strength, name, lastname,  position);
        cdd.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        cdd.show();
    }

    private class CustomDialogSheetInfo extends Dialog implements View.OnClickListener {

        public Activity c;
        public Button Esci;
        public TextView _creation_date, _title, _description, _numberOfDay, _trainer;
        private String training_sheet_id;
        private String customer_id;
        private String trainer_id;
        private String creation_date;
        private String title;
        private String description;
        private String number_of_days;
        private String strength;
        private String name;
        private String lastname;
        Integer position;

        public CustomDialogSheetInfo(Activity a, String training_sheet_id, String customer_id, String trainer_id, String creation_date, String title, String description, String number_of_days, String strength, String name, String lastname, Integer position)  {
            super(a);
            this.c = a;
            this.training_sheet_id = training_sheet_id;
            this.customer_id = customer_id;
            this.trainer_id = trainer_id;
            this.creation_date = creation_date.split("T")[0];
            this.title = title;
            this.description = description;
            this.number_of_days = number_of_days;
            this.strength = strength;
            this.name = name;
            this.lastname = lastname;
            this.position = position;
        }

        @Override
        protected void onCreate(Bundle savedInstanceState) {

            super.onCreate(savedInstanceState);
            requestWindowFeature(Window.FEATURE_NO_TITLE);
            setContentView(R.layout.dialog_info_sheet);
            Esci = (Button) findViewById(R.id.dialog_exit_from_info_sheet);

            _title = (TextView) findViewById(R.id.dialog_sheet_info_title);
            _description = (TextView) findViewById(R.id.tv_dialog_sheet_description);
            _creation_date = (TextView) findViewById(R.id.tv_dialog_sheet_date);
            _numberOfDay = (TextView) findViewById(R.id.tv_dialog_sheet_number_of_days);
            _trainer = (TextView) findViewById(R.id.tv_dialog_sheet_trainer);

            _title.setText(title);
            _description.setText(description);
            _numberOfDay.setText(number_of_days);
            _creation_date.setText(creation_date);
            _trainer.setText(name + " " + lastname);

            Esci.setOnClickListener(this);
        }


        @Override
        public void onClick(View v) {

            switch (v.getId()) {
                case R.id.dialog_exit_from_info_sheet:
                    //
                    dismiss();
                    break;
                default:
                    break;
            }
        }

    }

}
