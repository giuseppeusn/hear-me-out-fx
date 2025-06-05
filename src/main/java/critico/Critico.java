package critico; // Define que esta classe pertence ao pacote 'critico'. Pacotes ajudam a organizar o código.

import java.io.Serial; // Importa a anotação @Serial, usada para controle de versão em classes serializáveis.
import java.io.Serializable; // Importa a interface Serializable. Classes que implementam esta interface podem ter seus objetos convertidos em uma sequência de bytes (para salvar em arquivo, por exemplo).
import java.util.Date; // Importa a classe Date, usada para representar datas e horas.
import java.util.Objects; // Importa a classe Objects, que contém métodos utilitários para operar com objetos, como 'equals' e 'hash'.

public class Critico implements Serializable { // Declaração da classe pública 'Critico'. Ela implementa 'Serializable', permitindo que seus objetos sejam salvos/lidos.
    @Serial // Anotação para o campo serialVersionUID. Ajuda a garantir que a desserialização funcione corretamente se a classe for modificada.
    private static final long serialVersionUID = 1L; // Identificador único para a versão serializada da classe. Se você mudar a estrutura da classe de forma incompatível, deve mudar este ID.

    // Declaração dos campos (atributos) da classe. São 'private', o que significa que só podem ser acessados diretamente de dentro desta classe.
    private String nome; // Armazena o nome do crítico.
    private String cpf; // Armazena o CPF do crítico.
    private String email; // Armazena o email do crítico.
    private Date data_nascimento; // Armazena a data de nascimento do crítico.
    private String biografia; // Armazena a biografia do crítico.
    private String site; // Armazena o site ou blog do crítico.
    private String genero; // Armazena o gênero do crítico.

    // Construtor da classe. É chamado quando um novo objeto 'Critico' é criado (ex: new Critico(...)).
    public Critico(String nome, String cpf, Date data_nascimento, String email, String biografia, String site, String genero) {
        this.nome = nome; // 'this.nome' se refere ao campo da classe, e 'nome' (sem this) ao parâmetro do construtor. Atribui o valor do parâmetro ao campo.
        this.cpf = cpf; // Atribui o CPF.
        this.data_nascimento = data_nascimento; // Atribui a data de nascimento.
        this.email = email; // Atribui o email.
        this.biografia = biografia; // Atribui a biografia.
        this.site = site; // Atribui o site.
        this.genero = genero; // Atribui o gênero.
    }

    // Métodos Getters e Setters: Permitem ler (get) e modificar (set) os valores dos campos privados de fora da classe.

    public String getEmail() { // Getter para o campo 'email'.
        return email; // Retorna o valor do email.
    }

    public void setEmail(String email) { // Setter para o campo 'email'.
        this.email = email; // Modifica o valor do email.
    }

    public String getNome() { // Getter para 'nome'.
        return nome;
    }

    public void setNome(String nome) { // Setter para 'nome'.
        this.nome = nome;
    }

    public String getCpf() { // Getter para 'cpf'.
        return cpf;
    }

    public void setCpf(String cpf) { // Setter para 'cpf'.
        this.cpf = cpf;
    }

    public Date getData_nascimento() { // Getter para 'data_nascimento'.
        return data_nascimento;
    }

    public void setData_nascimento(Date data_nascimento) { // Setter para 'data_nascimento'.
        this.data_nascimento = data_nascimento;
    }

    public String getBiografia() { // Getter para 'biografia'.
        return biografia;
    }

    public void setBiografia(String biografia) { // Setter para 'biografia'.
        this.biografia = biografia;
    }

    public String getSite() { // Getter para 'site'.
        return site;
    }

    public void setSite(String site) { // Setter para 'site'.
        this.site = site;
    }

    public String getGenero() { // Getter para 'genero'.
        return genero;
    }

    public void setGenero(String genero) { // Setter para 'genero'.
        this.genero = genero;
    }

    @Override // Anotação que indica que o método a seguir está sobrescrevendo um método da superclasse (neste caso, da classe Object).
    public String toString() { // Método que retorna uma representação textual do objeto. Útil para debugging e logs.
        return "Critico{" + // Concatena strings para formar a representação.
                "nome='" + nome + '\'' +
                ", cpf='" + cpf + '\'' +
                ", email='" + email + '\'' +
                ", data_nascimento=" + data_nascimento +
                ", biografia='" + biografia + '\'' +
                ", site='" + site + '\'' +
                ", genero='" + genero + '\'' +
                '}';
    }
    @Override // Sobrescrevendo o método 'equals' da classe Object.
    public boolean equals(Object o) { // Define como dois objetos 'Critico' são comparados em termos de igualdade.
        if (this == o) return true; // Se os dois objetos são a mesma instância na memória, são iguais.
        if (o == null || getClass() != o.getClass()) return false; // Se 'o' é nulo ou não é da mesma classe, não são iguais.
        Critico critico = (Critico) o; // Converte (cast) o objeto 'o' para o tipo 'Critico' para poder acessar seus campos.
        return Objects.equals(cpf, critico.cpf); // Compara os dois críticos APENAS pelo campo 'cpf'. Se os CPFs são iguais, os objetos são considerados iguais.
    }

    @Override // Sobrescrevendo o método 'hashCode' da classe Object.
    public int hashCode() { // Retorna um código hash para o objeto. É importante sobrescrever junto com 'equals'.
        return Objects.hash(cpf); // Gera um código hash baseado APENAS no campo 'cpf'. Objetos que são 'equals' devem ter o mesmo 'hashCode'.
    }
} // Fim da classe Critico.