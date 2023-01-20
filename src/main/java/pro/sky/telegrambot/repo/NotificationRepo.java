package pro.sky.telegrambot.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import pro.sky.telegrambot.model.Notification;

import java.util.List;
@Repository
public interface NotificationRepo extends JpaRepository<Notification,Long> {

    Notification findAllById(long id);

}
