package viewPackage;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;

public class Main {

    public static void main(String[] args) {
        System.out.println("[Main] Starting Scout Unit Management...");
        System.out.println("[Main] Java version: " + System.getProperty("java.version"));
        System.out.println("[Main] Working directory: " + System.getProperty("user.dir"));

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            System.out.println("[Main] MySQL JDBC driver found.");
        } catch (ClassNotFoundException e) {
            System.err.println("[Main] *** MySQL JDBC driver NOT in classpath ***");
            System.err.println("[Main] Add mysql-connector-j to dependencies.");
            return;
        }

        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception ignored) {
            }
            try {
                System.out.println("[Main] Building MainFrame...");
                MainFrame frame = new MainFrame();
                System.out.println("[Main] MainFrame built. Showing window...");
                frame.setVisible(true);
                System.out.println("[Main] Window should now be visible.");
            } catch (Throwable t) {
                System.err.println("[Main] *** Error while creating MainFrame ***");
                t.printStackTrace();
            }
        });
    }
}
