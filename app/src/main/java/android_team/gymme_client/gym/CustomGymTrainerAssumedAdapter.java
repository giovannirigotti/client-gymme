package android_team.gymme_client.gym;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

import android_team.gymme_client.R;
import android_team.gymme_client.trainer.TrainerObject;

public class CustomGymTrainerAssumedAdapter extends ArrayAdapter<TrainerObject> {

    private static ArrayList<TrainerObject> trainers;
    private Activity context;

    public CustomGymTrainerAssumedAdapter(Activity _context, ArrayList<TrainerObject> _trainers) {
        super(_context, R.layout.notification_item, _trainers);
        this.context = _context;
        this.trainers = _trainers;
    }

    @Override
    public int getCount() {
        return trainers.size();
    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull final ViewGroup parent) {
        View r = convertView;
        CustomGymTrainerAssumedAdapter.ViewHolder viewHolder = null;
        if (r == null) {
            LayoutInflater layoutInflater = context.getLayoutInflater();
            r = layoutInflater.inflate(R.layout.gym_trainer_assumed_item, null);
            viewHolder = new CustomGymTrainerAssumedAdapter.ViewHolder(r);
            r.setTag(viewHolder);
        } else {
            viewHolder = (CustomGymTrainerAssumedAdapter.ViewHolder) r.getTag();
        }

        final String trainer_id = trainers.get(position).user_id;
        final String name = trainers.get(position).name;
        final String lastname = trainers.get(position).lastname;
        final String email = trainers.get(position).email;
        final String qualification = trainers.get(position).qualification;
        final String fiscal_code = trainers.get(position).fiscal_code;

        viewHolder.tv_gym_trainer_assumed_name.setText(name);
        viewHolder.tv_gym_trainer_assumed_lastname.setText(lastname);
        viewHolder.tv_gym_trainer_assumed_email.setText(email);

        viewHolder.btn_gym_trainer_assumed_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO:
                //LOGICA AGGIUNTA TRAINER SCELTO TRA I PROPRI DIPENDENDTI
                dismissTrainer(context, trainer_id, name, lastname, email, qualification, fiscal_code, position);
            }
        });
        return r;
    }

    class ViewHolder {
        TextView tv_gym_trainer_assumed_name, tv_gym_trainer_assumed_lastname, tv_gym_trainer_assumed_email;
        ImageView btn_gym_trainer_assumed_add;

        ViewHolder(View v) {
            tv_gym_trainer_assumed_name = v.findViewById(R.id.tv_gym_trainer_assumed_name);
            tv_gym_trainer_assumed_lastname = v.findViewById(R.id.tv_gym_trainer_assumed_lastname);
            tv_gym_trainer_assumed_email = v.findViewById(R.id.tv_gym_trainer_assumed_email);

            btn_gym_trainer_assumed_add = v.findViewById(R.id.btn_gym_trainer_assumed_add);
        }
    }


    public void dismissTrainer(Activity a, String trainer_id, String name, String lastname, String email, String qualification, String fiscal_code, Integer position) {
        CustomGymTrainerAssumedAdapter.CustomDialogRemoveTrainer cdd = new CustomGymTrainerAssumedAdapter.CustomDialogRemoveTrainer(a, trainer_id, name, lastname, email, qualification, fiscal_code, position);
        cdd.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        cdd.show();
    }


    private class CustomDialogRemoveTrainer extends Dialog implements View.OnClickListener {

        public Activity c;
        public Button Licenzia, Esci;
        public TextView _name, _lastname, _email, _qualification, _fiscal_code;
        public String trainer_id, name, lastname, email, qualification, fiscal_code;
        Integer position;

        public CustomDialogRemoveTrainer(Activity a, String trainer_id, String name, String lastname, String email, String qualification, String fiscal_code, Integer position) {
            super(a);
            this.c = a;
            this.trainer_id = trainer_id;
            this.name = name;
            this.lastname = lastname;
            this.email = email;
            this.qualification = qualification;
            this.fiscal_code = fiscal_code;
            this.position = position;
        }

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            requestWindowFeature(Window.FEATURE_NO_TITLE);
            setContentView(R.layout.dialog_dismiss_trainer);
            Licenzia = (Button) findViewById(R.id.dialog_confirm_user_type_yes);
            Esci = (Button) findViewById(R.id.dialog_confirm_user_type_no);

            _name = (TextView) findViewById(R.id.tv_dismiss_name);
            _lastname = (TextView) findViewById(R.id.tv_dismiss_lastname);
            _email = (TextView) findViewById(R.id.tv_dismiss_email);
            _qualification = (TextView) findViewById(R.id.tv_dismiss_qualification);
            _fiscal_code = (TextView) findViewById(R.id.tv_dismiss_fiscal_code);

            _name.setText(name);
            _lastname.setText(lastname);
            _email.setText(email);
            _qualification.setText(qualification);
            _fiscal_code.setText(fiscal_code);

            Licenzia.setOnClickListener(this);
            Esci.setOnClickListener(this);
        }


        @Override
        public void onClick(View v) {

            switch (v.getId()) {
                case R.id.dialog_confirm_user_type_yes:

                    GymMenageWorkerActivity.dismissTrainer(trainer_id, position);

                    dismiss();
                    break;
                case R.id.dialog_confirm_user_type_no:
                    //
                    dismiss();
                    break;
                default:
                    break;
            }
        }
    }

}
