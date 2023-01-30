package pro.sky.telegrambot.listener;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.CallbackQuery;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;

import com.pengrad.telegrambot.model.request.*;
import com.pengrad.telegrambot.request.BaseRequest;
import com.pengrad.telegrambot.request.EditMessageText;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.response.BaseResponse;
import com.pengrad.telegrambot.response.SendResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import pro.sky.telegrambot.model.Notification;
import pro.sky.telegrambot.service.NotificationService;

import javax.annotation.PostConstruct;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
public class TelegramBotUpdatesListener implements UpdatesListener {
    private NotificationService notificationService;

    public TelegramBotUpdatesListener(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    private Logger logger = LoggerFactory.getLogger(TelegramBotUpdatesListener.class);

    @Autowired
    private TelegramBot telegramBot;

    @PostConstruct
    public void init() {
        telegramBot.setUpdatesListener(this);
    }

    @Override
    public int process(List<Update> updates) {
        updates.stream()
                .filter(update -> update.message() != null)
                .filter(update -> update.message().text()!=null)
                        .forEach(this::processUpdate);
        logger.info("Processing update: {}", updates);

        return UpdatesListener.CONFIRMED_UPDATES_ALL;
    }

    private void processUpdate(Update update) {
        String userMessage = update.message().text();
        Long chatId = update.message().chat().id();
        telegramBot.execute(new SendMessage(chatId, "Какое-то приветственное сообщение, выберите команду"));
        switch (userMessage) {
            case "/part0":
//                telegramBot.execute(new SendMessage(chatId, "Выберите из пунктов"));
//                InlineKeyboardMarkup markupInLine = new InlineKeyboardMarkup();
//                List<List<InlineKeyboardButton>> rowsInLine = new ArrayList<>();
//                List<InlineKeyboardButton> rowInLine =new ArrayList<>();
//                rowInLine.add(new InlineKeyboardButton("Узнать информацию  о приюте"));
//                rowsInLine.add(rowInLine);


//                KeyboardButton keyboardButton1 =new KeyboardButton("Узнать информацию о приюте");
//                KeyboardButton keyboardButton2 =new KeyboardButton("Как взять собаку из приюта");
//                KeyboardButton keyboardButton3 =new KeyboardButton("Прислать отчет о питомце");
//                KeyboardButton keyboardButton4 =new KeyboardButton("Позвать волонтера");
//                ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup(
//                keyboardButton1,keyboardButton2,keyboardButton3,keyboardButton4);
//                replyKeyboardMarkup.resizeKeyboard(true);
//                replyKeyboardMarkup.oneTimeKeyboard(false);
//                replyKeyboardMarkup.selective(true);
//                replyKeyboardMarkup.addRow(keyboardButton1,keyboardButton2,keyboardButton3,keyboardButton4);

//                SendResponse sendResponse = telegramBot.execute(new SendMessage(
//                        chatId,"Выберите вопрос"
//                ).parseMode(ParseMode.HTML)
//                        .disableWebPagePreview(false)
//                        .replyMarkup(new ReplyKeyboardMarkup(new KeyboardButton("Узнать информацию о приюте"),
//                                new KeyboardButton("Как взять собаку из приюта"),
//                                new KeyboardButton("Прислать отчет о питомце"),
//                                new KeyboardButton("Позвать волонтера"))
////                                Если пользователь уже обращался к боту ранее, то новое обращение начинается с выбора запроса, с которым пришел пользователь???
//                        .oneTimeKeyboard(true)
//                                .resizeKeyboard(true)
//                                .selective(true)));
//
//                InlineKeyboardMarkup inlineKeyboard = new InlineKeyboardMarkup(
//                        new InlineKeyboardButton("Как взять собаку из приюта").callbackData("Вот так"),
//                        new InlineKeyboardButton("Прислать отчет о питомце").callbackData("Вот так")
//                );

//                SendResponse sendResponse1 = telegramBot.execute(new SendMessage(
//                        chatId,"Выберите"
//                ).parseMode(ParseMode.HTML)
//                                .disableWebPagePreview(false)
//                                .replyMarkup(new InlineKeyboardMarkup(
//                                        new InlineKeyboardButton("Как взять собаку из приюта").callbackData("Вот так"),
//                                        new InlineKeyboardButton("Прислать отчет о питомце").url("http://www.google.com"))
//                ));
//break;

                InlineKeyboardButton button1 = new InlineKeyboardButton("Как взять собаку из приюта").callbackData("option1");
                InlineKeyboardButton button2 = new InlineKeyboardButton("Прислать отчет о питомце").callbackData("option2");
                InlineKeyboardMarkup inlineKeyboard = new InlineKeyboardMarkup(
                        new InlineKeyboardButton[]{button1},
                        new InlineKeyboardButton[]{button2});
                SendMessage sendMessage = new SendMessage(chatId, "Please select an option:").replyMarkup(inlineKeyboard);
                telegramBot.execute(sendMessage);

        }
        }

}


//    @Scheduled(cron = "0 * * * * *") //   fixedDelay = 10_000L
//    public void run() {
//        long chatId = 259705925L; // 561895876L  botId 5880146872L - botId(chat?)  259705925L user id
//        LocalDateTime currentTime = LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES);
//
//        List<Notification> notList = notificationService.findAll();
//        List<Notification> resultList = notList.stream()
//                .filter(e -> e.getLocalDateTime().isEqual(currentTime))
//                .collect(Collectors.toList());
//        if (!resultList.isEmpty()) {
//            telegramBot.execute(new SendMessage(chatId, resultList.toString()));
//        }
//    }
