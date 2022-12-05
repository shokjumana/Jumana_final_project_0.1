package shk.jumana.jumanasfinalproject;


import static shk.jumana.jumanasfinalproject.R.id.etNameBook;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;


import android.content.Intent;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RatingBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;


import shk.jumana.jumanasfinalproject.data.Book;

public class addBook extends AppCompatActivity {

    private TextInputEditText etNameBook;
    private TextInputEditText etAuthor;
    private TextInputEditText etReleaseDate;
    private TextInputEditText etGenre;
    private RatingBar rbRateBook;
    private TextInputEditText etAbout;
    private Button btnCancelTask;
    private Button btnSaveTask;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_book);

        etNameBook = findViewById(R.id.etNameBook);
        etAuthor = findViewById(R.id.etAuthor);
        etReleaseDate=findViewById(R.id.etReleaseDate);
        etGenre = findViewById(R.id.etGenre);
        rbRateBook = findViewById(R.id.rbRateBook);
        etAbout=findViewById(R.id.etAbout);
        btnCancelTask = findViewById(R.id.btnCancelTask);
        btnSaveTask = findViewById(R.id.btnSaveTask);


        btnCancelTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(addBook.this, MainActivity.class);
                startActivity(i);

            }
        });

        btnSaveTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CheckAndSave();
            }

            private void CheckAndSave() {
                String Name = etNameBook.getText().toString();
                String Author = etAuthor.getText().toString();
                String Genre = etGenre.getText().toString();
                String About = etAbout.getText().toString();


                Book B = new Book();
                B.setName(Name);
                B.setAuthor(Author);
                B.setGenre(Genre);
                B.setAbout(About);

                //استخراج رقم المميز للمستعمل
                //uid - universal
                //user that signed before , مستخدم دخل مسبقا


                String owner = FirebaseAuth.getInstance().getCurrentUser().getUid();
                B.setOwner(owner);

                String key = FirebaseDatabase.getInstance().getReference()
                        .child("Book")//جذر جديد تحته يتم تخزين المعلومات
                        .child(owner)
                        .push()// add new مستخدم
                        .getKey();//استخراج الرقم المميز من المهمة التي سيتم اضافتها
                B.setKey(key);

                //جذر شجرة المعطيات , عنوان جذر شجرة المعطيات


                FirebaseDatabase.getInstance().getReference().child("Book").child(owner).child(key).setValue(B).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                        if (task.isSuccessful()) {
                            finish();
                            Toast.makeText(addBook.this, "added successfuly", Toast.LENGTH_SHORT).show();
                        } else {

                            Toast.makeText(addBook.this, "added failed", Toast.LENGTH_SHORT).show();
                        }


                    }
                });


            }
        });


    }
}


