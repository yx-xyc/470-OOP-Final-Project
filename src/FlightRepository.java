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
    public void deleteFlight(UUID flightId) {
        this.flights.remove(flightId);
    }
    public List<Flight> getAllFlights() {
        return new ArrayList<>(this.flights.values());
    }
    public List<Flight> getFlightsByIds(List<UUID> flightIds) {
        List<Flight> flights = new ArrayList<>();
        for (UUID flightId : flightIds) {
            Flight flight = this.flights.get(flightId);
            if (flight != null) {
                flights.add(flight);
            }
        }
        return flights;
    }
    public void close() throws IOException {
        this.databaseConnector.writeFlights(new ArrayList<>(this.flights.values()));
    }
}
