package pro.sky.telegrambot.service;

import org.springframework.stereotype.Service;
import pro.sky.telegrambot.model.Notification;
import pro.sky.telegrambot.repo.NotificationRepo;

import java.util.List;
@Service
public class NotificationService {

    private NotificationRepo notificationRepo;

    public NotificationService(NotificationRepo notificationRepo) {
        this.notificationRepo = notificationRepo;
    }

    public void save(Notification notification) {
        notificationRepo.save(notification);
    }

    public List<Notification> findAll() {
        return notificationRepo.findAll();
    }
}
