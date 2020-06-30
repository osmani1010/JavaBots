package es.osmani.core;

import es.osmani.weather.Model;
import es.osmani.weather.Weather;
import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.TelegramBotsApi;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Message;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.exceptions.TelegramApiException;
import org.telegram.telegrambots.exceptions.TelegramApiRequestException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/*

Applicacion creada por Osmani de la Maza

*/

public class Bot extends TelegramLongPollingBot {

    /*
    nombre - MiClimaBot
    descripción - Hola, te ayudare a consultar en clima en tu ciudad mas cercana
    about - He sido creado para hacer la vida del usuario un poco mas fácil
    commands:
    start - Esto es lo primero que debes hacer
    ajustes - Te cuento que puedo hacer
    ayuda - ¿Necesitas ayuda?
    */

    public static void main(String[] args) {
        ApiContextInitializer.init();
        TelegramBotsApi telegramBotsApi = new TelegramBotsApi();
        System.out.println("MiClimaBot ha arrancado");

        try {
            telegramBotsApi.registerBot(new Bot());

        } catch (TelegramApiRequestException e) {
            e.printStackTrace();
        }
    }


    public void sendMsg(Message msg, String text) {

        SendMessage sMsg = new SendMessage();
        sMsg.enableMarkdown(true);

        sMsg.setChatId(msg.getChatId().toString());

        sMsg.setReplyToMessageId(msg.getMessageId());

        sMsg.setText(text);
        try {

            setButtons(sMsg);
            sendMessage(sMsg);

        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }


    public void onUpdateReceived(Update upd) {

        System.out.print("Mensaje: ");
        System.out.println(upd.getMessage().getText());
        System.out.print(" el mensaje fue enviado por: ");
        System.out.println(upd.getMessage().getFrom().getFirstName());

        String fname = upd.getMessage().getFrom().getFirstName();
        String lname = upd.getMessage().getFrom().getLastName();
        String name = fname + " " + lname;

        String ans;

        Model model = new Model();

        Message msg = upd.getMessage();

        if (msg != null && msg.hasText()) {
            switch (msg.getText()) {
                case "/start":
                    sendMsg(msg, "Hola, estoy a tu servicio, escribe tu ciudad y buscaré el clima");
                    break;
                case "/ayuda":
                    ans = "El funcionamiento es muy fácil, " +
                            "debes escribir el nombre de la ciudad en donde quieras saber el tiempo";
                    System.out.println(ans);
                    sendMsg(msg, ans);
                    break;
                case "/quién_soy":
                    ans = "Tu nombre es " + name;
                    System.out.println(ans);
                    //message.setText(ans);
                    sendMsg(msg, ans);
                    break;
                case "/saluda":
                    ans = "Hola " + name + " espero que estés pasando un buen dia";
                    System.out.println(ans);
                    //message.setText(ans);
                    sendMsg(msg, ans);
                    break;
                default:
                    try {
                        sendMsg(msg, Weather.getWeather(msg.getText(), model));
                    } catch (IOException e) {
                        sendMsg(msg, "No he encontrado esa ciudad");
                    }

            }
        }

    }


    public void setButtons(SendMessage sMsg) {

        ReplyKeyboardMarkup repKbdMkp = new ReplyKeyboardMarkup();
        sMsg.setReplyMarkup(repKbdMkp);
        repKbdMkp.setSelective(true);
        repKbdMkp.setResizeKeyboard(true);
        repKbdMkp.setOneTimeKeyboard(false);

        List<KeyboardRow> kbdRowList = new ArrayList<>();

        KeyboardRow kbdFirstRow = new KeyboardRow();

        kbdFirstRow.add(new KeyboardButton("/quién_soy"));
        kbdFirstRow.add(new KeyboardButton("/saluda"));
        kbdFirstRow.add(new KeyboardButton("/ayuda"));

        kbdRowList.add(kbdFirstRow);
        repKbdMkp.setKeyboard(kbdRowList);

    }



    public String getBotUsername() {
        return Constants.getBotName();
    }

    public String getBotToken() {
        return Constants.getBotToken();
    }
}
