package Client;

import java.awt.event.*;

public class ClientControl {

  private ViewClientLogin viewLogin;
  private ViewClientExecute viewExecute;
  // private PopupFileClient viewPopup;
  public static boolean isConnect = false;
  private Client ftpClient;

  public void initViewExecute(String[] listfile) {
    viewExecute = new ViewClientExecute(viewLogin);
    viewExecute.updateFileServer(listfile);
    viewExecute.showListFile();
    viewExecute.addDownloadListener(new DownloadListener());
    viewExecute.addUploadListener(new UploadListener());
    viewLogin.setVisible(false);

    ftpClient.updateListFile(viewExecute);
  }

  public ClientControl(ViewClientLogin viewLogin) {
    this.viewLogin = viewLogin;
    this.viewLogin.addLoginListener(new LoginListener());
    this.viewLogin.addConnectListener(new ConnectListener());
  }

  public void connect(Address address) {
    try {

      if (address.hostname == null || address.port == null) {
        ViewClientLogin.messageConnect("Please, Enter address to connect (hostname and port)");
        return;
      }
      ftpClient = new Client(address.hostname, Integer.parseInt(address.port));
      if (ftpClient.ControlConnect()) {
        isConnect = true;
        ViewClientLogin.messageConnect("Connection Successed");
      } else {
        isConnect = false;
        ViewClientLogin.messageConnect("Connection Failed");
      }
    } catch (Exception e) {
      e.printStackTrace();
      ViewClientLogin.messageConnect(e.getStackTrace().toString().substring(0, 100));
    }

  }

  public void download(String filename) {
    ftpClient.DOWNLOAD(filename);
  }

  public void upload(String filename) {
    ftpClient.UPLOAD(filename);
  }

  // public void afterUpload() {

  // String[] listfile = this.client.LIST("");
  // viewExecute.resetFileServer();
  // viewExecute.ReLoadFileServer(listfile);
  // viewExecute.UpDateUI();

  // }

  class LoginListener implements ActionListener {

    @Override
    public void actionPerformed(ActionEvent e) {
      if (!isConnect) {
        ViewClientLogin.messageLogin("No Connection yet");
        return;
      }
      try {
        User user = viewLogin.getUser();
        if (!ftpClient.Login(user.getUsername().trim(), user.getPassword().trim())) {
          ViewClientLogin.messageLogin("Wrong username or password");
        } else {
          ViewClientLogin.messageLogin("Login Success");
          // gui nhan list file , excute here
          String[] listfile = ftpClient.LIST("");
          initViewExecute(listfile);
        }

      } catch (Exception e1) {
        ViewClientLogin.messageLogin(e1.getStackTrace().toString().substring(0, 100));
      }
    }

  }

  class ConnectListener implements ActionListener {

    @Override
    public void actionPerformed(ActionEvent e) {
      Address address = viewLogin.getAddress();

      connect(address);
    }

  }

  class DownloadListener implements ActionListener {

    @Override
    public void actionPerformed(ActionEvent e) {
      // todo
      String filename = viewExecute.getFileDownload();
      download(filename);
    }

  }

  class UploadListener implements ActionListener {

    @Override
    public void actionPerformed(ActionEvent e) {
      //

      String filename = viewExecute.getFileUpload();
      upload(filename);

      // afterUpload();

    }

  }

}

