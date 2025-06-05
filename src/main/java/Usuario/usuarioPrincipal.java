package Usuario;

public class usuarioPrincipal {
    private String nome;
    private String email;
    private String cpf;


    public usuarioPrincipal(String nome, String email, String cpf) {
        this.nome = nome;
        this.email = email;
        this.cpf = cpf;
    }

    public String getNome() {
        return nome;
    }
    public void setNome(String nome) {this.nome = nome;}
    public String getEmail() {return email;}
    public void setEmail(String email) {this.email = email;}
}
