import java.util.Date;

/**
 * Created by sylentbv on 4/23/2017.
 */
public class Movie {

    int ID;
    String MovieName;
    String MovieType;
    String Director;
    String Genre;
    String Description;
    String Actor1;
    String Actor2;
    String Actor3;
    Date DateAdded;

    public int getID() {
        return ID;
    }

    public String getMovieName() {
        return MovieName;
    }

    public void setMovieName(String movieName) {
        MovieName = movieName;
    }

    public String getMovieType() {
        return MovieType;
    }

    public void setMovieType(String movieType) {
        MovieType = movieType;
    }

    public Date getDateAdded() {
        return DateAdded;
    }

    public void setDateAdded(Date dateAdded) {
        DateAdded = dateAdded;
    }

    public String getDirector() {
        return Director;
    }

    public void setDirector(String director) {
        Director = director;
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

    public String getActor1() {
        return Actor1;
    }

    public void setActor1(String actor1) {
        Actor1 = actor1;
    }

    public String getActor2() {
        return Actor2;
    }

    public void setActor2(String actor2) {
        Actor2 = actor2;
    }

    public String getActor3() {
        return Actor3;
    }

    public void setActor3(String actor3) {
        Actor3 = actor3;
    }



    Movie(int i, String iMovieName, String iMovieType) {
        ID = i;
        MovieName = iMovieName;
        MovieType = iMovieType;
        DateAdded = new Date();
    }

    Movie(int i, String iMovieName, String iMovieType, String iDirector,
          String iGenre, String iDescription, String iActor1, String iActor2,
          String iActor3, Date iDateAdded) {
        ID = i;
        MovieName = iMovieName;
        MovieType = iMovieType;
        Director= iDirector;
        Genre = iGenre;
        Description=iDescription;
        Actor1=iActor1;
        Actor2=iActor2;
        Actor3=iActor3;
        DateAdded = iDateAdded;
    }

    @Override
    public String toString() {
        String sResult;
        sResult=MovieName + "("+MovieType+")";
        if(Actor1.length()>0){sResult+=" starring "+Actor1;}
        if(Actor2.length()>0){sResult+=" and "+Actor2;}
        if(Actor3.length()>0){sResult+=" and "+Actor3;}
        if(Director.length()>0){sResult+=" Directed by "+Director;}
        if(Description.length()>0){sResult+=" Synopsis: "+Description;}
        return sResult;
    }
}
