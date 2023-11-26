import org.apache.commons.dbcp2.BasicDataSource;
import org.springframework.jdbc.core.JdbcTemplate;

public class BDConnector {
    private JdbcTemplate bdConection;

    public BDConnector(String databaseUrl, Integer databasePort, String databaseUser, String databasePassword) {
        BasicDataSource dataSource = new BasicDataSource();

        if(databasePort == 3306){
            //MySQL
            dataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");
            dataSource.setUrl(String.format("jdbc:mysql://%s:%d/firebyteDB", databaseUrl, databasePort));
        }else if(databasePort == 1433){
            //SQL Server
            dataSource.setDriverClassName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            dataSource.setUrl(String.format("jdbc:sqlserver://%s:%d;database=firebyteDB;encrypt=true;trustServerCertificate=true", databaseUrl, databasePort));
        }else{
            //H2
            dataSource.setDriverClassName("org.h2.Driver");
            dataSource.setUrl(String.format("jdbc:h2:file:./firebyteDB", databaseUrl, databasePort));
        }

        dataSource.setUsername(databaseUser);
        dataSource.setPassword(databasePassword);

        bdConection = new JdbcTemplate(dataSource);
    }

    public JdbcTemplate getBdConection(){
        return bdConection;
    }
}