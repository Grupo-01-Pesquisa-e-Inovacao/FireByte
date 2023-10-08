package entities;

import java.time.LocalDateTime;

public class Log {
    private Integer id;
    private LocalDateTime dataHora;
    private Integer uso;
    private String componenteMonitorado;
    private Integer fkDispositivo;
}
