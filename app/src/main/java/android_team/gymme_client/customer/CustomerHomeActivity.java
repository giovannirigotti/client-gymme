package android_team.gymme_client.customer;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import android_team.gymme_client.R;
import android_team.gymme_client.login.LoginActivity;
import android_team.gymme_client.support.Drawer;
import butterknife.BindView;
import butterknife.ButterKnife;

public class CustomerHomeActivity extends AppCompatActivity {

    private int user_id;

    DrawerCustomerListener drawerCustomerListener;
    ;
    DrawerLayout drawerLayout;
    TextView tv_title;


    @BindView(R.id.btn_customer_home_profile)
    Button _btn_customer_home_profile;

    @BindView(R.id.btn_customer_home_notification)
    Button _btn_customer_home_notification;

    @BindView(R.id.btn_customer_home_gym)
    Button _btn_customer_home_gym;

    @BindView(R.id.btn_customer_home_course)
    Button _btn_customer_home_course;

    @BindView(R.id.btn_customer_home_training_sheets)
    Button _btn_customer_home_training_sheets;

    @BindView(R.id.drawer_home_link)
    LinearLayout drawer_home_link;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_home);
        ButterKnife.bind(this);


        Intent i = getIntent();
        if (!i.hasExtra("user_id")) {
            Toast.makeText(this, "user_id mancante", Toast.LENGTH_LONG).show();
            Intent new_i = new Intent(this, LoginActivity.class);
            startActivity(new_i);
        } else {
            user_id = i.getIntExtra("user_id", -1);
            Log.w("user_id ricevuto:", String.valueOf(user_id));
            if (user_id == -1) {
                Toast.makeText(this, "Utente non creato", Toast.LENGTH_LONG).show();
                Intent new_i = new Intent(this, LoginActivity.class);
                startActivity(new_i);
            }
        }

        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout_home_activity);
        drawerCustomerListener = new DrawerCustomerListener(this, user_id);
        tv_title = (TextView) findViewById(R.id.main_toolbar_title);
        tv_title.setText("Home");

        _btn_customer_home_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Log.e("REDIRECT", "Customer Profile Activity");
                Intent i = new Intent(getApplicationContext(), CustomerProfileActivity.class);
                i.putExtra("user_id", user_id);
                startActivity(i);
            }
        });

        _btn_customer_home_gym.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Log.e("REDIRECT", "Customer Manage Gym Activity");
                Intent i = new Intent(getApplicationContext(), CustomerManageGymActivity.class);
                i.putExtra("user_id", user_id);
                startActivity(i);
            }
        });

        _btn_customer_home_notification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Log.e("REDIRECT", "Customer Manage Gym Activity");
                Intent i = new Intent(getApplicationContext(), CustomerNotificationActivity.class);
                i.putExtra("user_id", user_id);
                startActivity(i);
            }
        });


        _btn_customer_home_course.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Log.e("REDIRECT", "Customer Manage Course Activity");
                Intent i = new Intent(getApplicationContext(), CustomerManageCourseActivity.class);
                i.putExtra("user_id", user_id);
                startActivity(i);
            }
        });

        _btn_customer_home_training_sheets.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Log.e("REDIRECT", "Customer Training Sheets Activity");
                Intent i = new Intent(getApplicationContext(), CustomerTrainingSheetsActivity.class);
                i.putExtra("user_id", user_id);
                startActivity(i);
            }
        });

        drawer_home_link.setPadding(20, 10, 20, 10);
        drawer_home_link.setBackground(getDrawable(R.drawable.rounded_rectangle));
        drawer_home_link.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorPrimary)));
    }


    //region DRAWER
    @Override
    protected void onPause() {
        super.onPause();
        Drawer.closeDrawer(drawerLayout);
    }

    public void ClickMenu(View view) {
        Drawer.openDrawer(drawerLayout);
    }

    public void ClickDrawer(View view) {
        Drawer.closeDrawer(drawerLayout);
    }

    public void customerToNotify(View view) {
        drawerCustomerListener.toNotify();
    }

    public void customerToTrainings(View view) {
        drawerCustomerListener.toTrainings();
    }

    public void customerToGym(View view) {
        drawerCustomerListener.toGym();
    }

    public void customerToCourse(View view) {
        drawerCustomerListener.toCourse();
    }

    public void customerToProfile(View view) {
        drawerCustomerListener.toProfile();
    }

    public void customerToHome(View view) {
        drawerCustomerListener.toHome();
    }
    //endregion
}
