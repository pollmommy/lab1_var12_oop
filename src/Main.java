public class Main {

    public static void main(String[] args) {
        Chat chat = new Chat(10);

        System.out.println("Задание 1 и 2: создаём разные виды сообщений");

        // клиентский код (TextMessage)
        ContentFilter noFilter = new NoFilter();
        ContentFilter profanityFilter = new ProfanityFilter(new String[]{"дурак", "тупица"});
        ContentFilter linkBlocker = new LinkBlockerFilter();

        TextMessage t1 = new TextMessage("Аня", "Привет! Как дела?", noFilter);
        TextMessage t2 = new TextMessage("Борис", "Зайди на http://спам-сайт.ру срочно", linkBlocker);
        TextMessage t3 = new TextMessage("Админ", "Не пиши тут как дурак",
                java.time.LocalDateTime.now(), profanityFilter, AuthorRole.ADMIN);
        t3.pin(); // закрепляем сообщение (задание 4), поле вместо отдельного класса

        SystemMessage sys = new SystemMessage("Пользователь Борис вышел из чата");

        // агрегация
        Attachment photo = new Attachment("отпуск.jpg", 2400);
        MediaMessage media = new MediaMessage("Аня", "Вот наше фото", photo);

        chat.add(t1);
        chat.add(t2);
        chat.add(t3);
        chat.add(sys);
        chat.add(media);

        System.out.println();
        System.out.println("Печать чата одним циклом, без единой проверки типа");
        chat.printAll();

        System.out.println();
        System.out.println("Вложение доступно само по себе, даже вне сообщения: " + photo);

        System.out.println();
        System.out.println("Непрочитанных сообщений: " + chat.countUnread());
        chat.markAsRead(t1.getNumber());
        System.out.println("Непрочитанных после пометки первого: " + chat.countUnread());
        System.out.println("Сообщений создано за всё время (статика общая для ВСЕХ наследников Message): "
                + Message.getMessageCount());

        System.out.println();
        System.out.println("Задание 3: один и тот же фильтр-интерфейс, разные реализации");
        ContentFilter[] filters = { noFilter, profanityFilter, linkBlocker };
        String sample = "Не веди себя как дурак и не пиши на http://спам.ру";
        for (ContentFilter f : filters) {
            // цикл не знает и не проверяет, какая именно реализация ему попалась
            // полиморфизм
            System.out.println(f.getClass().getSimpleName() + ": " + f.apply(sample));
        }

        System.out.println();
        System.out.println("Задание 2 и инварианты ЛР1: некорректные попытки отбиваются");

        tryAndCatch(() -> {
            TextMessage bad = new TextMessage("Аня", "", noFilter);
        }, "создать текстовое сообщение с пустым текстом");

        tryAndCatch(() -> {
            String longText = "a".repeat(600);
            TextMessage bad = new TextMessage("Аня", longText, noFilter);
        }, "создать текстовое сообщение длиной 600 символов (лимит TextMessage - 500)");

        tryAndCatch(() -> {
            String longText = "a".repeat(300);
            SystemMessage bad = new SystemMessage(longText);
        }, "создать системное сообщение длиной 300 символов (лимит SystemMessage - 200)");

        tryAndCatch(() -> {
            TextMessage bad = new TextMessage("", "Текст без автора", noFilter);
        }, "создать сообщение без автора");

        tryAndCatch(() -> {
            MediaMessage bad = new MediaMessage("Аня", "Подпись", null);
        }, "создать медиа-сообщение без вложения");

        System.out.println("Попытка \"отредактировать системное сообщение\": в классе SystemMessage " +
                "нет метода edit() вообще, поэтому нарушить это правило даже не получится написать");

        System.out.println();
        System.out.println("Поиск и выборка (работают одинаково для любых наследников Message)");
        Message[] found = chat.findByTextFragment("http");
        System.out.println("Найдено сообщений со словом \"http\": " + found.length);

        Message[] last2 = chat.getLastN(2);
        System.out.println("Последние 2 сообщения:");
        for (Message m : last2) {
            System.out.println("  " + m);
        }
    }

    static void tryAndCatch(Runnable action, String description) {
        try {
            action.run();
            System.out.println("Внимание: попытка \"" + description + "\" НЕ была отбита!");
        } catch (RuntimeException ex) {
            System.out.println("Попытка \"" + description + "\" отбита: " + ex.getMessage());
        }
    }
}

/*
Связи между классами и почему выбраны именно такие:

Chat --(композиция)--> Message
  Сообщение создаётся специально для этого чата, нигде больше не хранится
  и не используется. Если бы чат исчез, все его сообщения ушли бы вместе с ним.

Message --(наследование)--> TextMessage, SystemMessage, MediaMessage
  Отношение "является": TextMessage является сообщением, у него есть всё то
  же самое (номер, автор, время, прочитано), плюс свои особенности.
  Поэтому здесь оправдано наследование.

MediaMessage --(агрегация)--> Attachment
  Вложение существует независимо: оно как будто уже лежит в хранилище файлов
  до того, как его вложили в сообщение, и переживёт сообщение, если его удалить.
  Поэтому Attachment создаётся отдельно и передаётся в MediaMessage извне,
  а не создаётся самим сообщением.

TextMessage --(зависимость/использование)--> ContentFilter
  TextMessage не хранит "вид" фильтра как перечисление и не проверяет его
  тип - он просто вызывает filter.apply(...), не зная, какая именно
  реализация передана. Программирование к интерфейсу.

Задание 4 (композиция вместо наследования) - подсчёт классов:
  Если бы для "закреплено" и "роль автора" делали отдельные классы
  (TextMessage -> PinnedTextMessage -> PinnedAdminTextMessage), на каждую
  комбинацию двух независимых признаков (закреплено: да/нет, роль: user/admin)
  потребовалось бы 4 класса вместо 1 (TextMessage, PinnedTextMessage,
  AdminTextMessage, PinnedAdminTextMessage). Добавление третьего признака
  удвоило бы число классов снова.
  После переделки на композицию - один класс TextMessage с двумя полями
  (boolean pinned, AuthorRole role). Новая комбинация - это просто новое
  значение поля, а не новый класс. Отношение здесь "имеет" ("сообщение
  имеет роль автора и признак закрепления"), а не "является", поэтому
  наследование тут было бы неоправданным.
*/