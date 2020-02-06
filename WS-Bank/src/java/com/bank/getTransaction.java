/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package java.com.bank;

import javax.jws.WebService;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
//import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

/**
 *
 * @author Yanichi
 */
@WebService(serviceName = "getTransaction")
public class getTransaction {
    
    @WebMethod(operationName = "getTransactioHistory")
    @WebResult(name="transactionHistory")
    public String[][] getTransactionHistory(
            @WebParam(name = "account") String account,
            @WebParam(name = "awal") String awal,
            @WebParam(name = "akhir") String akhir) {
        Connection conn = null;
        ArrayList<Transaction> temp = new ArrayList<Transaction>();
        String[][] tempstring;
        try {
            // The newInstance() call is a work around for some
            // broken Java implementations
            Class.forName("com.mysql.jdbc.Driver").newInstance();
            conn =
       DriverManager.getConnection("jdbc:mysql://localhost/bank_transaction?" +
                                   "user=root&password=");
            
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("select * from users "
                    + "where account_number = "+account);
            String tempid;
            if (!rs.next()){
                rs = stmt.executeQuery("select user_id from virtual_accounts "
                        + "where id = "+account);
                if (!rs.next()){
                    tempstring = new String[1][1];
                    tempstring[0][0] = "akun tidak ditemukan"; 
                    return tempstring; 
                }
                else{
                    tempid = rs.getString("user_id");
                }
            }
            else{
                tempid = rs.getString("id");
            }
            rs = stmt.executeQuery("select * from transactions where "
                    + "type = \"credit\" and time > Cast(\""+awal+"\" as datetime) "
                    + "and time < Cast(\""+akhir+"\" as datetime) and user_id = "
                    +tempid +";");
            if (!rs.next()){
                tempstring = new String[1][1];
                tempstring[0][0] = "Tidak ada Transaksi memenuhi kriteria"; 
                return tempstring; 
            }
            else{
                rs.previous();
                while (rs.next()) {
                // retrieve and print the values for the current row
                    int id = rs.getInt("id");
                    int user_id = rs.getInt("user_id"); 
                    String type = rs.getString("type"); 
                    int amount = rs.getInt("amount"); 
                    int destination_account = rs.getInt("destination_account");
                    String timestamp =rs.getString("time");
                    Transaction tmp = new Transaction(
                            id,
                            user_id,
                            type,
                            amount,
                            destination_account,
                            timestamp
                            );
                    temp.add(tmp);
                }
                tempstring = new String[temp.size()][6];
                for(int i = 0; i <temp.size() ; i++){
                    tempstring[i] = temp.get(i).printString();
                }
                return tempstring;
            }
        } catch (Exception ex) {
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            ex.printStackTrace(pw);
            tempstring = new String[1][1];
            tempstring[0][0] = sw.toString(); 
            return tempstring; 
        }
        
        
    }
}
