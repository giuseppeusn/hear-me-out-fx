package com.hmo_fx.hear_me_out_fx;

import Usuario.ManagerUsuario;
import Usuario.Usuario;
import critico.ManageCritico;
import critico.Critico;
import music.ManageMusic;
import music.Music;
import Album.ManageAlbum;
import Album.Album;
import artista.ManageArtista;
import artista.Artista;

public class PersistenceFacade {

    public static boolean addEntity(Object entity) {
        if (entity instanceof Usuario) {
            Usuario user = (Usuario) entity;
            if (ManagerUsuario.cpfExists(user.getCpf())) {
                return false;
            }
            ManagerUsuario.addUsuario(user);
            return true;
        } else if (entity instanceof Critico) {
            Critico critic = (Critico) entity;
            if (ManageCritico.cpfExists(critic.getCpf())) {
                return false;
            }
            ManageCritico.addCritic(critic);
            return true;
        } else if (entity instanceof Music) {
            ManageMusic.addMusic((Music) entity);
            return true;
        } else if (entity instanceof Album) {
            ManageAlbum.addAlbum((Album) entity);
            return true;
        } else if (entity instanceof Artista) {
            ManageArtista.addArtista((Artista) entity);
            return true;
        }
        return false;
    }

    public static void updateEntity(Object oldEntity, Object newEntity) {
        if (oldEntity instanceof Usuario && newEntity instanceof Usuario) {
            ManagerUsuario.updateUsuario((Usuario) oldEntity, (Usuario) newEntity);
        } else if (oldEntity instanceof Critico && newEntity instanceof Critico) {
            ManageCritico.updateCritic((Critico) oldEntity, (Critico) newEntity);
        } else if (oldEntity instanceof Music && newEntity instanceof Music) {
            ManageMusic.updateMusic((Music) oldEntity, (Music) newEntity);
        } else if (oldEntity instanceof Album && newEntity instanceof Album) {
            ManageAlbum.updateAlbum((Album) oldEntity, (Album) newEntity);
        } else if (oldEntity instanceof Artista && newEntity instanceof Artista) {
            ManageArtista.updateArtista((Artista) oldEntity, (Artista) newEntity);
        }
    }
}