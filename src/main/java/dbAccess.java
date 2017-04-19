//import com.mysql.cj.api.jdbc.*;
//import com.mysql.cj.api.jdbc.Statement;

import java.sql.*;

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
    }
}
