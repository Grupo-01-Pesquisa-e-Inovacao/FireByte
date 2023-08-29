import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class LoginJava {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        List<String> listaEmail = new ArrayList<>();
        List<String> listaSenha = new ArrayList<>();

        listaEmail.add("kevin@firebyte.com");
        listaEmail.add("pedro@firebyte.com");
        listaEmail.add("danilo@firebyte.com");

        listaSenha.add("firebyte123");
        listaSenha.add("firebyte456");
        listaSenha.add("firebyte789");

        List<String> listaEmailADM = new ArrayList<>();
        List<String> listaSenhaADM = new ArrayList<>();

        listaEmailADM.add("admin@firebyte.com");
        listaSenhaADM.add("admin123");

        LoadingAnimation loadingAnimation = new LoadingAnimation();
        ValidarLogin validar = new ValidarLogin();

        boolean sairPrograma = false;
        while (!sairPrograma) {
            System.out.println("===========================================");
            System.out.println("Bem vindo!!!");
            System.out.println("Este é o sistema de monitoramento FireByte!");
            System.out.println("===========================================");

            boolean loginSucesso = false;
            boolean loginADM = false;

            while (!loginSucesso && !loginADM) {
                System.out.println("Digite seu Email:");
                String emailUsuario = scanner.nextLine();
                System.out.println("Digite sua Senha:");
                String senhaUsuario = scanner.nextLine();

                try {
                    loadingAnimation.run();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                String resultado = validar.emailSenha(emailUsuario, senhaUsuario, listaEmail, listaSenha);
                if (resultado.equals("\n-Login feito com sucesso!")) {
                    loginSucesso = true;
                    System.out.println(resultado);
                } else if (resultado.equals("\n-Login não encontrado!")) {
                    for (int i = 0; i < listaEmailADM.size(); i++) {
                        if (listaEmailADM.get(i).equals(emailUsuario) && listaSenhaADM.get(i).equals(senhaUsuario)) {
                            loginADM = true;
                            break;
                        }
                    }
                    if (!loginADM) {
                        System.out.println("Login não encontrado. Tente novamente.");
                    }
                }
            }

            if (loginADM) {
                System.out.println("===========================================");
                System.out.println("Bem vindo, ADM!");
                System.out.println("Menu ADM:");
                System.out.println("1. Criar novo usuário");
                System.out.println("2. Sair");
                int opcao = scanner.nextInt();
                scanner.nextLine();

                if (opcao == 1) {
                    System.out.println("Digite o email do novo usuário:");
                    String novoEmail = scanner.nextLine();
                    System.out.println("Digite a senha do novo usuário:");
                    String novaSenha = scanner.nextLine();

                    listaEmail.add(novoEmail);
                    listaSenha.add(novaSenha);

                    System.out.println("Novo usuário criado com sucesso!");
                } else if (opcao == 2) {
                    sairPrograma = true;
                }
            }
            System.out.println("===========================================");
            System.out.println("Deseja fazer outra operação?");
            System.out.println("1. Sim");
            System.out.println("2. Sair");
            int opcao = scanner.nextInt();
            scanner.nextLine();

            if (opcao == 2) {
                sairPrograma = true;
            }
        }
    }
}