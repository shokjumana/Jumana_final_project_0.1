package shk.jumana.jumanasfinalproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class Sign_In extends AppCompatActivity
{

    private TextInputEditText etEmail1;
    private TextInputEditText etPassword1;
    private Button btnSignIn1;
    private Button btnSignUp1;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        etEmail1=findViewById(R.id.etEmail1);
        etPassword1=findViewById(R.id.etPassword1);
        btnSignIn1=findViewById(R.id.btnSignIn1);
        btnSignUp1=findViewById(R.id.btnSignUp2);



        btnSignIn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent (Sign_In.this,MainActivity.class);
                startActivity(i);
            }
        });


        btnSignUp1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {

                Intent i = new Intent (Sign_In.this,Sign_Up.class);
                startActivity(i);

            }
        });


        btnSignIn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {

                CheckAndSave();

            }
        });

    }

    private void CheckAndSave()
    {

        String Email=etEmail1.getText().toString();
        String Password=etPassword1.getText().toString();
        boolean isOk=true;




        if (Email.length()==0)
        {
            etEmail1.setError("please enter your email");
            isOk=false;
            //checks if email=0
        }

        if (Password.length()==0)
        {
            etPassword1.setError("please enter your Password");
            isOk=false;
            //checks if password=0
        }

        if (Email.indexOf("@")<=0)
        {
            etEmail1.setError("your email is wrong");
            isOk=false;
            //checks if email hqs @
        }

        if (Password.length()<7)
        {
            etPassword1.setError("your password should be at least 7 characters");
            isOk=false;
            //checks if password has less then 7 characters =0
        }


        if (isOk)
        {
            FirebaseAuth auth=FirebaseAuth.getInstance();
            auth.signInWithEmailAndPassword(Email,Password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task)
                {
                    if (task.isSuccessful())
                    {
                        Toast.makeText(Sign_In.this, "Successful", Toast.LENGTH_SHORT).show();
                        Intent i = new Intent(Sign_In.this,MainActivity.class);
                        startActivity(i);
                        finish();
                    }

                    else
                        Toast.makeText(Sign_In.this, "Not Successful"+task.getException().getMessage(), Toast.LENGTH_SHORT).show();

                }
            });


        }

    }
}