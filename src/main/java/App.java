import entities.ComponentesDispositivos;
import entities.Dispositivo;
import entities.User;

import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.logging.Level;
import java.util.logging.Logger;

public class App {
    private static final AtomicBoolean isPaused = new AtomicBoolean(false);

    private static final String LOG_FILE_PATH = "firebyte_log.txt";

    public static void main(String[] args) throws InterruptedException {
        Scanner scanner = new Scanner(System.in);
        BDInterface bdInterface = new BDInterface();
        SystemMonitor systemMonitor = new SystemMonitor();

        System.out.println("===========================================");
        System.out.println("Bem vindo!!!");
        System.out.println("Este é o sistema de monitoramento FireByte!");
        System.out.println("===========================================");

        Logger oshiLogger = Logger.getLogger("oshi.util.platform.windows.PerfCounterWildcardQuery");
        oshiLogger.setLevel(Level.SEVERE);

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
                logAction(systemMonitor, "Login", "Login realizado com sucesso!");
                System.out.println("Login realizado com sucesso!");
            } else {
                logAction(systemMonitor, "Login", "Tentativa de login falhou para o email: " + emailUsuario);
                System.out.println("Login não encontrado. Tente novamente.");
            }
        }

        Dispositivo dispositivo = bdInterface.getDispositivo(systemMonitor.getMACAddress(), user.getFkEmpresa());
        ComponentesDispositivos CPU = bdInterface.returnComponenteDispositivo("CPU", dispositivo.getEnderecoMAC());
        ComponentesDispositivos DISK = bdInterface.returnComponenteDispositivo("DISK", dispositivo.getEnderecoMAC());
        ComponentesDispositivos RAM = bdInterface.returnComponenteDispositivo("RAM", dispositivo.getEnderecoMAC());
        ComponentesDispositivos REDE = bdInterface.returnComponenteDispositivo("REDE", dispositivo.getEnderecoMAC());

        if (CPU == null && DISK == null && RAM == null && REDE == null || dispositivo.getTaxaAtualizacao() == null) {
            System.out.println("Vimos que seu dispositivo ainda não está configurado,\n você pode configura-lo em nossa Dashboard!");
            System.exit(1);
        }

        new Thread(() -> {
            while (true) {
                System.out.println("Digite 'p' para pausar ou 'r' para retomar:");
                char input = scanner.next().charAt(0);
                if (input == 'p') {
                    logAction(systemMonitor, "Pause", "Monitoramento pausado.");
                    isPaused.set(true);
                    System.out.println("Pausado.");
                } else if (input == 'r') {
                    logAction(systemMonitor, "Resume", "Monitoramento retomado.");
                    isPaused.set(false);
                    System.out.println("Retomado.");
                }
            }
        }).start();

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

                if (REDE != null) {
                    logAndPrint(REDE.getId(), systemMonitor.getRedeUsage(), dataHoraCaptura);
                }

                Thread.sleep(dispositivo.getTaxaAtualizacao());
            }
        }
    }

    static void logAction(SystemMonitor systemMonitor, String action, String message) {
        try (FileWriter writer = new FileWriter(LOG_FILE_PATH, true)) {
            String logEntry = String.format("%s - MAC: %s - Local: %s - Ação: %s - Mensagem: %s%n",
                    LocalDateTime.now(), systemMonitor.getMACAddress(), "LocalDoDispositivo", action, message);
            writer.write(logEntry);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    static void logAndPrint(Integer fkcomponenteDispositivo, Double captura, LocalDateTime dataHora) {
        BDInterface bdInterface = new BDInterface();
        bdInterface.insertLog(fkcomponenteDispositivo, dataHora, captura);
        System.out.println(String.format("%s: Log de %s (%.0f%%) inserido com sucesso!", dataHora, fkcomponenteDispositivo, captura));
    }
}
