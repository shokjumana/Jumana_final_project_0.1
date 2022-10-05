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
        setContentView(R.layout.activity_sign_in);//يبني واجهة المستعمل بحيث تبني كل الكائنات الموجودة في ملف التنسيق ال xml

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
        //the OnClickListener() interface has an onClick(View v)
        // method that is called when the view (component) is clicked.
        // The code for a component's functionality is written inside this method,
        // and the listener is set using the setOnClickListener() method.
        //تحتوي واجهة OnClickListener () على طريقة onClick (View v) التي يتم استدعاؤها عند النقر فوق العرض (المكون).


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

                CheckAndSave();//the purpose of check and save is to go to the sign in page
                //توثيق خاص : used for signing in and signing out


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
            FirebaseAuth auth=FirebaseAuth.getInstance();// بفتح ال firebase و بتسجل فيو ال email and password
            auth.signInWithEmailAndPassword(Email,Password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task)
                {
                    if (task.isSuccessful())//check if creating account is complete and successful
                    {
                        Toast.makeText(Sign_In.this, "Successful", Toast.LENGTH_SHORT).show();
                        Intent i = new Intent(Sign_In.this,MainActivity.class);
                        startActivity(i);
                        finish();//when it completes it finishes and goes to the sign in page,close current activity
                    }

                    else
                        Toast.makeText(Sign_In.this, "Not Successful"+task.getException().getMessage(), Toast.LENGTH_SHORT).show();
//when it completes it finishes and goes to the main activity page,close current activity
                }
            });


        }

    }
}