package Usuario;

import java.io.Serial;
import java.io.Serializable;


public class usuarioPrincipal implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private String nome;
    private String email;
    private String cpf;
    private String telefone;


    public usuarioPrincipal(String nome, String email, String cpf, String telefone) {
        this.nome = nome;
        this.email = email;
        this.cpf = cpf;
        this.telefone = telefone;
    }

    public String getNome() {
        return nome;
    }
    public void setNome(String nome) {this.nome = nome;}
    public String getEmail() {return email;}
    public void setEmail(String email) {this.email = email;}
    public String getCpf() {return cpf;}
    public void setCpf(String cpf) {this.cpf = cpf;}
    public String getTelefone() {return telefone;}
    public void setTelefone(String telefone) {this.telefone = telefone;}

    @Override
    public String toString() {
        return "Nome" + nome + ", Email=" + email + ", Cpf" + cpf  + ", Telefone";
    }
}

