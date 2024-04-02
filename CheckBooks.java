import java.util.HashMap;
import java.util.Map;

public class CheckBooks {
    private Map<String, Integer> books = new HashMap<>();

    // Kitap envanterine kitap eklemek
    public void addBook(String bookId, int quantity) {
        books.put(bookId, books.getOrDefault(bookId, 0) + quantity);
    }

    // Kitabın mevcut olup olmadığını kontrol
    public boolean isBookAvailable(String bookId) {
        return books.getOrDefault(bookId, 0) > 0;
    }
}