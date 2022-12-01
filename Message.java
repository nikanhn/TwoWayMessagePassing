import java.io.Serializable;

public class Message implements Serializable {
    protected String type;
    protected String status;
    protected String text;

    /**
     * Defualt constructor
     */
    public Message(){
        this.type = "Undefined";
        this.status = "Undefined";
        this.text = "Undefined";
    }

    
    /**
     * Constructor
     * @param type
     * @param status
     * @param text
     */
    public Message(String type, String status, String text){
        setType(type);
        setStatus(status);
        setText(text);
    }

    
    /**
     * Method to set type
     * @param type
     */
    private void setType(String type){
        this.type = type;
    }

    
    /**
     * Method to set status
     * @param status
     */
    public void setStatus(String status){
        this.status = status;
    }

    
    /**
     * Method to set text
     * @param text
     */
    public void setText(String text){
        this.text = text;
    }

    
    /**
     * Method to return type
     * @return String
     */
    public String getType(){
        return this.type;
    }

    
    /**
     * Method to return status
     * @return String
     */
    public String getStatus(){
        return this.status;
    }

    
    /**
     * Method to return text
     * @return String
     */
    public String getText(){
        return this.text;
    }

}
