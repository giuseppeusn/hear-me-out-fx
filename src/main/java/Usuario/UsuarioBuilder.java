package Usuario;

public class UsuarioBuilder {
    private String nome;
    private String email;
    private String cpf;
    private String telefone;

    public UsuarioBuilder withNome(String nome) {
        this.nome = nome;
        return this;
    }

    public UsuarioBuilder withEmail(String email) {
        this.email = email;
        return this;
    }

    public UsuarioBuilder withCpf(String cpf) {
        this.cpf = cpf;
        return this;
    }

    public UsuarioBuilder withTelefone(String telefone) {
        this.telefone = telefone;
        return this;
    }

    public Usuario build() {
        return new Usuario(nome, email, cpf, telefone);
    }
}