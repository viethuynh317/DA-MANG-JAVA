package FTPCLient;

public class Main {
	  public static void main(String[] args) {
	    ViewClientLogin viewLogin = new ViewClientLogin();
	    new ClientControl(viewLogin);
	    viewLogin.setVisible(true);
	  }
	}
