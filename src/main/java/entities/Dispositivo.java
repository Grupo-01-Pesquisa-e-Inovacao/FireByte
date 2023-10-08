package entities;

public class Dispositivo {
    private Integer id;
    private String enderecoMAC;
    private Integer fkEmpresa;
    private String titulo;
    private String descricao;
    private Integer hasCPU;
    private Integer hasDisk;
    private Integer hasRAM;
    private Integer hasNetwork;
    private Integer alertCPU;
    private Integer criticalCPU;
    private Integer alertDisk;
    private Integer criticalDisk;
    private Integer alertRAM;
    private Integer criticalRAM;
    private Integer alertNetwork;
    private Integer criticalNetwork;
    private Integer alertTemperature;
    private Integer criticalTemperature;
    private Integer delayInMs;

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

    public Integer getHasCPU() {
        return hasCPU;
    }

    public void setHasCPU(Integer hasCPU) {
        this.hasCPU = hasCPU;
    }

    public Integer getHasDisk() {
        return hasDisk;
    }

    public void setHasDisk(Integer hasDisk) {
        this.hasDisk = hasDisk;
    }

    public Integer getHasRAM() {
        return hasRAM;
    }

    public void setHasRAM(Integer hasRAM) {
        this.hasRAM = hasRAM;
    }

    public Integer getHasNetwork() {
        return hasNetwork;
    }

    public void setHasNetwork(Integer hasNetwork) {
        this.hasNetwork = hasNetwork;
    }

    public Integer getDelayInMs() {
        return delayInMs;
    }

    public void setDelayInMs(Integer delayInMs) {
        this.delayInMs = delayInMs;
    }

    @Override
    public String toString() {
        return "entities.ComponenteDeRede{" +
                "id=" + id +
                ", enderecoMAC='" + enderecoMAC + '\'' +
                ", Empresa=" + fkEmpresa +
                ", titulo='" + titulo + '\'' +
                ", descricao='" + descricao + '\'' +
                ", hasCPU=" + hasCPU +
                ", hasDisk=" + hasDisk +
                ", hasRAM=" + hasRAM +
                ", hasNetwork=" + hasNetwork +
                ", alertCPU=" + alertCPU +
                ", criticalCPU=" + criticalCPU +
                ", alertDisk=" + alertDisk +
                ", criticalDisk=" + criticalDisk +
                ", alertRAM=" + alertRAM +
                ", criticalRAM=" + criticalRAM +
                ", alertNetwork=" + alertNetwork +
                ", criticalNetwork=" + criticalNetwork +
                ", alertTemperature=" + alertTemperature +
                ", criticalTemperature=" + criticalTemperature +
                ", delayInMs=" + delayInMs +
                '}';
    }
}
