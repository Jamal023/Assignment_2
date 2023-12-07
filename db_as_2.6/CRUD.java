import java.sql.*;

public class CRUD {
    private Connection connection;

    public CRUD(String url, String user, String password) throws SQLException {
        this.connection = DriverManager.getConnection(url, user, password);
    }

    // Method to insert a new book
    public void insertBook(String title, int authorId, double price, int stockCount) throws SQLException {
        String sql = "INSERT INTO Books (Title, AuthorID, Price, StockCount) VALUES (?, ?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, title);
            statement.setInt(2, authorId);
            statement.setDouble(3, price);
            statement.setInt(4, stockCount);
            statement.executeUpdate();
        }
    }



    public void retrieveBooks() throws SQLException {
        String sql = "SELECT * FROM Books JOIN Authors ON Books.AuthorID = Authors.AuthorID";
        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(sql)) {
            while (resultSet.next()) {
               
                System.out.println("Book Title: " + resultSet.getString("Title"));
                System.out.println("AuthorID: " + resultSet.getString("AuthorID"));
                System.out.println("Price: " + resultSet.getString("Price"));
                 System.out.println("StockCount: " + resultSet.getString("StockCount"));
               
            }
        }
    }

   
    public void updateBook(int bookId, String newTitle, double newPrice) throws SQLException {
        String sql = "UPDATE Books SET Title = ?, Price = ? WHERE BookID = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, newTitle);
            statement.setDouble(2, newPrice);
            statement.setInt(3, bookId);
            statement.executeUpdate();
        }
    }

    
    public void deleteBook(int bookId) throws SQLException {
        String sql = "DELETE FROM Books WHERE BookID = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, bookId);
            statement.executeUpdate();
        }
    }

    public void checkOrder(int customerId, int bookId, int quantity, double price) throws SQLException {
        String checkStockSql = "SELECT StockCount FROM Books WHERE BookID = ?";
        String insertOrderSql = "INSERT INTO Orders (CustomerID, OrderDate, TotalAmount) VALUES (?, CURRENT_DATE, ?)";
        String updateBookSql = "UPDATE Books SET StockCount = StockCount - ? WHERE BookID = ?";

        try {
            connection.setAutoCommit(false);

            // Check stock availability
            try (PreparedStatement checkStockStmt = connection.prepareStatement(checkStockSql)) {
                checkStockStmt.setInt(1, bookId);
                ResultSet resultSet = checkStockStmt.executeQuery();
                if (resultSet.next()) {
                    int stockCount = resultSet.getInt("StockCount");
                    if (stockCount < quantity) {
                        throw new SQLException("Not enough stock available.");
                    }
                } else {
                    throw new SQLException("Book not found.");
                }
            }

            // Insert order
            try (PreparedStatement insertOrderStmt = connection.prepareStatement(insertOrderSql, Statement.RETURN_GENERATED_KEYS)) {
                insertOrderStmt.setInt(1, customerId);
                insertOrderStmt.setDouble(2, price * quantity);
                insertOrderStmt.executeUpdate();
            }

            // Update book stock
            try (PreparedStatement updateBookStmt = connection.prepareStatement(updateBookSql)) {
                updateBookStmt.setInt(1, quantity);
                updateBookStmt.setInt(2, bookId);
                updateBookStmt.executeUpdate();
            }

            connection.commit();
        } catch (SQLException e) {
            connection.rollback();
            throw e;
        } finally {
            connection.setAutoCommit(true);
        }
    }

    public void close() throws SQLException {
        if (connection != null && !connection.isClosed()) {
            connection.close();
        }
    }
}

    
    

