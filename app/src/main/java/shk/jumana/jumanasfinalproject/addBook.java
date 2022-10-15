package shk.jumana.jumanasfinalproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import shk.jumana.jumanasfinalproject.data.Book;

public class addBook extends AppCompatActivity
{
    private TextInputEditText etTittleTask;
    private TextInputEditText etSubjectTask;
    private ImageButton imageTask;
    private Button btnCancelTask;
    private Button btnSaveTask;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_book);

        etTittleTask= findViewById(R.id.etTittleTask);
        etSubjectTask= findViewById(R.id.etSubjectTask);
        btnCancelTask= findViewById(R.id.btnCancelTask);
        btnSaveTask= findViewById(R.id.btnSaveTask);
        imageTask=findViewById(R.id.imageTask);




        btnSaveTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                CheckAndSave();
            }

            private void CheckAndSave()
            {
                String Tittle=etTittleTask.getText().toString();
                String Subject=etSubjectTask.getText().toString();


                Book B= new Book();
                B.setTitle(Tittle);
                B.setSubject(Subject);


                String owner= FirebaseAuth.getInstance().getCurrentUser().getUid();
                B.setOwners(owner);


                String key= FirebaseDatabase.getInstance().getReference().child("Book").child(owner).push().getKey();
                B.setKey(key);


                FirebaseDatabase.getInstance().getReference().child("Book").child(owner).child(key).setValue(B).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task)
                    {

                        if (task.isSuccessful())
                        {
                            finish();
                            Toast.makeText(addBook.this,"added succefuly",Toast.LENGTH_SHORT).show();
                        }
                        else
                        {
                            Toast.makeText(addBook.this,"added failled",Toast.LENGTH_SHORT).show();
                        }




                    }
                });







            }
        });






    }
}