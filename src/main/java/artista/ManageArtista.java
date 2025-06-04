package artista;

import artista.Artista;

import java.io.*;
import java.util.ArrayList;

public class ManageArtista {
    private static final String FILE_PATH = "artistas.dat";

    public static void saveFile(ArrayList<Artista> artista) {
        try {
            File file = new File(FILE_PATH);

            if (!file.exists()) {
                file.createNewFile();
            }

            ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(file));
            oos.writeObject(artista);
            oos.close();

        } catch (FileNotFoundException e) {
            System.out.println("Error: " + e.getMessage());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static ArrayList<Artista> readFile() {
        ArrayList<Artista> artistas = new ArrayList<>();

        try {
            File file = new File(FILE_PATH);

            if (file.exists()) {
                ObjectInputStream ois = new ObjectInputStream(new FileInputStream(FILE_PATH));
                artistas = (ArrayList<Artista>) ois.readObject();
                ois.close();
            }

        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Error: " + e.getMessage());
        }

        return artistas;
    }

    public static void addArtista(Artista artista) {
        ArrayList<Artista> artistas = readFile();
        artistas.add(artista);
        saveFile(artistas);
    }

    public static void removeArtista(String name, String email) {
        ArrayList<Artista> artistas = readFile();

        Artista found = null;
        for (Artista a : artistas) {
            if (a.getName().equalsIgnoreCase(name) && a.getEmail().equalsIgnoreCase(email)) {
                found = a;
                break;
            }
        }

        if (found != null) {
            artistas.remove(found);
            saveFile(artistas);
            System.out.println("Artista removido com sucesso.");
        } else {
            System.out.println("Artista não encontrado para remoção.");
        }
    }

    public static void updateArtista(Artista oldArtista, Artista newArtista) {
        ArrayList<Artista> artistas = readFile();
        for (int i = 0; i < artistas.size(); i++) {
            if (artistas.get(i).equals(oldArtista)) {
                artistas.set(i, newArtista);
                saveFile(artistas);
                return;
            }
        }
    }
}
