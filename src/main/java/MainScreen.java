

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import org.json.*;


import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLSocketFactory;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.*;
import java.net.URI;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by sylentbv on 4/25/2017.
 */
public class MainScreen extends JFrame implements WindowListener{
    //controls
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
    private JScrollPane resultsScrollPane;
    private JButton lookUpMovieButton;
    private JTextField imdbIDTF;
    private JButton closeButton;
    private JButton viewIMDBInfoButton;
    private JButton lookUpBookButton;
    private JButton lookUpAlbumButton;
    private JButton webInfoAlbumButton;
    private JTextField albumInfoURLTF;

    dbAccess mainDB;
    int updateID;

    final int mainForm = 0;
    final int movieForm = 1;
    final int bookForm = 2;
    final int albumForm = 3;

    MainScreen(){
        //set up window
        setContentPane(rootPanel);
        pack();
        setTitle("Media Tracking Center");
        addWindowListener(this);

        setSize(new Dimension(500, 500));
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

                String searchMediaType = mediaTypeCB.getSelectedItem().toString();
                //set update id to -1 for new
                updateID = -1;

                switch (searchMediaType){
                    case "Movie":
                        mainDB.searchMovies("","Search All");
                        clearMovieData();
                        switchTabs(movieForm,mainForm);
                        break;
                    case "Book":
                        mainDB.searchBooks("","Search All");
                        clearBookData();
                        switchTabs(bookForm,mainForm);
                        break;
                    case "Album":
                        mainDB.searchAlbums("","Search All");
                        clearAlbumData();
                        switchTabs(albumForm,mainForm);
                        break;
                }
            }
        });

        editButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int currentRow = resultsJT.getSelectedRow();

                if (currentRow == -1) {      // -1 means no row is selected. Display error message.
                    JOptionPane.showMessageDialog(rootPane, "Please choose a record to update.");
                    return;
                }

                //call tab for appropriate selected media type
                String searchMediaType = searchMediaTypeCB.getSelectedItem().toString();
                //set update id to current row id
                updateID = currentRow;

                switch (searchMediaType){
                    case "Movie":
                        setMovieData(currentRow);
                        switchTabs(movieForm,mainForm);
                        break;
                    case "Book":
                        setBookData(currentRow);
                        switchTabs(bookForm,mainForm);
                        break;
                    case "Album":
                        setAlbumData(currentRow);
                        switchTabs(albumForm,mainForm);
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
                        String[] movieColumnNames = {"ID", "Movie Name","Movie Type",
                                "Director","Genre","Description",
                                "Actor 1","Actor 2","Actor 3","IMDB ID", "Date Added"};
                        for(int i=0;i<movieColumnNames.length;i++){
                            resultsJT.getColumnModel().getColumn(i).setHeaderValue(movieColumnNames[i]);
                        }
                        setSize(new Dimension(800, 500));
                        break;
                    case "Book":
                        mainDB.searchBooks(searchText,searchGenre);
                        resultsJT.setModel(mainDB.bookDM);
                        String[] bookColumnNames = {"ID", "Book Name","Author",
                                "Genre","Description","ISBN","Date Added"};
                        for(int i=0;i<bookColumnNames.length;i++){
                            resultsJT.getColumnModel().getColumn(i).setHeaderValue(bookColumnNames[i]);
                        }
                        setSize(new Dimension(650, 500));
                        break;
                    case "Album":
                        mainDB.searchAlbums(searchText,searchGenre);
                        resultsJT.setModel(mainDB.albumDM);
                        String[] albumColumnNames = {"ID", "Album Name","Artist",
                                "Genre","Description", "Album Info URL", "Date Added"};
                        for(int i=0;i<albumColumnNames.length;i++){
                            resultsJT.getColumnModel().getColumn(i).setHeaderValue(albumColumnNames[i]);
                        }
                        setSize(new Dimension(500, 550));
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
                    return;
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

        closeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int quit = JOptionPane.showConfirmDialog(MainScreen.this,"Close tracker?",
                        "Close",JOptionPane.OK_CANCEL_OPTION);
                if(quit==JOptionPane.OK_OPTION) {
                    mainDB.shutdown();
                    System.exit(0);
                }
            }
        });

        cancelBookButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                clearBookData();
                switchTabs(mainForm,bookForm);
            }
        });

        cancelMovieButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                clearMovieData();
                switchTabs(mainForm,movieForm);
            }
        });

        cancelAlbumButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                clearAlbumData();
                switchTabs(mainForm,albumForm);
            }
        });

        saveMovieButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String fMovieName = movieNameTF.getText();
                if(fMovieName.length()==0){
                    JOptionPane.showMessageDialog(MainScreen.this,"Please enter a movie name!");
                    return;
                }
                String fMovieType = movieTypeCB.getSelectedItem().toString();
                String fDirector = directorTF.getText();
                String fGenre = movieGenreCB.getSelectedItem().toString();
                String fDescription = movieDescriptionTF.getText();
                String fActor1 = actor1TF.getText();
                String fActor2 = actor2TF.getText();
                String fActor3 = actor3TF.getText();
                String fIMDBID = imdbIDTF.getText();
                java.util.Date date = new java.util.Date();
                Date fDateAdded = new java.sql.Date(date.getTime());

                if(updateID==-1){
                    //new record
                    mainDB.movieDM.insertRow(fMovieName,fMovieType,fDirector,fGenre,
                            fDescription,fActor1,fActor2,fActor3, fIMDBID,fDateAdded);
                }
                else{
                    //update existing record
                    mainDB.movieDM.updateRow(updateID,fMovieName,fMovieType,fDirector,
                            fGenre,fDescription,fActor1,fActor2,fActor3, fIMDBID);
                }

                clearMovieData();
                switchTabs(mainForm,movieForm);
            }
        });

        saveBookButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String fBookName = bookNameTF.getText();
                if(fBookName.length()==0){
                    JOptionPane.showMessageDialog(MainScreen.this,"Please enter a book name!");
                    return;
                }
                String fAuthor = authorTF.getText();
                String fGenre = bookGenreCB.getSelectedItem().toString();
                String fDescription = bookDescriptionTF.getText();
                String fISBN = isbnTF.getText();
                java.util.Date date = new java.util.Date();
                Date fDateAdded = new java.sql.Date(date.getTime());

                if(updateID==-1){
                    //new record
                    mainDB.bookDM.insertRow(fBookName,fAuthor,fGenre,fDescription,fISBN,fDateAdded);
                }
                else{
                    //update existing record
                    mainDB.bookDM.updateRow(updateID,fBookName,fAuthor,fGenre,fDescription,fISBN);
                }

                clearMovieData();
                switchTabs(mainForm,bookForm);
            }
        });

        saveAlbumButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String fAlbumName = albumNameTF.getText();
                if(fAlbumName.length()==0){
                    JOptionPane.showMessageDialog(MainScreen.this,"Please enter a album name!");
                    return;
                }
                String fArtist = artistTF.getText();
                if(fArtist.length()==0){
                    JOptionPane.showMessageDialog(MainScreen.this,"Please enter an artist!");
                    return;
                }
                String fGenre = albumGenreCB.getSelectedItem().toString();
                String fDescription = albumDescriptionTF.getText();
                java.util.Date date = new java.util.Date();
                Date fDateAdded = new java.sql.Date(date.getTime());
                String fURL = albumInfoURLTF.getText();

                if(updateID==-1){
                    //new record
                    mainDB.albumDM.insertRow(fAlbumName,fArtist,fGenre,fDescription, fURL,fDateAdded);
                }
                else{
                    //update existing record
                    mainDB.albumDM.updateRow(updateID,fAlbumName,fArtist,fGenre,fDescription, fURL);
                }

                clearMovieData();
                switchTabs(mainForm,albumForm);
            }
        });

        lookUpMovieButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String fMovieName = movieNameTF.getText();
                if(fMovieName.length()==0){
                    JOptionPane.showMessageDialog(MainScreen.this,"Please enter a movie name!");
                    return;
                }


                //set up http post call
                HttpClient httpClient = new DefaultHttpClient();
                try {
                    String encodedMovieName = URLEncoder.encode(fMovieName,"UTF-8");

                    //call omdb api for movie info based upon title
                    String url="http://www.omdbapi.com/?t="+encodedMovieName;
                    HttpPost request = new HttpPost(url);
                    //get and parse response
                    HttpResponse response = httpClient.execute(request);
                    String jsonString = EntityUtils.toString(response.getEntity());
                    JSONObject obj = new JSONObject(jsonString);
                    String jDirector = obj.getString("Director");
                    String jIMDBID = obj.getString("imdbID");
                    String jActors = obj.getString("Actors");
                    String[] aActors = jActors.split(",");
                    String jDescription = obj.getString("Plot");
                    String jTitle = obj.getString("Title");

                    //assign values to fields
                    movieNameTF.setText(jTitle);
                    directorTF.setText(jDirector);
                    imdbIDTF.setText(jIMDBID);
                    actor1TF.setText(aActors[0]);
                    if(aActors.length>1){actor2TF.setText(aActors[1]);}
                    if(aActors.length>2){actor3TF.setText(aActors[2]);}
                    if(jDescription.length()>1000){
                        movieDescriptionTF.setText(jDescription.substring(0,999));}
                    else{
                        movieDescriptionTF.setText(jDescription);
                    }


                }catch (Exception ex) {
                    System.out.println(ex.getMessage());
                } finally {
                    httpClient.getConnectionManager().shutdown();
                }
            }
        });

        viewIMDBInfoButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String iIMDBID = imdbIDTF.getText();

                if(iIMDBID.length()<5) {
                    JOptionPane.showMessageDialog(MainScreen.this, "IMDB ID needs to be looked up first!");
                    return;
                }

                try {
                    URL sIMDBURL = new URL("http://www.imdb.com/title/"+iIMDBID+"/");
                    openWebpage(sIMDBURL.toURI());
                } catch (Exception ex) {
                    ex.printStackTrace();
                }

            }
        });

        lookUpBookButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String fISBN = isbnTF.getText();
                if(fISBN.length()==0){
                    JOptionPane.showMessageDialog(MainScreen.this,"Please enter an ISBN value!");
                    return;
                }

                String apiKey="";
                //look up access key
                try (BufferedReader reader = new BufferedReader(new FileReader("key.txt"))){
                    apiKey = reader.readLine();
                }
                catch (IOException ex){
                    System.out.println(ex);
                }

                //set up http post call
                HttpClient httpClient = new DefaultHttpClient();
                try {
                    //call googlebooks api for book info based upon isbn
                    String url="https://www.googleapis.com/books/v1/volumes?q=isbn:"+fISBN+"&key="+apiKey;

                    //Need to set up ssl authentication for correct call
                    SSLSocketFactory sslsocketfactory = (SSLSocketFactory) SSLSocketFactory.getDefault();
                    URL myurl = new URL(url);
                    HttpsURLConnection conn = (HttpsURLConnection)myurl.openConnection();
                    conn.setSSLSocketFactory(sslsocketfactory);
                    InputStream inputstream = conn.getInputStream();
                    InputStreamReader inputstreamreader = new InputStreamReader(inputstream);
                    BufferedReader bufferedreader = new BufferedReader(inputstreamreader);

                    //get json response
                    String jsonString="";
                    String string = null;
                    while ((string = bufferedreader.readLine()) != null) {
                        jsonString+=string;
                    }

                    //parse json object
                    JSONObject obj = new JSONObject(jsonString);
                    JSONArray itemArray = obj.getJSONArray("items");
                    JSONObject volumes =null;
                    int len = itemArray.length();

                    for(int j=0; j<len; j++) {
                        JSONObject json = itemArray.getJSONObject(j);
                        volumes = json.getJSONObject("volumeInfo");
                    }

                    String jBookName = "";
                    String jAuthor = "";
                    String jDescription = "";

                    jBookName = volumes.getString("title");
                    JSONArray authorsArray = volumes.getJSONArray("authors");
                    len = authorsArray.length();

                    for(int j=0; j<len; j++) {
                        jAuthor += authorsArray.getString(j)+",";
                    }
                    jAuthor = jAuthor.substring(0,jAuthor.length()-1);
                    jDescription = volumes.getString("description");

                    //assign results
                    bookNameTF.setText(jBookName);
                    authorTF.setText(jAuthor);
                    if(jDescription.length()>1000){
                        bookDescriptionTF.setText(jDescription.substring(0,999));}
                    else{
                        bookDescriptionTF.setText(jDescription);
                    }

                }catch (Exception ex) {
                    System.out.println(ex.getMessage());
                } finally {
                    httpClient.getConnectionManager().shutdown();
                }
            }
        });

        lookUpAlbumButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String fAlbumName = albumNameTF.getText();
                if(fAlbumName.length()==0){
                    JOptionPane.showMessageDialog(MainScreen.this,"Please enter an album name!");
                    return;
                }

                String fArtistName = artistTF.getText();
                if(fAlbumName.length()==0){
                    JOptionPane.showMessageDialog(MainScreen.this,"Please enter an artist name!");
                    return;
                }

                String apiKey="";
                //look up access key
                try (BufferedReader reader = new BufferedReader(new FileReader("lastfmKey.txt"))){
                    apiKey = reader.readLine();
                }
                catch (IOException ex){
                    System.out.println(ex);
                }

                //set up http post call
                HttpClient httpClient = new DefaultHttpClient();
                try {
                    String encodedAlbumName = URLEncoder.encode(fAlbumName,"UTF-8");
                    String encodedArtistName = URLEncoder.encode(fArtistName,"UTF-8");

                    //call last FM api for album info based upon title
                    String url="http://ws.audioscrobbler.com/2.0/?method=album.getInfo&api_key="+apiKey+
                            "&format=json&album="+encodedAlbumName+"&artist="+encodedArtistName;
                    HttpPost request = new HttpPost(url);
                    //get and parse response
                    HttpResponse response = httpClient.execute(request);
                    String jsonString = EntityUtils.toString(response.getEntity());
                    JSONObject obj = new JSONObject(jsonString);

                    JSONObject albumObj = obj.getJSONObject("album");

                    String jURl = albumObj.getString("url");

                    //assign values to fields
                    albumInfoURLTF.setText(jURl);


                }catch (Exception ex) {
                    System.out.println(ex.getMessage());
                } finally {
                    httpClient.getConnectionManager().shutdown();
                }
            }
        });

        webInfoAlbumButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String urlString = albumInfoURLTF.getText();

                if(urlString.length()<5) {
                    JOptionPane.showMessageDialog(MainScreen.this, "Album Info needs to be looked up first!");
                    return;
                }

                try {
                    URL sAlbumInfo = new URL(urlString);
                    openWebpage(sAlbumInfo.toURI());
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });

    }

    //Try to load the uri using the default pc browser
    public static void openWebpage(URI uri) {

        Desktop desktop = Desktop.isDesktopSupported() ? Desktop.getDesktop() : null;
        if (desktop != null && desktop.isSupported(Desktop.Action.BROWSE)) {
            try {
                desktop.browse(uri);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    //populate fields with selected record
    private void setAlbumData(int iID) {
        albumNameTF.setText(mainDB.albumDM.getValueAt(iID,1).toString()+"");
        artistTF.setText(mainDB.albumDM.getValueAt(iID,2).toString()+"");
        albumGenreCB.setSelectedItem(mainDB.albumDM.getValueAt(iID,3).toString()+"");
        albumDescriptionTF.setText(mainDB.albumDM.getValueAt(iID,4).toString()+"");
        albumInfoURLTF.setText(mainDB.albumDM.getValueAt(iID,5).toString()+"");
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
        albumInfoURLTF.setText("");
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
