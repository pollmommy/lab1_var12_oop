// Класс-контейнер
// Связь с Message - композиция: сообщение создаётся специально для этого
// чата (другой код его не хранит и не использует параллельно), и если бы
// чат уничтожили, все его сообщения ушли бы вместе с ним
public class Chat {

    private Message[] messages; // хранилище закрыто
    private int count;          // сколько мест реально занято

    public Chat(int capacity) {
        messages = new Message[capacity];
        count = 0;
    }

    public void add(Message message) {
        if (count >= messages.length) {
            throw new IllegalStateException("В чате нет свободного места для нового сообщения");
        }
        messages[count] = message;
        count++;
    }

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

    // Поиск работает через getPreview() общий для всех разновидностей
    // метод, объявленный в Message. Chat ни разу не спрашивает, с каким именно наследником работает
    public Message[] findByTextFragment(String fragment) {
        Message[] buffer = new Message[count];
        int found = 0;

        for (int i = 0; i < count; i++) {
            if (messages[i].getPreview().contains(fragment)) {
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

    public Message[] getLastN(int n) {
        int howMany = Math.min(n, count);
        Message[] result = new Message[howMany];

        for (int i = 0; i < howMany; i++) {
            result[i] = messages[count - howMany + i];
        }
        return result;
    }

    // Печать всех сообщений одним циклом, без единой проверки типа
    public void printAll() {
        for (int i = 0; i < count; i++) {
            System.out.println(messages[i]);
        }
    }
}