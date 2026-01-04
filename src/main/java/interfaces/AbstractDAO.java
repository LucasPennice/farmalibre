package interfaces;

import java.sql.*;
import Utils.DbUtil;

public abstract class AbstractDAO {

    protected Connection connection;

    protected void startConnection() throws SQLException {
        connection = DbUtil.getConnection();
    }

    protected void closeConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
