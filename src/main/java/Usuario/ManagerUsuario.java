package Usuario;

import java.io.*;
import java.util.ArrayList;

public class ManagerUsuario {
    private static final String FILE_PATH = "usuarios.dat";

    public static void saveFile(ArrayList<Usuario> usuarios) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILE_PATH))) {
            oos.writeObject(usuarios);
        } catch (IOException e) {
            System.out.println("Erro ao salvar o arquivo de usuários: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static ArrayList<Usuario> readFile() {
        ArrayList<Usuario> usuarios = new ArrayList<>();
        File file = new File(FILE_PATH);

        if (file.exists() && file.length() > 0) {
            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
                usuarios = (ArrayList<Usuario>) ois.readObject();
            } catch (IOException | ClassNotFoundException e) {
                System.out.println("Erro ao ler o arquivo de usuários: " + e.getMessage());
                e.printStackTrace();
            }
        }
        return usuarios;
    }

    public static boolean cpfExists(String cpf) {
        ArrayList<Usuario> usuarios = readFile();
        for (Usuario usuario : usuarios) {
            if (usuario.getCpf().equals(cpf)) {
                return true;
            }
        }
        return false;
    }

    public static void addUsuario(Usuario usuario) {
        ArrayList<Usuario> usuarios = readFile();
        usuarios.add(usuario);
        saveFile(usuarios);
    }

    public static void removeUsuario(String email) {
        ArrayList<Usuario> usuarios = readFile();
        boolean removed = usuarios.removeIf(u -> u.getEmail().equalsIgnoreCase(email));
        if (removed) {
            saveFile(usuarios);
            System.out.println("Conta removida com sucesso.");
        } else {
            System.out.println("Conta não encontrada para remoção.");
        }
    }

    public static void updateUsuario(Usuario oldUsuario, Usuario newUsuario) {
        ArrayList<Usuario> usuarios = readFile();
        for (int i = 0; i < usuarios.size(); i++) {
            if (usuarios.get(i).getCpf().equals(oldUsuario.getCpf())) {
                usuarios.set(i, newUsuario);
                saveFile(usuarios);
                return;
            }
        }
    }
}