package mandrejczuk.configuration;

import mandrejczuk.Main;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Properties;

public final class Configuration {

    private final Properties tokenProperties = new Properties();

    private final Properties radiosProperties = new Properties();


    private Configuration() {
        try (var is = Configuration.class.getResourceAsStream("/config.properties"))
        {
            tokenProperties.load(is);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        try (var is = Configuration.class.getResourceAsStream("/audio/radios/radios.properties"))
        {
            radiosProperties.load(is);
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
    public String getToken()
    {
        return tokenProperties.getProperty("token");
    }

    public void save()
    {
        String targetConfigPath = "target/classes/audio/radios/radios.properties";
        try(var output = new FileOutputStream(targetConfigPath)) {

            radiosProperties.store(output,"aktualizacja");
       }  catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public Properties getRadiosProperties() {
        return radiosProperties;
    }
}
