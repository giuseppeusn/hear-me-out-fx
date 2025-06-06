package artista;

import java.io.Serial;
import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Artista implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private String name;
    private String email;
    private Date launchDate;
    private String bio;
    private String cover;
    private String nacionalidade;
    private String site;
    private String generoMusical;

    public Artista(String name, String email, String launchDate, String bio, String cover, String nacionalidade, String site, String generoMusical) {
        this.name = name;
        this.email = email;
        this.launchDate = this.parseDate(launchDate);
        this.bio = bio;
        this.cover = cover;
        this.nacionalidade = nacionalidade;
        this.site = site;
        this.generoMusical = generoMusical;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Date getLaunchDate() {
        return launchDate;
    }

    public void setLaunchDate(Date launchDate) {
        this.launchDate = launchDate;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public String getNacionalidade() {
        return nacionalidade;
    }

    public void setNacionalidade(String nacionalidade) {
        this.nacionalidade = nacionalidade;
    }

    public String getSite() {
        return site;
    }

    public void setSite(String site) {
        this.site = site;
    }

    public String getGeneroMusical() {
        return generoMusical;
    }

    public void setGeneroMusical(String generoMusical) {
        this.generoMusical = generoMusical;
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
        return "Artista" + name + ", email" + email + ", Lançamento" + launchDate + ", bio" + bio + ", cover" + cover + ", nacionalidade" + nacionalidade + ", site" + site + ", genero" + generoMusical;
    }
}
