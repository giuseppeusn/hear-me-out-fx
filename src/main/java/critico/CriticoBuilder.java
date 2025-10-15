package critico;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

public class CriticoBuilder {
    private String nome;
    private String cpf;
    private Date data_nascimento;
    private String email;
    private String biografia;
    private String site;
    private String genero;

    public CriticoBuilder withNome(String nome) {
        this.nome = nome;
        return this;
    }

    public CriticoBuilder withCpf(String cpf) {
        this.cpf = cpf;
        return this;
    }

    public CriticoBuilder withEmail(String email) {
        this.email = email;
        return this;
    }

    public CriticoBuilder withDataNascimento(LocalDate localDate) {
        if (localDate != null) {
            this.data_nascimento = Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
        }
        return this;
    }

    public CriticoBuilder withBiografia(String biografia) {
        this.biografia = biografia;
        return this;
    }

    public CriticoBuilder withSite(String site) {
        this.site = site;
        return this;
    }

    public CriticoBuilder withGenero(String genero) {
        this.genero = genero;
        return this;
    }

    public Critico build() {
        return new Critico(nome, cpf, data_nascimento, email, biografia, site, genero);
    }
}