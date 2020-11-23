package FTPCLient;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import java.awt.event.*;
import java.io.File;
import java.awt.*;

public class PopupFileClient extends JFrame {
  private static JLabel titleLabel;
  private static JTextArea txtShowFile;
  private static JScrollPane wrapText;
  private static JFrame popupFile;

  public PopupFileClient() {
    viewFileClientPopup();
  }

  public static void viewFileClientPopup() {
    String listFile = "";
    File file = new File("DATA");
    String[] fileSystem = file.list();
    for (int i = 0; i < fileSystem.length; i++) {
      listFile += fileSystem[i] + "\n";
    }
    popupFile = new JFrame();
    popupFile.setSize(400, 400);
    popupFile.setLocation(300, 150);
    popupFile.setTitle("File System");
    titleLabel = new JLabel("File System");
    titleLabel.setBounds(150, 10, 100, 25);
    titleLabel.setFont(new Font("Serif", Font.BOLD, 17));

    txtShowFile = new JTextArea(100, 100);

    txtShowFile.setEditable(false);
    wrapText = new JScrollPane(txtShowFile, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
        JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
    wrapText.setBounds(5, 50, 375, 300);
    txtShowFile.setText(listFile);
    popupFile.add(titleLabel);
    popupFile.add(wrapText);
    // popupFile.setDefaultCloseOperation(3);
    popupFile.add(new JPanel());
    popupFile.setVisible(true);
  }

  // public static void showFileSystem() {
  // popupFile.setVisible(true);
  // }

}
