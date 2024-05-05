import javax.swing.*;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

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

                        } else {
                            UserAccount userAccount = ar.getUserAccount(username, password);
                            FlightsView flightsView = new FlightsView(userAccount, fr);

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
}