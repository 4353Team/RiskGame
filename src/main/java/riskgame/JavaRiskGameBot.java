package riskgame;



//
//   Echo bot and Logging bot combined
//




import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class JavaRiskGameBot extends TelegramLongPollingBot {

    @Override
    public void onUpdateReceived(Update update) {

        // We check if the update has a message and the message has text
        if (update.hasMessage() && update.getMessage().hasText()) {
            // Set variables
            String message_text = update.getMessage().getText();
            long chat_id = update.getMessage().getChatId();

            SendMessage message = new SendMessage() // Create a message object object
                    .setChatId(chat_id)
                    .setText(message_text);
         /*   try {
                execute(message); // Sending our message object to user
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }*/
        }


        // We check if the update has a message and the message has text
        if (update.hasMessage() && update.getMessage().hasText()) {
            // Set variables
            String user_first_name = update.getMessage().getChat().getFirstName();
            String user_last_name = update.getMessage().getChat().getLastName();
            String user_username = update.getMessage().getChat().getUserName();
            long user_id = update.getMessage().getChat().getId();
            String message_text = update.getMessage().getText();
            long chat_id = update.getMessage().getChatId();
            String answer = message_text;
            SendMessage message = new SendMessage() // Create a message object object
                    .setChatId(chat_id)
                    .setText(answer);

            log(user_first_name, user_last_name, Long.toString(user_id), message_text, answer);
           /* try {
                execute(message);// Sending our message object to user (the same message they sent)
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
            */
        }

        //  System.out.println(update.getMessage().getText());
        // System.out.println(update.getMessage().getFrom().getFirstName());

        String command = update.getMessage().getText();

        SendMessage message = new SendMessage();

        if(command.equals("/myname")){
            System.out.print(update.getMessage().getFrom().getFirstName());
            message.setText(update.getMessage().getFrom().getFirstName());

        }

        if(command.equals("/mylastname")){
            System.out.print(update.getMessage().getFrom().getLastName());
            message.setText(update.getMessage().getFrom().getLastName());

        }

        if(command.equals("/myfullname")){
            System.out.print(update.getMessage().getFrom().getFirstName() + " " + update.getMessage().getFrom().getLastName());
            message.setText(update.getMessage().getFrom().getFirstName() + " " + update.getMessage().getFrom().getLastName());

        }

        if(command.equals("/creategame")){
            System.out.print("Enter the game ID for this game: ");
            message.setText("Enter the game ID for this game: ");

        }

        if(command.equals("/joingame")){
            System.out.print("Enter the game ID for the game you want to join: ");
            message.setText("Enter the game ID for the game you want to join: ");


        }

        if(command.equals("/hi")){
            message.setText("Howdy");
        }

        message.setChatId(update.getMessage().getChatId());

        try {
            execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }



    }

    @Override
    public String getBotUsername() {
        // Return bot username
        // If bot username is @MyAmazingBot, it must return 'MyAmazingBot'
        return "JavaRiskGameBot";
    }

    @Override
    public String getBotToken() {
        // Return bot token from BotFather
        return "770792838:AAFFi3rXaNmBraEdvGzvvC-FLl-cSINBd3o";
    }

    private void log(String first_name, String last_name, String user_id, String txt, String bot_answer) {
        System.out.println("\n ----------------------------");
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Date date = new Date();
        System.out.println(dateFormat.format(date));
        System.out.println("Message from " + first_name + " " + last_name + ". (id = " + user_id + ") \n Text - " + txt);
        System.out.println("Bot answer: \n Text - " + bot_answer);
    }
}