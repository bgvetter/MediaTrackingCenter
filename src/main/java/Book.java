import java.util.Date;

/**
 * Created by sylentbv on 4/25/2017.
 */
public class Book {

    int ID;
    String BookName;
    String Author;
    String Genre;
    String Description;
    String ISBN;
    Date DateAdded;

    public int getID() {
        return ID;
    }

    public String getBookName() {
        return BookName;
    }

    public void setBookName(String bookName) {
        BookName = bookName;
    }

    public String getAuthor() {
        return Author;
    }

    public void setAuthor(String author) {
        Author = author;
    }

    public String getGenre() {
        return Genre;
    }

    public void setGenre(String genre) {
        Genre = genre;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public String getISBN() {
        return ISBN;
    }

    public void setISBN(String ISBN) {
        this.ISBN = ISBN;
    }

    public Date getDateAdded() {
        return DateAdded;
    }

    public void setDateAdded(Date dateAdded) {
        DateAdded = dateAdded;
    }

    Book(String iBookName){
        BookName = iBookName;
        DateAdded = new Date();
    }

    Book(int iID, String iBookName, String iAuthor, String iGenre, String iDescription,
         String iISBN, Date iDateAdded){
        ID = iID;
        BookName=iBookName;
        Author=iAuthor;
        Genre=iGenre;
        Description = iDescription;
        ISBN=iISBN;
        DateAdded= iDateAdded;
    }

    @Override
    public String toString() {
        String sResult;
        sResult=BookName;
        if(Author.length()>0){sResult+=" written by "+Author;}
        if(ISBN.length()>0){sResult+=" ISBN "+ISBN;}
        if(Description.length()>0){sResult+=" Synopsis: "+Description;}
        return sResult;
    }

}
