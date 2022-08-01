package com.example.android.libretto;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<Book>> {

    /**
     * Constant value for the book loader ID.
     */
    private static final int BOOK_LOADER_ID = 1;

    /**
     * URL for book data from the Google Books API
     */
    private static final String GOOGLE_BOOKS_REQUEST_URL = "https://www.googleapis.com/books/v1/volumes";

    /**
     * Adapter for the list of books
     */
    private BookAdapter mAdapter;

    /**
     * Empty state Text View
     */
    private TextView mEmptyStateTextView;

    /**
     * Progress bar
     */
    private View mProgressBar;

    /**
     * Inserted search query
     */
    private String mQuery;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            Intent settingsIntent = new Intent(this, SettingsActivity.class);
            startActivity(settingsIntent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public Loader<List<Book>> onCreateLoader(int i, Bundle bundle) {

        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        String maxResults = sharedPrefs.getString(
                getString(R.string.settings_max_results_key),
                getString(R.string.settings_max_results_default));

        String orderBy = sharedPrefs.getString(
                getString(R.string.settings_order_by_key),
                getString(R.string.settings_order_by_default)
        );

        Uri baseUri = Uri.parse(GOOGLE_BOOKS_REQUEST_URL);
        Uri.Builder uriBuilder = baseUri.buildUpon();

        //append url parameters from user input and preferences
        uriBuilder.appendQueryParameter("q", mQuery);
        uriBuilder.appendQueryParameter("prettyPrint", "false");
        uriBuilder.appendQueryParameter("maxResults", maxResults);
        uriBuilder.appendQueryParameter("orderBy", orderBy);

        return new BookLoader(this, uriBuilder.toString());
    }

    @Override
    public void onLoadFinished(Loader<List<Book>> loader, List<Book> books) {
        mProgressBar = findViewById(R.id.loading_spinner);
        mProgressBar.setVisibility(View.GONE);
        mEmptyStateTextView.setText(R.string.no_books);
        // Clear the adapter of previous books data
        mAdapter.clear();

        // If there is a valid list of {@link Book}s, then add them to the adapter's
        // data set. This will trigger the ListView to update.
        if (books != null && !books.isEmpty()) {
            mAdapter.addAll(books);
        }
    }

    @Override
    public void onLoaderReset(Loader<List<Book>> loader) {
        // Loader reset to clear out existing data.
        mAdapter.clear();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Find a reference to the empty state {@link TextView}
        mEmptyStateTextView = (TextView) findViewById(R.id.empty_text_view);

        // Find a reference to the {@link ListView} in the layout
        ListView bookListView = (ListView) findViewById(R.id.list);
        bookListView.setEmptyView(mEmptyStateTextView);

        // Create a new adapter that takes an empty {@link ArrayList} list of books as input
        mAdapter = new BookAdapter(this, new ArrayList<Book>());

        // Set the adapter on the {@link ListView} so the list can be populated in the user interface
        bookListView.setAdapter(mAdapter);

        // Find a reference to a progress bar and hide it
        mProgressBar = findViewById(R.id.loading_spinner);
        mProgressBar.setVisibility(View.GONE);

        // Check if Internet is connected
        if (isConnected()) {
            // when the app is loaded the Empty State view is shown with instructions
            // how to get the books' list populated
            mEmptyStateTextView.setText(R.string.no_books);
        } else {
            // if there is no internet connection, the message is displayed to the user.
            mEmptyStateTextView.setText(R.string.no_internet);
        }

        // Find a reference to the EditText view, where the user will type in query keywords
        final EditText searchQuery = (EditText) findViewById(R.id.query_text);

        // Find a reference to the ImageView view which is a search button
        ImageView searchButton = (ImageView) findViewById(R.id.query_button);
        // Add a click lisener to a button so that the search is executed when the button is clicked
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Get inserted text
                mQuery = searchQuery.getText().toString();

                // Execute search query if the inserted text is not an emty string
                if (!TextUtils.isEmpty(mQuery)) {

                    // Show the spinning progress bar
                    mProgressBar.setVisibility(View.VISIBLE);

                    // Check if Internet is connected
                    if (isConnected()) {

                        // Get a reference to the LoaderManager, in order to interact with loaders.
                        LoaderManager loaderManager = getLoaderManager();

                        // check if the loader exists or not yet initialized
                        if (getLoaderManager().getLoader(BOOK_LOADER_ID) == null) {
                            // initialize the loader
                            loaderManager.initLoader(BOOK_LOADER_ID, null, MainActivity.this);
                        } else {
                            // restart the loader
                            loaderManager.restartLoader(BOOK_LOADER_ID, null, MainActivity.this);
                        }
                    } else {
                        // if there is no internet connection, the message is displayed to the user.
                        mProgressBar.setVisibility(View.GONE);
                        mEmptyStateTextView.setText(R.string.no_internet);
                        mAdapter.clear();
                    }
                }
            }
        });
    }

    /**
     * Check if the Internet is connected
     *
     * @return param return true if connected
     */
    boolean isConnected() {
        // Get a reference to the ConnectivityManager to check state of network connectivity
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        // Get details on the currently active default data network
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
    }
}