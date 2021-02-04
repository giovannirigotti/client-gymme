package android_team.gymme_client;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.os.Bundle;
import android.view.View;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity2 extends AppCompatActivity {

    @BindView(R.id.drawer_layout_home_activity)
    DrawerLayout drawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        ButterKnife.bind(this);
    }

    public void ClickMenu(View view) {
        openDrawer(drawerLayout);
    }

    public void openDrawer(DrawerLayout drawerLayout){
        drawerLayout.openDrawer(GravityCompat.START);
    }

    public void ClickDrawer (View view){
        closeDrawer(drawerLayout);
    }

    public void closeDrawer (DrawerLayout drawerLayout){
        if (drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.closeDrawer(GravityCompat.START);
        }

    }

}