/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package java.com.bank;


import javax.xml.bind.annotation.XmlRootElement;


/**
 *
 * @author Yanichi
 */
@XmlRootElement(name="Transaction")
public class Transaction {
    private int id;
    private int user_id;
    private String type;
    private int amount;
    private int destination_account;
    private String timestamp;
    public Transaction(
            int id, 
            int user_id, 
            String type, 
            int amount, 
            int destination_account,
            String timestamp
            ){
        this.id = id;
        this.user_id = user_id;
        this.type = type;
        this.amount = amount;
        this.destination_account = destination_account;
        this.timestamp = timestamp;
    }
    public int getId(){
        return id;
    }
    public int getUser_Id(){
        return user_id;
    }
    public String getType(){
        return type;
    }
    public String[] printString(){
        String[] temp = new String[6];
        temp[0] = Integer.toString(id) ;
        temp[1] = Integer.toString(user_id);
        temp[2] = type;
        temp[3] = Integer.toString(amount);
        temp[4] = Integer.toString(destination_account);
        temp[5] = timestamp;
        return temp;
    }
}