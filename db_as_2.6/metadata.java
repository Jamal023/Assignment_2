import java.sql.*;

public class metadata {
    private Connection connection;

    public metadata(String url, String user, String password) throws SQLException {
        this.connection = DriverManager.getConnection(url, user, password);
    }

    public void displayTablesInfo() throws SQLException {
        DatabaseMetaData metaData = connection.getMetaData();
        ResultSet tables = metaData.getTables(null, null, "%", new String[]{"TABLE"});
        while (tables.next()) {
            String tableName = tables.getString("TABLE_NAME");
            System.out.println("Table Name: " + tableName);

            // Display columns for each table
            ResultSet columns = metaData.getColumns(null, null, tableName, "%");
            while (columns.next()) {
                System.out.println("   Column: " + columns.getString("COLUMN_NAME") +
                                   ", Type: " + columns.getString("TYPE_NAME") +
                                   ", Size: " + columns.getInt("COLUMN_SIZE"));
            }
        }
    }

    public void displayPrimaryKeyInfo(String tableName) throws SQLException {
        DatabaseMetaData metaData = connection.getMetaData();
        ResultSet primaryKeys = metaData.getPrimaryKeys(null, null, tableName);
        while (primaryKeys.next()) {
            System.out.println("Primary Key: " + primaryKeys.getString("COLUMN_NAME"));
        }
    }

    public void displayForeignKeyInfo(String tableName) throws SQLException {
        DatabaseMetaData metaData = connection.getMetaData();
        ResultSet foreignKeys = metaData.getImportedKeys(null, null, tableName);
        while (foreignKeys.next()) {
            System.out.println("Foreign Key: " + foreignKeys.getString("FKCOLUMN_NAME") +
                               ", References: " + foreignKeys.getString("PKTABLE_NAME") +
                               "." + foreignKeys.getString("PKCOLUMN_NAME"));
        }
    }

    public void close() throws SQLException {
        if (connection != null && !connection.isClosed()) {
            connection.close();
        }
    }
}
