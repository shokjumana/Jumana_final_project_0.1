package shk.jumana.jumanasfinalproject.data;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import shk.jumana.jumanasfinalproject.R;
import shk.jumana.jumanasfinalproject.addBook;

//build the adapter for one kind of jop, which is "Book" .
public class BookAdapter extends ArrayAdapter<Book>
{
    public BookAdapter(@NonNull Context context)
    {
        super(context, R.layout.book_item);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent)
    {
        //بناء واجهة لعرض المهمة ال book
        View vBook= LayoutInflater.from(getContext()).inflate(R.layout.book_item,parent,false);

        TextView tvBookName=vBook.findViewById(R.id.tvBookName);
        TextView tvBookAuthor=vBook.findViewById(R.id.tvBookAuthor);
        TextView tvBookGenre=vBook.findViewById(R.id.tvBookGenre);
        RatingBar bookRating=vBook.findViewById(R.id.bookRating);
        Button btnEdit=vBook.findViewById(R.id.btnEdit);
        ImageView BookImage=vBook.findViewById(R.id.BookImage);

//book كائن من نوع مهمة ومنستخرج قيمتو من ال
        final Book book=getItem(position);

        tvBookName.setText(book.getName());
        tvBookAuthor.setText(book.getAuthor());
        tvBookGenre.setText(book.getGenre());
        bookRating.setRating(book.getRate());



        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                Intent i=new Intent(getContext(), addBook.claas);
                i.putExtra("book",book);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                getContext().startActivity(i);
            }
        });







        return vBook;


    }
}
