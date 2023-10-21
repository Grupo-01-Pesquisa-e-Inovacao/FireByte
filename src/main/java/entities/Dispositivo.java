package entities;

public class Dispositivo {
    private Integer id;
    private String enderecoMAC;
    private Integer fkEmpresa;
    private String titulo;
    private String descricao;
    private Boolean ativo;
    private Integer taxaAtualizacao;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getEnderecoMAC() {
        return enderecoMAC;
    }

    public void setEnderecoMAC(String enderecoMAC) {
        this.enderecoMAC = enderecoMAC;
    }

    public Integer getFkEmpresa() {
        return fkEmpresa;
    }

    public void setFkEmpresa(Integer fkEmpresa) {
        this.fkEmpresa = fkEmpresa;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public Boolean getAtivo() {
        return ativo;
    }

    public void setAtivo(Boolean ativo) {
        this.ativo = ativo;
    }

    public Integer getTaxaAtualizacao() {
        return taxaAtualizacao;
    }

    public void setTaxaAtualizacao(Integer taxaAtualizacao) {
        this.taxaAtualizacao = taxaAtualizacao;
    }

    @Override
    public String toString() {
        return """
                    Dispositivo:
                                        id= %d
                        ,             enderecoMAC= %s
                        ,             fkEmpresa= %d
                        ,             titulo= %s
                        ,             descricao= %s
                        ,             ativo= %b
                        ,             taxaAtualizacao= %d
                """.formatted(
                id
                , enderecoMAC
                , fkEmpresa
                , titulo
                , descricao
                , ativo
                , taxaAtualizacao
        );
    }
}
