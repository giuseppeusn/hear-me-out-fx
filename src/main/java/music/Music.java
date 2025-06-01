package music;

import java.io.Serial;
import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Music implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private String name;
    private String duration;
    private Date launchDate;
    private String cover;
    private String artist;
    private String album;

    public Music(String name, String duration, String launchDate, String cover, String artist, String album) {
        this.name = name;
        this.duration = duration;
        this.launchDate = this.parseDate(launchDate);
        this.cover = cover;
        this.artist = artist;
        this.album = album;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
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

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public String getAlbum() {
        return album;
    }

    public void setAlbum(String album) {
        this.album = album;
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
        return "Música" + name + ", Duração=" + duration + ", Lançamento" + launchDate + ", Capa" + cover + ", Artista" + artist + ", Album" + album;
    }
}
