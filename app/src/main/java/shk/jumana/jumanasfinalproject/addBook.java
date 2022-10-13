package shk.jumana.jumanasfinalproject;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.SeekBar;

import com.google.android.material.textfield.TextInputEditText;

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



            }
        });






    }
}