import java.time.LocalDateTime;

// Медиа-сообщение (с вложением) - третий наследник Message
//
// Связь с Attachment - агрегация:
// вложение создаётся до сообщения и передаётся извне через конструктор,
// MediaMessage его не создаёт и не управляет его временем жизни
public class MediaMessage extends Message {

    public static final int MAX_LENGTH = 200;

    private String caption;                  // подпись к файлу, может быть пустой
    private final Attachment attachment;

    public MediaMessage(String author, String caption, Attachment attachment, LocalDateTime sentAt) {
        super(author, sentAt);
        if (attachment == null) {
            throw new IllegalArgumentException("Вложение обязательно для медиа-сообщения");
        }
        this.attachment = attachment;

        if (caption == null || caption.isBlank()) {
            this.caption = ""; // подпись необязательна просто фото без текста
        } else {
            checkLength(caption); // если подпись есть, на неё действует общее правило длины
            this.caption = caption;
        }
    }

    public MediaMessage(String author, String caption, Attachment attachment) {
        this(author, caption, attachment, LocalDateTime.now());
    }

    public Attachment getAttachment() {
        return attachment;
    }

    @Override
    public int getMaxLength() {
        return MAX_LENGTH;
    }

    @Override
    public String getPreview() {
        String captionPart = caption.isEmpty() ? "" : caption + " ";
        return captionPart + "[вложение: " + attachment + "]";
    }
}