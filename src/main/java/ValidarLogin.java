import org.springframework.jdbc.core.JdbcTemplate;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

public class ValidarLogin {
    //DataBase
    BDConnector conexao = new BDConnector();
    JdbcTemplate con = conexao.getBdConection();

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

        con.execute("""
                CREATE TABLE IF NOT EXISTS usuario (
                    id INT PRIMARY KEY AUTO_INCREMENT,
                    email VARCHAR(255) NOT NULL,
                    senha VARCHAR(255) NOT NULL,
                    admin BOOLEAN NOT NULL
                )""");

        if (opcao == 4) {
            listaEmailADM.add(novoEmail);
            listaSenhaADM.add(novaSenha);
            con.update("INSERT INTO usuario (email, senha, admin) VALUES (?, ?, ?)",
                    novoEmail, novaSenha, true);
        } else if (opcao == 1) {
            listaEmail.add(novoEmail);
            listaSenha.add(novaSenha);
            con.update("INSERT INTO usuario (email, senha, admin) VALUES (?, ?, ?)",
                    novoEmail, novaSenha, false);
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
            String operationSystem = systemMonitor.getOperationSystem();
            String manufacture = systemMonitor.manufacturer();
            Integer architecture = systemMonitor.architecture();
            Long ramUsage = systemMonitor.getRamUsage();
            Long diskUsage = systemMonitor.getDiskUsage();
            Double temperature = systemMonitor.getTemperature();

            con.execute("""
                CREATE TABLE IF NOT EXISTS log (
                id INT PRIMARY KEY AUTO_INCREMENT,
                ramUsage DECIMAL(5, 2),
                diskUsage DECIMAL(5, 2),
                temperature DECIMAL(5, 2),
                logDateTime DATETIME
                )""");

            con.update("INSERT INTO log ( ramUsage, diskUsage, temperature, logDateTime) VALUES ( ?, ?, ?, ?)",
                    ramUsage, diskUsage, temperature, LocalDateTime.now());

            System.out.println(con.queryForList("SELECT * FROM log"));

            System.out.println("CPU: " + decimalFormat.format(cpuUsage) + "%");
            System.out.println("Sistema Operacional: " + operationSystem);
            System.out.println("Fabricante: " + manufacture);
            System.out.println("Aquitetura " + architecture + " Bits");
            System.out.println("RAM: " + decimalFormat.format(ramUsage) + "%");
            System.out.println("Disco: " + decimalFormat.format(diskUsage) + "%");
            System.out.println("Temperatura: " + decimalFormat.format(temperature) + "°C");

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


            if ( cpuUsage > 80 ||  ramUsage > 80 || diskUsage > 80 || temperature > 80) {
                System.out.println("ALERTA GERAL: Algum dos componentes está com uso ou temperatura elevadas (80%/75% ou 80ºC)");
            } else {
                System.out.println("SISTEMA OPERANDO EM BOM ESTADO");
            }

            System.out.print("\033[H\033[2J");
            System.out.flush();

            System.out.println("Deseja continuar exibindo os dados do sistema? (1 para sim, 2 para sair)");
            int opcao = scanner.nextInt();
            if (opcao == 2) {
                continuarExibindo = false;
            }
        }
    }
}
