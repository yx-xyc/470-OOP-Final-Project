import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class UserAccount extends Account implements Cloneable{
	

	private ArrayList<UUID> flightIdList;
	
	public UserAccount(int id, String name, int age, String email, String username, String password, ArrayList<UUID> flightIdList) {
		super(id, name, age, email, username, password);
		flightIdList = new ArrayList<>(flightIdList);
	}
	
	public ArrayList<UUID> getFlightIdList() {
		return flightIdList;
	}

	public void setFlightIdList(ArrayList<UUID> flightIdList) {
		this.flightIdList = flightIdList;
	}

	public void addFlight(UUID id) {
		flightIdList.add(id);
	}
	
	public void deleteFlight(UUID id) {
		flightIdList.remove(id);

	}
	
	public UserAccount clone() throws CloneNotSupportedException{
        return (UserAccount) super.clone();
	}

	public String toCSVRow() {
		if (flightIdList.isEmpty()){
			return String.valueOf(id) + "," + name + "," + String.valueOf(age) + "," + email + "," + username + "," + password;
		}
		else {
			StringBuilder sb = new StringBuilder();
        	for (int i = 0; i < flightIdList.size(); i++) {
            	sb.append(flightIdList.get(i).toString());
            	if (i < flightIdList.size() - 1) {
                	sb.append(",");
            	}
        	}
        	return String.valueOf(id) + "," + name + "," + String.valueOf(age) + "," + email + "," + username + "," + password + "," + sb.toString();
		}
    }

    public static UserAccount fromCSVRow(String row) throws ParseException {
        String[] fields = row.split(",");
        int id = Integer.parseInt(fields[0]);
        String name = fields[1];
        int age = Integer.parseInt(fields[2]);
        String email = fields[3];
        String username = fields[4];
        String password = fields[5];
		ArrayList<UUID> flightList = new ArrayList<>();
		if (fields.length == 6){			
			return new UserAccount(id, name, age, email, username, password, flightList);
		}
		else{
			for (int i = 6; i < fields.length; i++) {
				flightList.add(UUID.fromString(fields[i]));
			}			 
        	return new UserAccount(id, name, age, email, username, password, flightList);
		}
    }

}
