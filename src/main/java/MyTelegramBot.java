import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

public class MyTelegramBot extends TelegramLongPollingBot {
    private final String BOT_USERNAME;
    private final String BOT_TOKEN;
    private final String URL;

    public MyTelegramBot(String BOT_USERNAME, String BOT_TOKEN, String URL) throws TelegramApiException {
        this.BOT_USERNAME = BOT_USERNAME;
        this.BOT_TOKEN = BOT_TOKEN;
        this.URL = URL;

        TelegramBotsApi botsApi = new TelegramBotsApi(DefaultBotSession.class);
        botsApi.registerBot(this);
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            long chatId = update.getMessage().getChatId();
            String answer = update.getMessage().getText();
            String[] answerSeparated = answer.split(" ");

            switch (answerSeparated[0]) {
                case "/help":
                    sendMessage("""
                            Type /image to get picture of the day
                            Type /date yyyy-mm-dd to get picture of the day for the specified date
                            Type /help to see this message again""", chatId);
                    break;
                case "/start":
                    sendMessage("""
                            I am bot that can send you astronomy picture of the day from NASA!
                            Type /help to get all commands that i can process""", chatId);
                    break;
                case "/image":
                    String image = Utils.getPicUrl(URL);
                    sendMessage(image, chatId);
                    break;
                case "/date":
                    if (answerSeparated[1].matches("[0-9]{4}-[0-9]{2}-[0-9]{2}")) {
                        String dateImage = Utils.getPicUrl(URL + "&date=" + answerSeparated[1]);
                        sendMessage(dateImage, chatId);
                    } else {
                        sendMessage("You wrote the date in the wrong format! Please, change it and try again", chatId);
                    }
                    break;
                default:
                    sendMessage("Sorry, I don't understand you", chatId);
            }
        }
    }

    public void sendMessage(String text, long chatId) {
        SendMessage message = new SendMessage();
        message.setText(text);
        message.setChatId(chatId);
        try {
            execute(message);
        } catch (TelegramApiException e) {
            System.out.println("Unexpected error");
        }
    }

    @Override
    public String getBotUsername() {
        return BOT_USERNAME;
    }

    @Override
    public String getBotToken() {
        return BOT_TOKEN;
    }
}
