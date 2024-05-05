import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class AccountRepository {
    private UserDatabaseConnector userDatabaseConnector;
    private Map<Integer, UserAccount> userAccounts;
    public AccountRepository(String path) throws IOException, ParseException {
        this.userDatabaseConnector = new UserDatabaseConnector(path);
        this.userAccounts = new HashMap<>();
        List<UserAccount> userAccountList = this.userDatabaseConnector.readUsers();
        for (UserAccount ua : userAccountList) {
            this.userAccounts.putIfAbsent(ua.getId(), ua);
        }
    }

    public boolean login(String username, String password) {
        for (Integer id : userAccounts.keySet()) {
            UserAccount user = userAccounts.get(id);
            if (user.getUsername() == username && user.getPassword() == password) {
                return true;
            }
        }
        return false;
    }

    public void bookFlight(int userId, UUID flightId) throws CloneNotSupportedException{
        UserAccount user = (userAccounts.get(userId)).clone();
        ArrayList<UUID> flightIdList = user.getFlightIdList();
        flightIdList.add(flightId);
        user.setFlightIdList(flightIdList);
        this.userAccounts.put(userId, user);
    }
    
    public void cancelFlight(int userId, UUID flightId) throws CloneNotSupportedException{
        UserAccount user = (userAccounts.get(userId)).clone();
        ArrayList<UUID> flightIdList = user.getFlightIdList();
        flightIdList.remove(flightId);
        user.setFlightIdList(flightIdList);
        this.userAccounts.put(userId, user);
    }

    public List<UUID> getAllFlights(int userId) {
        UserAccount user = userAccounts.get(userId);
        ArrayList<UUID> flightIdList = user.getFlightIdList();
        return flightIdList;
    }

    public void updateDatabase() throws IOException{
        List<UserAccount> userList = new ArrayList<>(userAccounts.values());
        userDatabaseConnector.writeUsers(userList);
    }
}
