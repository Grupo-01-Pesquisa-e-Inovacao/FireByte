import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;

public class ValidarLogin {
    //DataBase
    BDConnector conexao = new BDConnector();
    JdbcTemplate con = conexao.getBdConection();

    User getUser(String emailUsuario, String senhaUsuario) {
        try{
            //Retorna o usuário e a empresa dele
            User user = con.queryForObject(
                    "SELECT u.*, e.nomeFantasia AS empresaNome " +
                            "FROM usuario u " +
                            "INNER JOIN empresa e ON u.fkEmpresa = e.idEmpresa " +
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

    ComponenteDeRede getComponente(String endMAC, Integer idEmpresa) {
        try {
            //Retorna o componente com o MAC provido
            ComponenteDeRede componente = con.queryForObject("SELECT * FROM componenteDeRede WHERE enderecoMAC = ?",
                    new BeanPropertyRowMapper<>(ComponenteDeRede.class),
                    endMAC
            );
            return componente;
        } catch (EmptyResultDataAccessException e) {
            con.update("INSERT INTO componenteDeRede (enderecoMAC, fkEmpresa) VALUES (?, ?)", endMAC, idEmpresa);
            return null;
        }
    }
}
