package shk.jumana.jumanasfinalproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SearchView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import shk.jumana.jumanasfinalproject.data.Book;
import shk.jumana.jumanasfinalproject.data.BookAdapter;

/**
 * main activity
 */

public class MainActivity extends AppCompatActivity
{
    //build a وسيط
    private ImageButton btnAddBook;
    //تجهيز الوسيط
    BookAdapter bookAdapter;
    //list to show all the books
    ListView bookList;

    private SearchView searchView;




        @SuppressLint("MissingInflatedId")
        @Override
        protected void onCreate(Bundle savedInstanceState)
        {
            {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main);
            //بناء شاشة التنسيق وكل الكائنات التي تحويه

            btnAddBook=findViewById(R.id.btnAddBook);
            //بناء وسيط
            bookAdapter = new BookAdapter(getApplicationContext());
            //تجهيز مؤشر لقائمة العرض
            bookList=findViewById(R.id.BookList);

            bookList.setAdapter(bookAdapter);



                ReadBookFromFirebase();

            // doing search bar
                searchView=findViewById(R.id.searchView);
                searchView.setActivated(true);
                searchView.setQueryHint("type your keyword here");
                searchView.onActionViewExpanded();
                searchView.setIconified(false);
                searchView.clearFocus();



            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query)
                {
                    return false;
                }

                @Override
                public boolean onQueryTextChange(String newText)
                {

                    bookAdapter.getFilter().filter(newText);
                    return false;
                }
            });




                //downloading and working on listener for every change on
                // قاعدة البيانات and cleans the given info so it deletes
                //it and downloads new info.

                //button that make the user go from the main activity page to the add book page to add books.
            btnAddBook.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view)
                {
                    Intent i = new Intent(MainActivity.this,addBook.class);
                    startActivity(i);
                }
            });
            }
        }


    //دالة مسؤولة عن فحص و تشغيل ال menu
    //build menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.main_menu,menu);//بتحط المحل الي الها يعني اللا R هو اللا res يعني resourse
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item)
    {
        if (item.getItemId() == R.id.itmProfile)
        {
            Intent i = new Intent(MainActivity.this, ProfileActivity.class);
            startActivity(i);
        }

        if (item.getItemId() == R.id.itmSignOut)
        {
            //تسجيل خروج
            //FirebaseAuth.getInstance().signOut();
            //finish();


            //A dialog is a small window that prompts the user to make a decision or enter additional information.
            // A dialog does not fill the screen and is normally used for modal events-
            // that require users to take an action before they can proceed.
            //getting ready to build dialog
            AlertDialog.Builder builder= new AlertDialog.Builder(this);
            builder.setTitle("Sign Out");
            builder.setMessage("Are You Sure?");
            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i)
                {
                    //dialog interface هو برامتر الي ببين ايش هي الديالوج
                    //hide dialog
                    dialogInterface.dismiss();
                    //اخفاء شاشة
                    //sign out from the screen
                    //build kan from firebase auth to sign out
                    FirebaseAuth.getInstance().signOut();
                    //sign out
                    finish();

                }
            });
            builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i)
                {
                    //build dialog
                    //خروج وتخزين
                    dialogInterface.cancel();
                }
            });
            AlertDialog dialog=builder.create();
            //shows the screen
            dialog.show();
        }

        return true;
    }


    private void ReadBookFromFirebase()
    {

        //مؤشر لجذر قاعدة البيانات التابعة للمشروع

        DatabaseReference reference= FirebaseDatabase.getInstance().getReference();
        //listener لمراقبة اي تغيير يحدث تحت الجذر المحدد
        //اي تغيير بقيمة صفة او حذف او اضافة كائن يتم اعلام ال listener
        //عندها يتم تنزيل,تحميل كل المعطيات الموجودة تحت الجذر
        //كل معالجي الحدث ببدو ب on
        FirebaseDatabase.getInstance().getReference().child("Book").addValueEventListener(new ValueEventListener()
            //تسمح للمستخدم ان يرى التغييرات التي تحدث في الداتا وتقوم بالعمل حسبه
        {
            /**
             *                  دالة معالجة حدث عند تغيير اي قيمة في ال firebase
             * @param snapshot يحوي نسخة عن كل المعطيات تحت العنوان المراقبل يعني الي محطوط علي listener
             */
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot)
            {
                // تغيير معطيات بال firebase
                //بعطي معطيات
                //الكائن الي فيو بحوي معيطيات الي بكونو تحت الجذر
                //remove all tasks

                bookAdapter.clear();

                for (DataSnapshot d:snapshot.getChildren())//ال snapshot من نوع d, يمر على جميع المعطيات
                {
                    Book b=d.getValue(Book.class);//استخراج الابن, جميع الابناء, استخراج كائن محفوظ
                    bookAdapter.add(b);

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error)
            {

            }
        });


    }
}