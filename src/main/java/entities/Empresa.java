package entities;

public class Empresa {
    private String nomeFantasia;
    private String razaoSocial;
    private String CNPJ;
    private String telefone;
    private String email;

    public Empresa(String nomeFantasia, String razaoSocial, String CNPJ, String telefone, String email) {
        this.nomeFantasia = nomeFantasia;
        this.razaoSocial = razaoSocial;
        this.CNPJ = CNPJ;
        this.telefone = telefone;
        this.email = email;
    }

    public String getNomeFantasia() {
        return nomeFantasia;
    }

    public void setNomeFantasia(String nomeFantasia) {
        this.nomeFantasia = nomeFantasia;
    }

    public String getRazaoSocial() {
        return razaoSocial;
    }

    public void setRazaoSocial(String razaoSocial) {
        this.razaoSocial = razaoSocial;
    }

    public String getCNPJ() {
        return CNPJ;
    }

    public void setCNPJ(String CNPJ) {
        this.CNPJ = CNPJ;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
