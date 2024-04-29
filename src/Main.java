import java.io.IOException;
import java.text.ParseException;
import java.util.List;

public class Main {
    public static void main(String[] args) throws IOException, ParseException {
        FlightRepository fr = new FlightRepository("src/resources/flights.csv");
        List<Flight> flights = fr.getAllFlights();
        FlightsView flightsView = new FlightsView(flights);

    }
}