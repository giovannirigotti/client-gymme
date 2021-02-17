package android_team.gymme_client.support;

import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.widget.VideoView;

import androidx.appcompat.app.AppCompatActivity;

import android_team.gymme_client.R;

public class NoNetworkActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.no_network_activity);
        VideoView videoView = (VideoView) findViewById(R.id.videoView);

        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mp.setLooping(true);
            }
        });

        MediaPlayer mediaPlayer = new MediaPlayer();


        videoView.setVideoURI(Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.no_internet));

        videoView.start();

    }


}
