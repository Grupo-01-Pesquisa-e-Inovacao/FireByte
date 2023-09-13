import java.util.Random;

public class SystemMonitor {
    private Random random = new Random();

    public double getCpuUsage() {
        // Simulação: Retornar um valor entre 0 e 100 representando o uso da CPU
        return random.nextDouble() * 100;
    }

    public double getRamUsage() {
        // Simulação: Retornar um valor entre 0 e 100 representando o uso da RAM
        return random.nextDouble() * 100;
    }

    public double getDiskUsage() {
        // Simulação: Retornar um valor entre 0 e 100 representando o uso do disco
        return random.nextDouble() * 100;
    }

    public double getTemperature() {
        // Simulação: Retornar um valor entre 0 e 100 representando a temperatura em graus Celsius
        return random.nextDouble() * 100;
    }

    public double getLog() {
        // Simulação: Retornar um valor entre 0 e 100 representando a temperatura em graus Celsius
        return random.nextDouble() * 100;
    }
}
