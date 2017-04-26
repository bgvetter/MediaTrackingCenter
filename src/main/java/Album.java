import java.util.Date;

/**
 * Created by sylentbv on 4/25/2017.
 */
public class Album {

    int ID;
    String AlbumName;
    String Artist;
    String Genre;
    String Description;
    Date DateAdded;

    public int getID() {
        return ID;
    }

    public String getAlbumName() {
        return AlbumName;
    }

    public void setAlbumName(String AlbumName) {
        AlbumName = AlbumName;
    }

    public String getArtist() {
        return Artist;
    }

    public void setArtist(String Artist) {
        Artist = Artist;
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

    public Date getDateAdded() {
        return DateAdded;
    }

    public void setDateAdded(Date dateAdded) {
        DateAdded = dateAdded;
    }

    Album(String iAlbumName, String iArtist){
        AlbumName = iAlbumName;
        Artist = iArtist;
        DateAdded = new Date();
    }

    Album(int iID, String iAlbumName, String iArtist, String iGenre, String iDescription, Date iDateAdded){
        ID = iID;
        AlbumName=iAlbumName;
        Artist=iArtist;
        Genre=iGenre;
        Description = iDescription;
        DateAdded= iDateAdded;
    }

    @Override
    public String toString() {
        String sResult;
        sResult=AlbumName;
        if(Artist.length()>0){sResult+=" written by "+Artist;}
        if(Description.length()>0){sResult+=" Synopsis: "+Description;}
        return sResult;
    }

}
