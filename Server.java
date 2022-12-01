import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

/**
 *
 */
public class Server {
    class ServerThread implements Runnable {
        private Socket connection;

        private ObjectOutputStream objout;
        private ObjectInputStream objin;
        private InputStream in;
        private OutputStream out;

        private boolean isLogin;
        private boolean isClosed;
        
        /**
         * Constructor for ServerThread that takes a Socket argument
         * @param sk
         * @throws IOException
         */
        public ServerThread(Socket sk) throws IOException {
            this.connection = sk;
        }
        
        
        /**
         * Method to handle the message that takes a Message argument
         * @param msg
         * @return none
         */
        private void handleMsg(Message msg) {
        	//define different switch and cases for login, text, and logout
            switch (msg.getType()) {
                case "login":
                    System.out.println("Client@port:" + connection.getPort() + " login");
                    isLogin = true;
                    try {
                    	//print success message on console
                        objout.writeObject(new Message("login", "success", "login success"));
                        objout.flush();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    break;
                case "text":
                    System.out.println("Client@port:" + connection.getPort() + " send text" + msg.getText());
                    if (!isLogin) {
                        try {
                        	//print fail message in console
                            objout.writeObject(new Message("text", "fail", "please login first"));
                            objout.flush();
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    } else {
                        try {
                        	//print success message
                            objout.writeObject(new Message("text", "success", msg.getText().toUpperCase()));
                            objout.flush();
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    }
                    break;
                    case "logout":
                        System.out.println("Client@port:" + connection.getPort() + " logout");
                        isLogin = false;
                        isClosed = true;
                        try {
                        	//print success message for logout
                            objout.writeObject(new Message("logout", "success", "logout success"));
                            objout.flush();
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                        break;
            }
        }


        @Override
        /**
         * Method for running server
         * @param none
         * @return none
         */
        public void run() {
            try {
                in = connection.getInputStream();
                out = connection.getOutputStream();
                objout = new ObjectOutputStream(out);
                objin = new ObjectInputStream(in);
                while (!isClosed) {
                    Message msg = (Message) objin.readObject();
                    handleMsg(msg);
                }
                System.out.println("Client@port:" + connection.getPort() + " closed");
            } catch (IOException | ClassNotFoundException e) {
                throw new RuntimeException(e);
            }

        }
    }

    private ServerSocket serverSocket;
    private int port;

    
    /**
     * Default Constructor for Server class
     */
    public Server() {
        this.port = 8080;
    }

    
    /**
     * Constructor for Server class
     * @param port
     */
    public Server(int port) {
        this.port = port;
    }



    /**
     * Method to start the server. New ServerThread will be created on each connection.
     * @param none
     * @return none
     */
    public void start() {
        try {
        	//create new instance of serverSocket
            serverSocket = new ServerSocket(port);
            System.out.println("Server started");
            while (true) {
            	//new object from Socket called connection
                Socket connection = serverSocket.accept();
                //print info of connection like IP address, Host address, and port number
                System.out.println("New connection from " + connection.getInetAddress().getHostAddress() + ": " + connection.getPort());
                //new object of ServerThread
                ServerThread serverThread = new ServerThread(connection);
                new Thread(serverThread).start();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        Server server = new Server();
        server.start();
    }
}
