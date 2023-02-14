package shk.jumana.jumanasfinalproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

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

import java.util.UUID;

import shk.jumana.jumanasfinalproject.data.Book;

public class ProfileActivity extends AppCompatActivity
{
    private static final int IMAGE_PICK_CODE = 100;
    private static final int PERMISSION_CODE=101;
    private Button btnProUpload;
    private TextInputEditText ProfileName;
    private TextInputEditText ProfileLastName;
    private TextInputEditText ProfileEmail;
    private TextInputEditText ProfileBirthday;
    private TextInputEditText ProfileLang;
    private TextInputEditText ProfileCountry;
    private ImageView ProfileImage;
    private Button btnProfSave;

    private Uri filePath;
    private Uri toUploadimageUri;
    private Uri downladuri;
    StorageTask uploadTask;
    private Book book;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        btnProUpload=findViewById(R.id.btnProUpload);
        SharedPreferences preferences=getSharedPreferences("mypref",MODE_PRIVATE);
        String key = preferences.getString("key", "");

        if(key.length()==0) {
            Toast.makeText(this, "No key found", Toast.LENGTH_SHORT).show();
        }
        else {
            Toast.makeText(this, "key:"+key, Toast.LENGTH_SHORT).show();
        }

        ProfileName=findViewById(R.id.ProfileName);
        ProfileLastName=findViewById(R.id.ProfileLastName);
        ProfileEmail=findViewById(R.id.ProfileEmail);
        ProfileBirthday=findViewById(R.id.ProfileBirthday);
        ProfileLang=findViewById(R.id.ProfileLang);
        ProfileCountry=findViewById(R.id.ProfileCountry);
        ProfileImage=findViewById(R.id.ProfileImage);
        btnProfSave=findViewById(R.id.btnProfSave);

        btnProfSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
            }
        });

        btnProUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                if(toUploadimageUri!= null)
                    uploadImage(toUploadimageUri);
                else
                    checkAndSave();
            }
        });


        ProfileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
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
    }

    private void checkAndSave()
    {
        String FirstName = ProfileName.getText().toString();
        String LastName = ProfileLastName.getText().toString();
        String ProEmail = ProfileEmail.getText().toString();
        String ProLanguage = ProfileLang.getText().toString();
        String ProCountry = ProfileCountry.getText().toString();


        Book book = new Book();

        //استخراج رقم المميز للمستعمل
        //uid - universal
        //user that signed before , مستخدم دخل مسبقا

        String owner = FirebaseAuth.getInstance().getCurrentUser().getUid();
        book.setOwner(owner);

        String key = FirebaseDatabase.getInstance().getReference()
                .child("Book")//جذر جديد تحته يتم تخزين المعلومات
                .child(owner)
                .push()// add new مستخدم
                .getKey();//استخراج الرقم المميز من المهمة التي سيتم اضافتها
        book.setKey(key);

        //جذر شجرة المعطيات , عنوان جذر شجرة المعطيات


        FirebaseDatabase.getInstance().getReference().child("Book").child(owner).child(key).setValue(book).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

                if (task.isSuccessful()) {
                    finish();
                    Toast.makeText(ProfileActivity.this, "added successfuly", Toast.LENGTH_SHORT).show();
                } else {

                    Toast.makeText(ProfileActivity.this, "added failed", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    private void pickImageFromGallery()
    {
        //intent to pick image
        Intent intent=new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent,IMAGE_PICK_CODE);

    }


    /**private void createBook(Book book)
    {
        //.1
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        //.2
        DatabaseReference reference =
                database.getReference();
        //to get the user uid (or other details like email)
        FirebaseAuth auth=FirebaseAuth.getInstance();
        String uid = auth.getCurrentUser().getUid();
        book.setOwner(uid);

        String key = reference.child("tasks").push().getKey();
        book.setKey(key);
        reference.child("tasks").child(uid).child(key).setValue(book).
                addOnCompleteListener(ProfileActivity.this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful())
                        {
                            Toast.makeText(ProfileActivity.this, "add successful", Toast.LENGTH_SHORT).show();
                            finish();
                        }
                        else
                        {
                            Toast.makeText(ProfileActivity.this, "add failed"+task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            task.getException().printStackTrace();
                        }

                    }
                });

    }**/


   /** private void dataHandler()
    {
        boolean isok=true;
        String Name =ProfileName.getText().toString();
        String LastName =ProfileLastName.getText().toString();
        String ProEmail=ProfileEmail.getText().toString();
        String ProBirthday=ProfileBirthday.getText().toString();
        String ProLanguage=ProfileLang.getText().toString();
        String ProCountry=ProfileCountry.getText().toString();

//        String text=etText.getText().toString();
//        String  date=etDueDate.getText().toString();
//        int necessary=skbrNecessary.getProgress();
        if(Name.length()==0)
        {
            ProfileName.setError("Title can not be empty");
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
            book=new Book();
            book.setName(Name);
            //createTask(t);
            if(uploadTask!=null || (uploadTask!=null && uploadTask.isInProgress()))
            {
                Toast.makeText(this, " uploadTask.isInProgress(", Toast.LENGTH_SHORT).show();
            }
            else
                uploadImage(toUploadimageUri);

        }

    }*/

    private void uploadImage(Uri filePath) {
        {

            if (filePath != null) {
               // final ProgressDialog progressDialog = new ProgressDialog(this);
             //   progressDialog.setTitle("Uploading...");
             //   progressDialog.show();
                FirebaseStorage storage = FirebaseStorage.getInstance();
                StorageReference storageReference = storage.getReference();
                final StorageReference ref = storageReference.child("images/" + UUID.randomUUID().toString());
                uploadTask = ref.putFile(filePath)
                        .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {

                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
               //                 progressDialog.dismiss();
                                ref.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Uri> task) {
                                        downladuri = task.getResult();
                                        book.setImage(downladuri.toString());

                                    }
                                });

                                Toast.makeText(getApplicationContext(), "Uploaded", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                            //    progressDialog.dismiss();
                                Toast.makeText(getApplicationContext(), "Failed " + e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        })
                        .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                                double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot
                                        .getTotalByteCount());
                             //   progressDialog.setMessage("Uploaded " + (int) progress + "%");
                            }
                        });
            } else {
                book.setImage("");
                checkAndSave();
            }
        }
    }
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
        protected void onActivityResult(int requestCode, int resultCode, Intent data)
        {
            super.onActivityResult(requestCode,resultCode,data);
            if (resultCode==RESULT_OK && requestCode== IMAGE_PICK_CODE){
                //set image to image view
                toUploadimageUri = data.getData();
                ProfileImage.setImageURI(toUploadimageUri);
            }
        }
    }
