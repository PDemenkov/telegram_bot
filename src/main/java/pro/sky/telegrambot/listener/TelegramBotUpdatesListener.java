package pro.sky.telegrambot.listener;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.Message;
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
    private final String REGEX = "([0-9\\.\\:\\s]{16})(\\s)([\\W+]+)";

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
                telegramBot.execute(new SendMessage(chatId, "Введите дату и задачу формата - 01.01.2022 20:00 Сделать домашнюю работу"));
            }
            String message = update.message().text();
            long messageId = update.message().messageId();
            long chatId = update.message().chat().id();

            Pattern pattern = Pattern.compile(REGEX);
            Matcher matcher = pattern.matcher(message);

            createNotification(matcher, messageId, chatId);
            notificationService.confirmMatch(matcher, chatId);
        });
        return UpdatesListener.CONFIRMED_UPDATES_ALL;
    }

    public void createNotification(Matcher matcher, long messageId, long chatId) {
        if (matcher.matches()) {
            String date = matcher.group(1); //date
            String text2 = matcher.group(3); // Text
            Notification notification = new Notification();
            notification.setLocalDateTime(LocalDateTime.parse(date, DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm")));
            notification.setMessageText(text2);
            notification.setChatID(messageId);
            notificationService.save(notification);
            notificationService.showEvent(notification, chatId);
        }
    }

    @Scheduled(cron = "0 * * * * *") //   fixedDelay = 10_000L
    public void run() {
        long chatId = 259705925L; // 561895876L  botId 5880146872L
        LocalDateTime currentTime = LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES);

        List<Notification> notList = notificationService.findAll();
        List<Notification> resultList = notList.stream()
                .filter(e -> e.getLocalDateTime().isEqual(currentTime))
                .collect(Collectors.toList());
        if (!resultList.isEmpty()) {
            telegramBot.execute(new SendMessage(chatId, resultList.toString()));
        }
    }
}
