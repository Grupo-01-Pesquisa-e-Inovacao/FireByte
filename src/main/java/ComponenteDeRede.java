public class ComponenteDeRede {
    private Integer idComponente;
    private String enderecoMAC;
    private Integer fkEmpresa;
    private String titulo;
    private String descricao;
    public Integer hasCPU;
    public Integer hasDisk;
    public Integer hasRAM;
    public Integer hasNetwork;
    public Integer hasTemperature;
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
    public Integer delayInMs;

    public Integer getIdComponente() {
        return idComponente;
    }

    public void setIdComponente(Integer idComponente) {
        this.idComponente = idComponente;
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

    public Integer getAlertCPU() {
        return alertCPU;
    }

    public void setAlertCPU(Integer alertCPU) {
        this.alertCPU = alertCPU;
    }

    public Integer getCriticalCPU() {
        return criticalCPU;
    }

    public void setCriticalCPU(Integer criticalCPU) {
        this.criticalCPU = criticalCPU;
    }

    public Integer getAlertDisk() {
        return alertDisk;
    }

    public void setAlertDisk(Integer alertDisk) {
        this.alertDisk = alertDisk;
    }

    public Integer getCriticalDisk() {
        return criticalDisk;
    }

    public void setCriticalDisk(Integer criticalDisk) {
        this.criticalDisk = criticalDisk;
    }

    public Integer getAlertRAM() {
        return alertRAM;
    }

    public void setAlertRAM(Integer alertRAM) {
        this.alertRAM = alertRAM;
    }

    public Integer getCriticalRAM() {
        return criticalRAM;
    }

    public void setCriticalRAM(Integer criticalRAM) {
        this.criticalRAM = criticalRAM;
    }

    public Integer getAlertNetwork() {
        return alertNetwork;
    }

    public void setAlertNetwork(Integer alertNetwork) {
        this.alertNetwork = alertNetwork;
    }

    public Integer getCriticalNetwork() {
        return criticalNetwork;
    }

    public void setCriticalNetwork(Integer criticalNetwork) {
        this.criticalNetwork = criticalNetwork;
    }

    public Integer getAlertTemperature() {
        return alertTemperature;
    }

    public void setAlertTemperature(Integer alertTemperature) {
        this.alertTemperature = alertTemperature;
    }

    public Integer getCriticalTemperature() {
        return criticalTemperature;
    }

    public void setCriticalTemperature(Integer criticalTemperature) {
        this.criticalTemperature = criticalTemperature;
    }

    @Override
    public String toString() {
        return "ComponenteDeRede{" +
                "idComponente=" + idComponente +
                ", enderecoMAC='" + enderecoMAC + '\'' +
                ", fkEmpresa=" + fkEmpresa +
                ", titulo='" + titulo + '\'' +
                ", descricao='" + descricao + '\'' +
                ", hasCPU=" + hasCPU +
                ", hasDisk=" + hasDisk +
                ", hasRAM=" + hasRAM +
                ", hasNetwork=" + hasNetwork +
                ", hasTemperature=" + hasTemperature +
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
