import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.text.ParseException;
interface LoginCallback {
    void onLoginSuccess(String username, String password, boolean isAdmin);
}
public class LoginGUI extends JFrame {
    private JTextField usernameField;
    private JTextField passwordField;
    private JButton loginButton;
    private JLabel messageLabel;
    private JLabel usernameLabel;
    private JLabel passwordLabel;

    private AccountRepository accountRepository;
    private FlightRepository flightRepository;
    private LoginCallback loginCallback;

    public LoginGUI(AccountRepository accountRepository, FlightRepository flightRepository) throws IOException, ParseException {
        this.accountRepository = accountRepository;
        this.flightRepository = flightRepository;

        setTitle("Login Page");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(null);

        usernameLabel = new JLabel("Username:");
        usernameLabel.setBounds(50, 50, 100, 30);
        passwordLabel = new JLabel("Password:");
        passwordLabel.setBounds(50, 100, 100, 30);
        usernameField = new JTextField();
        usernameField.setBounds(150, 50, 200, 30);
        passwordField = new JTextField();
        passwordField.setBounds(150, 100, 200, 30);
        loginButton = new JButton("Log in");
        loginButton.setBounds(150, 150, 100, 30);
        messageLabel = new JLabel();
        messageLabel.setBounds(150, 200, 200, 30);
        
        add(usernameLabel);
        add(usernameField);
        add(passwordLabel);
        add(passwordField);
        add(loginButton);
        add(messageLabel);

        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = usernameField.getText();
                String password = passwordField.getText();
                if (username.equals("admin") && password.equals("admin")) {
                    // new admin GUI
                    LoginGUI.openFileWithDefaultApplication("src/resources/flights.csv");
                    System.exit(0);
                    dispose();
                }
                else if (accountRepository.login(username, password)) {
                    // new user GUI
                    JOptionPane.showMessageDialog(LoginGUI.this, "Login successful!");
                    UserAccount userAccount = accountRepository.getUserAccount(username, password);
                    FlightsGUI flightsGUI = new FlightsGUI(userAccount.getId(), flightRepository, accountRepository);
                    dispose();
                } else {
                    messageLabel.setText("Wrong username or password");
                    passwordField.setText("");
                }
            }
        });
    }
    public static void openFileWithDefaultApplication(String filePath) {
        if (Desktop.isDesktopSupported()) {
            Desktop desktop = Desktop.getDesktop();
            try {
                File file = new File(filePath);
                desktop.open(file);
            } catch (IOException e) {
                JOptionPane.showMessageDialog(null, "Failed to open file: " + filePath, "Error", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(null, "Desktop is not supported on this platform.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}