import java.time.LocalDateTime;

// Обычное текстовое сообщение - первый наследник Message.
//
// Задание 4 (композиция вместо наследования): вместо отдельных классов
// PinnedTextMessage и PinnedAdminTextMessage у сообщения просто есть
// поле pinned (boolean) и поле role (AuthorRole)
public class TextMessage extends Message {

    public static final int MAX_LENGTH = 500;

    private String text;
    private final ContentFilter filter; // интерфейс - фильтр передан извне, тип неизвестен
    private final AuthorRole role;
    private boolean pinned;

    public TextMessage(String author, String text, LocalDateTime sentAt,
                       ContentFilter filter, AuthorRole role) {
        super(author, sentAt); // вызов конструктора базового класса
        this.filter = filter;
        this.role = role;
        this.pinned = false;
        setText(text);
    }

    // Упрощённый конструктор
    public TextMessage(String author, String text, ContentFilter filter) {
        this(author, text, LocalDateTime.now(), filter, AuthorRole.USER);
    }

    // Текстовое сообщение можно редактировать
    public void edit(String newText) {
        setText(newText);
    }

    private void setText(String text) {
        checkLength(text);              // общая проверка длины из Message
        this.text = filter.apply(text); // применяем фильтр, не знает какой именно
    }

    public void pin() {
        pinned = true;
    }

    public void unpin() {
        pinned = false;
    }

    public boolean isPinned() {
        return pinned;
    }

    public AuthorRole getRole() {
        return role;
    }

    @Override
    public int getMaxLength() {
        return MAX_LENGTH;
    }

    @Override
    public String getPreview() {
        String pin = pinned ? "ЗАКРЕПЛЕНО " : "";
        String roleTag = (role == AuthorRole.ADMIN) ? "[admin] " : "";
        return pin + roleTag + text;
    }
}