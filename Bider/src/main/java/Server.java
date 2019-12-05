import com.google.gson.Gson;
import sun.jvm.hotspot.memory.SystemDictionary;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;

public class Server {

    private static String usersFile = "users.json";
    private static String bidsFile = "bids.json";
    private static String productsFile = "products.json";

    private static BinarySemaphore usersFileMutex = new BinarySemaphore(true);
    private static BinarySemaphore bidsFileMutex = new BinarySemaphore(true);
    private static BinarySemaphore productsFileMutex = new BinarySemaphore(true);

    private static List<ServerThread> threads = new ArrayList<ServerThread>();
    private static BinarySemaphore threadsMutex = new BinarySemaphore(true);

    public static void main(String[] args) {
        try {
            ServerSocket server = new ServerSocket(2909);
            while (true) {
                Socket socket = server.accept();
                ServerThread thread = new ServerThread(socket);
                thread.start();

                threadsMutex.P();
                threads.add(thread);
                threadsMutex.V();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static boolean login(User user) throws IOException {
        usersFileMutex.P();
        BufferedReader reader = new BufferedReader(new FileReader(usersFile));
        User[] users = new Gson().fromJson(reader, User[].class);
        reader.close();
        usersFileMutex.V();

        for (int i = 0; i < users.length; i++) {
            if (users[i].username.equals(user.username) && users[i].password.equals(user.password))
                return true;
        }
        return false;
    }

    public static List<Bid> createBid(Bid bid) throws IOException {
        bidsFileMutex.P();
        BufferedReader reader = new BufferedReader(new FileReader(bidsFile));
        Bid[] bids = new Gson().fromJson(reader, Bid[].class);
        reader.close();

        List<Bid> productBids = new ArrayList<Bid>(Arrays.asList(bids));
        productBids.add(bid);

        Writer writer = new FileWriter(bidsFile);
        new Gson().toJson(productBids, writer);

        writer.flush();
        writer.close();
        System.out.println("bid got");
        bidsFileMutex.V();

        ArrayList<Bid> finalBids = new ArrayList<Bid>();
        for (int i = 0; i < productBids.size(); i++) {
            if (productBids.get(i).productName.equals(bid.productName))
                finalBids.add(productBids.get(i));
        }


        return finalBids;
    }

    public static List<Bid> getBids(Product product) throws IOException {
        bidsFileMutex.P();
        BufferedReader reader = new BufferedReader(new FileReader(bidsFile));
        Bid[] bids = new Gson().fromJson(reader, Bid[].class);
        reader.close();
        bidsFileMutex.V();

        List<Bid> productBids = new ArrayList<Bid>();

        for(int i = 0; i < bids.length; i++) {
            if (bids[i].productName.equals(product.name))
                productBids.add(bids[i]);
        }

        return productBids;
    }


    public static List<Product> getProducts() throws IOException {
        productsFileMutex.P();
        BufferedReader reader = new BufferedReader(new FileReader(productsFile));
        Product[] products = new Gson().fromJson(reader, Product[].class);
        reader.close();
        productsFileMutex.V();
        List<Product> list = new ArrayList<Product>();
        Collections.addAll(list, products);
        return list;
    }

}



class BinarySemaphore { // used for mutual exclusion
    private boolean value;

    BinarySemaphore(boolean initValue) {
        value = initValue;
    }

    public synchronized void P() { // atomic operation // blocking
        while (!value) {
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        value = false;
    }

    public synchronized void V() { // atomic operation // non-blocking
        value = true;
        notify(); // wake up a process from the queue
    }
}

