import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class LoginJava {
    public static void main(String[] args) throws InterruptedException {
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

            Boolean loginSucesso = false;
            Boolean loginADM = false;
            Boolean isUser = false;

            while (!loginSucesso && !loginADM) {
                System.out.println("Digite seu Email:");
                String emailUsuario = scanner.nextLine();
                System.out.println("Digite sua Senha:");
                String senhaUsuario = scanner.nextLine();

                try {
                    loadingAnimation.run(1000); // 1000 milissegundos (1 segundos)
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt(); // Re-set
                    e.printStackTrace();
                }

                String resultado = validar.emailSenha(emailUsuario, senhaUsuario, listaEmail, listaSenha);
                if (resultado.equals("\n-Login feito com sucesso!")) {
                    loginSucesso = true;
                    isUser = !validar.isADM(emailUsuario, listaEmailADM);
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
                System.out.println("2. Exibir Dados do Sistema");
                System.out.println("3. Sair");
                System.out.println("4. Criar novo usuário ADM");
                Integer opcao = scanner.nextInt();
                scanner.nextLine();

                if (opcao == 1 || opcao == 4) {
                    System.out.println("Digite o email do novo usuário:");
                    String novoEmail = scanner.nextLine();
                    System.out.println("Digite a senha do novo usuário:");
                    String novaSenha = scanner.nextLine();

                    validar.criarNovoUsuario(novoEmail, novaSenha, listaEmail, listaSenha, opcao, listaEmailADM, listaSenhaADM);

                    System.out.println("Novo usuário criado com sucesso!");
                } else if (opcao == 2) {
                    validar.exibirDadosSistema(scanner);
                } else if (opcao == 3) {
                    sairPrograma = true;
                }
            } else if (isUser) {
                System.out.println("===========================================");
                System.out.println("Bem vindo!");
                System.out.println("Menu Usuário:");
                System.out.println("1. Exibir Dados do Sistema");
                System.out.println("2. Sair");
                Integer opcao = scanner.nextInt();
                scanner.nextLine();

                if (opcao == 1) {
                    validar.exibirDadosSistema(scanner);
                } else if (opcao == 2) {
                    sairPrograma = true;
                }
            }
        }
    }
}