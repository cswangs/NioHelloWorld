package com.luban.bio;

import java.io.IOException;
import java.net.Socket;
import java.util.Arrays;
import java.util.Scanner;

public class BioClient {
  public static void main(String[] args) {

    try {
      final Socket socket = new Socket("127.0.0.1", 1333);
      new Thread() {
        @Override
        public void run() {
          while (!socket.isClosed()) {
            try {
              byte[] b = new byte[2048];
              int read = socket.getInputStream().read(b);
              if (read > 0) {
                System.out.println(new String(Arrays.copyOf(b, read)));
              }
            } catch (Exception e) {
              System.out.println(e.getMessage());
              if ("Socket closed".equals(e.getMessage())) {
                break;
              }
              e.printStackTrace();
            }
          }
        }
      }.start();
      boolean stop = false;
      while (!stop) {
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNextLine()) {
          String s = scanner.nextLine();
          if (s.equals("exit")) {
            socket.close();
            stop = true;
            break;
          }
          socket.getOutputStream().write(s.getBytes());
        }
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
