// Вторая реализация ContentFilter: заменяет запрещённые слова на "***"
// Простая версия - обычный поиск и замена подстроки
public class ProfanityFilter implements ContentFilter {

    private final String[] bannedWords;

    public ProfanityFilter(String[] bannedWords) {
        this.bannedWords = bannedWords;
    }

    @Override
    public String apply(String text) {
        String result = text;
        for (String word : bannedWords) {
            result = result.replace(word, "***");
        }
        return result;
    }
}