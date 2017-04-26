//import com.mysql.cj.api.jdbc.*;
//import com.mysql.cj.api.jdbc.Statement;

import java.sql.*;
import java.sql.Date;

/**
 * Created by sylentbv on 4/19/2017.
 */
public class dbAccess {

    private static final String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";        //Configure the driver needed
    private static final String DB_CONNECTION_URL = "jdbc:mysql://localhost:3306/";     //Connection string – where's the database?
    private static final String USER = "bradv";
    private static final String DB_NAME = "mediaTracking";
    private static final String PASSWORD = System.getenv("MYSQLPW");
    static Statement statement = null;
    static Connection conn = null;
    static ResultSet rsMovies = null;
    static ResultSet rsBooks = null;
    static ResultSet rsAlbums = null;
    static ResultSet rsGenre = null;

    public static movieDataModel movieDM;
    public static albumDataModel albumDM;
    public static bookDataModel bookDM;

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
    }

    private void createTables(){
        try{
            //create the table if it doesn't exist
            String createTableSQLMovies =
                    "CREATE TABLE IF NOT EXISTS Movies (" +
                            "ID int NOT NULL AUTO_INCREMENT, " +
                            "MovieName VARCHAR (100), " +
                            "MovieType VARCHAR (10), " +
                            "Director VARCHAR (100), " +
                            "Genre varchar(50), " +
                            "Description VARCHAR (250), " +
                            "Actor1 VARCHAR (100), " +
                            "Actor2 VARCHAR (100), " +
                            "Actor3 VARCHAR (100), " +
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
                            "Description VARCHAR (250), " +
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
                            "Description VARCHAR (250), " +
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
                        "DateAdded) " +
                        "values(?,?,?,?,?,?,?,?,?)";
                psInsert = conn.prepareStatement(sPreparedSQL);

                //insert data
                psInsert.setString(1,"Tron");
                psInsert.setString(2,"DVD");
                psInsert.setString(3,"BV");
                psInsert.setString(4,"SciFi");
                psInsert.setString(5,"Good");
                psInsert.setString(6,"Jeff Bridges");
                psInsert.setString(7,null);
                psInsert.setString(8,null);
                java.util.Date date = new java.util.Date();
                psInsert.setDate(9,new java.sql.Date(date.getTime()));
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
                        "DateAdded) " +
                        "values(?,?,?,?,?)";
                psInsert = conn.prepareStatement(sPreparedSQL);

                //insert data
                psInsert.setString(1,"Nevermind");
                psInsert.setString(2,"Nirvana");
                psInsert.setString(3,"Grunge");
                psInsert.setString(4,"Good");
                java.util.Date date = new java.util.Date();
                psInsert.setDate(5,new java.sql.Date(date.getTime()));
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
                //If no current movieDataModel, then make one
                albumDM = new albumDataModel(rsAlbums);
            } else {
                //Or, if one already exists, update its ResultSet
                albumDM.updateResultSet(rsAlbums);
            }

            return true;

        } catch (Exception e) {
            System.out.println("Error loading or reloading movies");
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
                //If no current movieDataModel, then make one
                bookDM = new bookDataModel(rsBooks);
            } else {
                //Or, if one already exists, update its ResultSet
                bookDM.updateResultSet(rsBooks);
            }

            return true;

        } catch (Exception e) {
            System.out.println("Error loading or reloading movies");
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
