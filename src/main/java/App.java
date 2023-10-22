import entities.ComponentesDispositivos;
import entities.Dispositivo;
import entities.User;

import java.time.LocalDateTime;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

public class App {
    public static void main(String[] args) throws InterruptedException {
        Scanner scanner = new Scanner(System.in);
        BDInterface bdInterface = new BDInterface();
        SystemMonitor systemMonitor = new SystemMonitor();

        System.out.println("===========================================");
        System.out.println("Bem vindo!!!");
        System.out.println("Este é o sistema de monitoramento FireByte!");
        System.out.println("===========================================");

        //Desabilitar Warnings do Oshi
        Logger oshiLogger = Logger.getLogger("oshi.util.platform.windows.PerfCounterWildcardQuery");
        oshiLogger.setLevel(Level.SEVERE);

        // LOGIN
        User user = null;
        boolean loginSucesso = false;
        while (!loginSucesso) {
            System.out.println("Digite seu Email:");
            String emailUsuario = scanner.nextLine();
            System.out.println("Digite sua Senha:");
            String senhaUsuario = scanner.nextLine();

            user = bdInterface.getUser(emailUsuario, senhaUsuario);
            if (user != null) {
                loginSucesso = true;
                System.out.println("Login realizado com sucesso!");
            } else {
                System.out.println("Login não encontrado. Tente novamente.");
            }
        }

        //PEGAR DISPOSITIVO
        Dispositivo dispositivo = bdInterface.getDispositivo(systemMonitor.getMACAddress(), user.getFkEmpresa()); //Validar se o dispositivo já está cadastrado (se não está cria um novo)
        //PEGAR COMPONENTES DO DISPOSITIVO
        ComponentesDispositivos CPU = BDInterface.returnComponenteDispositivo("CPU", dispositivo.getEnderecoMAC());
        ComponentesDispositivos DISK = BDInterface.returnComponenteDispositivo("DISK", dispositivo.getEnderecoMAC());
        ComponentesDispositivos RAM = BDInterface.returnComponenteDispositivo("RAM", dispositivo.getEnderecoMAC());
        ComponentesDispositivos REDE = BDInterface.returnComponenteDispositivo("REDE", dispositivo.getEnderecoMAC());
        ComponentesDispositivos USBDISPONIVEIS = BDInterface.returnComponenteDispositivo("USBDISPONIVEIS", dispositivo.getEnderecoMAC());
        ComponentesDispositivos USBCONECTADOS = BDInterface.returnComponenteDispositivo("USBCONECTADOS", dispositivo.getEnderecoMAC());
        //VERIFICAR CONFIGURAÇÃO
        if(CPU == null && DISK == null && RAM == null && REDE == null || dispositivo.getTaxaAtualizacao() == null){
            System.out.println("Vimos que seu dispositivo ainda não está configurado,\n você pode configura-lo em nossa Dashboard!");
            System.exit(1);
        }

        //MONITORAMENTO
        while (true) {
            LocalDateTime dataHoraCaptura = LocalDateTime.now();

            if (CPU != null) {
                logAndPrint(CPU.getId(), systemMonitor.getCpuUsage(), dataHoraCaptura);
            }

            if (DISK != null) {
                logAndPrint(DISK.getId(), systemMonitor.getDiskUsage(), dataHoraCaptura);
            }

            if (RAM != null) {
                logAndPrint(RAM.getId(), systemMonitor.getRamUsage(), dataHoraCaptura);
            }

            if (REDE != null){
                logAndPrint(REDE.getId(), systemMonitor.getRedeUsage(), dataHoraCaptura);
            }
            if (USBCONECTADOS != null){
                logAndPrint(USBCONECTADOS.getId(), systemMonitor.grupoDeDispositivosUsb.size(), dataHoraCaptura);
            }
            if (USBDISPONIVEIS != null){
                logAndPrint(USBDISPONIVEIS.getId(), systemMonitor.quantidadeDispositivosUsbTotal - systemMonitor.grupoDeDispositivosUsb.size(), dataHoraCaptura);
            }

            Thread.sleep(dispositivo.getTaxaAtualizacao());
        }
    }

    static void logAndPrint(Integer fkcomponenteDispositivo, Double captura, LocalDateTime dataHora) {
        BDInterface bdInterface = new BDInterface();
        bdInterface.insertLog(fkcomponenteDispositivo, dataHora, captura);
        System.out.println(String.format("%s: Log de %s (%.0f%%) inserido com sucesso!",dataHora, fkcomponenteDispositivo, captura));
    }
    static void logAndPrint(Integer fkcomponenteDispositivo, Integer captura, LocalDateTime dataHora) {
        BDInterface bdInterface = new BDInterface();
        bdInterface.insertLog(fkcomponenteDispositivo, dataHora, captura);
        System.out.println(String.format("%s: Log de %s (%.0f%%) inserido com sucesso!",dataHora, fkcomponenteDispositivo, captura));
    }
}