import entities.Dispositivo;
import entities.ComponentesDispositivos;
import entities.User;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;

import java.time.LocalDateTime;

public class BDInterface {
    //DataBase
    static BDConnector conexao = new BDConnector();
    static JdbcTemplate con = conexao.getBdConection();

    User getUser(String emailUsuario, String senhaUsuario) {
        try{
            //Retorna o usuário e a empresa dele
            User user = con.queryForObject(
                    "SELECT * FROM Usuario WHERE email = ? AND senha = ?",
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

    Dispositivo getDispositivo(String endMAC, Integer idEmpresa) {
        try {
            //Retorna o dispositivo com o MAC provido
            Dispositivo dispositivo = con.queryForObject("SELECT * FROM dispositivo WHERE enderecoMAC = ?",
                    new BeanPropertyRowMapper<>(Dispositivo.class),
                    endMAC
            );
            return dispositivo;
        } catch (EmptyResultDataAccessException e) {
            con.update("INSERT INTO dispositivo (enderecoMAC, fkEmpresa) VALUES (?, ?)", endMAC, idEmpresa);
            return getDispositivo(endMAC, idEmpresa);
        }
    }

    static ComponentesDispositivos returnComponenteDispositivo(String nameComponente, String endMAC){
        try{
            ComponentesDispositivos componentesDispositivos = con.queryForObject(
                    "SELECT cd.id AS id, tc.id AS fkTipoCompoenente, d.id AS fkDispositivo FROM dispositivo d " +
                            "INNER JOIN componentesDispositivos cd ON d.id = cd.fkDispositivo " +
                            "INNER JOIN tipoComponente tc ON cd.fkTipoComponente = tc.id " +
                            "WHERE tc.nome = ? AND d.enderecoMAC = ?",
                    new BeanPropertyRowMapper<>(ComponentesDispositivos.class),
                    nameComponente, endMAC
            );
            return componentesDispositivos;
        }catch(EmptyResultDataAccessException e){
            return null;
        }
    }

    void insertLog(Integer fkcomponenteDispositivo, LocalDateTime dataHora, Double captura){
        con.update("INSERT INTO log (fkcomponenteDispositivo, dataHora, captura) VALUES (?, ?, ?)",
                fkcomponenteDispositivo, dataHora, captura);
    }
}
