import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class DotEnv {
    private final Map<String, String> envMap;

    public DotEnv() {
        envMap = new HashMap<>();
        load();
    }

    private void load() {
        try {
            String workingDir = System.getProperty("user.dir");
            String envFilePath = workingDir + File.separator + "/src/main/java/.env";
            BufferedReader reader = new BufferedReader(new FileReader(envFilePath));
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("=");
                if (parts.length == 2) {
                    envMap.put(parts[0], parts[1]);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String get(String key) {
        return envMap.get(key);
    }
}
