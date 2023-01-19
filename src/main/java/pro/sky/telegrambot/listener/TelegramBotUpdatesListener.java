package pro.sky.telegrambot.listener;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
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

        updates.forEach(update -> {
            logger.info("Processing update: {}", update);

            if (update.message() != null && update.message().text().equals("/start")) {
                long chatId = update.message().chat().id();
                SendResponse response = telegramBot.execute(new SendMessage(chatId, "Введите дату и задачу формата - 01.01.2022 20:00 Сделать домашнюю работу"));
            }

            String message = update.message().text();
            long messageId = update.message().messageId();
            long chatId = update.message().chat().id();

            Pattern pattern = Pattern.compile("([0-9\\.\\:\\s]{16})(\\s)([\\W+]+)");
            Matcher matcher = pattern.matcher(message);
            if (matcher.matches()) {
                String date = matcher.group(1);
                SendMessage msg1 = new SendMessage(chatId, date); //date time
                telegramBot.execute(msg1);
                String text2 = matcher.group(3); // Text
                SendMessage msg3 = new SendMessage(chatId, text2);
                telegramBot.execute(msg3);
                Notification notification = new Notification();
                notification.setLocalDateTime(LocalDateTime.parse(date, DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm")));
                notification.setMessageText(text2);
                notification.setChatID(messageId);
                notificationService.save(notification);

                SendMessage sendMessage = new SendMessage(chatId, notification.toString());
                telegramBot.execute(sendMessage);

            }
            if (matcher.matches()) {
                SendMessage msg = new SendMessage(chatId, "Формат совпал,запись создана");
                telegramBot.execute(msg);
            } else {
                SendMessage msg = new SendMessage(chatId, "Формат не совпал, задайте событие заново");
                telegramBot.execute(msg);
            }


        });
        return UpdatesListener.CONFIRMED_UPDATES_ALL;
    }

    @Scheduled(cron = "0 * * * * *" ) //   fixedDelay = 10_000L    "0/10 * * * * *"
    public void run() {
        long chatId = 259705925L; // 561895876L  botId 5880146872L
        LocalDateTime currentTime = LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES);

        List<Notification>   notList = notificationService.findAll();
        List<Notification> resultList = notList.stream()
                .filter(e -> e.getLocalDateTime().isEqual(currentTime))
                .collect(Collectors.toList());

        SendResponse listMessage = telegramBot.execute(new SendMessage(chatId, resultList.toString()));

    }
}
