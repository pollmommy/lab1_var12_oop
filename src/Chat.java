// Класс-контейнер
// Хранит сообщения во внутреннем массиве, который наружу не отдаёт
public class Chat {

    private Message[] messages;
    private int count;          // сколько мест реально занято

    public Chat(int capacity) {
        messages = new Message[capacity];
        count = 0;
    }

    // Добавить сообщение в чат
    public void add(Message message) {
        if (count >= messages.length) {
            throw new IllegalStateException("В чате нет свободного места для нового сообщения");
        }
        messages[count] = message;
        count++;
    }

    // Пометить сообщение прочитанным по номеру
    public boolean markAsRead(int number) {
        for (int i = 0; i < count; i++) {
            if (messages[i].getNumber() == number) {
                messages[i].markAsRead();
                return true;
            }
        }
        return false;
    }

    public int countUnread() {
        int result = 0;
        for (int i = 0; i < count; i++) {
            if (!messages[i].isRead()) {
                result++;
            }
        }
        return result;
    }

    // Поиск сообщений по фрагменту текста
    public Message[] findByTextFragment(String fragment) {
        Message[] buffer = new Message[count];
        int found = 0;

        for (int i = 0; i < count; i++) {
            if (messages[i].getText().contains(fragment)) {
                buffer[found] = messages[i];
                found++;
            }
        }

        Message[] result = new Message[found];
        for (int i = 0; i < found; i++) {
            result[i] = buffer[i];
        }
        return result;
    }

    // Последние N сообщений
    public Message[] getLastN(int n) {
        int howMany = Math.min(n, count);
        Message[] result = new Message[howMany];

        for (int i = 0; i < howMany; i++) {
            result[i] = messages[count - howMany + i];
        }
        return result;
    }
}
