package julio.cardGame.cardGameServer;

import julio.cardGame.cardGameServer.http.HttpServer;

import java.io.Closeable;
import java.io.IOException;
import java.sql.*;

public class Main {
    public static void main(String[] args) {

        System.out.println("Card game server started...");

        HttpServer httpServer = new HttpServer();

        httpServer.start();

        /*try {
            Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/postgres", "mtcgbackend", "mtcgBackendPwd");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }*/

    }
}