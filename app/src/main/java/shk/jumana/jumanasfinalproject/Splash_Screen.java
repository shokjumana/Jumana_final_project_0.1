package shk.jumana.jumanasfinalproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

public class Splash_Screen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);




        Handler H = new Handler();
        Runnable r= new Runnable() {
            @Override
            public void run() {
                // to open new activity and move from current page to the next one
                Intent i= new Intent(Splash_Screen.this,Sign_In.class);
                startActivity(i);
                //to close current page
                finish();

            }
        };

        H.postDelayed(r,3000);

    }
}