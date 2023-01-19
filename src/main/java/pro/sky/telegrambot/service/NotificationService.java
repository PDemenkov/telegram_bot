package pro.sky.telegrambot.service;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.request.SendMessage;
import org.springframework.stereotype.Service;
import pro.sky.telegrambot.model.Notification;
import pro.sky.telegrambot.repo.NotificationRepo;

import java.util.List;
import java.util.regex.Matcher;

@Service
public class NotificationService {
    private TelegramBot telegramBot;
    private NotificationRepo notificationRepo;

    public NotificationService(TelegramBot telegramBot, NotificationRepo notificationRepo) {
        this.telegramBot = telegramBot;
        this.notificationRepo = notificationRepo;
    }

    public void save(Notification notification) {
        notificationRepo.save(notification);
    }

    public List<Notification> findAll() {
        return notificationRepo.findAll();
    }

    public Message showEvent(Notification notification, long chatId) {
        SendMessage sendMessage = new SendMessage(chatId, notification.toString());
        return telegramBot.execute(sendMessage).message();
    }
    public void confirmMatch(Matcher matcher, long chatId) {
        if (matcher.matches()) {
            SendMessage msg = new SendMessage(chatId, "Формат совпал,запись создана");
            telegramBot.execute(msg);
        } else {
            SendMessage msg = new SendMessage(chatId, "Формат не совпал, задайте событие заново");
            telegramBot.execute(msg);
        }
    }
}
