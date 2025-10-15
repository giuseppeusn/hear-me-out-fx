package music;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class MusicBuilder {
    private String name;
    private String duration;
    private String launchDate;
    private String cover;
    private String artist;
    private String album;
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    public MusicBuilder withName(String name) {
        this.name = name;
        return this;
    }

    public MusicBuilder withDuration(String duration) {
        this.duration = duration;
        return this;
    }

    public MusicBuilder withLaunchDate(LocalDate launchDate) {
        if (launchDate != null) {
            this.launchDate = launchDate.format(FORMATTER);
        }
        return this;
    }

    public MusicBuilder withCover(String cover) {
        this.cover = cover;
        return this;
    }

    public MusicBuilder withArtist(String artist) {
        this.artist = artist;
        return this;
    }

    public MusicBuilder withAlbum(String album) {
        this.album = album;
        return this;
    }

    public Music build() {
        return new Music(name, duration, launchDate, cover, artist, album);
    }
}