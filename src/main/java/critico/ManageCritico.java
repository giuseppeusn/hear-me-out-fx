package critico; // Esta classe também pertence ao pacote 'critico'.

import java.io.*; // Importa todas as classes do pacote java.io, usadas para entrada e saída de dados (arquivos).
import java.util.ArrayList; // Importa a classe ArrayList, usada para criar listas dinâmicas de objetos.

public class ManageCritico { // Declaração da classe pública 'ManageCritico'.
    private static final String FILE_PATH = "criticos.dat"; // Campo estático e final: define o nome do arquivo onde os dados dos críticos serão armazenados. 'static' significa que pertence à classe, não a uma instância. 'final' significa que não pode ser alterado.
    // Uma sugestão anterior era "data/criticos.dat" para salvar numa subpasta. Você está usando "criticos.dat" (salva na raiz do projeto).

    // Método estático para salvar uma lista de críticos em arquivo.
    public static void saveFile(ArrayList<Critico> criticos) { // O parâmetro foi renomeado de 'critico' para 'criticos' (ou listaCriticos) em sugestões anteriores para clareza, mas o seu usa 'critico'.
        // File dataDir = new File("data"); // Criação de subpasta 'data' (opcional, como nas sugestões).
        // if (!dataDir.exists()) {
        //     dataDir.mkdirs();
        // }
        // File file = new File(dataDir, FILE_PATH); // Se usasse a subpasta.
        File file = new File(FILE_PATH); // Cria um objeto File representando o arquivo no caminho especificado.

        try { // Inicia um bloco try-catch para tratar possíveis exceções de I/O (entrada/saída).
            if (!file.exists()) { // Verifica se o arquivo não existe.
                file.createNewFile(); // Se não existir, cria um novo arquivo vazio.
            }

            // Cria um ObjectOutputStream, que permite escrever objetos Java em um fluxo de saída (neste caso, um arquivo).
            // FileOutputStream abre um fluxo de escrita para o arquivo.
            // O try-with-resources (ex: try (ObjectOutputStream oos = ...)) fecharia o 'oos' automaticamente. Você está fechando manualmente.
            ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(file));
            oos.writeObject(criticos); // Escreve a lista inteira de objetos 'Critico' no arquivo. A lista e os objetos Critico devem ser Serializable.
            oos.close(); // Fecha o ObjectOutputStream, liberando recursos e garantindo que os dados sejam gravados.

        } catch (FileNotFoundException e) { // Captura a exceção se o arquivo não puder ser aberto/criado para escrita.
            System.out.println("Error (FileNotFoundException em saveFile): " + e.getMessage()); // Imprime uma mensagem de erro.
        } catch (IOException e) { // Captura outras exceções de I/O que podem ocorrer durante a escrita.
            // throw new RuntimeException(e); // Anteriormente, você lançava uma RuntimeException. Agora está imprimindo.
            System.err.println("Error (IOException em saveFile): " + e.getMessage());
            e.printStackTrace(); // Imprime o rastreamento da pilha da exceção para debugging.
        }
    }

    // Método estático para ler a lista de críticos do arquivo.
    public static ArrayList<Critico> readFile() {
        ArrayList<Critico> criticos = new ArrayList<>(); // Inicializa uma nova lista vazia para armazenar os críticos lidos.
        // File file = new File("data", FILE_PATH); // Se usasse a subpasta 'data'.
        File file = new File(FILE_PATH); // Cria um objeto File para o arquivo de dados.

        if (file.exists() && file.length() > 0) { // Verifica se o arquivo existe e se não está vazio.
            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) { // Usa try-with-resources para garantir que 'ois' seja fechado.
                // ObjectInputStream lê objetos Java de um fluxo de entrada.
                // Lê o objeto do arquivo. Espera-se que seja um ArrayList<Critico>.
                Object readObject = ois.readObject();
                if (readObject instanceof ArrayList) { // Verifica se o objeto lido é de fato um ArrayList.
                    // É necessário um cast (conversão de tipo). Como não podemos verificar o tipo genérico em runtime (devido ao type erasure do Java),
                    // um cast direto para ArrayList<Critico> geraria um "unchecked cast warning".
                    @SuppressWarnings("unchecked") // Suprime o warning do compilador para o cast não verificado.
                    ArrayList<?> tempList = (ArrayList<?>) readObject; // Faz o cast para ArrayList de tipo desconhecido.

                    // Itera pela lista lida e verifica se cada objeto é uma instância de Critico antes de adicionar à lista final.
                    // Isso torna a leitura mais segura contra ClassCastException se o arquivo estiver corrompido com objetos de tipo errado.
                    for (Object obj : tempList) {
                        if (obj instanceof Critico) {
                            criticos.add((Critico) obj);
                        } else {
                            System.err.println("Objeto inválido encontrado no arquivo (não é Critico): " + obj);
                        }
                    }
                } else {
                    System.err.println("Formato de objeto inesperado no arquivo (não é ArrayList).");
                }
                // ois.close(); // Não é necessário com try-with-resources.
            } catch (EOFException e) { // Captura exceção se o arquivo terminar inesperadamente durante a leitura (pode estar vazio ou corrompido).
                System.err.println("Arquivo encontrado vazio ou com final inesperado (EOFException): " + file.getAbsolutePath() + " - " + e.getMessage());
            } catch (IOException | ClassNotFoundException e) { // Captura exceções de I/O ou se a classe 'Critico' não for encontrada (ex: se foi renomeada ou o arquivo .class não está no classpath).
                System.err.println("Erro ao ler arquivo (IOException ou ClassNotFoundException): " + e.getMessage() + " (" + file.getAbsolutePath() + ")");
                e.printStackTrace();
            }
        } // Se o arquivo não existe ou está vazio, retorna a lista 'criticos' vazia.
        return criticos; // Retorna a lista de críticos (pode estar vazia).
    }

    // Método estático para adicionar um novo crítico à lista e salvar.
    public static void addCritic(Critico critico) {
        if (critico == null) { // Verificação de segurança.
            System.err.println("Tentativa de adicionar um crítico nulo.");
            return;
        }
        // Adicionada verificação de CPF existente (você a tinha no FormularioCritico, mas é bom ter aqui também como salvaguarda).
        if (cpfExists(critico.getCpf())) {
            System.err.println("ManageCritico: CPF " + critico.getCpf() + " já cadastrado. Crítico '" + critico.getNome() + "' não foi adicionado.");
            // Idealmente, a UI não deveria permitir chegar aqui se o CPF já existe.
            // Você pode querer que este método lance uma exceção ou retorne um booleano para a UI.
            return;
        }
        ArrayList<Critico> criticos = readFile(); // Lê a lista atual de críticos do arquivo.
        criticos.add(critico); // Adiciona o novo crítico à lista.
        saveFile(criticos); // Salva a lista atualizada de volta no arquivo.
        System.out.println("Crítico adicionado via ManageCritico: " + critico.getNome());
    }

    // Método estático para remover um crítico da lista pelo CPF.
    public static void removeCritic(String cpfARemover) {
        ArrayList<Critico> criticos = readFile(); // Lê a lista atual.
        Critico found = null; // Variável para armazenar o crítico encontrado.

        if (cpfARemover == null || cpfARemover.trim().isEmpty()) { // Verifica se o CPF fornecido é válido.
            System.out.println("CPF para remoção é nulo ou vazio.");
            return; // Sai do método se o CPF não for válido.
        }

        // Loop para encontrar o crítico com o CPF correspondente.
        for (Critico c : criticos) {
            if (c.getCpf() != null && c.getCpf().equals(cpfARemover)) { // Compara o CPF do crítico na lista com o CPF a ser removido.
                found = c; // Se encontrar, armazena o objeto e...
                break;     // ...sai do loop (assume que CPF é único).
            }
        } // Fim do loop for.

        // Bloco if-else para remover ou reportar que não foi encontrado. Este bloco está corretamente fora do loop.
        if (found != null) { // Se o crítico foi encontrado...
            criticos.remove(found); // Remove o crítico da lista.
            saveFile(criticos);     // Salva a lista modificada.
            System.out.println("Critico removido com sucesso com CPF: " + cpfARemover);
        } else { // Se o crítico não foi encontrado...
            System.out.println("Critico não encontrado para remoção com CPF: " + cpfARemover);
        }
    } // Fim do método removeCritic. (A chave extra que causava erro aqui foi removida).

    // Método estático para atualizar um crítico existente.
    public static void updateCritic(Critico criticoAntigo, Critico criticoNovo) {
        // Verificações para os parâmetros de entrada.
        if (criticoAntigo == null || criticoNovo == null || criticoAntigo.getCpf() == null) {
            System.err.println("Erro: criticoAntigo, criticoNovo ou CPF do criticoAntigo é nulo na tentativa de atualização.");
            return;
        }

        ArrayList<Critico> criticos = readFile(); // Lê a lista atual.
        boolean updated = false; // Flag para rastrear se a atualização ocorreu.
        for (int i = 0; i < criticos.size(); i++) { // Itera pela lista usando um índice.
            // A comparação abaixo usa o método Critico.equals() que você sobrescreveu (que compara pelo CPF).
            // criticoAntigo é o objeto que estava no formulário ANTES da edição (contém o CPF original).
            if (criticos.get(i).equals(criticoAntigo)) {
                criticos.set(i, criticoNovo); // Substitui o objeto antigo na lista pelo objeto com os novos dados.
                saveFile(criticos);           // Salva a lista atualizada.
                updated = true;               // Define a flag como true.
                System.out.println("Crítico atualizado com sucesso: " + criticoNovo.getCpf());
                return; // Sai do método, pois a atualização foi feita.
            }
        }

        // Se o loop terminar e 'updated' ainda for false, o crítico não foi encontrado.
        if (!updated) {
            System.out.println("Crítico não encontrado para atualização com CPF (do criticoAntigo): " + criticoAntigo.getCpf());
        }
    }

    // Método auxiliar para verificar se um CPF já existe na base de dados.
    public static boolean cpfExists(String cpf) {
        if (cpf == null || cpf.trim().isEmpty()) { // Validação básica do CPF.
            return false;
        }
        ArrayList<Critico> criticos = readFile(); // Lê todos os críticos.
        for (Critico c : criticos) { // Itera por eles.
            if (c.getCpf() != null && c.getCpf().equals(cpf)) { // Compara os CPFs.
                return true; // Se encontrar um CPF igual, retorna true.
            }
        }
        return false; // Se o loop terminar sem encontrar, retorna false.
    }
} // Fim da classe ManageCritico.