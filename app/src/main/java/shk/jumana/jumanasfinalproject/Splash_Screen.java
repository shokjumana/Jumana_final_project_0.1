package shk.jumana.jumanasfinalproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.google.firebase.auth.FirebaseAuth;

public class Splash_Screen extends AppCompatActivity
{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);


        //start next activity screen auto after of time

        Handler H = new Handler();
        Runnable r= new Runnable()
        {
            @Override
            public void run()
            {
                //فحص هل تم الدخول مسبقا
                FirebaseAuth auth=FirebaseAuth.getInstance();
                if(auth.getCurrentUser() == null)
                {
                // to open new activity and move from current page to the next one
                Intent i= new Intent(Splash_Screen.this,Sign_In.class);
                startActivity(i);
                //to close current page
                finish();

            }
                else //اذا لم تسجل الدخول مسبقا اذهب الى صفحة ال main

                {
                    Intent i = new Intent(Splash_Screen.this, MainActivity.class);
                    startActivity(i);
                    finish();
                }

                }
        };

        H.postDelayed(r,3000);

    }
}