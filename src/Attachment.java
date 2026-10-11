// Вложение (файл). Отдельная, самостоятельная сущность
// Связь с MediaMessage - агрегация, а не композиция: вложение создаётся
// до сообщения, передаётся в сообщение извне через конструктор, и продолжает существовать независимо,
// даже если сообщение, в которое его вложили, удалить
public class Attachment {

    private final String fileName;
    private final int sizeKb;

    public Attachment(String fileName, int sizeKb) {
        if (fileName == null || fileName.isBlank()) {
            throw new IllegalArgumentException("Имя файла вложения не может быть пустым");
        }
        if (sizeKb <= 0) {
            throw new IllegalArgumentException("Размер вложения должен быть больше нуля");
        }
        this.fileName = fileName;
        this.sizeKb = sizeKb;
    }

    public String getFileName() {
        return fileName;
    }

    public int getSizeKb() {
        return sizeKb;
    }

    @Override
    public String toString() {
        return fileName + " (" + sizeKb + " КБ)";
    }
}