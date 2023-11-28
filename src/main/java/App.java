import entities.ComponentesDispositivos;
import entities.Dispositivo;
import entities.User;
import java.io.FileWriter;
import java.io.IOException;
import org.simplejavamail.api.email.Email;
import org.simplejavamail.api.mailer.Mailer;
import org.simplejavamail.api.mailer.config.TransportStrategy;
import org.simplejavamail.email.EmailBuilder;
import org.simplejavamail.mailer.MailerBuilder;
import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.ThreadLocalRandom;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.Timer;
import java.util.TimerTask;

public class App {
    private static final AtomicBoolean isPaused = new AtomicBoolean(false);

    private static final String USER_LOG_FILE = "user_actions_log";
    private static final String COMPONENT_LOG_FILE = "component_logs";
    private static final String LOG_FILE_EXTENSION = ".txt";
    public static void main(String[] args) throws InterruptedException {
        Scanner scanner = new Scanner(System.in);

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
            BDInterface productionDatabase = new BDInterface("44.209.179.217", 1433, "sa", "esqueci@senha");
            user = productionDatabase.getUser(emailUsuario, senhaUsuario);

            if (user != null) {
                System.out.println("Precisamos provar sua identidade.");
                System.out.println("Enviamos um código de autorização para seu email, coloque-o aqui:");
                //2FA
                int authCode = ThreadLocalRandom.current().nextInt(100000, 999999 + 1);
                sendAuthEmail(emailUsuario, authCode);
                while (!loginSucesso){
                    int clientAuthCode = scanner.nextInt();
                    if(clientAuthCode == authCode){
                        System.out.println("Login Realizado com Sucesso!");
                        loginSucesso = true;
                        logAction(systemMonitor, "Login", "Login realizado com sucesso!", user, LocalDateTime.now());
                    }else{
                        System.out.println("Código inválido!\nInsira o código novamente:");
                    }
                }
            } else {
                logAction(systemMonitor, "Login", "Tentativa de login falhou para o email: " + emailUsuario, LocalDateTime.now());
                System.out.println("Login não encontrado. Tente novamente.");
            }
        }

        //PEGAR DISPOSITIVO
        BDInterface productionDatabase = new BDInterface("44.209.179.217", 1433, "sa", "esqueci@senha");
        Dispositivo dispositivo = productionDatabase.getDispositivo(systemMonitor.getMACAddress(), user.getFkEmpresa()); //Validar se o dispositivo já está cadastrado (se não está cria um novo)
        //PEGAR COMPONENTES DO DISPOSITIVO
        ComponentesDispositivos CPU = BDInterface.returnComponenteDispositivo("CPU", dispositivo.getEnderecoMAC());
        ComponentesDispositivos DISK = BDInterface.returnComponenteDispositivo("DISCO", dispositivo.getEnderecoMAC());
        ComponentesDispositivos RAM = BDInterface.returnComponenteDispositivo("RAM", dispositivo.getEnderecoMAC());
        ComponentesDispositivos REDE = BDInterface.returnComponenteDispositivo("REDE", dispositivo.getEnderecoMAC());
        //VERIFICAR CONFIGURAÇÃO
        if(CPU == null && DISK == null && RAM == null && REDE == null || dispositivo.getTaxaAtualizacao() == null){
            System.out.println("Vimos que seu dispositivo ainda não está configurado,\n você pode configura-lo em nossa Dashboard!");
            System.exit(1);
        }

        User finalUser = user;
        new Thread(() -> {
            while (true) {
                System.out.println("Digite 'p' para pausar ou 'r' para retomar:");
                char input = scanner.next().charAt(0);
                if (input == 'p') {
                    logAction(systemMonitor, "Pause", "Monitoramento pausado.", finalUser, LocalDateTime.now());
                    isPaused.set(true);
                    System.out.println("Pausado.");
                } else if (input == 'r') {
                    logAction(systemMonitor, "Resume", "Monitoramento retomado.", finalUser, LocalDateTime.now());
                    isPaused.set(false);
                    System.out.println("Retomado.");
                }
            }
        }).start();

        //RESTART CHECK
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new RestartCheck(productionDatabase, dispositivo.getEnderecoMAC(), systemMonitor.getOperationSystem()), 0, 2000);

        //MONITORAMENTO
        while (true) {
            if (!isPaused.get()) {
                LocalDateTime dataHoraCaptura = LocalDateTime.now();
                if (CPU != null) {
                    InsertLogIntoDatabase(CPU.getId(), systemMonitor.getCpuUsage(), dataHoraCaptura);
                    LogComponente(CPU.getId(), systemMonitor.getCpuUsage(), dataHoraCaptura);
                    if (systemMonitor.getCpuUsage() > 80) { //TODO pegar o valor dos parâmetros
                        SlackIntegration.publishMessage("C065CP21H0T", String.format("Sua CPU está em %.2f%% de uso!", systemMonitor.getCpuUsage()));
                    }
                }

                if (DISK != null) {
                    InsertLogIntoDatabase(DISK.getId(), systemMonitor.getDiskUsage(), dataHoraCaptura);
                    LogComponente(DISK.getId(), systemMonitor.getDiskUsage(), dataHoraCaptura);
                    if (systemMonitor.getDiskUsage() > 80) { //TODO pegar o valor dos parâmetros
                        SlackIntegration.publishMessage("C065CP21H0T", String.format("Seu DISCO está em %.2f%% de uso!", systemMonitor.getDiskUsage()));
                    }
                }

                if (RAM != null) {
                    InsertLogIntoDatabase(RAM.getId(), systemMonitor.getRamUsage(), dataHoraCaptura);
                    LogComponente(RAM.getId(), systemMonitor.getRamUsage(), dataHoraCaptura);
                    if (systemMonitor.getRamUsage() > 80) { //TODO pegar o valor dos parâmetros
                        SlackIntegration.publishMessage("C065CP21H0T", String.format("Sua RAM está em %.2f%% de uso!", systemMonitor.getRamUsage()));
                    }
                }

                if (REDE != null) {
                    System.out.println(systemMonitor.getRedeUsage());
                    InsertLogIntoDatabase(REDE.getId(), systemMonitor.getRedeUsage(), dataHoraCaptura);
                    LogComponente(REDE.getId(), systemMonitor.getRedeUsage(), dataHoraCaptura);
                    if (systemMonitor.getRedeUsage() > 80) { //TODO pegar o valor dos parâmetros
                        SlackIntegration.publishMessage("C065CP21H0T", String.format("Sua REDE está em %.2f%% de uso!", systemMonitor.getRedeUsage()));
                    }
                }
                Thread.sleep(dispositivo.getTaxaAtualizacao());
            }
        }
    }

    static String getUserLogFileName() {
        return USER_LOG_FILE + "_" + getCurrentDateTime() + LOG_FILE_EXTENSION;
    }

    static String getComponentLogFileName() {
        return COMPONENT_LOG_FILE + "_" + getCurrentDateTime() + LOG_FILE_EXTENSION;
    }

    static String getCurrentDateTime() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
        return LocalDateTime.now().format(formatter);
    }

    static void logAction(SystemMonitor systemMonitor, String action, String message, LocalDateTime dataHora) {
        try (FileWriter writer = new FileWriter(getUserLogFileName(), true)) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd HH:mm:ss:ms");
            String dataHoraFormatada = dataHora.format(formatter);

            String logEntry = String.format("%s - MAC: %s - Ação: %s - Mensagem: %s%n",
                    dataHoraFormatada, systemMonitor.getMACAddress(), action, message);
            writer.write(logEntry);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    static void logAction(SystemMonitor systemMonitor, String action, String message, User user, LocalDateTime dataHora) {
        try (FileWriter writer = new FileWriter(getUserLogFileName(), true)) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd HH:mm:ss:ms");
            String dataHoraFormatada = dataHora.format(formatter);

            String logEntry = String.format("%s - MAC: %s - Usuário: %s - Ação: %s - Mensagem: %s%n",
                    dataHoraFormatada,systemMonitor.getMACAddress(), user.getEmail(), action, message);
            writer.write(logEntry);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    static void LogComponente(Integer fkcomponenteDispositivo, Double captura, LocalDateTime dataHora) {
        try (FileWriter writer = new FileWriter(getComponentLogFileName(), true)) {

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd HH:mm:ss:ms");
            String dataHoraFormatada = dataHora.format(formatter);

            String logEntry = String.format("%s Log de %s (%.0f%%) inserido com sucesso!\n",
                    dataHoraFormatada, fkcomponenteDispositivo, captura);

            System.out.print(logEntry);
            writer.write(logEntry);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    //2FA
    static void sendAuthEmail(String clientEmail, int authCode){
        Email email = EmailBuilder.startingBlank()
                .from("Netminder", "NetminderFirebyte@hotmail.com")
                .to("Client", clientEmail)
                .withSubject("Código de autenticação Firebyte")
                .withHTMLText("<html> " +
                        "<body style='font-family: Roboto, sans-serif;'> " +
                        "<div style='padding: 3px 3px; text-align: center; width: 35%;'>" +
                        "<img style='height: 50px;' src='https://github.com/NetMinder-Enterprise/FireByte-Frontend/blob/main/site/public/assets/fireByteLogo.png?raw=true'>" +
                        "</div>" +
                        "<h1 style='color: #2f3374'>Aqui está seu código de autorização</h1>" +
                        "<p style='color: #3d4298'>Utilizamos esse tipo de verificação para manter sua segurança!</p>" +
                        "<div style='background-color: #e8e0ff; border-radius: 10px; padding: 1px 1px; text-align: center; width: 35%;'>" +
                        "<h1 style='color: #6168d1'>"+ authCode +"</h1>" +
                        "</div>" +
                        "<p style='color: #3d4298'>Este código expira em 15 minutos.</p>" +
                        "</body>" +
                        "</html>")
                .buildEmail();
        Mailer mailer = MailerBuilder
                .withSMTPServer("SMTP.office365.com", 587, "NetminderFirebyte@hotmail.com", "Netminder123@")
                .withTransportStrategy(TransportStrategy.SMTP_TLS)
                .buildMailer();
        mailer.sendMail(email);
    }

    static void InsertLogIntoDatabase(Integer fkcomponenteDispositivo, Double captura, LocalDateTime dataHora) {
        BDInterface productionDatabase = new BDInterface("44.209.179.217", 1433, "sa", "esqueci@senha");
        productionDatabase.insertLog(fkcomponenteDispositivo, dataHora, captura);
        BDInterface localDatabase = new BDInterface("localhost", 3306, "firebyte", "1234");
        localDatabase.insertLog(1, dataHora, captura);
    }

    //Restart Feature
    static class RestartCheck extends TimerTask {
        BDInterface productionDatabase;
        String endMAC;
        String operationSystem;

        public RestartCheck(BDInterface productionDatabase, String endMAC, String operationSystem) {
            this.productionDatabase = productionDatabase;
            this.endMAC = endMAC;
            this.operationSystem = operationSystem;
        }

        @Override
        public void run() {
            if(!productionDatabase.DispositivoIsActive(endMAC)){
                productionDatabase.ActiveDispositivo(endMAC);
                if(operationSystem.toLowerCase().contains("windows")){
                    try {
                        Runtime.getRuntime().exec("Shutdown -r");
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }else{
                    try {
                        Runtime.getRuntime().exec("reboot");
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        }
    }
}