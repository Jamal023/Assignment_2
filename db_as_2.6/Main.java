import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        CRUD crud = null;
        metadata metadataAccessor = null;
        String url = "jdbc:postgresql://localhost:5432/Assignment_2";
        String user = "postgres";
        String password = "2305";

        try {
            Class.forName("org.postgresql.Driver");
            Connection connection = DriverManager.getConnection(url, user, password);
            System.out.println("Connected to the database successfully!");

            boolean exit = false;
            while (!exit) {
                System.out.println("\nMenu:");
                System.out.println("1. Check Order");
                System.out.println("2. Insert Book");
                System.out.println("3. Retrieve Books");
                System.out.println("4. Update Book");
                System.out.println("5. Delete Book");
                System.out.println("6. Display Tables Info");
                System.out.println("7. Display Primary Key Info");
                System.out.println("8. Display Foreign Key Info");
                System.out.println("9. Exit");
                System.out.print("Enter your choice: ");
                int choice = scanner.nextInt();

                switch (choice) {
                    case 1:
                        if (crud == null) {
                            crud = new CRUD(url, user, password);
                        }
                        crud.checkOrder(1, 1, 1, 1);
                        break;
                    case 2:
                        if (crud == null) {
                            crud = new CRUD(url, user, password);
                        }
                        scanner.nextLine(); // Consume newline character
                        System.out.println("Enter Book Title:");
                        String title = scanner.nextLine();
                        System.out.println("Enter Author ID:");
                        int authorID = scanner.nextInt();
                        System.out.println("Enter Price:");
                        double price = scanner.nextDouble();
                        System.out.println("Enter Stock Count:");
                        int stockCount = scanner.nextInt();

                        crud.insertBook(title, authorID, price, stockCount);
                        break;
                    case 3:
                        if (crud == null) {
                            crud = new CRUD(url, user, password);
                        }
                        crud.retrieveBooks();
                        break;
                    case 4:
                        if (crud == null) {
                            crud = new CRUD(url, user, password);
                        }
                        crud.updateBook(1, "Updated Example Book", 22.99);
                        break;
                    case 5:
                        if (crud == null) {
                            crud = new CRUD(url, user, password);
                        }
                        crud.deleteBook(2);
                        break;
                    case 6:
                        if (metadataAccessor == null) {
                            metadataAccessor = new metadata(url, user, password);
                        }
                        metadataAccessor.displayTablesInfo();
                        break;
                    case 7:
                        if (metadataAccessor == null) {
                            metadataAccessor = new metadata(url, user, password);
                        }
                        metadataAccessor.displayPrimaryKeyInfo("Books");
                        break;
                    case 8:
                        if (metadataAccessor == null) {
                            metadataAccessor = new metadata(url, user, password);
                        }
                        metadataAccessor.displayForeignKeyInfo("Orders");
                        break;
                    case 9:
                        exit = true;
                        break;
                    default:
                        System.out.println("Invalid choice! Please enter a valid option.");
                        break;
                }
            }

            connection.close();

        } catch (ClassNotFoundException e) {
            System.out.println("PostgreSQL JDBC Driver not found!");
            e.printStackTrace();
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
            e.printStackTrace();
        } finally {
            scanner.close();
        }
    }
}