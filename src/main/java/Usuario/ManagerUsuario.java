package Usuario;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class ManagerUsuario {
    private static final String FILE_PATH = "usuarios.dat";

    public static void saveFile(ArrayList<usuarioPrincipal> usuarios) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILE_PATH))) {
            oos.writeObject(usuarios);
        } catch (IOException e) {
            System.out.println("Erro ao salvar o arquivo de usuários: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static ArrayList<usuarioPrincipal> readFile() {
        ArrayList<usuarioPrincipal> usuarios = new ArrayList<>();
        File file = new File(FILE_PATH);

        if (file.exists() && file.length() > 0) {
            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
                usuarios = (ArrayList<usuarioPrincipal>) ois.readObject();
            } catch (IOException | ClassNotFoundException e) {
                System.out.println("Erro ao ler o arquivo de usuários: " + e.getMessage());
                e.printStackTrace();
            }
        }
        return usuarios;
    }

    public static boolean cpfExists(String cpf) {
        ArrayList<usuarioPrincipal> usuarios = readFile();
        for (usuarioPrincipal usuario : usuarios) {
            if (usuario.getCpf().equals(cpf)) {
                return true;
            }
        }
        return false;
    }

    public static void addUsuario(usuarioPrincipal usuario) {
        ArrayList<usuarioPrincipal> usuarios = readFile();
        usuarios.add(usuario);
        saveFile(usuarios);
    }

    public static void removeUsuario(String email) {
        ArrayList<usuarioPrincipal> usuarios = readFile();
        boolean removed = usuarios.removeIf(u -> u.getEmail().equalsIgnoreCase(email));
        if (removed) {
            saveFile(usuarios);
            System.out.println("Conta removida com sucesso.");
        } else {
            System.out.println("Conta não encontrada para remoção.");
        }
    }

    public static boolean updateUsuario(usuarioPrincipal oldUsuario, usuarioPrincipal newUsuario) {
        ArrayList<usuarioPrincipal> usuarios = readFile();
        if (usuarios == null || usuarios.isEmpty()) {
            return false;
        }
        for (int i = 0; i < usuarios.size(); i++) {
            if (usuarios.get(i).getCpf().equals(oldUsuario.getCpf())) {
                usuarios.set(i, newUsuario);
                saveFile(usuarios);
                return true;
            }
        }
        return false;
    }
}