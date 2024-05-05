import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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
    private LoginCallback loginCallback;

    public LoginGUI(AccountRepository accountRepository, LoginCallback loginCallback) throws IOException, ParseException {
        this.accountRepository = accountRepository;
        this.loginCallback = loginCallback;

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
                    loginCallback.onLoginSuccess(username, password, true); // Notify success
                }
                else if (accountRepository.login(username, password)) {
                    // new user GUI
                    loginCallback.onLoginSuccess(username, password, false); // Notify success
                    JOptionPane.showMessageDialog(LoginGUI.this, "Login successful!");
                    dispose();
                } else {
                    messageLabel.setText("Wrong username or password");
                    passwordField.setText("");
                }
            }
        });
    }
}