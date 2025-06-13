package music;

import java.io.*;
import java.util.ArrayList;

public class ManageMusic {
    private static final String FILE_PATH = "musics.dat";

    public static void saveFile(ArrayList<Music> music) {
        try {
            File file = new File(FILE_PATH);

            if (!file.exists()) {
                file.createNewFile();
            }

            ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(file));
            oos.writeObject(music);
            oos.close();

        } catch (FileNotFoundException e) {
            System.out.println("Error: " + e.getMessage());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static ArrayList<Music> readFile() {
        ArrayList<Music> musics = new ArrayList<>();

        try {
            File file = new File(FILE_PATH);

            if (file.exists()) {
                ObjectInputStream ois = new ObjectInputStream(new FileInputStream(FILE_PATH));
                musics = (ArrayList<Music>) ois.readObject();
                ois.close();
            }

        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Error: " + e.getMessage());
        }

        return musics;
    }

    public static void addMusic(Music music) {
        ArrayList<Music> musics = readFile();
        musics.add(music);
        saveFile(musics);
    }

    public static void removeMusic(String name, String artist) {
        ArrayList<Music> musics = readFile();

        Music found = null;
        for (Music m : musics) {
            if (m.getName().equalsIgnoreCase(name) && m.getArtist().equalsIgnoreCase(artist)) {
                found = m;
                break;
            }
        }

        if (found != null) {
            musics.remove(found);
            saveFile(musics);
            System.out.println("Música removida com sucesso.");
        } else {
            System.out.println("Música não encontrada para remoção.");
        }
    }

    public static boolean updateMusic(Music oldMusic, Music newMusic) {
        ArrayList<Music> musics = readFile();
        if (musics == null || musics.isEmpty()) {
            return false;
        }
        for (int i = 0; i < musics.size(); i++) {
            String musicName = musics.get(i).getName();
            String musicArtist = musics.get(i).getArtist();

            if (musicName.equalsIgnoreCase(oldMusic.getName()) && musicArtist.equalsIgnoreCase(oldMusic.getArtist())) {
                musics.set(i, newMusic);
                saveFile(musics);
                return true;
            }
        }
        return false;

    }
}
