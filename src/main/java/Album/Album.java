package Album;

import java.io.Serial;
import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

public class Album implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private String name;
    private String artist;
    private Date launchDate;
    private String cover;

    public Album(String name, String artist, String launchDate, String cover) {
        this.name = name;
        this.artist = artist;
        this.launchDate = this.parseDate(launchDate);
        this.cover = cover;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public Date getLaunchDate() {
        return launchDate;
    }

    public void setLaunchDate(Date launchDate) {
        this.launchDate = launchDate;
    }

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    private Date parseDate(String data) {
        try {
            return new SimpleDateFormat("dd/MM/yyyy").parse(data);
        } catch (ParseException e) {
            e.printStackTrace();
            return new Date();
        }
    }

    @Override
    public String toString() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        return "Álbum: " + name + ", Artista: " + artist + ", Lançamento: " + sdf.format(launchDate) + ", Capa: " + cover;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Album album = (Album) o;
        return Objects.equals(name, album.name) &&
                Objects.equals(artist, album.artist) &&
                Objects.equals(launchDate, album.launchDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, artist, launchDate);
    }
}