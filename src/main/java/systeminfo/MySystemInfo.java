package systeminfo;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.lang.management.ManagementFactory;
import com.sun.management.OperatingSystemMXBean;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import oshi.SystemInfo;
import oshi.hardware.HardwareAbstractionLayer;
import oshi.hardware.PhysicalMemory;

public class MySystemInfo {
    public static void main(String[] args) {
        SystemInfo systemInfo = new SystemInfo();
        HardwareAbstractionLayer hardware = systemInfo.getHardware();

        OperatingSystemMXBean osBean = (OperatingSystemMXBean) ManagementFactory.getOperatingSystemMXBean();

        StringBuilder discosInfo = new StringBuilder();
        StringBuilder networkTrafficInfo = new StringBuilder();

        // Informações sobre discos rígidos
        File[] roots = File.listRoots();
        for (File root : roots) {
            discosInfo.append("Unidade: ").append(root.getAbsolutePath()).append("\n");
            discosInfo.append("Espaço Total (bytes): ").append(root.getTotalSpace()).append("\n");
            discosInfo.append("Espaço Livre (bytes): ").append(root.getFreeSpace()).append("\n\n");
        }

        // Informações sobre a RAM
        PhysicalMemory[] physicalMemory = hardware.getMemory().getPhysicalMemory().toArray(new PhysicalMemory[0]);
        int slotCount = physicalMemory.length;

        // Conectar ao banco de dados MySQL
        String url = "jdbc:mysql://localhost:3306/jar_individual_kevin";
        String username = "root";
        String password = "Lobinho@10";

        try (Connection connection = DriverManager.getConnection(url, username, password)) {
            System.out.println("Conexão ao MySQL estabelecida!");

            // Inserir informações no banco de dados
            String sql = "INSERT INTO informacoes_sistema (sistema_operacional, arquitetura, versao, total_memoria_ram, livre_memoria_ram, max_memoria_ram, informacoes_discos, data_captura, slots_memoria_ram) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setString(1, osBean.getName());
                statement.setString(2, osBean.getArch());
                statement.setString(3, osBean.getVersion());
                statement.setLong(4, Runtime.getRuntime().totalMemory());
                statement.setLong(5, Runtime.getRuntime().freeMemory());
                statement.setLong(6, Runtime.getRuntime().maxMemory());
                statement.setString(7, discosInfo.toString());
                statement.setTimestamp(8, new java.sql.Timestamp(new Date().getTime()));
                statement.setInt(9, slotCount);

                int rowsAffected = statement.executeUpdate();
                System.out.println(rowsAffected + " linhas afetadas.");
            } catch (SQLException e) {
                e.printStackTrace();
            }

            // Gerar o arquivo de log por dia
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
            String currentDay = dateFormat.format(new Date());
            String logFilePath = currentDay + "-log-de-discos-e-trafego.txt";
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(logFilePath, true))) {
                Date currentDate = new Date();

                writer.write("Data/Hora da Captura: " + currentDate.toString() + "\n\n");
                writer.write("Sistema Operacional: " + osBean.getName() + " ");
                writer.write("Arquitetura: " + osBean.getArch() + " ");
                writer.write("Versão: " + osBean.getVersion() + "\n\n");

                writer.write("Informações dos Discos Rígidos: \n");
                writer.write(discosInfo.toString());

                writer.write("\n\nNúmero de slots de memória RAM: " + slotCount);

                System.out.println("Log de sistema gerado em: " + logFilePath);

                // Registro de tráfego de rede
                String host = "moodle.sptech.school";
                int port = 80;

                try (Socket socket = new Socket(host, port)) {
                    InetAddress localAddress = socket.getLocalAddress();
                    InetAddress remoteAddress = socket.getInetAddress();

                    networkTrafficInfo.append("Conexão estabelecida com: ").append(host).append(":").append(port).append("\n");
                    networkTrafficInfo.append("Endereço local: ").append(localAddress.getHostAddress()).append("\n");
                    networkTrafficInfo.append("Endereço remoto: ").append(remoteAddress.getHostAddress()).append("\n");
                    networkTrafficInfo.append("Porta local: ").append(socket.getLocalPort()).append("\n");
                    networkTrafficInfo.append("Porta remota: ").append(socket.getPort()).append("\n");
                    networkTrafficInfo.append("\n");
                } catch (IOException e) {
                    e.printStackTrace();
                }

                writer.write("\n\nRegistro de Tráfego de Rede: \n");
                writer.write(networkTrafficInfo.toString());

                System.out.println("Log de tráfego de rede gerado no mesmo arquivo: " + logFilePath);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
