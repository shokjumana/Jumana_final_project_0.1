package shk.jumana.jumanasfinalproject;

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

public class Sign_Up extends AppCompatActivity {

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
                CheckAndSave();


            }
        });


    }

    private void CheckAndSave()
    {
        String Email=etEmail2.getText().toString();
        String Password=etPassword2.getText().toString();
        String ConfirmPassword=etConfirmPassword.getText().toString();
        boolean isOk=true;//


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
                @Override
                public void onComplete(@NonNull Task<AuthResult> task)
                {
                    if (task.isSuccessful())
                    {
                        Toast.makeText(Sign_Up.this, "creation successful", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                    else
                    {
                        Toast.makeText(Sign_Up.this, "creation failed"+task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }

                }
            });



        }

    }
}