import entities.ComponentesDispositivos;
import entities.Dispositivo;
import entities.User;

import java.time.LocalDateTime;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.logging.Level;
import java.util.logging.Logger;

public class App {
    private static final AtomicBoolean isPaused = new AtomicBoolean(false);

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
        Dispositivo dispositivo = bdInterface.getDispositivo(systemMonitor.getMACAddress(), user.getFkEmpresa());
        //PEGAR COMPONENTES DO DISPOSITIVO
        ComponentesDispositivos CPU = BDInterface.returnComponenteDispositivo("CPU", dispositivo.getEnderecoMAC());
        ComponentesDispositivos DISK = BDInterface.returnComponenteDispositivo("DISK", dispositivo.getEnderecoMAC());
        ComponentesDispositivos RAM = BDInterface.returnComponenteDispositivo("RAM", dispositivo.getEnderecoMAC());
        ComponentesDispositivos REDE = BDInterface.returnComponenteDispositivo("REDE", dispositivo.getEnderecoMAC());

        //VERIFICAR CONFIGURAÇÃO
        if(CPU == null && DISK == null && RAM == null && REDE == null || dispositivo.getTaxaAtualizacao() == null){
            System.out.println("Vimos que seu dispositivo ainda não está configurado,\n você pode configura-lo em nossa Dashboard!");
            System.exit(1);
        }

        // Iniciar thread para monitorar entrada do usuário
        new Thread(() -> {
            while (true) {
                System.out.println("Digite 'p' para pausar ou 'r' para retomar:");
                char input = scanner.next().charAt(0);
                if (input == 'p') {
                    isPaused.set(true);
                    System.out.println("Pausado.");
                } else if (input == 'r') {
                    isPaused.set(false);
                    System.out.println("Retomado.");
                }
            }
        }).start();

        //MONITORAMENTO
        while (true) {
            if (!isPaused.get()) {
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

                Thread.sleep(dispositivo.getTaxaAtualizacao());
            }
        }
    }

    static void logAndPrint(Integer fkcomponenteDispositivo, Double captura, LocalDateTime dataHora) {
        BDInterface bdInterface = new BDInterface();
        bdInterface.insertLog(fkcomponenteDispositivo, dataHora, captura);
        System.out.println(String.format("%s: Log de %s (%.0f%%) inserido com sucesso!",dataHora, fkcomponenteDispositivo, captura));
    }
}