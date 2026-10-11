// Роль автора сообщения.
// Нужна для задания 4: вместо отдельных классов PinnedTextMessage и
// PinnedAdminTextMessage у TextMessage просто есть поле этого типа.
// Это и есть "композиция вместо наследования" - разные роли не плодят новые классы.
public enum AuthorRole {
    USER,
    ADMIN
}