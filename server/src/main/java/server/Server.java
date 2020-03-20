package server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Vector;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Server {
    private Vector<ClientHandler> clients;
    private AuthService authService;
    private Socket socket;
    private static final Logger logger = Logger.getLogger(Server.class.getName());

    public AuthService getAuthService() {
        return authService;
    }

    public Server() {
        clients = new Vector<>();
        authService = new DBAuthService();
        ServerSocket server = null;

        final int PORT = 5050;

        DataInputStream in;
        DataOutputStream out;

        ExecutorService executorService = Executors.newFixedThreadPool(2);
        //2 потока установлено исключительно для проверки работоспособности;
        try {
            server = new ServerSocket(PORT);
            logger.log(Level.INFO, "Сервер запущен");
            while (true) {
                socket = server.accept();
                executorService.execute(() -> new ClientHandler(socket, this));
            }
        } catch (IOException e) {
            logger.log(Level.SEVERE, e.getMessage(), e);
        } finally {
            executorService.shutdown();
            try {
                if (socket != null) {
                    socket.close();
                }
            } catch (IOException e) {
                logger.log(Level.SEVERE, e.getMessage(), e);
            }
            try {
                if (server != null) {
                    server.close();
                }
            } catch (IOException e) {
                logger.log(Level.SEVERE, e.getMessage(), e);
            }
        }

    }

    public void broadcastMessage(String msg, String sender) {
        for (ClientHandler c : clients) {
            c.sendMessage(sender + ": " + msg);
        }
    }

    public void privateMessage(ClientHandler sender, String receiver, String msg) {
        String message = String.format("[ %s ] private [ %s ] : %s", sender.getNick(), receiver, msg);
        for (ClientHandler c : clients) {
            if (c.getNick().equals(receiver)) {
                c.sendMessage(message);
                if (!c.getNick().equals(sender.getNick())) {
                    sender.sendMessage(message);
                }
                return;
            }
        }
        sender.sendMessage("Пользователь не найден: " + receiver);
    }

    public void subscribe(ClientHandler clientHandler) {
        clients.add(clientHandler);
        broadcastClientList();
    }

    public void unsubscribe(ClientHandler clientHandler) {
        clients.remove(clientHandler);
        broadcastClientList();
    }

    public boolean isLoginAuthorized(String login) {
        for (ClientHandler c : clients) {
            if (c.getLogin().equals(login)) {
                return true;
            }
        }
        return false;
    }

    public void broadcastClientList() {
        StringBuilder sb = new StringBuilder("/clientlist ");
        for (ClientHandler c : clients) {
            sb.append(c.getNick()).append(" ");
        }
        String msg = sb.toString();
        for (ClientHandler c : clients) {
            c.sendMessage(msg);
        }
    }
}
