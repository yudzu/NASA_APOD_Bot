import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class Main {
    public static void main(String[] args) throws IOException, TelegramApiException {
        Properties prop = new Properties();
        FileInputStream fis = new FileInputStream("src/main/resources/config.properties");
        prop.load(fis);
        MyTelegramBot myTelegramBot = new MyTelegramBot("someapodbot", prop.getProperty("BOT_TOKEN"), "https://api.nasa.gov/planetary/apod?api_key=" + prop.getProperty("NASA_TOKEN"));
    }
}