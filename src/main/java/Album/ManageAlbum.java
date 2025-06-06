package Album;

import java.io.*;
import java.util.ArrayList;

public class ManageAlbum {
    private static final String FILE_PATH = "albums.dat";

    public static void saveFile(ArrayList<Album> albums) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILE_PATH))) {
            oos.writeObject(albums);
        } catch (IOException e) {
            System.err.println("Erro ao salvar o arquivo de álbuns: " + e.getMessage());
        }
    }

    public static ArrayList<Album> readFile() {
        ArrayList<Album> albums = new ArrayList<>();
        File file = new File(FILE_PATH);

        if (!file.exists() || file.length() == 0) {
            return albums;
        }

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(FILE_PATH))) {
            albums = (ArrayList<Album>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Erro ao ler o arquivo de álbuns: " + e.getMessage());
        }
        return albums;
    }

    public static void addAlbum(Album album) {
        ArrayList<Album> albums = readFile();
        albums.add(album);
        saveFile(albums);
    }

    public static void removeAlbum(String name, String artist) {
        ArrayList<Album> albums = readFile();
        Album found = null;
        for (Album a : albums) {
            if (a.getName().equalsIgnoreCase(name) && a.getArtist().equalsIgnoreCase(artist)) {
                found = a;
                break;
            }
        }

        if (found != null) {
            albums.remove(found);
            saveFile(albums);
            System.out.println("Álbum removido com sucesso.");
        } else {
            System.out.println("Álbum não encontrado para remoção.");
        }
    }

    public static void updateAlbum(Album oldAlbum, Album newAlbum) {
        ArrayList<Album> albums = readFile();
        for (int i = 0; i < albums.size(); i++) {
            if (albums.get(i).equals(oldAlbum)) {
                albums.set(i, newAlbum);
                saveFile(albums);
                return;
            }
        }
        System.out.println("Álbum não encontrado para atualização.");
    }
}