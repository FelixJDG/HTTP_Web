package com.example.EchoServer;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class Main {

    public static void main(String[] args) throws IOException {
        HashMap<String,List<String>> Headers = new HashMap<>();
        List<String> TextHtml = new ArrayList<>();
        List<String> ImagePng = new ArrayList<>();
        List<String> ImageXicon = new ArrayList<>();
        List<String> ImageJpg = new ArrayList<>();
        TextHtml.add(0,"text/html");
        TextHtml.add(1,"text");
        Headers.put("html",TextHtml);
        ImagePng.add(0,"image/png");
        ImagePng.add(1,"binary");
        Headers.put("png",ImagePng);
        ImageXicon.add(0,"image/x-icon");
        ImageXicon.add(1,"binary");
        Headers.put("ico",ImageXicon);
        ImageJpg.add(0,"image/jpg");
        ImageJpg.add(1,"binary");
        Headers.put("jpg",ImageJpg);
        File webform = new File("C:\\Users\\fooft\\OneDrive\\Documents\\Chess-AI-CW\\Website\\Homepage.html");
        // All ContentTypes - https://stackoverflow.com/questions/23714383/what-are-all-the-possible-values-for-http-content-type-header
        String[] contentType = {".","html"};
        String[] noSlash = {""};
        try {
            BufferedReader in = null;
            PrintWriter out = null;
            int a = 0;
            int portNumber = 80;
            ServerSocket serverSocket = new ServerSocket(portNumber);
            while (true) {
                Socket clientSocket = serverSocket.accept();
                out = new PrintWriter(clientSocket.getOutputStream(), true);
                in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                String line;
                if(a == 0) {
                    for (int b = 0; b < 20; b++) {
                        System.out.println(in.readLine());
                    }
                    a++;
                }
                int i=0;
                while((line = in.readLine()) != null){
                    System.out.println(line);
                    if(i==0) {
                        String[] splitLine = line.split(" ", 3);
                        if(splitLine[1].equals("/")){
                            webform = new File("C:\\Users\\fooft\\OneDrive\\Documents\\Chess-AI-CW\\Website\\Homepage.html");
                        }
                        else{
                            noSlash = splitLine[1].split("/",2);
                            contentType = noSlash[1].split("[.]",2);
                            webform = new File("C:\\Users\\fooft\\OneDrive\\Documents\\Chess-AI-CW\\Website\\" + noSlash[1]);
                        }
                    }
                    i++;
                    if (line.isBlank()) {
                        break;
                    }
                }
                InputStream inputStream = new FileInputStream(webform);
                //OutputStream outputStream = new FileOutputStream();
                FileReader fr = new FileReader(webform);
                int j;
                out.print("HTTP/1.x 200 OK\n");
                if((Headers.get(contentType[1]).get(1)).equals("text")){
                    out.print("Content-Type: " + Headers.get(contentType[1]).get(0) + "; charset=UTF-8\n");
                    out.print("\n\n");
                    while ((j = fr.read()) != -1) {
                        out.print((char) j);
                    }
                } else if((Headers.get(contentType[1]).get(1)).equals("binary")){
                    System.out.println(Headers.get(contentType[1]).get(0) + " - File");
                    byte[] bytes = Files.readAllBytes(Paths.get("C:\\Users\\fooft\\OneDrive\\Documents\\Chess-AI-CW\\Website\\" + noSlash[1]));
                    out.print("Content-Type: " + Headers.get(contentType[1]).get(0) + "\n");
                    long length = Files.size(Paths.get("C:\\Users\\fooft\\OneDrive\\Documents\\Chess-AI-CW\\Website\\" + noSlash[1]));
                    out.print("Content-Length: " + length + "\n)");
                    out.print("\n\n");
                    out.print(bytes);
                }
                out.flush();
                out.close();
                clientSocket.close();
            }
        }
        catch(Exception e){
            System.out.println(e);
        }
    }
}
