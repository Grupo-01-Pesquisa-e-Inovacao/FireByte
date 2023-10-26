import entities.ComponentesDispositivos;
import entities.Dispositivo;
import entities.User;
import org.simplejavamail.api.email.Email;
import org.simplejavamail.api.mailer.Mailer;
import org.simplejavamail.api.mailer.config.TransportStrategy;
import org.simplejavamail.email.EmailBuilder;
import org.simplejavamail.mailer.MailerBuilder;
import java.io.*;
import java.time.LocalDateTime;
import java.util.Scanner;
import java.util.concurrent.ThreadLocalRandom;
import java.util.logging.Level;
import java.util.logging.Logger;

public class App {
    public static void main(String[] args) throws InterruptedException, FileNotFoundException {
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
                    }else{
                        System.out.println("Código inválido!\nInsira o código novamente:");
                    }
                }
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
                PrintArchiveLog(REDE.getId(), systemMonitor.getRedeUsage(), dataHoraCaptura);
            }

            if (DISK != null) {
                logAndPrint(DISK.getId(), systemMonitor.getDiskUsage(), dataHoraCaptura);
                PrintArchiveLog(REDE.getId(), systemMonitor.getRedeUsage(), dataHoraCaptura);
            }

            if (RAM != null) {
                logAndPrint(RAM.getId(), systemMonitor.getRamUsage(), dataHoraCaptura);
                PrintArchiveLog(REDE.getId(), systemMonitor.getRedeUsage(), dataHoraCaptura);
            }

            /*if (REDE != null){
                logAndPrint(REDE.getId(), systemMonitor.getRedeUsage(), dataHoraCaptura);
                PrintArchiveLog(REDE.getId(), systemMonitor.getRedeUsage(), dataHoraCaptura);
            }*/

            Thread.sleep(dispositivo.getTaxaAtualizacao());
        }
    }

    //2FA
    static void sendAuthEmail(String clientEmail, int authCode){
        Email email = EmailBuilder.startingBlank()
                .from("Netminder", "danilo.pedrazzi@sptech.school")
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
                .withSMTPServer("SMTP.office365.com", 587, "danilo.pedrazzi@sptech.school", "#Gf54497114848")
                .withTransportStrategy(TransportStrategy.SMTP_TLS)
                .buildMailer();
        mailer.sendMail(email);
    }

    //LOCALLOGS
    static void PrintArchiveLog(Integer fkcomponenteDispositivo, Double captura, LocalDateTime dataHora) throws FileNotFoundException {
        // Nome do arquivo de saída
        String nomeArquivo = "log.txt";

        // Cria um objeto PrintStream para escrever no arquivo
        PrintStream gravador = new PrintStream(new FileOutputStream(nomeArquivo, true));

        // Redireciona a saída padrão (System.out) para o arquivo
        System.setOut(gravador);

        System.out.println(String.format("%s: Log de %s (%.0f%%) inserido com sucesso!",dataHora, fkcomponenteDispositivo, captura));

        // Mantém a saída padrão (System.out) no console
        System.setOut(new PrintStream(new FileOutputStream(FileDescriptor.out)));

        // Fecha o gravador (FIM GRAVAÇÃO)
        gravador.close();
    }

    static void logAndPrint(Integer fkcomponenteDispositivo, Double captura, LocalDateTime dataHora) {
        BDInterface bdInterface = new BDInterface();
        bdInterface.insertLog(fkcomponenteDispositivo, dataHora, captura);
        System.out.println(String.format("%s: Log de %s (%.0f%%) inserido com sucesso!",dataHora, fkcomponenteDispositivo, captura));
    }
}