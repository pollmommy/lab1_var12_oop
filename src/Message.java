import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

// Абстрактный класс-сущность "Сообщение".
//
// Это ОБЩИЙ базовый класс для всех разновидностей сообщений: TextMessage,
// SystemMessage, MediaMessage. Создать объект именно класса Message нельзя
// (abstract) - можно создавать только его конкретных наследников.
//
// Что здесь общее для всех разновидностей (поэтому лежит тут):
// - номер, автор, время отправки, статус прочтения - есть у любого сообщения;
// - общая проверка длины текста (checkLength) - сам текст и допустимая длина
//   у каждого наследника свои, а АЛГОРИТМ проверки один и тот же;
// - toString() - единый формат вывода для любого сообщения, наследники его
//   не меняют (final), они лишь говорят, что именно показать в getPreview().
public abstract class Message {

    private static int messageCount = 0;

    private final int number;
    private final String author;
    private final LocalDateTime sentAt;
    private boolean read;

    // Конструктор protected: вызвать его напрямую снаружи нельзя (Message
    // и так нельзя создать, он abstract), вызывать может только наследник
    // через super(...).
    protected Message(String author, LocalDateTime sentAt) {
        if (author == null || author.isBlank()) {
            throw new IllegalArgumentException("Автор сообщения не может быть пустым");
        }
        messageCount++;
        this.number = messageCount;
        this.author = author;
        this.sentAt = sentAt;
        this.read = false;
    }

    // Общая проверка длины текста. Наследник передаёт сюда свой текст,
    // а предельная длина берётся через абстрактный метод getMaxLength() -
    // у каждой разновидности сообщения он может быть своим.
    // final - наследник не может подменить саму логику проверки.
    protected final void checkLength(String text) {
        if (text == null || text.isBlank()) {
            throw new IllegalArgumentException("Текст сообщения не может быть пустым");
        }
        if (text.length() > getMaxLength()) {
            throw new IllegalArgumentException("Текст длиннее " + getMaxLength() + " символов");
        }
    }

    // Абстрактные методы - каждый наследник ОБЯЗАН их реализовать,
    // потому что только он знает, каким должен быть ответ.
    public abstract int getMaxLength();
    public abstract String getPreview(); // как показать содержимое в выводе и в поиске

    public int getNumber() { return number; }
    public String getAuthor() { return author; }
    public LocalDateTime getSentAt() { return sentAt; }
    public boolean isRead() { return read; }

    // final - прочитанное нельзя "вернуть" в непрочитанное, и ни один
    // наследник не должен иметь возможность переопределить это правило.
    public final void markAsRead() {
        read = true;
    }

    public static int getMessageCount() {
        return messageCount;
    }

    // final - формат вывода общий для ВСЕХ разновидностей сообщений,
    // наследники не должны иметь возможность его подменить.
    // Это и есть полиморфизм вместо ветвления по типу: toString() не
    // спрашивает "а какой это подтип", а просто вызывает getPreview(),
    // и каждый наследник сам знает, что туда подставить.
    @Override
    public final String toString() {
        String status = read ? "прочитано" : "не прочитано";
        String time = sentAt.format(DateTimeFormatter.ofPattern("HH:mm:ss"));
        return "[" + number + "] " + author + " (" + time + ", " + status + "): " + getPreview();
    }
}