package shk.jumana.jumanasfinalproject.data;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import shk.jumana.jumanasfinalproject.MainActivity;
import shk.jumana.jumanasfinalproject.R;
import shk.jumana.jumanasfinalproject.Sign_In;
import shk.jumana.jumanasfinalproject.Splash_Screen;
import shk.jumana.jumanasfinalproject.addBook;

//build the adapter for one kind of jop, which is "Book" .
public class BookAdapter extends ArrayAdapter<Book>
{
    //search 5:
    private ValueFilter valueFilter;
    List<String>bData;
    List<String>bStringFilterList;



    public BookAdapter(@NonNull Context context) {
        super(context, R.layout.book_item);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        //بناء واجهة لعرض المهمة ال book
        View vBook = LayoutInflater.from(getContext()).inflate(R.layout.book_item, parent, false);

        TextView tvBookName = vBook.findViewById(R.id.tvBookName);
        TextView tvBookAuthor = vBook.findViewById(R.id.tvBookAuthor);
        TextView tvBookGenre = vBook.findViewById(R.id.tvBookGenre);
        TextInputEditText etreleaseDate=vBook.findViewById(R.id.etreleaseDate);
        TextView tvBabout=vBook.findViewById(R.id.tvBabout);
        RatingBar bookRating = vBook.findViewById(R.id.bookRating);
        Button btnEdit = vBook.findViewById(R.id.btnEdit);
        Button btnDelete = vBook.findViewById(R.id.btnDelete);
        ImageView BookImage = vBook.findViewById(R.id.BookImage);

        //book كائن من نوع مهمة ومنستخرج قيمتو من ال
        final Book book = getItem(position);

        tvBookName.setText(book.getName());
        tvBookAuthor.setText(book.getAuthor());
        tvBookGenre.setText(book.getGenre());
        etreleaseDate.setText(book.getDate());
        tvBabout.setText(book.getAbout());
        bookRating.setRating(book.getRate());
        BookImage.setImageURI(Uri.parse(book.getImage()));


        //button that deletes the book that was written
        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseDatabase.getInstance().getReference().child("Book").child(book.getKey())
                        .removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(getContext(), "Deleted successfully", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getContext(), "Deleted unsuccessfully", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });


        //button that edits the book info we wrote about and saves it
btnEdit.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View view)
    {
        Intent i = new Intent(getContext(),addBook.class);
        i.putExtra("Book", String.valueOf(book));
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        getContext().startActivity(i);
        }
});

        return vBook;
    }


    //search 1:
    @NonNull
    @Override
             public Filter getFilter()
                 {
                 //search 4:
                    if (valueFilter ==null)
                        {
                            valueFilter = new ValueFilter();
                        }
                            return valueFilter;
                 }


    //search 2:
    private class ValueFilter extends Filter
    {
        @Override
        protected FilterResults performFiltering(CharSequence toSearch) {
            FilterResults results = new FilterResults();
            List<String> all = new ArrayList<>();
            if (toSearch != null && toSearch.length() > 0) {
                List<String> filterlist = new ArrayList<>();


                //search 3:
                DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("book");
                reference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        clear();
                        for (DataSnapshot d : snapshot.getChildren()) {
                            //يمر على جميع معطيات قيم d
                            String[] s = toSearch.toString().split(" ");
                            //cuts the sentence to words and for every word it starts to search.
                            Book b = d.getValue(Book.class);
                            //استخراج الكائن محفوظ
                            int count = 0;
                            for (int i = 0; i < s.length; i++) {
                                // MainActivity mainActivity=(MainActivity) getContext();

                                if (b.getName().toLowerCase().contains(s[i].toLowerCase()))
                                    // turns all the letters from big letters to small letters and searches.
                                {
                                    count++;
                                    filterlist.add(b.getName());
                                }

                                if (b.getAuthor().toLowerCase().contains(s[i].toLowerCase())) {
                                    count++;
                                    filterlist.add(b.getAuthor());
                                }

                                if (b.getGenre().toLowerCase().contains(s[i].toLowerCase())) {
                                    count++;
                                    filterlist.add(b.getGenre());
                                }

                                if (b.getAbout().toLowerCase().contains(s[i].toLowerCase())) {
                                    count++;
                                    filterlist.add(b.getAbout());
                                }

                            }

                            if (count > 0) {
                                add(b);//اضافة كائن للوسيط
                            }
                        }

                    }


                    @Override
                    public void onCancelled(@NonNull DatabaseError error)
                    {
                    }
                });

                results.count=filterlist.size();
                results.values=filterlist;
            }
            else
            {
                results.count=all.size();
                results.values=all;
            }
            return results;
        }

        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults)
        {
            //search 6:
            bData=(List<String>) filterResults.values;
            notifyDataSetChanged();

        }
    }
}
