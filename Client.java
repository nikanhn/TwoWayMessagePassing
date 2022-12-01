import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;

public class Client {
    private Socket sk;
    private String host;
    private int port;
    private ObjectOutputStream objout;
    private ObjectInputStream objin;
    private InputStream in;
    private OutputStream out;

    private int clientNo;

    
    /**
     * Constructor that throws exception
     * Resource: https://rollbar.com/blog/can-constructors-throw-exceptions-in-java/#
     * @param host
     * @param port
     * @param clientNo
     * @throws Exception
     */
    public Client(String host, int port, int clientNo) throws Exception {
        this.host = host;
        this.port = port;
        this.clientNo = clientNo;
        this.sk = new Socket(host, port);
        this.in = sk.getInputStream();
        this.out = sk.getOutputStream();
        this.objout = new ObjectOutputStream(out);
        this.objin = new ObjectInputStream(in);
    }

    
    /**
     * Method to send message that throws exception
     * @param msg
     * @throws Exception
     * @return none
     */
    private void sendMsg(Message msg) throws Exception {
    	//pass msg to writeObject method
        objout.writeObject(msg)
        ;
        //call flush method to clear writer from elements not inside it
        //resource: https://www.geeksforgeeks.org/writer-flush-method-in-java-with-examples/
        objout.flush();
        
        //set result from calling readObject to msgReturn
        Message msgReturn = (Message) objin.readObject();
        System.out.println("Response to client " + clientNo + ": " + msgReturn.getText());
    }

    
    /**
     * Method to login that throws exception
     * @throws Exception
     * @param none
     * @return none
     */
    public void login() throws Exception {
        System.out.println("Client " + clientNo + " login");
        sendMsg(new Message("login", "", ""));
    }

    
    /**
     * Method for text that calls sendMsg and throws exception
     * @param text
     * @throws Exception
     */
    public void text(String text) throws Exception {
        sendMsg(new Message("text", "", text));
    }
    
    
    /**
     * Method for logout that calls sendMsg and throws exception
     * @param none
     * @throws Exception
     * @return none
     */
    public void logout() throws Exception {
        sendMsg(new Message("logout", "", ""));
    }

    public static void main(String[] args) {
        try {
        	//create client objects and call different methods of the class
            Client client1 = new Client("localhost", 8080, 1);
            Client client2 = new Client("localhost", 8080, 2);
            Client client3 = new Client("localhost", 8080, 3);
//            client1.login();
            client2.login();
            client3.login();
            client1.text("this is client 1");
            client2.text("this is client 2");
            client3.text("this is client 3");
            client1.logout();
            client2.logout();
            client3.logout();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
