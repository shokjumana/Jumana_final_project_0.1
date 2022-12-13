package shk.jumana.jumanasfinalproject;


import static shk.jumana.jumanasfinalproject.R.id.etNameBook;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;


import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;

import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RatingBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;


import java.util.UUID;

import shk.jumana.jumanasfinalproject.data.Book;

public class addBook extends AppCompatActivity
{
    private static final int IMAGE_PICK_CODE = 100;
    private static final int PERMISSION_CODE = 101;
    private Uri filePath;
    private Uri toUploadimageUri;
    private Uri downladuri;
    StorageTask uploadBook;
    private Book b;



    private TextInputEditText etNameBook;
    private TextInputEditText etAuthor;
    private TextInputEditText etReleaseDate;
    private TextInputEditText etGenre;
    private RatingBar rbRateBook;
    private TextInputEditText etAbout;
    private Button btnCancelTask;
    private Button btnSaveTask;
    private ImageButton btnImageBook;
    private Button LoadBookPic;



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
        btnCancelTask = findViewById(R.id.btnCancelTask);
        btnSaveTask = findViewById(R.id.btnSaveTask);

        //upload: 3
        btnImageBook = findViewById(R.id.btnImageBook);
        LoadBookPic = findViewById(R.id.LoadBookPic);

        SharedPreferences preferences = getSharedPreferences("mypref", MODE_PRIVATE);
        String key = preferences.getString("key", "");
        if (key.length() == 0) {
            Toast.makeText(this, "No key found", Toast.LENGTH_SHORT).show();

        } else {
            Toast.makeText(this, "key:" + key, Toast.LENGTH_SHORT).show();
        }

        //upload: 4
        btnImageBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //check runtime permission
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

        //upload: 5
        private void uploadImage (Uri filePath){

            if (filePath != null) {
                final ProgressDialog progressDialog = new ProgressDialog(this);
                progressDialog.setTitle("Uploading...");
                progressDialog.show();
                FirebaseStorage storage = FirebaseStorage.getInstance();
                StorageReference storageReference = storage.getReference();
                final StorageReference ref = storageReference.child("images/" + UUID.randomUUID().toString());
                uploadBook = ref.putFile(filePath)
                        .addOnSuccessListener(new OnSuccessListener<uploadBook.Task>() {

                            @Override
                            public void onSuccess(uploadBook.TaskSnapshot taskSnapshot) {
                                progressDialog.dismiss();
                                ref.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Uri> task) {
                                        downladuri = task.getResult();
                                        b.setImage(downladuri.toString());
                                        CheckAndSave(b);

                                    }
                                });

                                Toast.makeText(getApplicationContext(), "Uploaded", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                progressDialog.dismiss();
                                Toast.makeText(getApplicationContext(), "Failed " + e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        })
                        .addOnProgressListener(new OnProgressListener<uploadBook.TaskSnapshot>() {
                            @Override
                            public void onProgress(uploadBook.TaskSnapshot taskSnapshot) {
                                double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot
                                        .getTotalByteCount());
                                progressDialog.setMessage("Uploaded " + (int) progress + "%");
                            }
                        });
            } else {
                b.setImage("");
                CheckAndSave(b);
            }
        }


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
        }


        public void onRequestPermissionsResult ( int requestCode, @NonNull String[] permissions,
            @NonNull int[] grantResults){
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

    //handle result of picked images
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        addBook.super.onActivityResult(requestCode,resultCode,data);
        if (resultCode==RESULT_OK && requestCode== IMAGE_PICK_CODE){
            //set image to image view
            toUploadimageUri = data.getData();
            btnImageBook.setImageURI(toUploadimageUri);
        }
    }

    private void pickImageFromGallery()
        {
            //intent to pick image
            Intent intent = new Intent(Intent.ACTION_PICK);
            intent.setType("image/*");
            startActivityForResult(intent, IMAGE_PICK_CODE);
        }
    }
}




