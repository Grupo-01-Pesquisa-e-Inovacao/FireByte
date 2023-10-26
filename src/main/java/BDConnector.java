import org.apache.commons.dbcp2.BasicDataSource;
import org.springframework.jdbc.core.JdbcTemplate;

public class BDConnector {
    private JdbcTemplate bdConection;

    public BDConnector() {
        DotEnv dotEnv = new DotEnv();

        BasicDataSource dataSource = new BasicDataSource();

        //MYSQL -> com.mysql.cj.jdbc.Driver
        //SQL SERVER -> com.microsoft.sqlserver.jdbc.SQLServerDriver
        //H2 -> org.h2.Driver
        dataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");

        //MYSQL -> jdbc:mysql://localhost:3306/mydb
        //SQL SERVER -> jdbc:sqlserver://localhost:1433;database=mydb
        // H2- > jdbc:h2:file:./mydb
        dataSource.setUrl("jdbc:mysql://75.101.254.216:3306/firebyteDB");

        dataSource.setUsername("firebyte");
        dataSource.setPassword("1234");

        bdConection = new JdbcTemplate(dataSource);
    }

    public JdbcTemplate getBdConection(){
        return bdConection;
    }
}