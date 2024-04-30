import java.util.ArrayList;

public class UserAccount extends Account {
	
	private ArrayList<Flight> flightList;
	
	public UserAccount(String name, int age, String email, String username, String password) {
		super(name, age, email, username, password);
		flightList = new ArrayList<>();
	}
	
	public ArrayList<Flight> getTicketList() {
		return flightList;
	}

	public void setTicketList(ArrayList<Flight> ticketList) {
		this.flightList = ticketList;
	}

	public void addTicket(Flight t) {
		flightList.add(t);
	}
	
	public void deleteTicket(Flight t) {
		flightList.remove(t);
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
