package pro.sky.telegrambot.model;

import com.sun.istack.NotNull;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;
@Entity
@Table(name = "Notification_table")
public class Notification {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id",nullable = false)
    private Long id;

    @Column(name = "time")
    @NotNull
    private LocalDateTime localDateTime;

    @Column(name = "messageText")
    private String messageText;

    @Column(name = "chatID")
    private Long chatID;

    public Notification() {
    }

    public Notification(Long id, LocalDateTime localDateTime, String messageText, Long chatID) {
        this.id = id;
        this.localDateTime = localDateTime;
        this.messageText = messageText;
        this.chatID = chatID;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDateTime getLocalDateTime() {
        return localDateTime;
    }

    public void setLocalDateTime(LocalDateTime localDateTime) {
        this.localDateTime = localDateTime;
    }

    public String getMessageText() {
        return messageText;
    }

    public void setMessageText(String messageText) {
        this.messageText = messageText;
    }

    public Long getChatID() {
        return chatID;
    }

    public void setChatID(Long chatID) {
        this.chatID = chatID;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Notification that = (Notification) o;
        return id == that.id && chatID == that.chatID && Objects.equals(localDateTime, that.localDateTime) && Objects.equals(messageText, that.messageText);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, localDateTime, messageText, chatID);
    }

    @Override
    public String toString() {
        return "Напоминание" +
                "id=" + id +
                ", Время напоминания" + localDateTime +
                ", Задача" + messageText + '\'' +
                ", № сообщения в чате" + chatID +
                '}';
    }
}
