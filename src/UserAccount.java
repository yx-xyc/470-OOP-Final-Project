import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class UserAccount extends Account {
	
	private List<UUID> flightList;
	
	public UserAccount(String name, int age, String email, String username, String password) {
		super(name, age, email, username, password);
		flightList = new ArrayList<>();
	}

	public void setFlightList(List<UUID> flightList) {
		this.flightList = flightList;
	}

	public List<UUID> getFlightList() {
		return flightList;
	}

	public void addFlight(UUID flightUUID) {
		flightList.add(flightUUID);
	}

	public void cancelFlight(UUID flightUUID) {
		flightList.remove(flightUUID);
	}
	
	public String displayFlights() {
		String str = "";
		for (int i = 0; i < flightList.size()-1; i++) {
			str += ((flightList.get(i)).toString()+"\n"); // may need to change toString()
		}
		str += (flightList.get(flightList.size()-1)).toString();
		return str;
	}

}
