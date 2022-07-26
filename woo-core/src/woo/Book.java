package woo;

import java.io.*;

public class Book extends Product implements Serializable {
    
    /** Serial number for serialization. */
    private static final long serialVersionUID = 202007111940L;
    
    /** Title, author and isbn of the book. */
    private String _title;
    private String _author;
    private String _isbn;
    
    /** Constructor. */
    public Book(String productId, String supplierId, int price,
    int criticalValue, int amount, String title, String author, String isbn){
        super(productId, supplierId, price, criticalValue, amount);
        _title = title;
        _author = author;
        _isbn = isbn;
        super.setDueDate(3);
    }

    /** Get the title. */
    public String getTitle(){
        return _title;
    }

    /** Get the author. */
    public String getAuthor(){
        return _author;
    }

    /** Get the isbn. */
    public String getISBN(){
        return _isbn;
    }

    @Override
    @SuppressWarnings("nls")
    public String toString(){
        String s = "BOOK|";
        String s1 = super.toString();
        String s2 = "|" + _title + "|" + _author + "|" + _isbn;
        s = s + s1 + s2;
        return s;
    }
}
