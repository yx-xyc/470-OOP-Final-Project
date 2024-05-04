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
    public List<Flight> getFlightsByUUIDs(List<UUID> flightUUIDs) {
        List<Flight> flights = new ArrayList<>();
        for (Flight flight : this.flights.values()) {
            if (flightUUIDs.contains(flight.getFlightId())) {
                flights.add(flight);
            }
        }
        return flights;
    }
    public void bookFlight(UUID flightId) {
        Flight flight = this.flights.get(flightId);
        flight.setRemainingTicket(flight.getRemainingTicket() - 1);
    }
    public void cancelFlight(UUID flightId) {
        Flight flight = this.flights.get(flightId);
        flight.setRemainingTicket(flight.getRemainingTicket() + 1);
    }
    public void saveFlightRepository() throws IOException {
        databaseConnector.writeFlights(getAllFlights());
    }
}
