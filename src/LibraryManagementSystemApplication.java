import java.awt.print.Book;
import java.util.Scanner;

public class LibraryManagementSystemApplication {
    static int INDEX = 100;
    static int quantity = 0;
    static String[][] books = new String[INDEX][4];
    static String[][] patrons = new String[INDEX][4];
    static String[][] transactions = new String[INDEX][3];

    public static void main(String[] args) {

    }

   

    static void requestBook(String title, String author) {
        int pageNumber = randomPage();
        int ISBN = randomISBN();
        System.out.println("Kitap talebiniz tarafımızca alındı!");
        System.out.println("Kitap Adı:" + title);
        System.out.println("Yazar Adı:" + author);
        System.out.println("Kitap Sayfa Sayısı:" + pageNumber);
        System.out.println("Kitap ISBN:" + ISBN);


    }

    static void truncateBooksArrayOnDeletion(String ISBN) {
        int foundIndex = -1;
        for (int i = 0; i < quantity; i++) {
            if (books[i][2].equals(ISBN)) {
                foundIndex = i;
                break;
            }
        }
        if (foundIndex == -1) {
            System.out.println("Kitap Bulunamadı!");
            return;
        }
        String[][] newBooks = new String[quantity - 1][4];
        for (int i = 0; i < foundIndex; i++) {
            newBooks[i] = books[i];
        }
        for (int i = foundIndex + 1; i < quantity; i++) {
            newBooks[i - 1] = books[i];
        }
        books = newBooks;
        quantity--;

        System.out.println("Kitap başarıyla silindi ve dizi güncellendi.");
    }

    static void deleteBook(String ISBN){
        if (quantity == 0) {
            System.out.println("Kütüphanede kitap bulunmamaktadır.");
        } else {
            int foundIndex = -1;
            for (int i = 0; i < quantity; i++) {
                if (books[i][2].equals(ISBN)) {
                    foundIndex = i;
                    break;
                }
            }
            if (foundIndex != -1) {
                truncateBooksArrayOnDeletion(ISBN);
            } else {
                System.out.println("Kitap bulunamadı!");
            }
        }
    }
}

