import entities.Dispositivo;
import entities.User;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;

import java.time.LocalDateTime;

public class BDInterface {
    //DataBase
    BDConnector conexao = new BDConnector();
    JdbcTemplate con = conexao.getBdConection();

    User getUser(String emailUsuario, String senhaUsuario) {
        try{
            //Retorna o usuário e a empresa dele
            User user = con.queryForObject(
                    "SELECT u.*, e.nomeFantasia AS empresaNome " +
                            "FROM usuario u " +
                            "INNER JOIN empresa e ON u.fkEmpresa = e.id " +
                            "WHERE u.email = ? AND u.senha = ?",
                    new BeanPropertyRowMapper<>(User.class),
                    emailUsuario,
                    senhaUsuario
            );
            return user;
        }catch(EmptyResultDataAccessException e){
            //Retorna null se não achar o usuário
            return null;
        }
    }

    Dispositivo getComponente(String endMAC, Integer idEmpresa) {
        try {
            //Retorna o componente com o MAC provido
            Dispositivo componente = con.queryForObject("SELECT * FROM dispositivo WHERE endMAC = ?",
                    new BeanPropertyRowMapper<>(Dispositivo.class),
                    endMAC
            );
            return componente;
        } catch (EmptyResultDataAccessException e) {
            con.update("INSERT INTO dispositivo (endMAC, fkEmpresa) VALUES (?, ?)", endMAC, idEmpresa);
            return getComponente(endMAC, idEmpresa);
        }
    }

    void insertLog(LocalDateTime dataHora, Double uso, String componente, Integer idDispositivo){
        con.update("INSERT INTO log (dataHora, uso, componenteMonitorado, fkdispositivo) VALUES (?, ?, ?, ?)",
                dataHora, uso, componente, idDispositivo);
    }
}
