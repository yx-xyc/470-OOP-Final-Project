import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Main {
    public static void main(String[] args) throws IOException, ParseException {
        FlightRepository fr = new FlightRepository("src/resources/flights.csv");
        List<Flight> flights = fr.getAllFlights();
        List<UUID> bookedFlightUUIDs = new ArrayList<>();
        bookedFlightUUIDs.add(UUID.fromString("91d9e1a5-747a-4fca-bcd0-3dcb0bd1e81a"));
        bookedFlightUUIDs.add(UUID.fromString("6a909ffc-6fff-402f-8ee0-408543d45baf"));
        UserAccount userAccount = new UserAccount(
                "Vincent",
                24,
                "yx2021@nyu.edu",
                "test_name",
                "test_password"
        );
        userAccount.setFlightList(bookedFlightUUIDs);
        FlightsView flightsView = new FlightsView(flights, userAccount, fr);

    }
}