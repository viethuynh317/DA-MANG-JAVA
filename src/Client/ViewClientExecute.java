package Client;

import javax.swing.JFrame;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import java.awt.event.*;
import java.io.File;
import java.util.Vector;
import java.awt.*;

public class ViewClientExecute extends JFrame implements ActionListener {

  /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
private static Vector<String> listFile = new Vector<String>();
  private JFrame executeFrame;
  private JFrame loginFrame;

  private static JTextArea txtListFile;
  private JTextField txtFileDownLoad;
  private JFileChooser fileChooser;
  private JButton btnDownload;
  private JButton btnUpload;
  private JButton btnFileChoose;
  private JButton btnDisconnect;
  private JButton fileSystem;
  private JTextField txtFileChoose;
  private JTextField showHost;
  private JTextField showPort;
  private JScrollPane listFileServer;

  public ViewClientExecute(JFrame loginFrame) {
    super("FTP Client");
    ViewClientExecute.listFile = new Vector<>();
    this.loginFrame = loginFrame;

    initView();

  }

  private void initView() {
    executeFrame = new JFrame();
    executeFrame.setSize(700, 500);
    executeFrame.setResizable(false);

    // setting content

    txtListFile = new JTextArea(50, 20);
    listFileServer = new JScrollPane(txtListFile, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
        JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
    txtFileDownLoad = new JTextField(30);
    btnDownload = new JButton("Download");
    btnUpload = new JButton("Upload");
    txtFileChoose = new JTextField(30);
    showHost = new JTextField();
    showHost.setText("Host: " + ViewClientLogin.hostName());
    showPort = new JTextField();
    showPort.setText("Port: " + ViewClientLogin.portNumber());

    showHost.setEditable(false);
    showPort.setEditable(false);
    btnDisconnect = new JButton("Disconnect");
    fileSystem = new JButton("Show File");

    fileChooser = new JFileChooser();
    // fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
    btnFileChoose = new JButton("Choose File");
    btnFileChoose.addActionListener(new ActionListener() {

      @Override
      public void actionPerformed(ActionEvent e) {
        int returnVal = fileChooser.showOpenDialog(null);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
          File file = fileChooser.getSelectedFile();
          txtFileChoose.setText(file.getAbsolutePath());
        }
      }

    });

    JLabel downloadLabel = new JLabel("Download: ");
    downloadLabel.setBounds(360, 150, 80, 25);
    JLabel uploadLabel = new JLabel("Upload: ");
    uploadLabel.setBounds(360, 220, 80, 25);
    JLabel fileSystemLabel = new JLabel("File System on Server");
    fileSystemLabel.setBounds(90, 90, 200, 25);
    fileSystemLabel.setFont(new Font("Serif", Font.BOLD, 15));
    listFileServer.setBounds(20, 120, 300, 300);
    txtListFile.setEditable(false);
    txtFileDownLoad.setBounds(360, 180, 150, 25);
    btnDownload.setBounds(540, 180, 100, 23);
    txtFileChoose.setBounds(360, 250, 150, 25);
    btnFileChoose.setBounds(540, 250, 100, 23);
    btnUpload.setBounds(540, 280, 100, 23);

    showHost.setBounds(20, 20, 300, 25);
    showPort.setBounds(360, 20, 100, 25);

    btnDisconnect.setBounds(540, 20, 100, 25);
    btnDisconnect.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        executeFrame.setVisible(false);

        ViewClientLogin.messageConnect("");
        ViewClientLogin.messageLogin("");
        ClientControl.isConnect = false;
        loginFrame.setVisible(true);
        // viewLogin.messageConnect("");
        // viewLogin.messageLogin("");
      }
    });

    fileSystem.setBounds(540, 120, 100, 25);
    fileSystem.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {

        new PopupFileClient();
      }
    });
    // JPanel content = new JPanel();
    // content.setLayout(new BorderLayout());
    // content.add(fileChooser);
    executeFrame.add(listFileServer);
    executeFrame.add(downloadLabel);
    executeFrame.add(txtFileDownLoad);
    executeFrame.add(btnDownload);
    executeFrame.add(uploadLabel);
    executeFrame.add(txtFileChoose);
    executeFrame.add(btnFileChoose);
    executeFrame.add(btnUpload);
    executeFrame.add(showHost);
    executeFrame.add(showPort);
    executeFrame.add(btnDisconnect);
    executeFrame.add(fileSystemLabel);
    executeFrame.add(fileSystem);
    executeFrame.add(new JLabel());
    // content.setContentPane(content);
    executeFrame.add(new JPanel());
    executeFrame.setDefaultCloseOperation(3);
    executeFrame.setVisible(true);

  }

  public void updateFileServer(String[] list) {
    for (int i = 0; i < list.length; i++) {
      ViewClientExecute.listFile.add(list[i]);
    }

  }

  public void showListFile() {
    String temp = "";
    for (String i : ViewClientExecute.listFile) {
      temp += i + "\n";
    }
    // System.out.println(listFile.size());
    ViewClientExecute.txtListFile.setText(temp);
  }

  @Override
  public void actionPerformed(ActionEvent e) {
    // TODO Auto-generated method stub

  }

  public void addDownloadListener(ActionListener downloads) {
    btnDownload.addActionListener(downloads);
  }

  public void addUploadListener(ActionListener uploads) {
    btnUpload.addActionListener(uploads);
  }

  public String getFileDownload() {
    return txtFileDownLoad.getText();
  }

  public String getFileUpload() {
    return txtFileChoose.getText();
  }

  // public void resetFileServer() {
  // // for (int i = 0; i < listFile.size(); i++) {
  // // listFile.remove(i);
  // // }
  // // System.out.println(listFile.size());
  // this.listFile.clear();
  // }

  public void updateListFile(String filename) {
    listFile.add(filename);
    showListFile();
  }
}

