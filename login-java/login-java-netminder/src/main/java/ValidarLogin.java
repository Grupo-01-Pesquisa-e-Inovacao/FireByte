import java.util.List;

public class ValidarLogin {
    String emailSenha(String emailUsuario, String senhaUsuario, List<String> listaEmail, List<String> listaSenha) {
        for (int i = 0; i < listaEmail.size(); i++) {
            if (listaEmail.get(i).equals(emailUsuario) && listaSenha.get(i).equals(senhaUsuario)) {
                return "\n-Login feito com sucesso!";
            }
        }
        return "\n-Login não encontrado!";
    }
}