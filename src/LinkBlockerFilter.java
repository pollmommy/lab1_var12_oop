// Третья реализация ContentFilter: блокирует ссылки
// Разбиваем текст на слова по пробелу и каждое слово, начинающееся
// с "http://" или "https://", заменяем на предупреждение
public class LinkBlockerFilter implements ContentFilter {

    private static final String MARKER = "[ссылка заблокирована]";

    @Override
    public String apply(String text) {
        String[] words = text.split(" ");
        StringBuilder result = new StringBuilder();

        for (int i = 0; i < words.length; i++) {
            String word = words[i];
            if (word.startsWith("http://") || word.startsWith("https://")) {
                result.append(MARKER);
            } else {
                result.append(word);
            }
            if (i < words.length - 1) {
                result.append(" ");
            }
        }
        return result.toString();
    }
}