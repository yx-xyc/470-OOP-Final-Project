import java.io.*;
import java.nio.file.*;
import java.text.ParseException;
import java.util.*;

public class UserDatabaseConnector {
    private Path filePath;
    public UserDatabaseConnector(String filePath) {
        this.filePath = Paths.get(filePath);
    }
    public List<UserAccount> readUsers() throws IOException, ParseException {
        List<UserAccount> users = new ArrayList<>();
        List<String> lines = Files.readAllLines(this.filePath);
        for (String line : lines) {
            UserAccount user = UserAccount.fromCSVRow(line);
            users.add(user);
        }
        return users;
    }
    public void writeUsers(List<UserAccount> users) throws IOException {
        StringBuilder stringBuilder = new StringBuilder();
        for (UserAccount user : users) {
            stringBuilder.append(user.toCSVRow()).append("\n");
        }
        Files.writeString(this.filePath, stringBuilder.toString());
    }
}

