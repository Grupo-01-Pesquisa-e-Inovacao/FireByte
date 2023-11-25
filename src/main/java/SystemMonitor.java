import com.github.britooo.looca.api.core.Looca;
import com.github.britooo.looca.api.group.discos.DiscoGrupo;
import com.github.britooo.looca.api.group.memoria.Memoria;
import com.github.britooo.looca.api.group.processador.Processador;
import com.github.britooo.looca.api.group.rede.Rede;
import com.github.britooo.looca.api.group.rede.RedeInterface;
import com.github.britooo.looca.api.group.sistema.Sistema;
import com.github.britooo.looca.api.group.temperatura.Temperatura;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

public class SystemMonitor {

    Looca looca = new Looca();
    Rede rede = looca.getRede();
    Processador processador = looca.getProcessador();
    Memoria memoria = looca.getMemoria();
    Temperatura temperatura = looca.getTemperatura();
    DiscoGrupo grupoDeDiscos = looca.getGrupoDeDiscos();
    Sistema sistema = looca.getSistema();

    public String getOperationSystem() {
        // Retorna o sistema operacional.
        return sistema.getSistemaOperacional();
    }

    public String manufacturer(){
        // Retorna o nome da fabricante do sistema, por exemplo, no caso do Windows (Microsoft).
        return sistema.getFabricante();
    }

    public Integer architecture(){
        // Retorna o número de bits do sistema operacional (se é 32 ou 64).
        return sistema.getArquitetura();
    }

    public String getMACAddress() {
        // Retorna o endereço MAC da placa de rede, que atua como um identificador único da máquina
        return rede.getGrupoDeInterfaces().getInterfaces().get(0).getEnderecoMac();
    }

    public double getCpuUsage() {
        // Retorna um valor entre 0 e 100% representando o uso da CPU
        return processador.getUso();
    }

    public Double getRamUsage() {
        // Retorna um valor entre 0 e 100% representando o uso da RAM
        return (double) ((memoria.getEmUso() * 100) / memoria.getTotal());
    }

    public Double getDiskUsage() {
        // Retorna um valor entre 0 e 100% representando o uso do disco
        return (double) ((grupoDeDiscos.getVolumes().get(0).getDisponivel() * 100) / grupoDeDiscos.getVolumes().get(0).getTotal());
    }

    public double getTemperature() {
        // Retorna um valor entre 0 e 100% representando a temperatura em graus Celsius
        return temperatura.getTemperatura();
    }

    public double getRedeUsage() {
        // Retorna um valor de 0 a 100% representando quantos % da velocidade de internet está usando (Max 100mbps)
        RedeInterface componenteRede = rede.getGrupoDeInterfaces().getInterfaces().get(0);

        long bytesEnviados = componenteRede.getBytesEnviados();

        if (bytesEnviados != 0) {
            return (137887744.0 / bytesEnviados) * 100;
        } else {
            return 0.0;
        }
    }

}