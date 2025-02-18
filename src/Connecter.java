import java.sql.Connection;
import java.sql.DriverManager;

public class Connecter {
    private static final int MAX_RETRIES = 3;
    private static final int RETRY_DELAY_MS = 1000;

    public Connection obtenirconnexion() {
        Connection conn = null;
        int retries = 0;
        
        while (retries < MAX_RETRIES && conn == null) {
            try {
            	Class.forName("com.mysql.cj.jdbc.Driver");
                conn = DriverManager.getConnection(
                    "jdbc:mysql://localhost/mydatabase1",
                    "root",
                    "root"
                );
                
                if (conn != null && !conn.isClosed()) {
                    return conn;
                }
            } catch (Exception e) {
                retries++;
                if (retries < MAX_RETRIES) {
                    try {
                        Thread.sleep(RETRY_DELAY_MS);
                    } catch (InterruptedException ie) {
                        Thread.currentThread().interrupt();
                        break;
                    }
                }
            }
        }
        return conn;
    }
}