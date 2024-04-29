import java.text.ParseException;
import java.util.*;

public class Flight {

    private static TimeConverter timeConverter = new TimeConverter();

    private UUID flightId;
    private long departureTime;
    private long arrivalTime;
    private String departureCity;
    private String arrivalCity;
    private int occupancy;
    private String arlineId;
    private int remainingTicket;

    public Flight(UUID flightId,
                  long departureTime,
                  long arrivalTime,
                  String departureCity,
                  String arrivalCity,
                  String arlineId,
                  int occupancy,
                  int remainingTicket) {
        this.flightId = flightId;
        this.departureTime = departureTime;
        this.arrivalTime = arrivalTime;
        this.departureCity = departureCity;
        this.arrivalCity = arrivalCity;
        this.arlineId = arlineId;
        this.occupancy = occupancy;
        this.remainingTicket = remainingTicket;
    }
    public Flight(long departureTime,
                  long arrivalTime,
                  String departureCity,
                  String arrivalCity,
                  String arlineId,
                  int occupancy,
                  int remainingTicket) {
        this.flightId = UUID.randomUUID();
        this.departureTime = departureTime;
        this.arrivalTime = arrivalTime;
        this.departureCity = departureCity;
        this.arrivalCity = arrivalCity;
        this.arlineId = arlineId;
        this.occupancy = occupancy;
        this.remainingTicket = remainingTicket;
    }

    public UUID getFlightId() {
        return flightId;
    }
    public long getDepartureTime() {
        return departureTime;
    }
    public void setDepartureTime(long departureTime) {
        this.departureTime = departureTime;
    }
    public long getArrivalTime() {
        return arrivalTime;
    }
    public void setArrivalTime(long arrivalTime) {
        this.arrivalTime = arrivalTime;
    }
    public String getDepartureCity() {
        return departureCity;
    }
    public void setDepartureCity(String departureCity) {
        this.departureCity = departureCity;
    }
    public String getArrivalCity() {
        return arrivalCity;
    }
    public void setArrivalCity(String arrivalCity) {
        this.arrivalCity = arrivalCity;
    }
    public int getOccupancy() {
        return occupancy;
    }
    public void setOccupancy(int occupancy) {
        this.occupancy = occupancy;
    }
    public String getArlineId() {
        return arlineId;
    }
    public void setArlineId(String arlineId) {
        this.arlineId = arlineId;
    }
    public int getRemainingTicket() {
        return remainingTicket;
    }
    public void setRemainingTicket(int remainingTicket) {
        this.remainingTicket = remainingTicket;
    }
    public String toCSVRow() {
        return flightId.toString() + "," + timeConverter.convertTimeToString(departureTime) + "," +
                timeConverter.convertTimeToString(arrivalTime) + "," +
                departureCity + "," + arrivalCity + "," + arlineId + "," +
                occupancy + "," + remainingTicket;
    }
    public static Flight fromCSVRow(String row) throws ParseException {
        String[] fields = row.split(",");
        UUID flightId = UUID.fromString(fields[0]);
        long departureTime = timeConverter.convertStringToTime(fields[1]);
        long arrivalTime = timeConverter.convertStringToTime(fields[2]);
        String departureCity = fields[3];
        String arrivalCity = fields[4];
        String arlineId = fields[5];
        int occupancy = Integer.parseInt(fields[6]);
        int remainingTicket = Integer.parseInt(fields[7]);
        return new Flight(
          flightId,
          departureTime,
          arrivalTime,
          departureCity,
          arrivalCity,
          arlineId,
          occupancy,
          remainingTicket
        );
    }
}