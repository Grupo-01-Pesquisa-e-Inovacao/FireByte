import com.github.britooo.looca.api.core.Looca;
import com.github.britooo.looca.api.group.discos.DiscoGrupo;
import com.github.britooo.looca.api.group.memoria.Memoria;
import com.github.britooo.looca.api.group.processador.Processador;
import com.github.britooo.looca.api.group.temperatura.Temperatura;

public class SystemMonitor {

    Looca looca = new Looca();
    Processador processador = looca.getProcessador();
    Memoria memoria = looca.getMemoria();
    Temperatura temperatura = looca.getTemperatura();
    DiscoGrupo grupoDeDiscos = looca.getGrupoDeDiscos();

    public double getCpuUsage() {
        // Retorna um valor entre 0 e 100% representando o uso da CPU
        return processador.getUso();
    }

    public Long getRamUsage() {
        // Retorna um valor entre 0 e 100% representando o uso da RAM
        return (memoria.getEmUso() * 100) / memoria.getTotal();
    }

    public Long getDiskUsage() {
        // Retorna um valor entre 0 e 100% representando o uso do disco
        return (grupoDeDiscos.getVolumes().get(0).getDisponivel() * 100) / grupoDeDiscos.getVolumes().get(0).getTotal();
    }

    public double getTemperature() {
        // Retorna um valor entre 0 e 100% representando a temperatura em graus Celsius
        return temperatura.getTemperatura();
    }
}