package julio.cardGame.cardGameServer.database.db;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import java.io.Closeable;
import java.sql.*;

public class DbConnection implements Closeable {
    private static DbConnection instance;

    private Connection connection;

//
    private static final HikariConfig config = new HikariConfig("src/main/resources/hikari.properties");
    private static final HikariDataSource ds = new HikariDataSource(config);

    public DbConnection() {
        /*try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            System.err.println("PostgreSQL JDBC driver not found");
            e.printStackTrace();
        }*/
    }

    /*public Connection connect(String database) throws SQLException {
        return DriverManager.getConnection("jdbc:postgresql://localhost:5432/" + database, "mtcgbackend", "mtcgBackendPwd");
    }*/

    /*public Connection connect() throws SQLException {
        //HikariConfig config = new HikariConfig()

        return connect(Constants.DB_NAME);
    }*/

    public Connection connect() throws SQLException {
        //HikariConfig config = new HikariConfig("src/main/resources/hikari.properties");
        //HikariDataSource ds = new HikariDataSource(config);

        return ds.getConnection();
                //connect(Constants.DB_NAME);
    }


    public Connection getConnection() {
        if( connection==null ) {
            try {
                connection = DbConnection.getInstance().connect();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
        return connection;
    }


    public PreparedStatement prepareStatement(String sql) throws SQLException {
        return getConnection().prepareStatement(sql);
    }

    /*public static boolean executeSql(Connection connection, String sql) throws SQLException {
        return executeSql(connection, sql, false);
    }*/


    @Override
    public void close() {
        if( connection!=null ) {
            try {
                connection.close();
            } catch (SQLException throwables) {
                System.out.println("HERE");
                throwables.printStackTrace();
            }
            connection = null;
        }
    }

    public static DbConnection getInstance() {
        if(instance==null)
            instance = new DbConnection();
        return instance;
    }
}
