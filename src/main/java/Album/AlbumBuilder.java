package Album;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class AlbumBuilder {
    private String name;
    private String artist;
    private String launchDate;
    private String cover;
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    public AlbumBuilder withName(String name) {
        this.name = name;
        return this;
    }

    public AlbumBuilder withArtist(String artist) {
        this.artist = artist;
        return this;
    }

    public AlbumBuilder withLaunchDate(LocalDate launchDate) {
        if (launchDate != null) {
            this.launchDate = launchDate.format(FORMATTER);
        }
        return this;
    }

    public AlbumBuilder withCover(String cover) {
        this.cover = cover;
        return this;
    }

    public Album build() {
        return new Album(name, artist, launchDate, cover);
    }
}