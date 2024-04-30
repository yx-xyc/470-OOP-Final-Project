import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class FlightRepository {
    private DatabaseConnector databaseConnector;
    private Map<UUID, Flight> flights;
    public FlightRepository(String path) throws IOException, ParseException {
        this.databaseConnector = new DatabaseConnector(path);
        this.flights = new HashMap<>();
        List<Flight> flightList = this.databaseConnector.readFlights();
        for (Flight flight : flightList) {
            this.flights.putIfAbsent(flight.getFlightId(), flight);
        }
    }
    public void addFlight(Flight flight) {
        this.flights.putIfAbsent(flight.getFlightId(), flight);
    }
    public void removeFlight(Flight flight) {
        UUID uuidToRemove = flight.getFlightId();
        this.flights.remove(uuidToRemove);
    }
    public List<Flight> getAllFlights() {
        return new ArrayList<>(this.flights.values());
    }

}
