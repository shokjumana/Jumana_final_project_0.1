package shk.jumana.jumanasfinalproject;


import static shk.jumana.jumanasfinalproject.R.id.etNameBook;


import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.util.Date;
import java.util.UUID;


import shk.jumana.jumanasfinalproject.data.Book;

public class addBook extends AppCompatActivity
{
    private static final int IMAGE_PICK_CODE=100;
    private static final int PERMISSION_CODE=101;

    private TextInputEditText etNameBook;
    private TextInputEditText etAuthor;
    private TextInputEditText etReleaseDate;
    private TextInputEditText etGenre;
    private RatingBar rbRateBook;
    private TextInputEditText etAbout;
    private Button btnCancel;
    private Button btnSaveTask;

    //upload :1 add xml image
    //upload:2

    private Uri toUpLoadimageUri;
    private Uri downloaduri;
    StorageTask uploadBook;
    private Book b=new Book();
    private ImageView btnImageBook;
    private Uri filePath;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_book);

        etNameBook = findViewById(R.id.etNameBook);
        etAuthor = findViewById(R.id.etAuthor);
        etReleaseDate = findViewById(R.id.etReleaseDate);
        etGenre = findViewById(R.id.etGenre);
        rbRateBook = findViewById(R.id.rbRateBook);
        etAbout = findViewById(R.id.etAbout);
        btnCancel = findViewById(R.id.btnCancel);
        btnSaveTask = findViewById(R.id.btnSaveTask);

        SharedPreferences preferences=getSharedPreferences("mypref",MODE_PRIVATE);
        String key = preferences.getString("key", "");

        if(key.length()==0) {
            Toast.makeText(this, "No key found", Toast.LENGTH_SHORT).show();
        }
        else {
            Toast.makeText(this, "key:"+key, Toast.LENGTH_SHORT).show();
        }

        //upload: 3
        btnImageBook = findViewById(R.id.btnImageBook);
        
        //upload : 4
        btnImageBook.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(), "image", Toast.LENGTH_SHORT).show();
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
                        //permission not granted, request it.
                        String[] permissions = {Manifest.permission.READ_EXTERNAL_STORAGE};
                        //show popup for runtime permission
                        requestPermissions(permissions, PERMISSION_CODE);
                    } else {
                        //permission already granted
                        pickImageFromGallery();
                    }
                }
            }
        });


        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(addBook.this, MainActivity.class);
                startActivity(i);
            }
        });


        btnSaveTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                if(toUpLoadimageUri!= null)
                    uploadImage(toUpLoadimageUri);
                else
                    CheckAndSave();
//                CheckAndSave();
//                dataHandler();
            }
        });
    }


    private void CheckAndSave() {
        boolean isOk = true;
        String Name = etNameBook.getText().toString();
        String Author = etAuthor.getText().toString();
        String Genre = etGenre.getText().toString();
        String About = etAbout.getText().toString();

        if (Name.length() * Author.length() * About.length() * Genre.length() == 0) {
            etNameBook.setError("one of the files are empty");
            isOk = false;
        }

        b.setName(Name);
        b.setAuthor(Author);
        b.setGenre(Genre);
        b.setAbout(About);

        //استخراج رقم المميز للمستعمل
        //uid - universal
        //user that signed before , مستخدم دخل مسبقا
        String owner = FirebaseAuth.getInstance().getCurrentUser().getUid();
        b.setOwner(owner);

        String key = FirebaseDatabase.getInstance().getReference()
                .child("Book")//جذر جديد تحته يتم تخزين المعلومات
                .child(owner)
                .push()// add new مستخدم
                .getKey();//استخراج الرقم المميز من المهمة التي سيتم اضافتها
        b.setKey(key);

        if (isOk)
        {
            //جذر شجرة المعطيات , عنوان جذر شجرة المعطيات
            FirebaseDatabase.getInstance().getReference().child("Book").child(key).setValue(b).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {

                    if (task.isSuccessful()) {
                        finish();
                        Toast.makeText(addBook.this, "added successfully", Toast.LENGTH_SHORT).show();
                    } else {

                        Toast.makeText(addBook.this, "added failed", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }


    private void uploadImage(Uri filePath)
    {
        if(filePath != null)
        {
            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Uploading...");
            progressDialog.show();
            FirebaseStorage storage= FirebaseStorage.getInstance();
            StorageReference storageReference = storage.getReference();
            final StorageReference ref = storageReference.child("images/"+ UUID.randomUUID().toString());
            uploadBook=ref.putFile(filePath)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {

                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            progressDialog.dismiss();
                            ref.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                                @Override
                                public void onComplete(@NonNull Task<Uri> task) {
                                    downloaduri = task.getResult();
                                    b.setImage(downloaduri.toString());
                                    CheckAndSave();
                                }
                            });

                            Toast.makeText(getApplicationContext(), "Uploaded", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressDialog.dismiss();
                            Toast.makeText(getApplicationContext(), "Failed "+e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            double progress = (100.0*taskSnapshot.getBytesTransferred()/taskSnapshot
                                    .getTotalByteCount());
                            progressDialog.setMessage("Uploaded "+(int)progress+"%");
                        }
                    });
        }else
        {
            b.setImage("");
            CheckAndSave();
        }
    }


    /** private void createBook(Book b)
    {
        //1.
        FirebaseDatabase database=FirebaseDatabase.getInstance();
        //2.
        DatabaseReference reference=database.getReference();
        FirebaseAuth auth=FirebaseAuth.getInstance();
        String uid = auth.getCurrentUser().getUid();
        b.setOwner(uid);

        String key = reference.child("tasks").push().getKey();
        b.setKey(key);
        reference.child("tasks").child(uid).child(key).setValue(b).addOnCompleteListener(addBook.this, new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful())
                {
                    Toast.makeText(addBook.this, "add successful", Toast.LENGTH_SHORT).show();
                    finish();
                }
                else
                {
                    Toast.makeText(addBook.this, "add failed"+task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    task.getException().printStackTrace();
                }

            }
        });
    }*/

    private void dataHandler() 
    {
        boolean isok=true;
        String name=etNameBook.getText().toString();
//        String text=etText.getText().toString();
//        String  date=etDueDate.getText().toString();
//        int necessary=skbrNecessary.getProgress();
        if(name.length()==0)
        {
            etNameBook.setError("Title can not be empty");
            isok=false;

        }
//        if(text.length()==0)
//        {
//            etText.setError("Text can not be empty");
//            isok=false;
//
//        }
        if(isok)
        {
            b=new Book();
            //createTask(t);
            if(uploadBook!=null || (uploadBook!=null && uploadBook.isInProgress()))
            {
                Toast.makeText(this, " uploadTask.isInProgress(", Toast.LENGTH_SHORT).show();
            }
            else
                uploadImage(toUpLoadimageUri);
//            MyTask task=new MyTask();
//            task.setCreatedAt(new Date());
//            //task.setDueDate(new Date(date));
//            task.setText(text);
//            task.setTitle(title);
//            task.setImportant(important);
//            task.setNecessary(necessary);
//
//            //get user email to set is as the owner of this task
//            FirebaseAuth auth = FirebaseAuth.getInstance();
//            task.setOwner(auth.getCurrentUser().getEmail());
//// to get the database root reference
//            DatabaseReference reference= FirebaseDatabase.getInstance().getReference();
//
//           //to get uid(universal id)
//            String key=reference.child("MyTasks").push().getKey();
//            task.setKey(key);
//
//            reference.child("MyTasks").child(key).setValue(task).addOnCompleteListener(new OnCompleteListener<Void>() {
//                @Override
//                public void onComplete(@NonNull Task<Void> task) {
//                    if(task.isSuccessful())
//                    {
//                        Toast.makeText(AddTaskActivity.this, "Add Successful", Toast.LENGTH_LONG).show();
//                        Intent intent=new Intent(getBaseContext(),AddTaskActivity.class);
//                        startActivity(intent);
//                    }
//                    else
//                    {
//                        Toast.makeText(AddTaskActivity.this, "Add Faild", Toast.LENGTH_LONG).show();
//
//                    }
//                }
//            });
        }
    }

    private void pickImageFromGallery()
    {
        Intent intent=new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent,IMAGE_PICK_CODE);
    };

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case PERMISSION_CODE: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //permission was granted
                    pickImageFromGallery();
                } else {
                    //permission was denied
                    Toast.makeText(this, "Permission denied...!", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode,resultCode,data);
        if (resultCode==RESULT_OK && requestCode== IMAGE_PICK_CODE){
            //set image to image view
            toUpLoadimageUri = data.getData();
            btnImageBook.setImageURI(toUpLoadimageUri);
        }
    }
}
