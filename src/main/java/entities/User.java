package entities;

public class User {
    private Integer id;
    private Integer fkEmpresa;
    private Integer fkNivelAcesso;
    private String nome;
    private String email;
    private String senha;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getFkEmpresa() {
        return fkEmpresa;
    }

    public void setFkEmpresa(Integer fkEmpresa) {
        this.fkEmpresa = fkEmpresa;
    }

    public Integer getFkNivelAcesso() {
        return fkNivelAcesso;
    }

    public void setFkNivelAcesso(Integer fkNivelAcesso) {
        this.fkNivelAcesso = fkNivelAcesso;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    @Override
    public String toString() {
        return """
                    User:
                                        id= %d
                        ,             fkEmpresa= %d
                        ,             fkNivelAcesso= %d
                        ,             nome= %s
                        ,             email= %s
                        ,             senha= %s
                """.formatted(
                id
                , fkEmpresa
                , fkNivelAcesso
                , nome
                , email
                , senha
        );
    }
}
