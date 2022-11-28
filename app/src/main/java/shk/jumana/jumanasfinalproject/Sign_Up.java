package shk.jumana.jumanasfinalproject;

import static shk.jumana.jumanasfinalproject.R.id;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

/**
 * sign up
 */

public class Sign_Up extends AppCompatActivity {

    private TextInputEditText etUsername;
    private TextInputEditText etEmail2;
    private TextInputEditText etPassword2;
    private TextInputEditText etConfirmPassword;
    private Button btnSave;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);



        etEmail2=findViewById(R.id.etEmail2);
        etPassword2=findViewById(R.id.etPassword2);
        etConfirmPassword=findViewById(R.id.etConfirmPassword);
        btnSave=findViewById(R.id.btnSave);


        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                Toast.makeText(Sign_Up.this,"Username is"+etUsername,Toast.LENGTH_SHORT).show();



                CheckAndSave();//the purpose of check and save is to go to the sign in page
                //توثيق خاص : used for signing in and signing out


            }
        });


    }

    private void CheckAndSave()
    {
        String Email=etEmail2.getText().toString();
        String Password=etPassword2.getText().toString();
        String ConfirmPassword=etConfirmPassword.getText().toString();
        boolean isOk=true;//// بمشي على كل الفحوصات ويفحصهن

        //لما نعمل فحص احنا منحط احتمال انو غلط , عشان اذا كان الفحص غلط ,بوقف عن العمل

        if (Email.length()*Password.length()*ConfirmPassword.length()==0)//تفحص اذا كلمة السر وتأكيد كلمة السر يساوون 0
        {
            etEmail2.setError("one of the files are empty");
            isOk=false;
        }

        if (Password.equals(ConfirmPassword)==false)//تفحص اذا كلمة السر تساوي تاكيد كلمة السر
        {
            etConfirmPassword.setError("your password does not match");
            isOk=false;
        }

        if (isOk)
        {
            FirebaseAuth auth=FirebaseAuth.getInstance();
            auth.createUserWithEmailAndPassword(Email,Password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                /**
                 * * when the mission of this task-is complete
                 * @param task   info about the event
                 */

                @Override
                public void onComplete(@NonNull Task<AuthResult> task)
                {
                    if (task.isSuccessful())//check if creating account is complete and successful
                    {
                        //A toast provides simple feedback about an operation in a small popup.
                        // It only fills the amount of space required for the message
                        // and the current activity remains visible and interactive.
                        // Toasts automatically disappear after a timeout
                        Toast.makeText(Sign_Up.this, "creation successful", Toast.LENGTH_SHORT).show();
                        finish();//when it completes it finishes and goes to the sign in page,close current activity
                    }
                    else
                    {
                        Toast.makeText(Sign_Up.this, "creation failed"+task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }                        //gives a text that its not working - creation failed


                }
            });



        }

    }
}