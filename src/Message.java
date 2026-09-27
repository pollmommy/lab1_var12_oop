import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

// Класс-сущность
// Правила, которые класс обязан защищать:
// 1) текст сообщения не пустой и не длиннее MAX_LENGTH символов
// 2) автор не пустой
// 3) прочитанное сообщение нельзя вернуть в непрочитанные
public class Message {

    public static final int MAX_LENGTH = 500;

    private static int messageCount = 0;

    private int number;        // задаётся один раз в конструкторе, сеттера нет
    private String author;
    private String text;
    private LocalDateTime sentAt;
    private boolean read;

    // Хранилище вложений
    private String[] attachments;
    private int attachmentsCount;

    // Полный конструктор (время отправки указывается явно)
    public Message(String author, String text, LocalDateTime sentAt) {
        if (author == null || author.isBlank()) {
            throw new IllegalArgumentException("Автор сообщения не может быть пустым");
        }
        if (text == null || text.isBlank()) {
            throw new IllegalArgumentException("Текст сообщения не может быть пустым");
        }
        if (text.length() > MAX_LENGTH) {
            throw new IllegalArgumentException("Текст длиннее " + MAX_LENGTH + " символов");
        }

        messageCount++;
        this.number = messageCount;
        this.author = author;
        this.text = text;
        this.sentAt = sentAt;
        this.read = false;

        this.attachments = new String[10];
        this.attachmentsCount = 0;
    }

    // Упрощённый конструктор (время отправки берётся текущее)
    public Message(String author, String text) {
        this(author, text, LocalDateTime.now());
    }

    // Геттеры только к тем полям, которые нужно читать снаружи
    public int getNumber() { return number; }
    public String getAuthor() { return author; }
    public String getText() { return text; }
    public LocalDateTime getSentAt() { return sentAt; }
    public boolean isRead() { return read; }

    // Метод предметной области вместо сеттера isRead
    public void markAsRead() {
        read = true;
    }

    public void addAttachment(String fileName) {
        if (fileName == null || fileName.isBlank()) {
            throw new IllegalArgumentException("Имя вложения не может быть пустым");
        }
        if (attachmentsCount >= attachments.length) {
            throw new IllegalStateException("Слишком много вложений для этого сообщения");
        }
        attachments[attachmentsCount] = fileName;
        attachmentsCount++;
    }

    // ТАК ДЕЛАТЬ НЕЛЬЗЯ (метод отдаёт наружу ссылку на сам внутренний массив)
    public String[] getAttachmentsUnsafe() {
        return attachments;
    }

    // Правильный вариант (наружу отдаётся копия, а не сам массив)
    public String[] getAttachments() {
        String[] copy = new String[attachmentsCount];
        for (int i = 0; i < attachmentsCount; i++) {
            copy[i] = attachments[i];
        }
        return copy;
    }

    public static int getMessageCount() {
        return messageCount;
    }

    @Override
    public String toString() {
        String status = read ? "прочитано" : "не прочитано";
        String time = sentAt.format(DateTimeFormatter.ofPattern("HH:mm:ss"));
        return "[" + number + "] " + author + " (" + time + ", " + status + "): " + text;
    }
}