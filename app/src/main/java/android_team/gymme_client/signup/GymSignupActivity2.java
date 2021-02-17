package android_team.gymme_client.signup;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Switch;

import androidx.appcompat.app.AppCompatActivity;

import android_team.gymme_client.R;
import butterknife.BindView;
import butterknife.ButterKnife;

public class GymSignupActivity2 extends AppCompatActivity {

    @BindView(R.id.switchSignUpPool)
    Switch _switchSignUpPool;
    @BindView(R.id.switchSignupBoxRing)
    Switch _switchSignupBoxRing;
    @BindView(R.id.switchSignupAerobics)
    Switch _switchSignupAerobics;
    @BindView(R.id.switchSignUpSpa)
    Switch _switchSignUpSpa;
    @BindView(R.id.switchSignUpWifi)
    Switch _switchSignUpWifi;
    @BindView(R.id.switchSignUpParking)
    Switch _switchSignUpParking;
    @BindView(R.id.switchSignUpPersonalTrainer)
    Switch _switchSignUpPersonalTrainer;
    @BindView(R.id.switchSignUpNutritionist)
    Switch _switchSignUpNutritionist;
    @BindView(R.id.switchSignUpImpedenceBalance)
    Switch _switchSignUpImpedenceBalance;
    @BindView(R.id.switchSignUpCourses)
    Switch _switchSignUpCourses;
    @BindView(R.id.switchSignUpShowers)
    Switch _switchSignUpShowers;

    @BindView(R.id.signup_button_next2_gym)
    Button _signup_button_next2_gym;

    @BindView(R.id.progressBarSignup2Gym)
    ProgressBar progressBarSignup2Gym;

    int user_id;
    String email;
    String vat_number;
    String gym_name;
    String gym_address;
    String zip_code;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_signup_gym_2);

        ButterKnife.bind(this);


        user_id = getIntent().getIntExtra("user_id", -1);
        email = getIntent().getStringExtra("email");
        vat_number = getIntent().getStringExtra("vat_number");
        gym_name = getIntent().getStringExtra("gym_name");
        gym_address = getIntent().getStringExtra("gym_address");
        zip_code = getIntent().getStringExtra("zip_code");


        _signup_button_next2_gym.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                _signup_button_next2_gym.setEnabled(false);
                _signup_button_next2_gym.setVisibility(View.GONE);
                progressBarSignup2Gym.setVisibility(View.VISIBLE);

                boolean pool = _switchSignUpPool.isChecked();
                boolean boxRing = _switchSignupBoxRing.isChecked();
                boolean aerobics = _switchSignupAerobics.isChecked();
                boolean spa = _switchSignUpSpa.isChecked();
                boolean wifi = _switchSignUpWifi.isChecked();
                boolean parkingArea = _switchSignUpParking.isChecked();
                boolean personalTrainer = _switchSignUpPersonalTrainer.isChecked();
                boolean nutritionist = _switchSignUpNutritionist.isChecked();
                boolean impedanceBalance = _switchSignUpImpedenceBalance.isChecked();
                boolean courses = _switchSignUpCourses.isChecked();
                boolean showers = _switchSignUpShowers.isChecked();

                Intent i = new Intent(getApplicationContext(), GymSignupActivity3.class);
                i.putExtra("user_id", user_id);
                i.putExtra("email", email);
                i.putExtra("vat_number", vat_number);
                i.putExtra("gym_name", gym_name);
                i.putExtra("gym_address", gym_address);
                i.putExtra("zip_code", zip_code);
                i.putExtra("pool", pool);
                i.putExtra("box_ring", boxRing);
                i.putExtra("aerobics", aerobics);
                i.putExtra("spa", spa);
                i.putExtra("wifi", wifi);
                i.putExtra("parking_area", parkingArea);
                i.putExtra("personal_trainer", personalTrainer);
                i.putExtra("nutritionist", nutritionist);
                i.putExtra("impedance_balance", impedanceBalance);
                i.putExtra("courses", courses);
                i.putExtra("showers", showers);

                startActivity(i);

                _signup_button_next2_gym.setEnabled(true);
                _signup_button_next2_gym.setVisibility(View.VISIBLE);
                progressBarSignup2Gym.setVisibility(View.GONE);
            }
        });


    }
}
