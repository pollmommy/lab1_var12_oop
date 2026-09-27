import java.time.LocalDateTime;

public class Main {

    public static void main(String[] args) {
        Chat chat = new Chat(10);

        System.out.println("Корректные операции");

        Message m1 = new Message("Аня", "Привет! Как дела?");
        Message m2 = new Message("Борис", "Всё хорошо, спасибо", LocalDateTime.now());
        Message m3 = new Message("Аня", "Отлично, пока");

        chat.add(m1);
        chat.add(m2);
        chat.add(m3);

        System.out.println(m1);
        System.out.println(m2);
        System.out.println(m3);

        chat.markAsRead(m1.getNumber());
        System.out.println("Непрочитанных сообщений в чате: " + chat.countUnread());
        System.out.println("Сообщений создано за всё время: " + Message.getMessageCount());

        System.out.println();
        System.out.println("Некорректные попытки (все должны быть отбиты)");

        tryAndCatch(() -> {
            Message bad = new Message("Аня", ""); // пустой текст
        }, "отправить пустое сообщение");

        tryAndCatch(() -> {
            String longText = "a".repeat(600);
            Message bad = new Message("Аня", longText); // 600 символов при MAX_LENGTH = 500
        }, "отправить текст длиной 600 символов");

        tryAndCatch(() -> {
            Message bad = new Message("", "Текст без автора");
        }, "отправить сообщение без автора");

        System.out.println("Попытка \"снять признак прочтения\": в классе Message просто нет " +
                "метода для этого, поэтому нарушить правило даже не получится написать");

        System.out.println();
        System.out.println("Задание 5: проблема общей ссылки на массив вложений");

        m2.addAttachment("photo.png");
        m2.addAttachment("doc.pdf");

        System.out.println("ДО исправления: пользуемся getAttachmentsUnsafe() ");
        String[] unsafeAttachments = m2.getAttachmentsUnsafe();
        System.out.println("Вложения сейчас: " + joinNonEmpty(unsafeAttachments));

        unsafeAttachments[0] = null; // внешний код "удаляет" вложение напрямую
        System.out.println("Вложения после внешней правки массива: " + joinNonEmpty(m2.getAttachmentsUnsafe()));
        System.out.println("Видно: внутреннее состояние сообщения изменилось в обход всех проверок");

        System.out.println();
        System.out.println("ПОСЛЕ исправления: пользуемся безопасным getAttachments()");
        String[] safeAttachments = m2.getAttachments();
        System.out.println("Вложения (копия): " + joinNonEmpty(safeAttachments));

        safeAttachments[0] = null; // меняем только копию, а не оригинал
        String[] safeAttachmentsAgain = m2.getAttachments();
        System.out.println("Вложения после внешней правки копии: " + joinNonEmpty(safeAttachmentsAgain));
        System.out.println("Внутреннее состояние сообщения не изменилось - оно защищено");
    }

    // Runnable - готовый интерфейс (метод без параметров и без результата)
    static void tryAndCatch(Runnable action, String description) {
        try {
            action.run();
            System.out.println("Внимание: попытка \"" + description + "\" НЕ была отбита!");
        } catch (RuntimeException ex) {
            System.out.println("Попытка \"" + description + "\" отбита: " + ex.getMessage());
        }
    }

    static String joinNonEmpty(String[] array) {
        String result = "";
        for (String s : array) {
            if (s != null) {
                if (!result.isEmpty()) result += ", ";
                result += s;
            }
        }
        return result.isEmpty() ? "(нет)" : result;
    }
}

/*
Поле                         Правило                        Метод, где правило защищается

Message.author          автор не пустой                        конструкторы Message
Message.text            текст не пустой и не длиннее           конструкторы Message
                        MAX_LENGTH символов
Message.read            прочитанное нельзя вернуть             в классе нет метода "снять прочтение"
                        в непрочитанные
Message.number          номер задаётся один раз и не           присваивается в конструкторе, сеттера нет
                        меняется после создания
Message.attachments     внешний код не должен менять           getAttachments() возвращает копию массива
                        вложения в обход проверок
Chat.messages           контейнер не отдаёт свой               findByTextFragment(),
                        внутренний массив наружу               etLastN() возвращают новые массивы
*/