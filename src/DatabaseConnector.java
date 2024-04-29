import java.io.*;
import java.nio.file.*;
import java.text.ParseException;
import java.util.*;

public class DatabaseConnector {
    private Path filePath;

    public DatabaseConnector(String filePath) {
        this.filePath = Paths.get(filePath);
    }

    public List<Flight> readFlights() throws IOException, ParseException {
        List<Flight> flights = new ArrayList<>();
        List<String> lines = Files.readAllLines(this.filePath);
        for (String line : lines) {
            Flight flight = Flight.fromCSVRow(line);
            flights.add(flight);
        }
        return flights;
    }

    public void writeFlights(List<Flight> flights) throws IOException {
        StringBuilder stringBuilder = new StringBuilder();
        for (Flight flight : flights) {
            stringBuilder.append(flight.toCSVRow());
        }
        Files.writeString(this.filePath, stringBuilder.toString());
    }

}
