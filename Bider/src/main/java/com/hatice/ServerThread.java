package com.hatice;

import com.google.gson.Gson;

import java.io.*;
import java.net.Socket;
import java.util.List;

public class ServerThread extends Thread {
    private BufferedReader in;
    private BufferedWriter out;
    RequestType type;

    public ServerThread(Socket socket) {
        try {
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        try {
        String data;
        while ((data = in.readLine()) != null) {
            Request request = new Gson().fromJson(data, Request.class);
            type = request.type;
            System.out.println("Got here");
            switch (request.type) {
                case GET_PRODUCTS:
                    List<Product> products = null;
                    try {
                        products = Server.getProducts();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    Response response = new Response();
                    response.products = products;
                    try {
                        sendToClient(new Gson().toJson(response));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    break;
                case GET_BIDS:
                    List<Bid> bids = null;
                    try {
                        bids = Server.getBids(request.product);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    Response responseBid = new Response();
                    responseBid.bids = bids;
                    try {
                        sendToClient(new Gson().toJson(responseBid));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    break;
                case CREATE_BID:
                    try {
                       List<Bid> newBids  = Server.createBid(request.bid);
                       sendToClient(new Gson().toJson(newBids));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    break;
                case LOGIN:
                    Boolean login = null;
                    try {
                        login = Server.login(request.user);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    Response responseLogin = new Response();
                    responseLogin.login = login ? 1 : 0;
                    try {
                        sendToClient(new Gson().toJson(responseLogin));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    break;
            }
        } System.out.println("Client connection closed " + type);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void sendToClient(String json) throws IOException {
        out.write(json);
        out.newLine();
        out.flush();
    }
}
