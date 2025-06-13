package artista;

import artista.Artista;

import java.io.*;
import java.util.ArrayList;

public class ManageArtista {
    private static final String FILE_PATH = "artistas.dat";

    public static void saveFile(ArrayList<Artista> artista) {
        File file = new File(FILE_PATH);
        System.out.println("Arquivo existe? " + file.exists());

        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(file))) {
            oos.writeObject(artista);
            System.out.println("Arquivo salvo com sucesso!");
        } catch (FileNotFoundException e) {
            System.out.println("Erro: arquivo não encontrado - " + e.getMessage());
            e.printStackTrace();
        } catch (IOException e) {
            System.out.println("Erro de IO: " + e.getMessage());
            e.printStackTrace();
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

    public static boolean updateArtista(Artista oldArtista, Artista newArtista) {
        ArrayList<Artista> artistas = readFile();

        if (artistas == null || artistas.isEmpty()) {
            return false;
        }

        for (int i = 0; i < artistas.size(); i++) {
            Artista a = artistas.get(i);
            if (a.getName().equalsIgnoreCase(oldArtista.getName()) &&
                    a.getEmail().equalsIgnoreCase(oldArtista.getEmail())) {
                artistas.set(i, newArtista);
                saveFile(artistas);
                System.out.println("Artista atualizado com sucesso.");
                return true;
            }
        }

        return false;
    }
}
