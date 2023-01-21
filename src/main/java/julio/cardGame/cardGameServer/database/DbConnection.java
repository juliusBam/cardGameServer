package julio.cardGame.cardGameServer.database;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import julio.cardGame.cardGameServer.Main;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Closeable;
import java.sql.*;

public class DbConnection implements Closeable {
    private static DbConnection instance;

    private Connection connection;

    private static final HikariConfig config = new HikariConfig("src/main/resources/hikari.properties");
    private static final HikariDataSource ds = new HikariDataSource(config);

    public Connection connect() throws SQLException {
        Logger logger = LoggerFactory.getLogger(DbConnection.class);
        return ds.getConnection();

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
