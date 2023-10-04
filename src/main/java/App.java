import java.util.Scanner;

public class App {
    public static void main(String[] args) throws InterruptedException {
        Scanner scanner = new Scanner(System.in);
        ValidarLogin validarLogin = new ValidarLogin();
        SystemMonitor systemMonitor = new SystemMonitor();

        System.out.println("===========================================");
        System.out.println("Bem vindo!!!");
        System.out.println("Este é o sistema de monitoramento FireByte!");
        System.out.println("===========================================");

        // LOGIN
        User user = null;
        Boolean loginSucesso = false;
        while (!loginSucesso) {
            System.out.println("Digite seu Email:");
            String emailUsuario = scanner.nextLine();
            System.out.println("Digite sua Senha:");
            String senhaUsuario = scanner.nextLine();

            user = validarLogin.getUser(emailUsuario, senhaUsuario);
            if (user != null) {
                loginSucesso = true;
                System.out.println("Login realizado com sucesso!");
            } else {
                System.out.println("Login não encontrado. Tente novamente.");
            }
        }
        System.out.println(user);

        //PEGAR MÁQUINA
        //Validar se o componente já está cadastrado
        ComponenteDeRede componente = validarLogin.getComponente(systemMonitor.getMACAddress(), user.getFkEmpresa());
        String textoNovoComponente = "Vimos que está em um novo componente,\n Você terá que configurar esse novo componente em nossa dashboard {link}";
        while (componente == null){
            confirmacaoUsuario(scanner, textoNovoComponente);
            componente = validarLogin.getComponente(systemMonitor.getMACAddress(), user.getFkEmpresa());
        }
        System.out.println(componente);

        //Validar se o componente já está configurado
        boolean componenteConfigurado = componente.hasCPU != null && componente.hasDisk != null && componente.hasRAM != null && componente.hasNetwork != null && componente.hasTemperature != null;
        String textoConfigurarComponente = "Parece que você não configurou seu componente ainda!\nVá para nossa dashboard e configure seu componente.";
        while (!componenteConfigurado){
            confirmacaoUsuario(scanner, textoConfigurarComponente);
            componente = validarLogin.getComponente(systemMonitor.getMACAddress(), user.getFkEmpresa());
            componenteConfigurado = componente.hasCPU != null && componente.hasDisk != null && componente.hasRAM != null && componente.hasNetwork != null && componente.hasTemperature != null;
        }
        System.out.println(componente);

        //MONITORAMENTO
//        DateTimeFormatter formatadorDeDataHora = DateTimeFormatter.ofPattern("dd/MMMM/yyyy hh:mm:ss:ms");
//        do {
//            String dataHoraCaptura = formatadorDeDataHora.format(LocalDateTime.now());
//
//            Double cpuUsage = null;
//            if (componente.hasCPU) {
//                cpuUsage = systemMonitor.getCpuUsage();
//            }
//            Long diskUsage = null;
//            if (componente.hasDisk) {
//                diskUsage = systemMonitor.getDiskUsage();
//            }
//            Long ramUsage = null;
//            if (componente.hasRAM) {
//                ramUsage = systemMonitor.getRamUsage();
//            }
//            if (componente.hasNetwork) {
//                //TODO
//            }
//            Double temperature = null;
//            if (componente.hasTemperature) {
//                temperature = systemMonitor.getTemperature();
//            }
//
//            // Inserir na tabela log os dados que precisam ser monitoriados no Banco
//            System.out.println("Data e Hora: " + dataHoraCaptura);
//            if (cpuUsage != null) {
//                System.out.println(String.format("CPU: %.2f%%", cpuUsage));
//            }
//            if (diskUsage != null) {
//                System.out.println(String.format("Disco: %d%%", diskUsage));
//            }
//            if (ramUsage != null) {
//                System.out.println(String.format("RAM: %d%%", ramUsage));
//            }
//            //TODO Network
//            if (temperature != null) {
//                System.out.println(String.format("Temperatura: %.2f%%", temperature));
//            }
//
//             con.update("INSERT INTO captura (ramUsage, diskUsage, temperature, logDateTime) VALUES ( ?, ?, ?, ?)",
//                     ramUsage, diskUsage, temperature, LocalDateTime.now()); //todo modularidade
//
//            Thread.sleep(componente.delayInMs);
//        } while (true);
    }

    static void confirmacaoUsuario(Scanner scanner, String texto){
        int escolha;
        do{
            System.out.println(texto);
            System.out.println("\nAssim que estiver tudo pronto, digite 1:");
            escolha = scanner.nextInt();
        }while (escolha != 1);
    }
}