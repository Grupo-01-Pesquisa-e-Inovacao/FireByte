package systeminfo;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
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

        StringBuilder discosInfo = new StringBuilder();

        // Informações sobre discos rígidos
        File[] roots = File.listRoots();
        for (File root : roots) {
            discosInfo.append("Unidade: ").append(root.getAbsolutePath()).append("\n");
            discosInfo.append("Espaço Total (bytes): ").append(root.getTotalSpace()).append("\n");
            discosInfo.append("Espaço Livre (bytes): ").append(root.getFreeSpace()).append("\n");
            discosInfo.append("\n");
        }

        // Conectar ao banco de dados MySQL
        String url = "jdbc:mysql://localhost:3306/jar_individual_kevin";
        String username = "root";
        String password = "Lobinho@10";

        try (Connection connection = DriverManager.getConnection(url, username, password)) {
            System.out.println("Conexão ao MySQL estabelecida!");

            // Inserir informações no banco de dados
            String sql = "INSERT INTO informacoes_sistema (sistema_operacional, arquitetura, versao, total_memoria_ram, livre_memoria_ram, max_memoria_ram, informacoes_discos) VALUES (?, ?, ?, ?, ?, ?, ?)";
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setString(1, osBean.getName());
                statement.setString(2, osBean.getArch());
                statement.setString(3, osBean.getVersion());
                statement.setLong(4, Runtime.getRuntime().totalMemory());
                statement.setLong(5, Runtime.getRuntime().freeMemory());
                statement.setLong(6, Runtime.getRuntime().maxMemory());
                statement.setString(7, discosInfo.toString());

                int rowsAffected = statement.executeUpdate();
                System.out.println(rowsAffected + " linhas afetadas.");
            } catch (SQLException e) {
                e.printStackTrace();
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
