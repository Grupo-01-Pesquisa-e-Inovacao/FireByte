import com.github.britooo.looca.api.group.dispositivos.DispositivoUsb;

import java.util.ArrayList;
import java.util.List;

public class USB {
    public static void main(String[] args) {
        SystemMonitor systemMonitor = new SystemMonitor();

        System.out.println(systemMonitor.getQuantidadeDispositivosConectados().toString());

        //String url = "jdbc:mysql://localhost:3306/jar_individual_kevin";
       // String username = "root";
       // String password = "Lobinho@10";

    }
}
