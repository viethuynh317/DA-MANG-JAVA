package FTPCLient;

import java.net.DatagramPacket;
import java.net.DatagramSocket;

public class UpdateListFileServer extends Thread {
  private DatagramSocket datagramSocket;
  private DatagramPacket receivePacket;
  private ViewClientExecute viewExecute;
  private byte[] buf = new byte[1024];

  public UpdateListFileServer(DatagramSocket datagramSocket, ViewClientExecute viewExecute) {
    this.datagramSocket = datagramSocket;
    this.viewExecute = viewExecute;
    this.receivePacket = new DatagramPacket(buf, buf.length);
  }

  @Override
  public void run() {
    // TODO Auto-generated method stub
    super.run();
    while (true) {
      try {
        datagramSocket.receive(receivePacket);
        byte[] data = receivePacket.getData();

        if (data != null) {
          viewExecute.updateListFile(new String(data));
        }
      } catch (Exception e) {
        // TODO: handle exception
      }
    }
  }
}
