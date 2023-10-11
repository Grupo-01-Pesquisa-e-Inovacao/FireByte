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

        //PEGAR MÁQUINA
        //Validar se o dispositivo já está cadastrado (se não está cria um novo)
        Dispositivo dispositivo = bdInterface.getComponente(systemMonitor.getMACAddress(), user.getFkEmpresa());
        //Validar se o componente está configurado
        while (!isComponentConfigured(dispositivo)) {
            confirmacaoUsuario(scanner, "Vimos que está em um novo dispositivo,\n Você terá que configurar esse novo dispositivo em nossa dashboard {link}");
            dispositivo = bdInterface.getComponente(systemMonitor.getMACAddress(), user.getFkEmpresa());
        }

        //MONITORAMENTO
        while (true) {
            LocalDateTime dataHoraCaptura = LocalDateTime.now();

            if (dispositivo.getHasCPU() == 1) {
                logAndPrint("CPU", systemMonitor.getCpuUsage(), dataHoraCaptura, dispositivo.getId());
            }

            if (dispositivo.getHasDisk() == 1) {
                logAndPrint("DISK", systemMonitor.getDiskUsage(), dataHoraCaptura, dispositivo.getId());
            }

            if (dispositivo.getHasRAM() == 1) {
                logAndPrint("RAM", systemMonitor.getRamUsage(), dataHoraCaptura, dispositivo.getId());
            }

            if (dispositivo.getHasNetwork() == 1) {
                logAndPrint("REDE", systemMonitor.getRedeUsage(), dataHoraCaptura, dispositivo.getId());
            }

            logAndPrint("TEMP", systemMonitor.getTemperature(), dataHoraCaptura, dispositivo.getId());

            Thread.sleep(dispositivo.getDelayInMs());
        }
    }

    static boolean isComponentConfigured(Dispositivo dispositivo) {
        return dispositivo.getHasCPU() != null &&
                dispositivo.getHasDisk() != null &&
                dispositivo.getHasRAM() != null &&
                dispositivo.getHasNetwork() != null;
    }

    static void confirmacaoUsuario(Scanner scanner, String texto) {
        int escolha;
        do {
            System.out.println(texto);
            System.out.println("\nAssim que estiver tudo pronto, digite 1:");
            escolha = scanner.nextInt();
        } while (escolha != 1);
    }

    static void logAndPrint(String componente, Double usage, LocalDateTime dataHora, Integer dispositivoId) {
        BDInterface bdInterface = new BDInterface();
        bdInterface.insertLog(dataHora, usage, componente, dispositivoId);
        System.out.println(String.format("%s: Log de %s (%.0f%%) inserido com sucesso!",dataHora, componente, usage));
    }
}