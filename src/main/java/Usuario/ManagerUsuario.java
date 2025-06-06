package Usuario;

import java.io.*;
import java.util.ArrayList;

public class ManagerUsuario {
    private static final String FILE_PATH = "usuarios.dat";

    public static void saveFile(ArrayList<usuarioPrincipal> usuarios) {
        try {
            File file = new File(FILE_PATH);

            if (!file.exists()) {
                file.createNewFile();
            }

            ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(file));
            oos.writeObject(usuarios);
            oos.close();

        } catch (IOException e) {
            System.out.println("Erro ao salvar: " + e.getMessage());
        }
    }

    public static ArrayList<usuarioPrincipal> readFile() {
        ArrayList<usuarioPrincipal> usuarios = new ArrayList<>();

        try {
            File file = new File(FILE_PATH);

            if (file.exists()) {
                ObjectInputStream ois = new ObjectInputStream(new FileInputStream(FILE_PATH));
                usuarios = (ArrayList<usuarioPrincipal>) ois.readObject();
                ois.close();
            }

        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Erro ao ler: " + e.getMessage());
        }

        return usuarios;
    }

    public static void addUsuario(usuarioPrincipal usuario) {
        ArrayList<usuarioPrincipal> usuarios = readFile();
        usuarios.add(usuario);
        saveFile(usuarios);
    }

    public static void removeUsuario(String email) {
        ArrayList<usuarioPrincipal> usuarios = readFile();

        usuarioPrincipal found = null;
        for (usuarioPrincipal u : usuarios) {
            if (u.getEmail().equalsIgnoreCase(email)) {
                found = u;
                break;
            }
        }

        if (found != null) {
            usuarios.remove(found);
            saveFile(usuarios);
            System.out.println("Conta removida com sucesso.");
        } else {
            System.out.println("Conta não encontrada para remoção.");
        }
    }

    public static void updateUsuario(usuarioPrincipal oldUsuario, usuarioPrincipal newUsuario) {
        ArrayList<usuarioPrincipal> usuarios = readFile();
        for (int i = 0; i < usuarios.size(); i++) {
            if (usuarios.get(i).getEmail().equalsIgnoreCase(oldUsuario.getEmail())) {
                usuarios.set(i, newUsuario);
                saveFile(usuarios);
                return;
            }
        }
    }
}
