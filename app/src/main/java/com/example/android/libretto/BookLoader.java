package com.example.android.libretto;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.util.Log;

import java.util.List;

/**
 * Created by margarita baltakiene on 21/06/2017.
 */

public class BookLoader extends AsyncTaskLoader<List<Book>> {
    private String mUrl;

    public BookLoader(Context context, String url) {
        super(context);
        mUrl = url;
    }

    @Override
    protected void onStartLoading() {
        Log.i(BookLoader.class.getSimpleName(), "onStartLoading() method");
        forceLoad();
    }

    @Override
    public List<Book> loadInBackground() {
        Log.i(BookLoader.class.getSimpleName(), "loadInBackground() method");
        if (mUrl == null) {
            return null;
        }
        // Perform the network request, parse the response, and extract a list of books.
        List<Book> books = QueryUtils.fetchBookData(mUrl);
        return books;
    }
}
