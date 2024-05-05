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
                LoginGUI loginGui = new LoginGUI(ar, fr);
                loginGui.setVisible(true);
            } catch (IOException e) {
                throw new RuntimeException(e);
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }
        });
    }
}