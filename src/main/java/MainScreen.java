import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.ArrayList;

/**
 * Created by sylentbv on 4/25/2017.
 */
public class MainScreen extends JFrame implements WindowListener{
    private JPanel rootPanel;
    private JComboBox mediaTypeCB;
    private JButton addNewButton;
    private JTextField searchTextTF;
    private JComboBox searchGenreCB;
    private JComboBox searchMediaTypeCB;
    private JButton searchButton;
    private JTable resultsJT;
    private JButton editButton;
    private JButton deleteButton;
    private JPanel resultsPanel;
    private JPanel Main;
    private JTextField movieNameTF;
    private JComboBox movieTypeCB;
    private JComboBox movieGenreCB;
    private JTextField directorTF;
    private JTextField actor1TF;
    private JTextField actor2TF;
    private JTextField actor3TF;
    private JTextArea movieDescriptionTF;
    private JButton saveMovieButton;
    private JButton cancelMovieButton;
    private JPanel moviePanel;
    private JPanel bookPanel;
    private JTextField bookNameTF;
    private JTextField authorTF;
    private JComboBox bookGenreCB;
    private JTextField isbnTF;
    private JTextArea bookDescriptionTF;
    private JButton saveBookButton;
    private JButton cancelBookButton;
    private JPanel albumPanel;
    private JTextField albumNameTF;
    private JTextField artistTF;
    private JComboBox albumGenreCB;
    private JTextArea albumDescriptionTF;
    private JButton saveAlbumButton;
    private JButton cancelAlbumButton;
    private JTabbedPane tabbedPanel;

    dbAccess mainDB;


    MainScreen(){
        //set up window
        setContentPane(rootPanel);
        pack();
        setTitle("Media Tracking Center");
        addWindowListener(this);

        setSize(new Dimension(500, 400));
        setLocation(500,200);

        setVisible(true);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        mainDB = new dbAccess();

        //add data to drop down boxes
        setComboBoxes();
        //disable tabs
        switchTabs(0,1);
        switchTabs(0,2);
        switchTabs(0,3);

        //set up button listeners
        addListeners();
    }

    //add data to drop down boxes
    private void setComboBoxes() {
        ArrayList<String> genreAL = mainDB.genreAL;


        searchGenreCB.addItem("Search All");

        for(String s : genreAL){
            searchGenreCB.addItem(s);
            movieGenreCB.addItem(s);
            bookGenreCB.addItem(s);
            albumGenreCB.addItem(s);
        }

        ArrayList<String> mediaTypeAL = new ArrayList<>();
        mediaTypeAL.add("Movie");
        mediaTypeAL.add("Book");
        mediaTypeAL.add("Album");

        for(String s : mediaTypeAL){
            mediaTypeCB.addItem(s);
            searchMediaTypeCB.addItem(s);
        }

        ArrayList<String> movieTypeAL = new ArrayList<>();
        movieTypeAL.add("Blu-ray");
        movieTypeAL.add("DVD");

        for(String s : movieTypeAL){
            movieTypeCB.addItem(s);
        }
    }

    private void addListeners() {
        addNewButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //call tab for appropriate selected media type

                String searchMediaType = searchMediaTypeCB.getSelectedItem().toString();

                switch (searchMediaType){
                    case "Movie":
                        clearMovieData();
                        switchTabs(1,0);
                        break;
                    case "Book":
                        clearBookData();
                        switchTabs(2,0);
                        break;
                    case "Album":
                        clearAlbumData();
                        switchTabs(3,0);
                        break;
                }
            }
        });

        editButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int currentRow = resultsJT.getSelectedRow();

                if (currentRow == -1) {      // -1 means no row is selected. Display error message.
                    JOptionPane.showMessageDialog(rootPane, "Please choose a record to delete");
                }

                //call tab for appropriate selected media type
                String searchMediaType = searchMediaTypeCB.getSelectedItem().toString();

                switch (searchMediaType){
                    case "Movie":
                        setMovieData(currentRow);
                        switchTabs(1,0);
                        break;
                    case "Book":
                        setBookData(currentRow);
                        switchTabs(2,0);
                        break;
                    case "Album":
                        setAlbumData(currentRow);
                        switchTabs(3,0);
                        break;
                }
            }
        });

        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //populate jtable with results

                String searchText = searchTextTF.getText();
                String searchGenre = searchGenreCB.getSelectedItem().toString();
                String searchMediaType = searchMediaTypeCB.getSelectedItem().toString();

                switch (searchMediaType){
                    case "Movie":
                        mainDB.searchMovies(searchText,searchGenre);
                        resultsJT.setModel(mainDB.movieDM);
                        setSize(new Dimension(800, 400));
                        break;
                    case "Book":
                        mainDB.searchBooks(searchText,searchGenre);
                        resultsJT.setModel(mainDB.bookDM);
                        setSize(new Dimension(650, 400));
                        break;
                    case "Album":
                        mainDB.searchAlbums(searchText,searchGenre);
                        resultsJT.setModel(mainDB.albumDM);
                        setSize(new Dimension(500, 400));
                        break;
                }
            }
        });

        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //delete selected record
                int currentRow = resultsJT.getSelectedRow();

                if (currentRow == -1) {      // -1 means no row is selected. Display error message.
                    JOptionPane.showMessageDialog(rootPane, "Please choose a record to delete");
                }
                //call the delete function
                boolean deleted=false;
                String searchMediaType = searchMediaTypeCB.getSelectedItem().toString();

                switch (searchMediaType){
                    case "Movie":
                        deleted=mainDB.movieDM.deleteRow(currentRow);
                        //resultsJT.setModel(mainDB.movieDM);
                        break;
                    case "Book":
                        deleted=mainDB.bookDM.deleteRow(currentRow);
                        break;
                    case "Album":
                        deleted=mainDB.albumDM.deleteRow(currentRow);
                        break;
                }

                if (!deleted) {
                    JOptionPane.showMessageDialog(rootPane, "Error deleting record");
                }
            }
        });

        cancelBookButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                clearBookData();
                switchTabs(0,2);
            }
        });

        cancelMovieButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                clearMovieData();
                switchTabs(0,1);
            }
        });

        cancelAlbumButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                clearAlbumData();
                switchTabs(0,3);
            }
        });
    }

    //populate fields with selected record
    private void setAlbumData(int iID) {
        albumNameTF.setText(mainDB.albumDM.getValueAt(iID,1).toString()+"");
        artistTF.setText(mainDB.albumDM.getValueAt(iID,2).toString()+"");
        albumGenreCB.setSelectedItem(mainDB.albumDM.getValueAt(iID,3).toString()+"");
        albumDescriptionTF.setText(mainDB.albumDM.getValueAt(iID,4).toString()+"");
    }

    private void setBookData(int iID) {
        bookNameTF.setText(mainDB.bookDM.getValueAt(iID,1).toString()+"");
        authorTF.setText(mainDB.bookDM.getValueAt(iID,2).toString()+"");
        bookGenreCB.setSelectedItem(mainDB.bookDM.getValueAt(iID,3).toString()+"");
        bookDescriptionTF.setText(mainDB.bookDM.getValueAt(iID,4).toString()+"");
        isbnTF.setText(mainDB.bookDM.getValueAt(iID,5).toString()+"");
    }

    private void setMovieData(int iID) {
        movieNameTF.setText(mainDB.movieDM.getValueAt(iID,1).toString()+"");
        movieTypeCB.setSelectedItem(mainDB.movieDM.getValueAt(iID,2).toString());
        directorTF.setText(mainDB.movieDM.getValueAt(iID,3).toString()+"");
        movieGenreCB.setSelectedItem(mainDB.movieDM.getValueAt(iID,4).toString());
        movieDescriptionTF.setText(mainDB.movieDM.getValueAt(iID,5).toString()+"");
        actor1TF.setText(mainDB.movieDM.getValueAt(iID,6).toString()+"");
        actor2TF.setText(mainDB.movieDM.getValueAt(iID,7).toString()+"");
        actor3TF.setText(mainDB.movieDM.getValueAt(iID,8).toString()+"");

    }

    //enable and disable appropriate tabs
    private void switchTabs(int enableTabID,int disableTabID){

        tabbedPanel.setEnabledAt(enableTabID,true);
        tabbedPanel.setSelectedIndex(enableTabID);
        tabbedPanel.setEnabledAt(disableTabID,false);
    }

    //clear text fields for new records
    private void clearMovieData(){
        movieNameTF.setText("");
        movieTypeCB.setSelectedIndex(0);
        movieGenreCB.setSelectedIndex(0);
        directorTF.setText("");
        actor1TF.setText("");
        actor2TF.setText("");
        actor3TF.setText("");
        movieDescriptionTF.setText("");
    }
    private void clearBookData(){
        bookNameTF.setText("");
        bookGenreCB.setSelectedIndex(0);
        authorTF.setText("");
        isbnTF.setText("");
        bookDescriptionTF.setText("");
    }
    private void clearAlbumData(){
        albumNameTF.setText("");
        albumGenreCB.setSelectedIndex(0);
        artistTF.setText("");
        albumDescriptionTF.setText("");
    }


    @Override
    public void windowClosing(WindowEvent e) {
        mainDB.shutdown();
        System.out.println("closing");
    }
    @Override
    public void windowClosed(WindowEvent e) {}
    @Override
    public void windowOpened(WindowEvent e) {}
    @Override
    public void windowIconified(WindowEvent e) {}
    @Override
    public void windowDeiconified(WindowEvent e) {}
    @Override
    public void windowActivated(WindowEvent e) {}
    @Override
    public void windowDeactivated(WindowEvent e) {}
}
