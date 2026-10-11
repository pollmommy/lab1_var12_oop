// Первая реализация ContentFilter: фильтрации нет вообще, текст возвращается как есть
public class NoFilter implements ContentFilter {
    @Override
    public String apply(String text) {
        return text;
    }
}