import javax.swing.table.AbstractTableModel;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

/**
 * Created by sylentbv on 4/19/2017.
 */
public class movieDataModel extends AbstractTableModel  {

    private int rowCount = 0;
    private int colCount = 0;
    ResultSet resultSet;


    public movieDataModel(ResultSet rs) {
        this.resultSet = rs;
        setup();
    }

    //get row and column counts
    private void setup(){

        countRows();

        try{
            colCount = resultSet.getMetaData().getColumnCount();

        } catch (SQLException se) {
            System.out.println("Error counting columns" + se);
        }

    }

    //update data model with new record set
    public void updateResultSet(ResultSet newRS){
        resultSet = newRS;
        setup();
        fireTableDataChanged();
    }


    private void countRows() {
        rowCount = 0;
        try {
            //Move cursor to the start...
            resultSet.beforeFirst();
            // next() method moves the cursor forward one row and returns true if there is another row ahead
            while (resultSet.next()) {
                rowCount++;

            }
            resultSet.beforeFirst();

        } catch (SQLException se) {
            System.out.println("Error counting rows " + se);
        }

    }
    @Override
    public int getRowCount() {
        countRows();
        return rowCount;
    }

    @Override
    public int getColumnCount(){
        return colCount;
    }

    @Override
    public Object getValueAt(int row, int col){
        try{
            //  System.out.println("get value at, row = " +row);
            resultSet.absolute(row+1);
            Object o = resultSet.getObject(col+1);
            return o.toString();
        }catch (SQLException se) {
            System.out.println(se);
            //se.printStackTrace();
            return se.toString();

        }
    }

    //Delete row, return true if successful, false otherwise
    public boolean deleteRow(int row){
        try {
            resultSet.absolute(row + 1);
            resultSet.deleteRow();
            //Tell table to redraw itself
            fireTableDataChanged();
            return true;
        }catch (SQLException se) {
            System.out.println("Delete row error " + se);
            return false;
        }
    }

    //returns true if successful, false if error occurs
    public boolean insertRow(String iMovieName, String iMovieType, String iDirector,
                             String iGenre, String iDescription, String iActor1, String iActor2,
                             String iActor3, String iIMDBID, Date iDateAdded) {

        try {
            //Move to insert row, insert the appropriate data in each column, insert the row, move cursor back to where it was before we started
            resultSet.moveToInsertRow();
            resultSet.updateString("MovieName", iMovieName);
            resultSet.updateString("MovieType", iMovieType);
            resultSet.updateString("Director", iDirector);
            resultSet.updateString("Genre", iGenre);
            resultSet.updateString("Description", iDescription);
            resultSet.updateString("Actor1", iActor1);
            resultSet.updateString("Actor2", iActor2);
            resultSet.updateString("Actor3", iActor3);
            resultSet.updateString("IMDBID", iIMDBID);
            Date date = new Date();
            resultSet.updateDate("DateAdded", new java.sql.Date(date.getTime()));

            resultSet.insertRow();
            resultSet.moveToCurrentRow();
            fireTableDataChanged();
            return true;

        } catch (SQLException e) {
            System.out.println("Error adding row");
            System.out.println(e);
            return false;
        }

    }

    //updates an existing record in the recordset
    public boolean updateRow(int rowID, String iMovieName, String iMovieType, String iDirector,
                             String iGenre, String iDescription, String iActor1, String iActor2,
                             String iActor3, String iIMDBID) {

        try {
            resultSet.absolute(rowID+1);
            resultSet.updateString("MovieName", iMovieName);
            resultSet.updateString("MovieType", iMovieType);
            resultSet.updateString("Director", iDirector);
            resultSet.updateString("Genre", iGenre);
            resultSet.updateString("Description", iDescription);
            resultSet.updateString("Actor1", iActor1);
            resultSet.updateString("Actor2", iActor2);
            resultSet.updateString("Actor3", iActor3);
            resultSet.updateString("IMDBID", iIMDBID);
            resultSet.updateRow();
            fireTableDataChanged();
            return true;

        } catch (Exception e) {
            System.out.println("Error updating row");
            System.out.println(e);
            return false;
        }

    }
}
