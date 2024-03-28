import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class LibraryManagementSystemApplication {
    static int INDEX = 100;
    static int quantity = 0;
    static String[][] books = new String[INDEX][4];
    static String[][] patrons = new String[INDEX][4];
    static String[][] transactions = new String[INDEX][3];

    public static void main(String[] args) {

    }

    static void addBook(String title, String author, String ISBN, String pageNumber){

        books[quantity][0] = title;
        books[quantity][1] = author;
        books[quantity][2] = ISBN;
        books[quantity][3] = pageNumber;

        quantity++;

        System.out.println("Kitabı Başarıyla Eklediniz!");
    }

   

    static void requestBook(String title, String author) {
        //int pageNumber = randomPage(); write the methods with your own algorithm
        // int ISBN = randomISBN();
        System.out.println("Kitap talebiniz tarafımızca alındı!");
        System.out.println("Kitap Adı:" + title);
        System.out.println("Yazar Adı:" + author);
        System.out.println("Kitap Sayfa Sayısı:" + 0); // todo: this hard-coded value should be replaced with the actual page number
        System.out.println("Kitap ISBN:" + 0);


    }

    static void truncateBooksArrayOnDeletion(String ISBN) {
        int index = getBookIndexByID(ISBN);
        if (index == -1) {
            System.out.println("Kitap Bulunamadı!");
            return;
        }
        String[][] newBooks = new String[quantity - 1][4];
        for (int i = 0; i < index; i++) {
            newBooks[i] = books[i];
        }
        for (int i = index + 1; i < quantity; i++) {
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
            int index = getBookIndexByID(ISBN);
            if (index != -1) {
                truncateBooksArrayOnDeletion(ISBN);
            } else {
                System.out.println("Kitap bulunamadı!");
            }
        }
    }

    static void updateBook(String ISBN,String newTitle,String newAuthor,String newPageNumber){
        int index = getBookIndexByID(ISBN);
        if (index==-1){
            System.out.println("Kitap bulunamadı!");
        }else{
            books[index][0]=newTitle;
            books[index][1]=newAuthor;
            books[index][2]=ISBN;
            books[index][3]=newPageNumber;

            System.out.println(ISBN+" numaralı kitap güncellendi!");
        }
    }
    static int getBookIndexByID(String ISBN){
        int foundIndex = -1;
        for (int i = 0; i < quantity; i++) {
            if (books[i][2].equals(ISBN)) {
                foundIndex = i;
                return foundIndex;
            }
        }
        return foundIndex;
    }

    static boolean checkBookReturnDeadline(String patronID){
        boolean isLate = false;
        for (String transaction[]: transactions){
            if (transaction[1].equalsIgnoreCase(patronID)) {
                LocalDate dueDate = LocalDate.parse(transaction[2], DateTimeFormatter.ISO_DATE);
                LocalDate currentDate = LocalDate.now();
                if (currentDate.isAfter(dueDate)) {
                    isLate = true;
                    break;
                }
            }
        }

        if (isLate) {
            System.out.println("You cannot borrow a new book because the book's return date has passed!");
        } else {
            System.out.println("You can borrow new books.");
        }

        return isLate;
    }
}

