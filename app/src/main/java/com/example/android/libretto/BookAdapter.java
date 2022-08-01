package com.example.android.libretto;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by margarita baltakiene on 21/06/2017.
 */

public class BookAdapter extends ArrayAdapter<Book> {

    public BookAdapter(Activity context, ArrayList<Book> books) {
        // Initialize the ArrayAdapter's internal storage for the context and the list.
        super(context, 0, books);
    }

    @NonNull
    @Override
    public View getView(int position, View listItemView, ViewGroup parent) {
        // Check if the existing view is being reused, otherwise inflate the view
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.list_item, parent, false);
        }

        // Get the {@link Book} object located at this position in the list
        final Book currentBook = getItem(position);

        // Find the TextView in the list_item.xml layout with the ID book_text_view
        TextView bookTitleTextView = (TextView) listItemView.findViewById(R.id.book_text_view);
        // Get the book title from the current Book object and
        // set this text on the book_text_view
        bookTitleTextView.setText(currentBook.getBookTitle());

        // Find the TextView in the list_item.xml layout with the ID author_text_view
        TextView authorsTextView = (TextView) listItemView.findViewById(R.id.author_text_view);
        // Get the formatted output of the authors array from the current Book object and
        // set this text on the description text view
        authorsTextView.setText(formatAuthorsOutput(currentBook.getAuthors()));

        // Return the whole list item layout
        // so that it can be shown in the ListView
        return listItemView;
    }

    /**
     * Format the output of the authors array
     *
     * @param authors is the array of the book's authors
     * @return param returns formatted output of authors as a single string
     */
    private String formatAuthorsOutput(String[] authors) {
        String line = "";
        boolean first = true;
        if (authors == null) {
            return getContext().getString(R.string.unknown_author);
        }
        for (int i = 0; i < authors.length; i++) {
            if (first) {
                line = line + authors[i];
                first = false;
            } else {
                line = line + ", " + authors[i];
            }
        }
        return line;
    }
}
