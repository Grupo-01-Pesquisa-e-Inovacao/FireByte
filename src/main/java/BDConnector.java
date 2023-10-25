import org.apache.commons.dbcp2.BasicDataSource;
import org.springframework.jdbc.core.JdbcTemplate;

public class BDConnector {
    private JdbcTemplate bdConection;

    public BDConnector() {
        BasicDataSource dataSource = new BasicDataSource();

        //MYSQL -> com.mysql.cj.jdbc.Driver
        //SQL SERVER -> com.microsoft.sqlserver.jdbc.SQLServerDriver
        //H2 -> org.h2.Driver
        dataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");

        //MYSQL -> jdbc:mysql://localhost:3306/mydb
        //SQL SERVER -> jdbc:sqlserver://localhost:1433;database=mydb
        // H2- > jdbc:h2:file:./mydb
        dataSource.setUrl("jdbc:mysql://localhost:3306/firebyteDB");

        dataSource.setUsername("root");
        dataSource.setPassword("#Gf47060685847"); //Colocar em um .env

        bdConection = new JdbcTemplate(dataSource);
    }

    public JdbcTemplate getBdConection(){
        return bdConection;
    }
}

/*import org.apache.commons.dbcp2.BasicDataSource;
import org.springframework.jdbc.core.JdbcTemplate;

public class BDConnector {
    private JdbcTemplate bdConection;

    public BDConnector() {
        BasicDataSource dataSource = new BasicDataSource();

        // Configurando o banco de dados H2
        dataSource.setDriverClassName("org.h2.Driver");
        dataSource.setUrl("jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=FALSE");
        dataSource.setUsername("sa");
        dataSource.setPassword("");

        bdConection = new JdbcTemplate(dataSource);
    }

    public JdbcTemplate getBdConection(){
        return bdConection;
    }
}*/
