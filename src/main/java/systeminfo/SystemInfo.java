package systeminfo;

import java.lang.management.ManagementFactory;
import com.sun.management.OperatingSystemMXBean;
import java.io.File;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.Socket;
import java.util.Enumeration;
import java.io.IOException;

public class SystemInfo {
    public static void main(String[] args) {
        OperatingSystemMXBean osBean = (OperatingSystemMXBean) ManagementFactory.getOperatingSystemMXBean();

        // Informações sobre o SO
        System.out.println("Informações do SO:");
        System.out.println("Sistema Operacional: " + osBean.getName());
        System.out.println("Arquitetura: " + osBean.getArch());
        System.out.println("Versão: " + osBean.getVersion());

        // Informações sobre a CPU
        System.out.println("\nInformações da CPU:");
        System.out.println("Nome da CPU: " + System.getenv("PROCESSOR_IDENTIFIER"));
        System.out.println("Arquitetura da CPU: " + System.getenv("PROCESSOR_ARCHITECTURE"));
        System.out.println("Número de Núcleos: " + Runtime.getRuntime().availableProcessors());

        // Informações sobre a memória RAM
        long totalMemory = Runtime.getRuntime().totalMemory();
        long freeMemory = Runtime.getRuntime().freeMemory();
        long maxMemory = Runtime.getRuntime().maxMemory();
        System.out.println("\nInformações de Memória RAM:");
        System.out.println("Memória RAM Total (bytes): " + totalMemory);
        System.out.println("Memória RAM Livre (bytes): " + freeMemory);
        System.out.println("Memória RAM Máxima (bytes): " + maxMemory);

        // Informações sobre discos rígidos
        File[] roots = File.listRoots();
        System.out.println("\nInformações dos Discos Rígidos:");
        System.out.println("Número de Discos Rígidos: " + roots.length);
        for (File root : roots) {
            System.out.println("Unidade: " + root.getAbsolutePath());
            System.out.println("Espaço Total (bytes): " + root.getTotalSpace());
            System.out.println("Espaço Livre (bytes): " + root.getFreeSpace());
        }

        // Informações sobre a rede
        try {
            Enumeration<NetworkInterface> networkInterfaces = NetworkInterface.getNetworkInterfaces();
            System.out.println("\nInformações de Rede:");
            while (networkInterfaces.hasMoreElements()) {
                NetworkInterface networkInterface = networkInterfaces.nextElement();
                System.out.println("Interface de Rede: " + networkInterface.getName());
                Enumeration<InetAddress> inetAddresses = networkInterface.getInetAddresses();
                while (inetAddresses.hasMoreElements()) {
                    InetAddress address = inetAddresses.nextElement();
                    System.out.println("Endereço IP: " + address.getHostAddress());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Verificação de Portas de Rede
        System.out.println("\nVerificação de Portas de Rede:");
        String host = "example.com"; // Substitua pelo host que deseja verificar
        int[] ports = {80, 443, 22, 3389}; // Substitua pelas portas que deseja verificar

        for (int port : ports) {
            try {
                Socket socket = new Socket(host, port);
                System.out.println("Porta " + port + " está aberta em " + host);
                socket.close();
            } catch (IOException e) {
                System.out.println("Porta " + port + " está fechada em " + host);
            }
        }
    }
}
