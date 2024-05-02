import java.util.Random;
import java.util.Scanner;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.Period;


public class LibraryManagementSystemApplication {
    static int INDEX = 100;
    static int quantity = 0;
    static int transactionQuantity = 0;
    static int patronQuantity = 0;
    static String[][] books = new String[INDEX][4]; // title, author, ISBN, pageNumber
    static String[][] patrons = new String[INDEX][4]; // fullName, identityNumber, email, password
    static String[][] transactions = new String[INDEX][3]; // ISBN, ID, date
    static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        userHints();
    }

    static void updateTransactionArray(){
        // static void deleteUserInformation(String patronID) - Transactionlari silip truncate ediyor transaction Arrayi'ni.
        //Bu methodu ise yer kalmadiginda arrayi INDEX kadar arttiriyor.

        if( transactionQuantity == transactions.length-1){// basta 99 sa artiriyoruz size i. Sonrasinda 199, 299.
            String[][] newTransactions = new String[transactions.length + INDEX][3];
            for(int i =0; i<transactionQuantity;i++){
                newTransactions[i] = transactions[i];
            }
            transactions = newTransactions;
        }
        // 1. bu methodu check0utBook methodunda transactionlari eklemeden once kullanmamiz gerekir mi ?

        // 2. deleteUserInformation(String patronID) - methodunda String [patronQuantity-1] doğru mu ?
        // INDEX kullanmamiz daha dogru mu

        // 3. extendBooksArrayOnAddition() methodunda ->
        //  String[][] newBooks = new String[books.length + 1][4];
        // +1 degil +INDEX yapsak olur mu?

        // 4. kitap silme ve ekleme, patron silme ve ekleme ve transaction silme var.
        //   Sadece transaction eklemede arrayi guncelledim.
    }
    static void displayUpdatedArrays(){
        generateReports();
        for(int i =0;i<patronQuantity;i++){
            System.out.println("Patron Information: \n" +
                    " User "+(i+1)+": \n"+
                    "   Name & Surname: "+patrons[i][0] +
                    "\n   Identity number: " +patrons[i][1]+
                    "\n   Email: "+ patrons[i][2]+"\n");

            String userTransactionsList = "";

            for(int b=0;b<transactionQuantity;b++){
                if(transactions[b][1].equalsIgnoreCase(patrons[i][1])) {
                    userTransactionsList += "   Transaction No-"  + (b+1) +": \n"+
                            "      Book Name: " + books[getBookIndexByID(transactions[b][0])][0] + "\n"+
                            "      Book ISBN: " + transactions[b][0] + "\n"+
                            "      User ID: "   + transactions[b][1] + "\n" +
                            "      Checkout Date: " + transactions[b][2] + "\n";
                }
            }
            if(userTransactionsList.isEmpty()){
                userTransactionsList = " No transactions have been made.";
            }
            System.out.println("   "+patrons[i][0]+"'s transactions: \n" +  userTransactionsList);
        }
    }


    //    Oruj - [JA-07] Report generation start
    static void generateReports() {
        System.out.println("------------------Library Report------------------ \n" +
                "Total number of books in system: " + quantity + " \n" +
                "List of books: ");
        String report = "";
        int lineCount = 1;

        for (final String[] row : books) {
            report += lineCount + ": " + String.format("|Name:   %s |  %n" +
                    "   |Author:   %s |   %n" +
                    "   |ISBN:   %s |   %n" +
                    "   |Page Number:  %s |  %n" +
                    "   __________________ %n", row);

            lineCount++;
            report += "   Transaction history: \n   __________________ \n";
            if (lineCount > quantity) {
                break;
            }
        }
        System.out.println(report);
    }

    //    Oruj - [JA-07] Report generation END
//    Oruj - [JA-24] Book reservation start
    static void reserveBook(String patronId, String ISBN, int reservationTime) {
        int bookIndex = getBookIndexByID(ISBN);
        if (bookIndex != -1) {
            for (String[] patron : patrons) {
                if (patron[1].equals(patronId)) {
                    System.out.println("Reservation for " + books[bookIndex][0] +
                            "for " + reservationTime + " days is completed by " + patron[0]);
                    break;
                }
            }
        } else {
            System.out.println("The book you are looking for does not exist.");
        }
    }
//    Oruj - [JA-24] Book reservation End
static void userHints(){

    System.out.println("\nWelcome to Library System!");
    System.out.println("To perform a transaction within the system, please select one of the following transactions!");
    while (true){

        System.out.println("Please choose your next step!");
        System.out.println("1. Sign up");
        System.out.println("2. Log in");
        System.out.println("3. Exit");
        System.out.println("Please enter your transaction!");

        int choice = scanner.nextInt();
        scanner.nextLine();
        switch (choice){
            case 1:
                createPatronAccount();
                break;
            case 2:
                int loggedInPatronIndex = login();
                if (loggedInPatronIndex != -1){
                    displayMenu(loggedInPatronIndex);
                }
                break;
            case 3:
                System.out.println("Loggin off...");
                System.exit(0);
            default:
                System.out.println("Incorrect e-mail or passowrd, Please try again!");


        }
    }

}
    static void createPatronAccount(){
        System.out.println("Full Name:");
        String fullName = scanner.nextLine();
        System.out.println("ID Number:");
        String identityNumber = scanner.nextLine();
        System.out.println("E-mail:");
        String email = scanner.nextLine();
        System.out.println("Password:");
        String password = scanner.nextLine();

        patrons[patronQuantity][0] = fullName;
        patrons[patronQuantity][1] = identityNumber;
        patrons[patronQuantity][2] = email;
        patrons[patronQuantity][3] = password;
        patronQuantity++;

        System.out.println("Your account has been created succesfully!");

    }

    static int login(){
        System.out.println("E-mail:");
        String email = scanner.nextLine();
        System.out.println("Password:");
        String password = scanner.nextLine();
        int index = invalidLoginCheck(email, password);
        if (index!=-1){
            System.out.println("Logged in successfully! You may continue your transactions!");
            return index;
        }


        return -1;
    }


    static void displayMenu(int loggedInPatronIndex) {

        System.out.println("\n Welcome to Library Management System");
        String patronID = patrons[loggedInPatronIndex][1];
        String patronName = patrons[loggedInPatronIndex][0];

        while (true) {

            System.out.println("\n1. Add Book");
            System.out.println("2. Update Book");
            System.out.println("3. Delete Book");
            System.out.println("4. Available Books");
            System.out.println("5. Request Book");
            System.out.println("6. Search Book");
            System.out.println("7. Return Book");
            System.out.println("8. Check Book Return Deadline");
            System.out.println("9. Check Out Book");
            System.out.println("10. Reserve Book");
            System.out.println("11. Generate Book Recommendation");
            System.out.println("12. Generate Reports");
            System.out.println("13. Update User Info");
            System.out.println("14. Check User Eligibility For Checkout");
            System.out.println("15. Delete User Information");
            System.out.println("16. Display Updated Arrays");
            System.out.println("17. Exit");

            System.out.println("Please Enter Your Transaction");
            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice){
                case 1:
                    System.out.println("Please Enter Book Title");
                    String title = scanner.nextLine(); //todo: do not use numbers in variable names please
                    System.out.println("Please Enter Author");
                    String author = scanner.nextLine();
                    System.out.println("Please Enter ISBN");
                    String ISBN = scanner.nextLine();
                    System.out.println("Please Enter Number Of Pages");
                    String pageNumber = scanner.nextLine();

                    addBook(title, author, ISBN, pageNumber);
                    break;
                case 2:
                    System.out.println("Please Enter ISBN");
                    String newISBN = scanner.next();
                    System.out.println("Please Enter New Title Of The Book");
                    String newTitle = scanner.next();
                    System.out.println("Please Enter New Author");
                    scanner.nextLine();
                    String newAuthor = scanner.nextLine();
                    System.out.println("Please Enter New Number Of Pages");
                    String newPageNumber = scanner.next();

                    updateBook(newISBN, newTitle, newAuthor, newPageNumber);

                    break;
                case 3:
                    System.out.println("Please Enter ISBN Number");
                    ISBN = scanner.next();

                    deleteBook(ISBN);

                    break;

                case 4:
                    countTotalBooks();
                    viewAvailableBooks();

                    break;

                case 5:
                    System.out.println("Please Enter Title Of The Book");
                    title = scanner.next();
                    System.out.println("Please Enter Author Of The Book");
                    author = scanner.next();

                    requestBook(title, author);

                    break;

                case 6:
                   searchBooks();

                   break;

                case 7:
                    System.out.println("Please Enter Title Of The Book");
                    title = scanner.next();
                    System.out.println("Please Enter Author Of The Book");
                    author = scanner.next();
                    System.out.println("Please Enter ISBN Number Of The Book");
                    ISBN = scanner.next();
                    System.out.println("Please Enter Number Of Pages");
                    pageNumber = scanner.next();
                    returnBook(patronID, title, author, ISBN, pageNumber);

                    break;

                case 8:
                    checkBookReturnDeadline(patronID);

                    break;

                case 9:
                    System.out.println("Please Enter Book Name");
                    String bookName = scanner.next();
                    System.out.println("Please Enter Book ISBN");
                    String bookISBN = scanner.next();

                    String checkoutResponse = checkOutBook(patronID, bookName, bookISBN);
                    System.out.println(checkoutResponse);

                    break;

                case 10:
                    System.out.println("Please Enter ISBN Number");
                    ISBN = scanner.next();
                    System.out.println("PLease Enter Reservation Time");
                    int reservationTime = scanner.nextInt();

                    reserveBook(patronID, ISBN, reservationTime);

                    break;

                case 11:
                    String response = generateBookRecommendations(patronID);
                    System.out.println(response);

                    break;

                case 12:
                    generateReports();
                    break;

                case 13:
                    System.out.println("Please Enter Name Of The User");
                    String fullName = scanner.next();
                    System.out.println("Please Enter ID Number Of The User");
                    String identityNumber = scanner.next();
                    System.out.println("Please Enter E-mail Of User");
                    String email = scanner.next();
                    System.out.println("Please Enter Password");
                    String password = scanner.next();

                    updatePatronInfo(fullName, identityNumber, email, password);

                    break;

                case 14:
                    checkPatronEligibilityForCheckout(patronName);

                    break;

                case 15:
                    deleteUserInformation(patronID);

                    break;

                case 16:
                    displayUpdatedArrays();

                    break;

                case 17:
                    System.out.println("You Have Exit Library System. See You Next Time!!!");
                    System.exit(0);
                    break;
                default:
                    System.out.println("Invalid Choice of Transaction! Please Try Again!");

                    break;


            }

        }
    }

    static void addBook(String title, String author, String ISBN, String pageNumber) {
        if (quantity >= books.length) {
            extendBooksArrayOnAddition();
        }

        books[quantity][0] = title;
        books[quantity][1] = author;
        books[quantity][2] = ISBN;
        books[quantity][3] = pageNumber;

        quantity++;

        System.out.println("You have successfully added the book!");
    }

    static boolean isBookAvailable(String ISBN) {
        for (int i = 0; i < quantity; i++) {
            if (books[i][2].equals(ISBN)) {
                return true; // Kitap bulundu
            }
        }
        return false; // Kitap bulunamadı
    }

    static void viewAvailableBooks() {
        if (quantity == 0) {
            System.out.println("No books are currently available in the library.");
        } else {
            System.out.println("\nList of Available Books:");
            System.out.println("Title \t Author \t ISBN \t Page Number");
            for (int i = 0; i < quantity; i++) {
                System.out.println(books[i][0] + "\t" + books[i][1] + "\t" + books[i][2] + "\t" + books[i][3]);
            }
        }
    }
    static void viewBookDetails() {
        if (quantity == 0) {
            System.out.println("No books are currently available in the library.");
        } else {
            System.out.println("\nList of Available Books:");
            System.out.println("\n Total Number Of Books: " + quantity);
            System.out.println();
            System.out.println("Title \t\t\t Author \t\t ISBN \t\t Page Number");

            for (int i = 0; i < quantity; i++) {
                System.out.print(books[i][0] + "\t\t\t\t" + books[i][1] + "\t\t\t\t" + books[i][2] + "\t\t\t\t" + books[i][3]);

                if ((i + 1) % 5 == 0 || i == quantity - 1) {
                    System.out.println();
                } else {
                    System.out.print("\t\t");
                }
            }
        }
    }

    static void extendBooksArrayOnAddition() {
        String[][] newBooks = new String[books.length + 1][4];

        for (int i = 0; i < books.length; i++) {
            for (int j = 0; j < 4; j++) {
                newBooks[i][j] = books[i][j];
            }
        }

        books = newBooks;
    }


    static void updatePatronInfo(String fullName, String identityNumber, String email, String password) {


        int index = getPatronIndexByID(identityNumber);
        if (index != -1) {

            patrons[index][0] = fullName;
            patrons[index][1] = identityNumber;
            patrons[index][2] = email;
            patrons[index][3] = password;

            System.out.println("Patron information updated successfully!");
        } else {
            System.out.println("Patron not found!!!");
        }

    }

    static void requestBook(String title, String author) {
        int pageNumber = (int) (Math.random()*(500 - 50+1)) + 50;
        String ISBN = "ISBN -";
        for (int i=0; i<13; i++){
            ISBN += (int) (Math.random() * 10);
        }
        System.out.println("Kitap talebiniz tarafımızca alındı!");
        System.out.println("Kitap Adı:" + title);
        System.out.println("Yazar Adı:" + author);
        System.out.println("Kitap Sayfa Sayısı:" + pageNumber);
        System.out.println("Kitap ISBN:" + ISBN);

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

    static void deleteBook(String ISBN) {
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

    static void updateBook(String ISBN, String newTitle, String newAuthor, String newPageNumber) {
        int index = getBookIndexByID(ISBN);
        if (index == -1) {
            System.out.println("Book not found!");
        } else {
            books[index][0] = newTitle;
            books[index][1] = newAuthor;
            books[index][2] = ISBN;
            books[index][3] = newPageNumber;

            System.out.println(ISBN + " book number updated!");
        }
    }

    static int getPatronIndexByID(String id) {
        int index = -1;
        for (int i = 0; i < patrons.length; i++) {
            if (patrons[i][2].equals(id)) {
                index = i;
                break;
            }
        }
        return index;
    }

    static int getBookIndexByID(String ISBN) {
        int foundIndex = -1;
        for (int i = 0; i < quantity; i++) {
            if (books[i][2].equals(ISBN)) {
                foundIndex = i;
                return foundIndex;
            }
        }
        return foundIndex;
    }

    //    Oruj - [JA-22] Check patron Eligibility start
    static boolean checkPatronEligibilityForCheckout(String patronName) {
        boolean isLate = false;
        for (int i = transactionQuantity - 1; i >= 0; i--) {
            if (transactions[i][1].equalsIgnoreCase(patronName)) {
                DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                LocalDate dueDate = LocalDate.parse(transactions[i][2], dateFormatter);
                LocalDate currentDate = LocalDate.now();
                Period difference = Period.between(dueDate, currentDate);
                if(difference.getDays()>15 )
                {
                    isLate=true;
                    System.out.println("The return deadline of the borrowed book has passed 15 days. \n" +
                            "You are not allowed to checkout a new one");
                    break;
                }
            }
        }
        System.out.println("You can borrow a new book.");
        return isLate;
        //    isLate: TRUE - Döndüruyorsa User kitap alamaz!
        //    FALSE - User kitap ala bilir.
    }
//   Oruj - [JA-22] Check patron Eligibility END

    static boolean checkBookReturnDeadline(String patronId) {
        boolean isLate = false;
        DateTimeFormatter dateString = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        for (String[] transaction: transactions) {
            if (transaction[1] != null && transaction[1].equalsIgnoreCase(patronId)) {
                LocalDate dueDate = LocalDate.parse(transaction[2], dateString);
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

    static String checkOutBook(String identityNumber, String bookName, String bookISBN) {
        boolean isFound = false;
        String response = "ERROR: The book you are looking for can not be found!";
        for (String[] book : books) {
            if (book != null && book[2] != null && book[2].equals(bookISBN)) { // Check for null before equals
                isFound = true;

                LocalDate currentDate = LocalDate.now();
                DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                String formattedDate = currentDate.format(dateFormatter);

                transactions[transactionQuantity][0] = bookISBN;
                transactions[transactionQuantity][1] = identityNumber;
                transactions[transactionQuantity][2] = formattedDate;
                transactionQuantity++;

                break;
            }
        }
        if (isFound)
            response = "The book has borrowed. Good reading!";

        return response;
    }

    static void deleteUserInformation(String patronId) {
        int index = getPatronIndexByID(patronId);
        if (index == -1) {
            System.out.println("User not found!");
            return;
        }
        String[][] newPatrons = new String[patronQuantity - 1][4];
        int newIndex = 0;
        for (int i = 0; i < patronQuantity; i++) {
            if (i == index) {
                continue;
            }
            newPatrons[newIndex] = patrons[i];
            newIndex++;
        }
        patrons = newPatrons;
        patronQuantity--;

        cleanTransactionsByPatronID(patronId);

        System.out.println("The user has been deleted successfully.");
    }

    static void cleanTransactionsByPatronID(String patronId) {
        String[][] newTransactions = new String[transactionQuantity - 1][2];
        int newIndex = 0;
        for (int i = 0; i < transactionQuantity; i++) {
            if (transactions[i][1].equals(patronId)) {
                continue;
            }
            newTransactions[newIndex] = transactions[i];
            newIndex++;
        }
        transactions = newTransactions;
        transactionQuantity--;

        System.out.println("Transactions for the user with ID " + patronId + " have been cleaned successfully.");
    }

    static void returnBook(String patronId, String title, String author, String ISBN, String pageNumber) {
        if (!checkBookReturnDeadline(patronId)) {
            addBook(title, author, ISBN, pageNumber);
            System.out.println("The book was returned. The new book can be borrowed!");
        } else {
            System.out.println("You cannot borrow a new book without returning the book!");
        }
    }

    static String successMessage(String message){
       String response = "The process has been completed : " + message;
       return response;
    }

    static void countTotalBooks() {
        System.out.print("Total number of books: " + quantity);
    }

    static void searchBooks() {

        System.out.println(" Choose searching criteria: ");
        System.out.println("1. Title ");
        System.out.println("2. Author ");
        System.out.println("3. ISBN");
        int choice = scanner.nextInt();

        System.out.print("Enter the word you are looking for: ");
        String searchWord = scanner.next();

        switch (choice) {
            case 1:
                searchByTitle(searchWord);
                break;
            case 2:
                searchByAuthor(searchWord);
                break;
            case 3:
                searchByISBN(searchWord);
                break;
            default:
                System.out.println("Invalid choice!");
        }
    }

    static void searchByTitle(String title) {
        boolean isFound = false;
        for (String[] book : books) {
            if (book[0] != null && book[0].equalsIgnoreCase(title)) {
                displayBookInfo(book);
                isFound = true;
            }
        }
        if (!isFound) {
            System.out.println("No books matching the title were found.");
        }
    }

    static void searchByAuthor(String author) {
        boolean isFound = false;
        for (String[] book : books) {
            if (book[1] != null && book[1].equalsIgnoreCase(author)) {
                displayBookInfo(book);
                isFound = true;
            }
        }
        if (!isFound) {
            System.out.println("No books matching the author were found.");
        }
    }

    static void searchByISBN(String ISBN) {
        boolean isFound = false;
        for (String[] book : books) {
            if (book[2] != null && book[2].equalsIgnoreCase(ISBN)) {
                displayBookInfo(book);
                isFound = true;
            }
        }
        if (!isFound) {
            System.out.println("No book found matching ISBN number. ");
        }
    }

    static void displayBookInfo(String[] book) {
        System.out.println("Book's name: " + book[0]);
        System.out.println("Author: " + book[1]);
        System.out.println("ISBN: " + book[2]);
    }

    static int invalidLoginCheck(String email, String password) {
        for (int i = 0; i < patronQuantity; i++) {
            if (patrons[i][2].equals(email) && patrons[i][3].equals(password)) {
                System.out.println("Login successful. You can proceed!");
                return i;
            }
        }
        System.out.println("Invalid login! Please check your email or password!");
        return -1;
    }


    static String generateBookRecommendations(String patronId) {
        String bookISBN = findBookISBNByPatronId(patronId);
        if (bookISBN == null) {
            Random random = new Random();
            int randomIndex = random.nextInt(books.length);
            return "Recommended Book for You:" + books[randomIndex][0] + "The author of the book: " + books[randomIndex][1];

        } else {
            String bookAuthor = "";
            for (int i = 0; i < books.length; i++) {
                if (books[i][2].equals(bookISBN)) {
                    bookAuthor = books[i][1];
                }
            }
            System.out.println("Books Recommended for You: ");
            for (int j = 0; j < books.length; j++) {
                if (books[j][1].equals(bookAuthor)) {
                    return "Recommended Book for You:" + books[j][0] + "The author of the book: " + books[j][1];
                }
            }
            return "No books similar to your previous selections were found.";
        }
    }

    private static String findBookISBNByPatronId(String patronId) {
        for (int i = 0; i < transactions.length; i++) {
            if (transactions[i][1].equals(patronId)) {
                return transactions[i][0];
            }
        }
        return null;

    }

}

