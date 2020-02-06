/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package java.com.bank;

import javax.jws.WebService;
import javax.jws.WebMethod;
import javax.jws.WebParam;
//import javax.jws.WebResult;
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
@WebService(serviceName = "getDataRekening")
public class getDataRekening {

    /** This is a sample web service operation */
    @WebMethod(operationName = "getDataRekening")
    public String[][] hello(@WebParam(name = "account") String account) {
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
            ResultSet rs = stmt.executeQuery("select id,account_number,name,balance from users "
                    + "where account_number = "+account);
            String[] tmp = new String[6];
            rs.next();
            tmp[0] = rs.getString("name");
            tmp[1] = rs.getString("balance");
            tmp[2] = rs.getString("account_number");
            String tempid = rs.getString("id");
            rs = stmt.executeQuery("select * from transactions "
                    + "where user_id = "+tempid);
            while (rs.next()) {
            // retrieve and print the values for the current row
                int id = rs.getInt("id");
                int user_id = rs.getInt("user_id"); 
                String type = rs.getString("type"); 
                int amount = rs.getInt("amount"); 
                int destination_account = rs.getInt("destination_account");
                String timestamp =rs.getString("time");
                Transaction temporary = new Transaction(
                        id,
                        user_id,
                        type,
                        amount,
                        destination_account,
                        timestamp
                        );
                temp.add(temporary);
            }
            tempstring = new String[temp.size()+1][6];
            int i;
            for(i = 0; i <temp.size() ; i++){
                tempstring[i] = temp.get(i).printString();
            }
            tempstring[i] = tmp;
            return tempstring;
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
