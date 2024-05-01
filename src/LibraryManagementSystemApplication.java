import javax.sound.midi.Soundbank;
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
    Scanner scan = new Scanner(System.in);

    System.out.println("\nWelcome to Library System!");
    System.out.println("To perform a transaction within the system, please select one of the following transactions!");
    while (true){

        System.out.println("Please choose your next step!");
        System.out.println("1. Sign up");
        System.out.println("2. Log in");
        System.out.println("3. Exit");
        System.out.println("Please enter your transaction!");

        int choice = scan.nextInt();
        scan.nextLine();
        switch (choice){
            case 1:
                createPatronAccount(scan);
                break;
            case 2:
                int loggedInPatronIndex = login(scan);
                if (loggedInPatronIndex != -1){
                    displayMenu(scan, loggedInPatronIndex);
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
    static void createPatronAccount(Scanner scan){
        System.out.println("Full Name:");
        String fullName = scan.nextLine();
        System.out.println("ID Number:");
        String identityNumber = scan.nextLine();
        System.out.println("E-mail:");
        String email = scan.nextLine();
        System.out.println("Password:");
        String password = scan.nextLine();

        patrons[patronQuantity][0] = fullName;
        patrons[patronQuantity][1] = identityNumber;
        patrons[patronQuantity][2] = email;
        patrons[patronQuantity][3] = password;
        patronQuantity++;

        System.out.println("Your account has been created succesfully!");

    }

    static int login(Scanner scan){
        System.out.println("E-mail:");
        String email = scan.nextLine();
        System.out.println("Password:");
        String password = scan.nextLine();
        for (int i=0;i<patronQuantity;i++){
            if (patrons[i][2].equalsIgnoreCase(email)&&patrons[i][3].equalsIgnoreCase(password)){
                System.out.println("Logged in successfully! You may continue your transactions!");
                return i;
            }

        }
        System.out.println("Unvalid e-mail or password. Please try again!");
        return -1;
    }


        static String displayMenu(Scanner scan, int loggedInPatronIndex) {

            Scanner scanner = new Scanner(System.in);
        while (true) {

            System.out.println("\n Welcome Library Management System");
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
            String choice = scanner.next();

            switch (choice){
                case "1":
                    Scanner scanner1 = new Scanner(System.in);
                    System.out.println("Please Enter Book Title");
                    String title = scanner1.nextLine(); //todo: do not use numbers in variable names please
                    System.out.println("Please Enter Author");
                    String author = scanner1.nextLine();
                    System.out.println("Please Enter ISBN");
                    String ISBN = scanner1.nextLine();
                    System.out.println("Please Enter Number Of Pages");
                    String pageNumber = scanner1.nextLine();

                    addBook(title, author, ISBN, pageNumber);
                    break;
                case "2":
                    Scanner scanner2 = new Scanner(System.in);
                    System.out.println("Please Enter ISBN");
                    ISBN = scanner2.nextLine();
                    System.out.println("Please Enter New Title Of The Book");
                    String newTitle = scanner2.nextLine();
                    System.out.println("Please Enter New Author");
                    String newAuthor = scanner2.nextLine();
                    System.out.println("Please Enter New Number Of Pages");
                    String newPageNumber = scanner2.nextLine();

                    updateBook(ISBN, newTitle, newAuthor, newPageNumber);

                    break;
                case "3":
                    Scanner scanner3 = new Scanner(System.in);
                    System.out.println("Please Enter ISBN Number");
                    ISBN = scanner3.nextLine();

                    deleteBook(ISBN);

                    break;

                case "4":
                    Scanner scanner4 = new Scanner(System.in);
                    System.out.println("Please Enter ISBN Number Of Book");
                    ISBN = scanner4.nextLine();
                    countTotalBooks();

                    isBookAvailable(ISBN); //todo: we don't need this method here
                    // we should call a method that print out all the books

                    break;

                case "5":
                    Scanner scanner5 = new Scanner(System.in);
                    System.out.println("Please Enter Title Of The Book");
                    title = scanner5.nextLine();
                    System.out.println("Please Enter Author Of The Book");
                    author = scanner5.nextLine();

                    requestBook(title, author);

                    break;

                case "6":
                   searchBooks();

                   break;

                case "7":
                    Scanner scanner7 = new Scanner(System.in);
                    System.out.println("Please Enter User ID");
                    String patronId = scanner7.nextLine();
                    System.out.println("Please Enter Title Of The Book");
                    title = scanner7.nextLine();
                    System.out.println("Please Enter Author Of The Book");
                    author = scanner7.nextLine();
                    System.out.println("Please Enter ISBN Number Of The Book");
                    ISBN = scanner7.nextLine();
                    System.out.println("Please Enter Number Of Pages");
                    pageNumber = scanner7.nextLine();
                    returnBook(patronId, title, author, ISBN, pageNumber);

                    break;

                case "8":
                    Scanner scanner8 = new Scanner(System.in);
                    System.out.println("Please Enter ID Of User");
                    patronId = scanner8.nextLine();
                    checkBookReturnDeadline(patronId);

                    break;

                case "9":
                    Scanner scanner9 = new Scanner(System.in);
                    System.out.println("Please Enter Id Number");
                    String identityNumber = scanner9.nextLine();
                    System.out.println("Please Enter Book Name");
                    String bookName = scanner9.nextLine();
                    System.out.println("Please Enter Book ISBN");
                    String bookISBN = scanner9.nextLine();

                    checkOutBook(identityNumber, bookName, bookISBN);

                    break;

                case "10":
                    Scanner scanner10 = new Scanner(System.in);
                    System.out.println("Please Enter User ID");
                    patronId = scanner10.nextLine();
                    System.out.println("Please Enter ISBN Number");
                    ISBN = scanner10.nextLine();
                    System.out.println("PLease Enter Reservation Time");
                    int reservationTime = scanner10.nextInt();

                    reserveBook(patronId, ISBN, reservationTime);

                    break;

                case "11":
                    Scanner scanner11 = new Scanner(System.in);
                    System.out.println("Please Enter User ID");
                    patronId = scanner11.nextLine();

                    generateBookRecommendations(patronId);

                    break;

                case"12":
                    generateReports();
                    break;

                case "13":
                    Scanner scanner13 = new Scanner(System.in);
                    System.out.println("Please Enter Name Of The User");
                    String fullName = scanner13.nextLine();
                    System.out.println("Please Enter ID Number Of The User");
                    identityNumber = scanner13.nextLine();
                    System.out.println("Please Enter E-mail Of User");
                    String email = scanner13.nextLine();
                    System.out.println("Please Enter Password");
                    String password = scanner13.nextLine();

                    updatePatronInfo(fullName, identityNumber, email, password);

                    break;

                case "14":
                    Scanner scanner14 = new Scanner(System.in);
                    System.out.println("Please Enter User Name");
                    String patronName = scanner14.nextLine();

                    checkPatronEligibilityForCheckout(patronName);

                    break;

                case "15":
                    Scanner scanner15 = new Scanner(System.in);
                    System.out.println("Please Enter User ID Number");
                    patronId = scanner15.nextLine();

                    deleteUserInformation(patronId);

                    break;

                case "16":
                    displayUpdatedArrays();

                    break;

                case "17":
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
        if (quantity >= INDEX) {
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
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter the ISBN number of the book you want to view:");
        String isbn = scanner.nextLine();

        int index = getBookIndexByID(isbn);
        if (index != -1) {
            System.out.println("Book name: " + books[index][0]);
            System.out.println("Author name: " + books[index][1]);
            System.out.println("ISBN: " + books[index][2]);
            System.out.println("Number of pages: " + books[index][3]);
        } else {
            System.out.println("No book with the entered ISBN number was found!");
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
            ISBN += (int) (Math.random()-10);
        }
        System.out.println("Kitap talebiniz tarafımızca alındı!");
        System.out.println("Kitap Adı:" + title);
        System.out.println("Yazar Adı:" + author);
        System.out.println("Kitap Sayfa Sayısı:" + pageNumber); // todo: this hard-coded value should be replaced with the actual page number
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
            if (book[2].equals(bookISBN)) {
                isFound = true;

                LocalDate currentDate = LocalDate.now();
                DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                String formattedDate = currentDate.format(dateFormatter);

                transactions[transactionQuantity][0] = bookISBN;
                transactions[transactionQuantity][1] = identityNumber;
                transactions[transactionQuantity][2] = formattedDate;
                transactionQuantity++;

                truncateBooksArrayOnDeletion(bookISBN);
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
        Scanner get = new Scanner(System.in);

        System.out.println(" Choose searching criteria: ");
        System.out.println("1. Title ");
        System.out.println("2. Author ");
        System.out.println("3. ISBN");
        int choice = get.nextInt();

        System.out.print("Enter the word you are looking for: ");
        String searchWord = get.next();

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

