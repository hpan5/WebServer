import java.net.*;
import java.io.*;
import java.util.Scanner;
import java.nio.file.*;

class MyServer {
  //directories/dir
  //prevention from crashing
  //method: close socket multithread clients & timeout
  //directoreis listing on the pages
  //password protect webpage using authorization header
  //advanced: https support
  private static void callNewPage(String s, OutputStream outputStream) throws IOException {
    String pageName = s.split(" ")[1];
    if (pageName.equals("/"))  {
      pageName = "index";
    } else {
      pageName = pageName.substring(1);
    }

    String fileName = pageName.equals("favicon.ico") ? pageName : pageName + ".html";
    byte[] byteArr;
    String header = "HTTP/1.1 200 OK\n\n";
    try {
      byteArr = Files.readAllBytes(Paths.get(fileName));
    } catch (IOException ex) {
      header = "HTTP/1.1 404\n\n";
      byteArr = Files.readAllBytes(Paths.get("notfound.html"));
    }

    outputStream.write(header.getBytes());
    outputStream.write(byteArr);
  }


  public static void main(String[] args) throws IOException {
//HTTP/1.1 200 OK
    ServerSocket serverSocket = new ServerSocket(4040);
    //Myserver server = new Myserver();
    while (true) {
      Socket clientSocket = serverSocket.accept();
      InputStream inputStream = clientSocket.getInputStream();
      BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
      OutputStream outputStream = clientSocket.getOutputStream();


      String readString = bufferedReader.readLine();
      if (readString == null) continue;
      System.out.println("Receiving: " + readString);
      try{
        callNewPage(readString, outputStream);
      } catch (IOException ex) {
        callNewPage(" /notfound", outputStream);
      }

      if (!clientSocket.isClosed()) {
        clientSocket.close();
      }

    }

      //outputStream.write(("Received string " + readString + "\n").getBytes());
      //System.out.println("Receiving: " + readString);
    //}



    //serverSocket.close();
  }
}
