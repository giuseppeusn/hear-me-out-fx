package artista;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class ArtistaBuilder {
    private String name;
    private String email;
    private String launchDate;
    private String bio;
    private String cover;
    private String nacionalidade;
    private String site;
    private String generoMusical;
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    public ArtistaBuilder withName(String name) {
        this.name = name;
        return this;
    }

    public ArtistaBuilder withEmail(String email) {
        this.email = email;
        return this;
    }

    public ArtistaBuilder withLaunchDate(LocalDate launchDate) {
        if (launchDate != null) {
            this.launchDate = launchDate.format(FORMATTER);
        }
        return this;
    }

    public ArtistaBuilder withBio(String bio) {
        this.bio = bio;
        return this;
    }

    public ArtistaBuilder withCover(String cover) {
        this.cover = cover;
        return this;
    }

    public ArtistaBuilder withNacionalidade(String nacionalidade) {
        this.nacionalidade = nacionalidade;
        return this;
    }

    public ArtistaBuilder withSite(String site) {
        this.site = site;
        return this;
    }

    public ArtistaBuilder withGeneroMusical(String generoMusical) {
        this.generoMusical = generoMusical;
        return this;
    }

    public Artista build() {
        return new Artista(name, email, launchDate, bio, cover, nacionalidade, site, generoMusical);
    }
}