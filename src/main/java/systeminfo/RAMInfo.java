package systeminfo;

import oshi.SystemInfo;
import oshi.hardware.PhysicalMemory;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class RAMInfo {
    public static void main(String[] args) {
        SystemInfo systemInfo = new SystemInfo();
        PhysicalMemory[] physicalMemory = systemInfo.getHardware().getMemory().getPhysicalMemory().toArray(new PhysicalMemory[0]);

        int slotCount = physicalMemory.length;
        System.out.println("Número de slots de memória RAM: " + slotCount);

        // Gravar no banco de dados
        String url = "jdbc:mysql://localhost:3306/jar_individual_kevin";
        String username = "root";
        String password = "Lobinho@10";

        try (Connection connection = DriverManager.getConnection(url, username, password)) {
            System.out.println("Conexão ao MySQL estabelecida!");

            String sql = "INSERT INTO informacoes_sistema (slots_memoria_ram) VALUES (?)";

            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setInt(1, slotCount);

                int rowsAffected = statement.executeUpdate();
                System.out.println(rowsAffected + " linhas afetadas no banco de dados.");
            } catch (SQLException e) {
                e.printStackTrace();
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        // Atualizar o arquivo .txt
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
        String currentDay = dateFormat.format(new Date());
        String logFilePath = currentDay + "-log-de-discos-e-trafego.txt";

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(logFilePath, true))) {
            Date currentDate = new Date();
            writer.write("Data/Hora da Captura: " + currentDate.toString() + "\n\n");
            writer.write("Número de slots de memória RAM: " + slotCount);

            System.out.println("Registro do número de slots de memória RAM no arquivo " + logFilePath);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
