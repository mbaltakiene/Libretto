package com.example.android.libretto;

/**
 * Created by margarita baltakiene on 21/06/2017.
 */

public class Book {
    /**
     * Title of the book.
     */
    private String mBookTitle;
    /**
     * 1 or more authors of the book.
     */
    private String[] mAuthors;

    /**
     * Create a new Book object
     * used for the Food Category list
     *
     * @param title   is the name of the book
     * @param authors is the authors of the book
     */
    public Book(String title, String[] authors) {
        mBookTitle = title;
        mAuthors = authors;
    }

    /**
     * Get the book title
     *
     * @return param returns the book title
     */
    public String getBookTitle() {
        return mBookTitle;
    }

    /**
     * Get the book authors
     *
     * @return param returns the book authors
     */
    public String[] getAuthors() {
        return mAuthors;
    }

}
