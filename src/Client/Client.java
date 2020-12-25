package Client;

import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.Socket;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.Vector;

public class Client {
  private enum transferType {
    ASCII, BINARY
  }

  private static String directory = "DATA/"; // file transfed UPLOAD in current directory//data
  private static String[] sholdASCII = { "txt", "html", "htm", "cgi", "pl", "php", "cf", "svg", "asp", "rtf", "ps",
      "java", "py", "c", "cpp", "js", "css", "doc", "docx" };
  // transfer type

  // for control connection
  private Socket controlConnection;
  private PrintWriter controlWriter;
  private BufferedReader controlReader;
  private int controlPort;
  private String serverAddress;

  // for data Connection
  private Socket dataConnection;

  private static DatagramSocket datagramSocket;

  private transferType transferMode = transferType.ASCII;

  public Client(String serverAddress, int controlPort) {
    this.serverAddress = serverAddress;
    this.controlPort = controlPort;
  }

  public boolean ControlConnect() {
    try {
      this.controlConnection = new Socket(InetAddress.getByName(this.serverAddress), this.controlPort);
      if (!this.controlConnection.isConnected()) {
        return false;
      }
      this.controlWriter = new PrintWriter(controlConnection.getOutputStream(), true);
      this.controlReader = new BufferedReader(new InputStreamReader(controlConnection.getInputStream()));
      return true;

    } catch (Exception e) {
      e.printStackTrace();
      logDebug("Could connect to serverFTP.");
      return false;
    }
  }

  public boolean Login(String user, String password) {
    if (!isControlConnection()) {
      return false;
    } else {
      try {
        if (user != null) {
          boolean flag = false;
          // long prev = new Date().getTime();
          // send username
          controlWriter.println("USER " + user);
          while (!flag) {
            String response = controlReader.readLine().trim();
            logDebug(response);
            String[] splitResponse = response.split(" ");
            if (splitResponse[0].equals("531")) {
              return false;
            } else if (splitResponse[0].equals("530")) {
              return true;
            } else {
              logDebug("User OK.");
              flag = true;
            }
          }

          if (password != null && flag) {
            controlWriter.println("PASS " + password);
            while (true) {
              String response = controlReader.readLine();
              logDebug(response);
              String[] splitResponse = response.split(" ");
              if (splitResponse[0].equals("531")) {
                return false;
              } else {
                return true;
              }
            }

            // logDebug("Stoped wait for server response.");
            // return false;
          } else {
            logDebug("Can not Login.");
            return false;
          }
        } else {
          logDebug("Can not Login server.");
          return false;

        }
      } catch (Exception e) {
        e.printStackTrace();
        logDebug("Can not Login server.");
      }
      return false;
    }
  }

  public String[] LIST(String childFolder) {
    String[] listFile = null;
    if (!isControlConnection()) {
      return listFile;
    }
    try {
      openPassive();
      if (!isDataConnection()) {
        return listFile;
      }
      transferDatatype("");
      setDataType();
      controlWriter.println("LIST");
      BufferedReader rin = new BufferedReader(new InputStreamReader(this.dataConnection.getInputStream()));
      String line;
      Vector<String> lis = new Vector<>();
      while ((line = rin.readLine()) != null) {
        logDebug(line);
        lis.add(line);
      }

      listFile = new String[lis.size()];
      for (int i = 0; i < lis.size(); i++) {
        listFile[i] = lis.get(i);
      }
      closeDataConnection();
      return listFile;
    } catch (Exception e) {
      e.printStackTrace();
      return listFile;
    }
  }

  private boolean isControlConnection() {
    if (this.controlConnection == null || this.controlWriter == null || this.controlReader == null) {
      logDebug("Control connection is not OK.");
      return false;
    } else {
      return true;
    }
  }

  private boolean isDataConnection() {
    if (this.dataConnection == null) {
      return false;
    } else {
      return true;
    }
  }

  private void closeDataConnection() {
    try {
      if (this.dataConnection == null) {
        return;
      }
      this.dataConnection.close();

      this.dataConnection = null;
      logDebug("Data connection is close.");
    } catch (Exception e) {
      e.printStackTrace();
      logDebug("Can not close data connection.");
    }
  }

  private boolean transferDatatype(String filename) {
    if (filename == null) {
      return false;
    } else {
      int index = filename.lastIndexOf(".");
      String typeFile = filename.substring(index + 1, filename.length()).toLowerCase();
      this.transferMode = transferType.BINARY;

      for (String type : Client.sholdASCII) {
        if (typeFile.equals(type)) {
          this.transferMode = transferType.ASCII;
          return true;
        }
      }
      return false;
    }
  }

  private boolean setDataType() {
    try {
      if (this.transferMode == transferType.ASCII) {
        controlWriter.println("TYPE A");
      } else {
        controlWriter.println("TYPE I");
      }
      logDebug("set type: " + this.transferMode);

      String resType = controlReader.readLine().split(" ")[0];
      if (resType.equals("200")) {
        return true;
      }
      return false;
    } catch (Exception e) {
      e.printStackTrace();
      logDebug("Could request datatype to server.");
      return false;
    }
  }

  private void openPassive() {
    try {
      if (!isControlConnection()) {
        logDebug("Could not open PASV MODE.");
        return;
      }
      controlWriter.println("PASV");
      boolean flag = false;
      while (!flag) {
        String response = controlReader.readLine().trim();
        String[] resPasv = response.split(" ");
        if (resPasv[0].equals("227")) {
          String[] prPort = response.split(",");
          int dtPort = Integer.parseInt(prPort[prPort.length - 1]) + 256 * Integer.parseInt(prPort[prPort.length - 2]);
          logDebug(dtPort + "");
          this.openDataConnectionPasv(dtPort);
          if (datagramSocket == null) {
            datagramSocket = new DatagramSocket(dtPort);
          }

          flag = true;
        }
      }
    } catch (Exception e) {
      e.printStackTrace();
      logDebug("Could not open PASV MODE.");
    }
  }

  private void openDataConnectionPasv(int port) {
    try {
      dataConnection = new Socket(this.serverAddress, port);
      logDebug("Open data connection passive mode for client.");
    } catch (Exception e) {
      e.printStackTrace();
      logDebug("Could not open data connection pasv mode.");
    }
  }

  public void UPLOAD(String prevfile) {
    int index = prevfile.lastIndexOf("\\");
    String filename = null;
    if (index == -1) {
      filename = new String(prevfile);
    } else {
      filename = prevfile.substring(index + 1, prevfile.length());
    }
    // got filenam
    transferDatatype(filename);
    setDataType();
    openPassive();
    try {
      File file = new File(prevfile);
      if (!file.exists() || !isDataConnection()) {
        return;
      }
      controlWriter.println("STOR " + filename);
      String resStor = controlReader.readLine();
      logDebug(resStor);
      if (resStor.split(" ")[0].equals("150") && isDataConnection()) {
        logDebug("Starting transfer file " + filename);
        if (this.transferMode == transferType.BINARY) {
          BufferedInputStream bis = new BufferedInputStream(new FileInputStream(file));
          BufferedOutputStream bos = new BufferedOutputStream(dataConnection.getOutputStream());
          readDataBinary uploadBinary = new readDataBinary(bis, bos);
          uploadBinary.start();
          uploadBinary.join();

          bis.close();
          bos.close();

        } else {
          BufferedReader rin = new BufferedReader(new FileReader(file));
          PrintWriter rout = new PrintWriter(dataConnection.getOutputStream());
          readDataASCII uploadASCII = new readDataASCII(rin, rout);

          uploadASCII.start();
          uploadASCII.join();

          rin.close();
          rout.close();
        }
        logDebug("Finished send file " + filename);
      }
    } catch (Exception e) {
      e.printStackTrace();
      logDebug("Could not UPLOAD file" + filename);
    }
    closeDataConnection();
  }

  private void logDebug(String msg) {
    if (msg != "") {
      System.out.println(msg);
    }
  }

  public void DOWNLOAD(String filename) {
    File file = new File(directory + filename);

    if (file.exists()) {
      return;
    }
    // send PASV, receive response and open dataconnection on port
    if (!isControlConnection()) {
      return;
    }
    openPassive();
    transferDatatype(filename);
    if (!setDataType()) {
      logDebug("Could not set datatype transfer for the server.");
    }
    if (isDataConnection()) {
      try {
        controlWriter.println("RETR " + filename);
        String response = controlReader.readLine();
        logDebug(response);
        if (response.split(" ")[0].equals("150")) {
          if (transferMode == transferType.ASCII) {
            BufferedReader rin = new BufferedReader(new InputStreamReader(dataConnection.getInputStream()));
            PrintWriter rout = new PrintWriter(new FileOutputStream(file), true);
            readDataASCII downloadASCII = new readDataASCII(rin, rout);
            downloadASCII.start();
            downloadASCII.join();

            rin.close();
            rout.close();
          } else {
            BufferedInputStream fin = new BufferedInputStream(dataConnection.getInputStream());
            BufferedOutputStream fout = new BufferedOutputStream(new FileOutputStream(file));

            readDataBinary downloadBinary = new readDataBinary(fin, fout);
            downloadBinary.start();
            downloadBinary.join();

            fin.close();
            fout.close();
          }
          logDebug("Finished receive file " + filename);
        }
      } catch (Exception e) {
        e.printStackTrace();
        logDebug("Could not download file from server.");
      }
    } else {
      return;
    }
    closeDataConnection();
  }

  public void updateListFile(ViewClientExecute viewExecute) {
    new UpdateListFileServer(datagramSocket, viewExecute).start();

  }

  class readDataBinary extends Thread {
    private BufferedInputStream is;
    private BufferedOutputStream os;

    public readDataBinary(BufferedInputStream is, BufferedOutputStream os) {
      this.is = is;
      this.os = os;
    }

    @Override
    public void run() {
      byte[] buf = new byte[1024];
      int i;
      try {
        while ((i = is.read(buf, 0, 1024)) != -1) {
          os.write(buf, 0, i);
        }
        os.flush();
        // close data stream
        is.close();
        os.close();
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
  }

  // class support send and receive file from/to SERVER
  class readDataASCII extends Thread {
    private BufferedReader bfr;
    private PrintWriter prw;

    public readDataASCII(BufferedReader bfr, PrintWriter prw) {
      this.bfr = bfr;
      this.prw = prw;
    }

    @Override
    public void run() {
      String s;
      try {
        while ((s = bfr.readLine()) != null) {
          prw.println(s);
          // System.out.println(s);
        }
        prw.flush();
        prw.close();
        bfr.close();
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
  }
}
