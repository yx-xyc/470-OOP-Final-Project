import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.text.ParseException;

public class Main {
    public static void main(String[] args) throws IOException, ParseException {
        FlightRepository fr = new FlightRepository("src/resources/flights.csv");
        AccountRepository ar = new AccountRepository("src/resources/users.csv");
        SwingUtilities.invokeLater(() -> {
            try {
                LoginGUI loginGui = new LoginGUI(ar, new LoginCallback() {
                    @Override
                    public void onLoginSuccess(String username, String password, boolean isAdmin) {
                        if (isAdmin) {
                            openFileWithDefaultApplication("src/resources/flights.csv");
                            System.exit(0);
                        } else {
                            UserAccount userAccount = ar.getUserAccount(username, password);
                            FlightsGUI flightsGUI = new FlightsGUI(userAccount.getId(), fr, ar);
                        }
                    }
                });
                loginGui.setVisible(true);
            } catch (IOException e) {
                throw new RuntimeException(e);
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }
        });
    }
    private static void openFileWithDefaultApplication(String filePath) {
        if (Desktop.isDesktopSupported()) {
            Desktop desktop = Desktop.getDesktop();
            try {
                File file = new File(filePath);
                desktop.open(file);
            } catch (IOException e) {
                JOptionPane.showMessageDialog(null, "Failed to open file: " + filePath, "Error", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(null, "Desktop is not supported on this platform.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}