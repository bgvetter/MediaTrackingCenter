//import com.mysql.cj.api.jdbc.*;
//import com.mysql.cj.api.jdbc.Statement;

import java.sql.*;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by sylentbv on 4/19/2017.
 */
public class dbAccess {

    private static final String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";        //Configure the driver needed
    private static final String DB_CONNECTION_URL = "jdbc:mysql://localhost:3306/";     //Connection string – where's the database?
    private static final String USER = "bradv";
    private static final String DB_NAME = "mediaTracking";
    private static final String PASSWORD = "mediaTracking";
    static Statement statement = null;
    static Connection conn = null;
    static ResultSet rsMovies = null;
    static ResultSet rsBooks = null;
    static ResultSet rsAlbums = null;
    static ResultSet rsGenre = null;

    public static movieDataModel movieDM;
    public static albumDataModel albumDM;
    public static bookDataModel bookDM;

    public static ArrayList<String> genreAL;

    dbAccess() {
        //create the driver for accessing the db
        try {
            Class.forName(JDBC_DRIVER);
        } catch (ClassNotFoundException cnfe) {
            System.out.println("Can't instantiate driver class; check you have drives and classpath configured correctly?");
            cnfe.printStackTrace();
            System.exit(-1);  //No driver? Need to fix before anything else will work. So quit the program
        }

        //create the connection and statement object for running queries
        try{
            conn = DriverManager.getConnection(DB_CONNECTION_URL + DB_NAME, USER, PASSWORD);
            statement = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);}
        catch (SQLException se){
            se.printStackTrace();
        }

        createTables();
        loadAlbums();
        loadBooks();
        loadMovies();

        setGenres();
    }

    //set up genre list
    private void setGenres(){
        genreAL = new ArrayList();
        genreAL.add("SciFi");
        genreAL.add("Fantasy");
        genreAL.add("Romantic Comedy");
        genreAL.add("Period");
        genreAL.add("Metal");
        genreAL.add("Alternative");
        genreAL.add("Country");
        genreAL.add("Comedy");
        genreAL.add("Rock");
        genreAL.add("Action");
        genreAL.add("NonFiction");
        genreAL.add("Philosophy");
        genreAL.add("Drama");
        genreAL.add("Pop");

        Collections.sort(genreAL);
    }

    //create the table if it doesn't exist
    private void createTables(){
        try{

            String createTableSQLMovies =
                    "CREATE TABLE IF NOT EXISTS Movies (" +
                            "ID int NOT NULL AUTO_INCREMENT, " +
                            "MovieName VARCHAR (100), " +
                            "MovieType VARCHAR (10), " +
                            "Director VARCHAR (100), " +
                            "Genre varchar(50), " +
                            "Description VARCHAR (1000), " +
                            "Actor1 VARCHAR (100), " +
                            "Actor2 VARCHAR (100), " +
                            "Actor3 VARCHAR (100), " +
                            "IMDBID VARCHAR (50), " +
                            "DateAdded DateTime," +
                            "PRIMARY KEY(ID) " +
                            ")";

            statement.execute(createTableSQLMovies);
            System.out.println("Created movies table");

            //create the table if it doesn't exist
            String createTableSQLBooks =
                    "CREATE TABLE IF NOT EXISTS Books (" +
                            "ID int NOT NULL AUTO_INCREMENT, " +
                            "BookName VARCHAR (100), " +
                            "Author VARCHAR (100), " +
                            "Genre varchar(50), " +
                            "Description VARCHAR (1000), " +
                            "ISBN VARCHAR (15), " +
                            "DateAdded DateTime, " +
                            "PRIMARY KEY(ID) " +
                            ")";

            statement.execute(createTableSQLBooks);
            System.out.println("Created books table");

            //create the table if it doesn't exist
            String createTableSQLAlbums =
                    "CREATE TABLE IF NOT EXISTS Albums (" +
                            "ID int NOT NULL AUTO_INCREMENT, " +
                            "AlbumName VARCHAR (100), " +
                            "Artist VARCHAR (100), " +
                            "Genre varchar(50), " +
                            "Description VARCHAR (1000), " +
                            "albumURl varchar(200)," +
                            "DateAdded DateTime, " +
                            "PRIMARY KEY(ID) " +
                            ")";

            statement.execute(createTableSQLAlbums);
            System.out.println("Created albums table");

            testData();

        } catch (SQLException se) {
            se.printStackTrace();
        }
    }

    //Add test data if no data exists
    public void testData(){
        try {

            String sPreparedSQL;
            PreparedStatement psInsert;

            String sql = "Select * from Movies";

            ResultSet rs;
            rs = statement.executeQuery(sql);

            if(!rs.first()){
                //Empty movie results, insert test data
                sPreparedSQL = "Insert into Movies(MovieName," +
                        "MovieType," +
                        "Director," +
                        "Genre," +
                        "Description," +
                        "Actor1," +
                        "Actor2," +
                        "Actor3," +
                        "IMDBID," +
                        "DateAdded) " +
                        "values(?,?,?,?,?,?,?,?,?,?)";
                psInsert = conn.prepareStatement(sPreparedSQL);

                //insert data
                psInsert.setString(1,"Tron");
                psInsert.setString(2,"DVD");
                psInsert.setString(3,"BV");
                psInsert.setString(4,"SciFi");
                psInsert.setString(5,"Good");
                psInsert.setString(6,"Jeff Bridges");
                psInsert.setString(7,"");
                psInsert.setString(8,"");
                psInsert.setString(9,"");
                java.util.Date date = new java.util.Date();
                psInsert.setDate(10,new java.sql.Date(date.getTime()));
                psInsert.executeUpdate();

            }

            sql = "Select * from Books";

            rs = statement.executeQuery(sql);

            if(!rs.first()){
                //Empty movie results, insert test data
                sPreparedSQL = "Insert into Books(BookName," +
                        "Author," +
                        "Genre," +
                        "Description," +
                        "ISBN," +
                        "DateAdded) " +
                        "values(?,?,?,?,?,?)";
                psInsert = conn.prepareStatement(sPreparedSQL);

                //insert data
                psInsert.setString(1,"Hobbit");
                psInsert.setString(2,"J.R.R.Tolkien");
                psInsert.setString(3,"Fantasy");
                psInsert.setString(4,"12345");
                psInsert.setString(5,"Good");
                java.util.Date date = new java.util.Date();
                psInsert.setDate(6,new java.sql.Date(date.getTime()));
                psInsert.executeUpdate();

            }

            sql = "Select * from Albums";

            rs = statement.executeQuery(sql);

            if(!rs.first()){
                //Empty movie results, insert test data
                sPreparedSQL = "Insert into Albums(AlbumName," +
                        "Artist," +
                        "Genre," +
                        "Description," +
                        "AlbumURL, " +
                        "DateAdded) " +
                        "values(?,?,?,?,?,?)";
                psInsert = conn.prepareStatement(sPreparedSQL);

                //insert data
                psInsert.setString(1,"Nevermind");
                psInsert.setString(2,"Nirvana");
                psInsert.setString(3,"Grunge");
                psInsert.setString(4,"Good");
                psInsert.setString(5,"");
                java.util.Date date = new java.util.Date();
                psInsert.setDate(6,new java.sql.Date(date.getTime()));
                psInsert.executeUpdate();

            }
        }
        catch (Exception e) {
            System.out.println("Error loading test data");
            System.out.println(e);
            e.printStackTrace();
        }
    }

    //load movies from database, and update data model with results
    public static boolean loadMovies(){
        try{

            if (rsMovies!=null) {
                rsMovies.close();
            }

            String getAllData = "SELECT * FROM Movies";
            rsMovies = statement.executeQuery(getAllData);

            if (movieDM == null) {
                //If no current movieDataModel, then make one
                movieDM = new movieDataModel(rsMovies);
            } else {
                //Or, if one already exists, update its ResultSet
                movieDM.updateResultSet(rsMovies);
            }

            return true;

        } catch (Exception e) {
            System.out.println("Error loading or reloading movies");
            System.out.println(e);
            e.printStackTrace();
            return false;
        }
    }

    //load albums from database, and update data model with results
    public static boolean loadAlbums(){
        try{

            if (rsAlbums!=null) {
                rsAlbums.close();
            }

            String getAllData = "SELECT * FROM Albums";
            rsAlbums = statement.executeQuery(getAllData);

            if (albumDM == null) {
                //If no current albumDataModel, then make one
                albumDM = new albumDataModel(rsAlbums);
            } else {
                //Or, if one already exists, update its ResultSet
                albumDM.updateResultSet(rsAlbums);
            }

            return true;

        } catch (Exception e) {
            System.out.println("Error loading or reloading albums");
            System.out.println(e);
            e.printStackTrace();
            return false;
        }
    }

    //load books from database, and update data model with results
    public static boolean loadBooks(){
        try{

            if (rsBooks!=null) {
                rsBooks.close();
            }

            String getAllData = "SELECT * FROM Books";
            rsBooks = statement.executeQuery(getAllData);

            if (bookDM == null) {
                //If no current bookDataModel, then make one
                bookDM = new bookDataModel(rsBooks);
            } else {
                //Or, if one already exists, update its ResultSet
                bookDM.updateResultSet(rsBooks);
            }

            return true;

        } catch (Exception e) {
            System.out.println("Error loading or reloading books");
            System.out.println(e);
            e.printStackTrace();
            return false;
        }
    }

    //search movies
    public static boolean searchMovies(String searchText, String searchGenre){
        try{

            if (rsMovies!=null) {
                rsMovies.close();
            }

            String sPreparedSQL;
            PreparedStatement psSearch;

            String sqlSearch = "%"+searchText+"%";
            sPreparedSQL = "Select * from Movies where (MovieName like ? or " +
                    "MovieType like ? or " +
                    "Director like ? or " +
                    "Description like ? or " +
                    "Actor1 like ? or " +
                    "Actor2 like ? or " +
                    "Actor3 like ?)";
            if (searchGenre!="Search All") {
                sPreparedSQL=sPreparedSQL+ " and Genre = ?";
            }
            psSearch = conn.prepareStatement(sPreparedSQL,ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);

            //insert data
            psSearch.setString(1,sqlSearch);
            psSearch.setString(2,sqlSearch);
            psSearch.setString(3,sqlSearch);
            psSearch.setString(4,sqlSearch);
            psSearch.setString(5,sqlSearch);
            psSearch.setString(6,sqlSearch);
            psSearch.setString(7,sqlSearch);
            if (searchGenre!="Search All") {
                psSearch.setString(8, searchGenre);
            }
            rsMovies = psSearch.executeQuery();

            if (movieDM == null) {
                //If no current movieDataModel, then make one
                movieDM = new movieDataModel(rsMovies);
            } else {
                //Or, if one already exists, update its ResultSet
                movieDM.updateResultSet(rsMovies);
            }

            return true;

        } catch (Exception e) {
            System.out.println("Error searching movies");
            System.out.println(e);
            e.printStackTrace();
            return false;
        }
    }

    //search Books
    public static boolean searchBooks(String searchText, String searchGenre){
        try{

            if (rsBooks!=null) {
                rsBooks.close();
            }

            String sPreparedSQL;
            PreparedStatement psSearch;

            String sqlSearch = "%"+searchText+"%";
            sPreparedSQL = "Select * from Books where (BookName like ? or " +
                    "Author like ? or " +
                    "ISBN like ? or " +
                    "Description like ?)";
            if (searchGenre!="Search All") {
                sPreparedSQL=sPreparedSQL+ " and Genre = ?";
            }
            psSearch = conn.prepareStatement(sPreparedSQL,ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);

            //insert data
            psSearch.setString(1,sqlSearch);
            psSearch.setString(2,sqlSearch);
            psSearch.setString(3,sqlSearch);
            psSearch.setString(4,sqlSearch);
            if (searchGenre!="Search All") {
                psSearch.setString(5, searchGenre);
            }
            rsBooks = psSearch.executeQuery();

            if (bookDM == null) {
                //If no current BookDataModel, then make one
                bookDM = new bookDataModel(rsBooks);
            } else {
                //Or, if one already exists, update its ResultSet
                bookDM.updateResultSet(rsBooks);
            }

            return true;

        } catch (Exception e) {
            System.out.println("Error searching books");
            System.out.println(e);
            e.printStackTrace();
            return false;
        }
    }

    //search albums
    public static boolean searchAlbums(String searchText, String searchGenre){
        try{

            if (rsAlbums!=null) {
                rsAlbums.close();
            }

            String sPreparedSQL;
            PreparedStatement psSearch;

            String sqlSearch = "%"+searchText+"%";
            sPreparedSQL = "Select * from Albums where (AlbumName like ? or " +
                    "Artist like ? or " +
                    "Description like ?)";
            if (searchGenre!="Search All") {
                sPreparedSQL=sPreparedSQL+ " and Genre = ?";
            }
            psSearch = conn.prepareStatement(sPreparedSQL,ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);

            //insert data
            psSearch.setString(1,sqlSearch);
            psSearch.setString(2,sqlSearch);
            psSearch.setString(3,sqlSearch);
            if (searchGenre!="Search All") {
                psSearch.setString(4, searchGenre);
            }
            rsAlbums = psSearch.executeQuery();

            if (albumDM == null) {
                //If no current albumDataModel, then make one
                albumDM = new albumDataModel(rsAlbums);
            } else {
                //Or, if one already exists, update its ResultSet
                albumDM.updateResultSet(rsAlbums);
            }

            return true;

        } catch (Exception e) {
            System.out.println("Error searching albums");
            System.out.println(e);
            e.printStackTrace();
            return false;
        }
    }

    //Close the ResultSets, statement and connection, in that order.
    public static void shutdown(){
        try {
            if (rsMovies != null) {
                rsMovies.close();
                System.out.println("Result set closed");
            }
        } catch (SQLException se) {
            se.printStackTrace();
        }

        try {
            if (rsAlbums != null) {
                rsAlbums.close();
                System.out.println("Result set closed");
            }
        } catch (SQLException se) {
            se.printStackTrace();
        }

        try {
            if (rsBooks != null) {
                rsBooks.close();
                System.out.println("Result set closed");
            }
        } catch (SQLException se) {
            se.printStackTrace();
        }

        try {
            if (rsGenre != null) {
                rsGenre.close();
                System.out.println("Result set closed");
            }
        } catch (SQLException se) {
            se.printStackTrace();
        }

        try {
            if (statement != null) {
                statement.close();
                System.out.println("Statement closed");
            }
        } catch (SQLException se){
            //Closing the connection could throw an exception too
            se.printStackTrace();
        }

        try {
            if (conn != null) {
                conn.close();
                System.out.println("Database connection closed");
            }
        }
        catch (SQLException se) {
            se.printStackTrace();
        }
    }
}
