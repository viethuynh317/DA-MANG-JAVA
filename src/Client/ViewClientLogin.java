package Client;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import java.awt.event.*;
import java.awt.*;

public class ViewClientLogin extends JFrame implements ActionListener {
  /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
private static JTextField txtHostname;
  private static JTextField txtPort;
  private JTextField txtUsername;
  private JPasswordField txtPassword;
  private JButton btnLogin;
  private JButton btnConnect;

  public static JTextField messageConnect;
  public static JTextField messageLogin;

  ViewClientLogin() {
    super("CLIENT Login");

    initViewLogin();

  }

  public void initViewLogin() {
    this.setSize(600, 430);
    txtUsername = new JTextField(15);
    txtPassword = new JPasswordField(15);
    txtHostname = new JTextField();
    txtPort = new JTextField();
    btnLogin = new JButton("Login");
    btnConnect = new JButton("Connect");
    messageLogin = new JTextField(100);
    messageConnect = new JTextField(100);

    JLabel userLabel = new JLabel("Username: ");
    JLabel passLabel = new JLabel("Password: ");
    JLabel hostLabel = new JLabel("Host: ");
    JLabel portLabel = new JLabel("Port: ");
    JLabel titleLabel = new JLabel("CLIENT LOGIN");
    titleLabel.setFont(new Font("Serif", Font.BOLD, 17));
    // design loginFrame
    hostLabel.setBounds(50, 100, 100, 25);
    txtHostname.setBounds(120, 100, 160, 25);
    portLabel.setBounds(300, 100, 80, 25);
    txtPort.setBounds(370, 100, 50, 25);
    btnConnect.setBounds(430, 100, 90, 25);
    messageConnect.setBounds(50, 150, 470, 25);
    messageConnect.setEditable(false);

    userLabel.setBounds(50, 220, 100, 25);
    txtUsername.setBounds(120, 220, 160, 25);
    passLabel.setBounds(300, 220, 100, 25);
    txtPassword.setBounds(370, 220, 150, 25);
    btnLogin.setBounds(260, 330, 80, 25);
    titleLabel.setBounds(240, 20, 200, 25);
    messageLogin.setBounds(50, 280, 470, 25);
    messageLogin.setEditable(false);
    JPanel loginFrame = new JPanel();
    loginFrame.setLayout(new BorderLayout());
    loginFrame.add(titleLabel);
    loginFrame.add(hostLabel);
    loginFrame.add(txtHostname);
    loginFrame.add(portLabel);
    loginFrame.add(txtPort);
    loginFrame.add(btnConnect);
    loginFrame.add(messageConnect);

    loginFrame.add(userLabel);
    loginFrame.add(txtUsername);
    loginFrame.add(passLabel);
    loginFrame.add(txtPassword);
    loginFrame.add(messageLogin);
    loginFrame.add(btnLogin);
    loginFrame.add(new JLabel());
    this.setContentPane(loginFrame);
    this.setDefaultCloseOperation(3);
    this.setResizable(false);
  }

  public static void messageLogin(String message) {
    messageLogin.setText(message);
  }

  public static void messageConnect(String message) {
    messageConnect.setText(message);

  }

  @Override
  public void actionPerformed(ActionEvent e) {
    // TODO Auto-generated method stub

  }

  @SuppressWarnings("deprecation")
public User getUser() {
    return new User(txtUsername.getText(), txtPassword.getText());
  }

  public Address getAddress() {
    return new Address(txtHostname.getText(), txtPort.getText());
  }

  public void addLoginListener(ActionListener login) {
    btnLogin.addActionListener(login);
  }

  public void addConnectListener(ActionListener connect) {
    btnConnect.addActionListener(connect);
  }

  public static String hostName() {
    return txtHostname.getText();
  }

  public static String portNumber() {
    return txtPort.getText();
  }

}

