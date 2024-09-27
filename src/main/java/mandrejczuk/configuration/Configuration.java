package mandrejczuk.configuration;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Properties;

public final class Configuration {

    private final Properties properties = new Properties();

    private Configuration() {
        try {
            Path filePath = Path.of(System.getProperty("user.dir"), "src", "main", "resources", "config.properties");
            properties.load(Files.newBufferedReader(filePath));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static final class ConfigurationHolder {
        private static final Configuration SINGLETON = new Configuration();
    }

    public static Configuration getInstance(){
        return ConfigurationHolder.SINGLETON;
    }

    public  void setVolume(int volume)
    {
        if(volume >=0 && volume <= 100) properties.setProperty("volume",String.valueOf(volume));
        else throw new  IllegalArgumentException();
    }

    public void setPrefix(String prefix)
    {
        if(prefix.length() == 1 &&prefix.matches("[/%.,;'!@#$^&*()-]")) properties.setProperty("command_prefix",prefix);
        else throw new  IllegalArgumentException();
    }

    public String getToken()
    {
        return properties.getProperty("token");
    }
    public int getVolume()
    {
        return Integer.parseInt(properties.getProperty("volume"));
    }
    public String getPrefix()
    {
        return properties.getProperty("command_prefix");
    }


}
