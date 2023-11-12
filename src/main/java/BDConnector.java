import org.apache.commons.dbcp2.BasicDataSource;
import org.springframework.jdbc.core.JdbcTemplate;

public class BDConnector {
    private JdbcTemplate bdConection;

    public BDConnector(String databaseUrl,String databaseUser, String databasePassword) {
        DotEnv dotEnv = new DotEnv();

        BasicDataSource dataSource = new BasicDataSource();

        //MYSQL -> com.mysql.cj.jdbc.Driver
        //SQL SERVER -> com.microsoft.sqlserver.jdbc.SQLServerDriver
        //H2 -> org.h2.Driver
        dataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");

        //MYSQL -> jdbc:mysql://localhost:3306/mydb
        //SQL SERVER -> jdbc:sqlserver://localhost:1433;database=mydb
        // H2- > jdbc:h2:file:./mydb
        dataSource.setUrl(databaseUrl);

        dataSource.setUsername(databaseUser);
        dataSource.setPassword(databasePassword);

        bdConection = new JdbcTemplate(dataSource);
    }

    public JdbcTemplate getBdConection(){
        return bdConection;
    }
}