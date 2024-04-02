import java.util.Scanner;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;


public class LibraryManagementSystemApplication {
    static int INDEX = 100;
    static int quantity = 0;
    static int transactionQuantity=0;
    static int patronQuantity = 0;
    static String[][] books = new String[INDEX][4];
    static String[][] patrons = new String[INDEX][4];
    static String[][] transactions = new String[INDEX][3];

    public static void main(String[] args) {


    }

    static String displayMenu(){
        System.out.println("\n Welcome Library Management System");
        System.out.println("1. Add/Edit Book");
        System.out.println("2. Delete Book");
        System.out.println("3. Add/Edit Patron");
        System.out.println("4. Exit");

        Scanner scanner = new Scanner(System.in);
        return scanner.nextLine();

    }

    static void addBook(String title, String author, String ISBN, String pageNumber){

        books[quantity][0] = title;
        books[quantity][1] = author;
        books[quantity][2] = ISBN;
        books[quantity][3] = pageNumber;

        quantity++;

        System.out.println("You have successfully added the book!");
    }


   static void extendBooksArrayOnAddition() {
       String[] newBooks = new String[books.length + 1];

       for (int i = 0; i < books.length; i++) {
           newBooks[i][0] = books[i][0];
           newBooks[i][1] = books[i][1];
           newBooks[i][2] = books[i][2];
           newBooks[i][3] = books[i][3];
       }
        books = newBooks;

       System.out.println("Book Has Been Added Successfully!");
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
            System.out.println("Book Not Found!");
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

        System.out.println("The book has been successfully deleted and the array has been updated.");
    }

    static void deleteBook(String ISBN){
        if (quantity == 0) {
            System.out.println("There are no books in the library.");
        } else {
            int index = getBookIndexByID(ISBN);
            if (index != -1) {
                truncateBooksArrayOnDeletion(ISBN);
            } else {
                System.out.println("Book not found!");
            }
        }
    }

    static void updateBook(String ISBN,String newTitle,String newAuthor,String newPageNumber){
        int index = getBookIndexByID(ISBN);
        if (index==-1){
            System.out.println("Book not found!");
        }else{
            books[index][0]=newTitle;
            books[index][1]=newAuthor;
            books[index][2]=ISBN;
            books[index][3]=newPageNumber;

            System.out.println(ISBN+" book number updated!");
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
    static String checkOutBook(String identityNumber,String bookName, String bookISBN){
        boolean isFound= false;
        String response="ERROR: The book you are looking for can not be found!";
        for(String[] book: books){
            if(book[3].equals(bookISBN)){
                isFound=true;

                LocalDate currentDate = LocalDate.now();
                DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                String formattedDate = currentDate.format(dateFormatter);

                transactions[quantity][0]= bookISBN;
                transactions[quantity][1]= identityNumber;
                transactions[quantity][2]= formattedDate;
                transactionQuantity++;

                truncateBooksArrayOnDeletion(bookISBN);
                break;
            }
        }
        if(isFound)
            response=  "The book has borrowed. Good reading!";
        return response;
    }

    static void deleteUserInformation(String patronID) {
        int index = getPatronIndexByID(patronID);
        if (index == -1) {
            System.out.println("User not found!");
            return;
        }
        String[][] newPatrons = new String[patronQuantity - 1][4];
        for (int i = 0; i < index; i++) {
            newPatrons[i] = patrons[i];
        }
        for (int i = index + 1; i < patronQuantity; i++) {
            newPatrons[i - 1] = patrons[i];
        }
        patrons = newPatrons;
        patronQuantity--;

        cleanTransactionsByPatronID(patronID);

        System.out.println("The user has been deleted successfully.");
    }
    static void cleanTransactionsByPatronID(String patronID) {
        for (int i = 0; i < transactionQuantity; i++) {
            if (transactions[i][1].equals(patronID)) {
                transactions[i] = null;
                for (int j = i; j < transactionQuantity - 1; j++) {
                    transactions[j] = transactions[j + 1];
                }
                transactions[transactionQuantity - 1] = null;
                transactionQuantity--;
                break;
            }
        }
    }
    static int getPatronIndexByID(String patronID) {
        int foundIndex = -1;
        for (int i = 0; i < patronQuantity; i++) {
            if (patrons[i][0].equals(patronID)) {
                foundIndex = i;
                break;
            }
        }
        return foundIndex;
    }
    
    static void returnBook(String patronID, String title, String author, String ISBN, String pageNumber) {
        if (!checkBookReturnDeadline(patronID)) {
            addBook(title,author,ISBN,pageNumber);
            System.out.println("The book was returned. The new book can be borrowed!");
        } else {
            System.out.println("You cannot borrow a new book without returning the book!");
        }
    }
}

