import org.springframework.jdbc.core.JdbcTemplate;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

public class ValidarLogin {
    //DataBase
    BDConnector conexao = new BDConnector();
    JdbcTemplate con = conexao.getBdConection();

    Boolean emailSenha(String emailUsuario, String senhaUsuario) {
        // Lógica para validar o login no Banco
        return false;
    }
}
