public class User {
    private Integer idUsuario;
    private Integer fkEmpresa;
    private String nome;
    private String email;
    private String senha;
    private Integer isAdmin;

    public Integer getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(Integer idUsuario) {
        this.idUsuario = idUsuario;
    }

    public Integer getFkEmpresa() {
        return fkEmpresa;
    }

    public void setFkEmpresa(Integer fkEmpresa) {
        this.fkEmpresa = fkEmpresa;
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

    public Integer getADM() {
        return isAdmin;
    }

    public void setADM(Integer ADM) {
        isAdmin = ADM;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + idUsuario +
                ", idEmpresa=" + fkEmpresa +
                ", nome='" + nome + '\'' +
                ", email='" + email + '\'' +
                ", senha='" + senha + '\'' +
                ", isADM=" + isAdmin +
                '}';
    }
}
