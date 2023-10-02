import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class App {
    public static void main(String[] args) throws InterruptedException {
        Scanner scanner = new Scanner(System.in);
        ValidarLogin validarLogin = new ValidarLogin();

        List<String> users = new ArrayList<>();

        System.out.println("===========================================");
        System.out.println("Bem vindo!!!");
        System.out.println("Este é o sistema de monitoramento FireByte!");
        System.out.println("===========================================");

        Boolean loginSucesso = false;

        while (!loginSucesso) {
            System.out.println("Digite seu Email:");
            String emailUsuario = scanner.nextLine();
            System.out.println("Digite sua Senha:");
            String senhaUsuario = scanner.nextLine();

            Boolean resultado = validar.emailSenha(emailUsuario, senhaUsuario);
            if (resultado) {
                loginSucesso = true;
                System.out.println("Login realizado com sucesso!");
            } else {
                System.out.println("Login não encontrado. Tente novamente.");
            }
        }

        // Pegar a empresa do usuário.
        // Procurar pelo idCPU/endMECRede em todas as tabelas da empresa do usuário.
        // Se houver uma tabela com o idCPU/endMECRede igual dessa máquina (e ela já estiver configurada), já insere os dados nessa tabela.
        // Se não houver uma tabela com o idCPU/endMECRede dessa máquina, cria uma nova tabela de dispositivo e pede ao usuário configurar na dashboard.

        //Config na dash:
        //Título
        //Descrição
        //Componentes a serem monitorados
        //Métrica de aviso individual de cada componente


        SystemMonitor systemMonitor = new SystemMonitor();
        DecimalFormat decimalFormat = new DecimalFormat("0.00"); //Refatorar para o que aprendemos em aula
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss"); //Refatorar para o que aprendemos em aula
        do {
            String dataHoraCaptura = dateFormat.format(new Date());

            // Verificar quais dados são monitorados para essa máquina no Banco

            // Double cpuUsage = systemMonitor.getCpuUsage();
            // Long ramUsage = systemMonitor.getRamUsage();
            // Long diskUsage = systemMonitor.getDiskUsage();
            // Double temperature = systemMonitor.getTemperature();


            // Criar a tabela Log se ela não existe ainda

            // criarTabela(componentesNecessários)
            // con.execute("""
            //     CREATE TABLE log (
            //     id INT PRIMARY KEY AUTO_INCREMENT,
            //     ramUsage DECIMAL(5, 2),
            //     diskUsage DECIMAL(5, 2),
            //     temperature DECIMAL(5, 2),
            //     logDateTime DATETIME
            //     )""");


            // Inserir na tabela log os dados que precisam ser monitoriados no Banco
            // con.update("INSERT INTO log ( ramUsage, diskUsage, temperature, logDateTime) VALUES ( ?, ?, ?, ?)",
            //         ramUsage, diskUsage, temperature, LocalDateTime.now());

            System.out.println("Data e Hora: " + dataHoraCaptura);
            System.out.println("CPU: " + decimalFormat.format(cpuUsage) + "%");
            System.out.println("RAM: " + decimalFormat.format(ramUsage) + "%");
            System.out.println("Disco: " + decimalFormat.format(diskUsage) + "%");
            System.out.println("Temperatura: " + decimalFormat.format(temperature) + "°C");
        }while (true)
    }
}