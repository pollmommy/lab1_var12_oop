import java.time.LocalDateTime;

// Системное сообщение - второй наследник Message.
// Пример: "Пользователь вышел из чата", "Чат создан"
//
// Главное отличие от TextMessage: в этом классе нет метода edit() вообще
// Это требование "не редактируется" Правило защищено отсутствием метода
public class SystemMessage extends Message {

    public static final int MAX_LENGTH = 200;

    private final String text;

    public SystemMessage(String text, LocalDateTime sentAt) {
        super("Система", sentAt); // автор у системных сообщений всегда фиксированный
        checkLength(text);
        this.text = text;
    }

    public SystemMessage(String text) {
        this(text, LocalDateTime.now());
    }

    @Override
    public int getMaxLength() {
        return MAX_LENGTH;
    }

    @Override
    public String getPreview() {
        return "[СИСТЕМА] " + text;
    }
}