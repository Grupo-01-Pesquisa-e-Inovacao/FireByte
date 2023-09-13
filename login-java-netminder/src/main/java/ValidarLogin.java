import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

public class ValidarLogin {
    String emailSenha(String emailUsuario, String senhaUsuario, List<String> listaEmail, List<String> listaSenha) {
        for (int i = 0; i < listaEmail.size(); i++) {
            if (listaEmail.get(i).equals(emailUsuario) && listaSenha.get(i).equals(senhaUsuario)) {
                return "\n-Login feito com sucesso!";
            }
        }
        return "\n-Login não encontrado!";
    }

    boolean isADM(String emailUsuario, List<String> listaEmailADM) {
        return listaEmailADM.contains(emailUsuario);
    }

    void criarNovoUsuario(String novoEmail, String novaSenha, List<String> listaEmail, List<String> listaSenha, int opcao, List<String> listaEmailADM, List<String> listaSenhaADM) {
        if (opcao == 4) {
            listaEmailADM.add(novoEmail);
            listaSenhaADM.add(novaSenha);
        } else if (opcao == 1) {
            listaEmail.add(novoEmail);
            listaSenha.add(novaSenha);
        }
    }

    void exibirDadosSistema(Scanner scanner) throws InterruptedException {
        SystemMonitor systemMonitor = new SystemMonitor();
        DecimalFormat decimalFormat = new DecimalFormat("0.00");
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

        System.out.println("Dados do Sistema:");

        boolean continuarExibindo = true;

        while (continuarExibindo) {
            LoadingAnimation loadingAnimation = new LoadingAnimation();
            loadingAnimation.run(5000);

            String dataHoraCaptura = dateFormat.format(new Date());
            System.out.println("Data e Hora: " + dataHoraCaptura);

            Double cpuUsage = systemMonitor.getCpuUsage();
            Double ramUsage = systemMonitor.getRamUsage();
            Double diskUsage = systemMonitor.getDiskUsage();
            Double temperature = systemMonitor.getTemperature();
            Double logUse = systemMonitor.getLog();

            System.out.println("CPU: " + decimalFormat.format(cpuUsage) + "%");
            System.out.println("RAM: " + decimalFormat.format(ramUsage) + "%");
            System.out.println("Disco: " + decimalFormat.format(diskUsage) + "%");
            System.out.println("Temperatura: " + decimalFormat.format(temperature) + "°C");
            System.out.println("Logs: " + decimalFormat.format(logUse) + "%");

            if (cpuUsage > 80) {
                System.out.println("Alerta: Uso da CPU acima de 80%");
            }
            if (ramUsage > 80) {
                System.out.println("Alerta: Uso da RAM acima de 80%");
            }
            if (diskUsage > 80) {
                System.out.println("Alerta: Uso do Disco acima de 80%");
            }
            if (temperature > 80) {
                System.out.println("Alerta: Temperatura acima de 80°C");
            }
            if (logUse > 75){
                System.out.println("Alerta: Logs nos ponto de acesso acima de 75%");
            }

            if (cpuUsage > 80 || ramUsage > 80 || diskUsage > 80 || temperature > 80 || logUse > 75) {
                System.out.println("ALERTA GERAL: Algum dos componentes está com uso ou temperatura elevadas (80%/75% ou 80ºC)");
            } else {
                System.out.println("SISTEMA OPERANDO EM BOM ESTADO");
            }

            System.out.print("\033[H\033[2J");
            System.out.flush();

            System.out.println("Deseja continuar exibindo os dados do sistema? (1 para sim, 2 para sair)");
            Integer opcao = scanner.nextInt();
            if (opcao == 2) {
                continuarExibindo = false;
            }
        }
    }
}
