package com.hmo_fx.hear_me_out_fx;

import Usuario.Usuario;
import critico.Critico;
import music.Music;
import Album.Album;
import artista.Artista;
import critico.CriticoBuilder;
import music.MusicBuilder;
import Album.AlbumBuilder;
import artista.ArtistaBuilder;
import Usuario.UsuarioBuilder;

import java.time.LocalDate;

public class EntityFactory {

    public static Usuario createUsuario(String nome, String email, String cpf, String telefone) {
        return new UsuarioBuilder()
                .withNome(nome)
                .withEmail(email)
                .withCpf(cpf)
                .withTelefone(telefone)
                .build();
    }

    public static Critico createCritico(String nome, String cpf, LocalDate dataNascimento, String email, String biografia, String site, String genero) {
        return new CriticoBuilder()
                .withNome(nome)
                .withCpf(cpf)
                .withDataNascimento(dataNascimento)
                .withEmail(email)
                .withBiografia(biografia)
                .withSite(site)
                .withGenero(genero)
                .build();
    }

    public static Artista createArtista(String name, String email, LocalDate launchDate, String bio, String cover, String nacionalidade, String site, String generoMusical) {
        return new ArtistaBuilder()
                .withName(name)
                .withEmail(email)
                .withLaunchDate(launchDate)
                .withBio(bio)
                .withCover(cover)
                .withNacionalidade(nacionalidade)
                .withSite(site)
                .withGeneroMusical(generoMusical)
                .build();
    }

    public static Music createMusic(String name, String duration, LocalDate launchDate, String cover, String artist, String album) {
        return new MusicBuilder()
                .withName(name)
                .withDuration(duration)
                .withLaunchDate(launchDate)
                .withCover(cover)
                .withArtist(artist)
                .withAlbum(album)
                .build();
    }

    public static Album createAlbum(String name, String artist, LocalDate launchDate, String cover) {
        return new AlbumBuilder()
                .withName(name)
                .withArtist(artist)
                .withLaunchDate(launchDate)
                .withCover(cover)
                .build();
    }
}