package entities;

public class ComponentesDispositivos {
    Integer id;
    Integer fkTipoCompoenente;
    Integer fkDispositivo;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getFkTipoCompoenente() {
        return fkTipoCompoenente;
    }

    public void setFkTipoCompoenente(Integer fkTipoCompoenente) {
        this.fkTipoCompoenente = fkTipoCompoenente;
    }

    public Integer getFkDispositivo() {
        return fkDispositivo;
    }

    public void setFkDispositivo(Integer fkDispositivo) {
        this.fkDispositivo = fkDispositivo;
    }

    @Override
    public String toString() {
        return """
                    componentesDispositivos:
                                        id= %d
                        ,             fkTipoCompoenente= %d
                        ,             fkDispositivo= %d
                """.formatted(
                id
                , fkTipoCompoenente
                , fkDispositivo
        );
    }
}
