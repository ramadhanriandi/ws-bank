/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package java.com.bank;

import com.mysql.jdbc.*;
import javax.jws.WebService;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

/**
 *
 * @author Yanichi
 */
@WebService(serviceName = "Transfer")
public class Transfer {

    /** This is a sample web service operation */
    @WebMethod(operationName = "Transfer")
    public String Transfer(
            @WebParam(name = "asal") String asal,
            @WebParam(name = "tujuan") String tujuan,
            @WebParam(name = "nominal") String nominal){
        Connection conn = null;
        try {
            Class.forName("com.mysql.jdbc.Driver").newInstance();
                conn =
         DriverManager.getConnection("jdbc:mysql://localhost/bank_transaction?" +
                                       "user=root&password=1234");
                Statement statement = conn.createStatement();
                ResultSet rs = statement.executeQuery("select * from users"
                        + " where account_number="+asal);
                if (!rs.next()){
                    return "gagal salah nomor rekening asal";
                }
                else if (rs.getInt("balance")<Integer.parseInt(nominal)){
                    return "gagal saldo tidak cukup";
                }
                else {
                    rs = statement.executeQuery("select * from users"
                        + " where account_number="+tujuan);
                    if (!rs.next()) {
                        rs = statement.executeQuery("select * "
                                + "from virtual_accounts where id="+tujuan);
                        if (!rs.next()){
                            return "gagal salah nomor rekening tujuan";
                        }
                        else{
                            String tempid = rs.getString("user_id");
                            String SQL = "UPDATE users SET balance = balance - " + nominal 
                        +" WHERE account_number = " + asal;
                            PreparedStatement stmt = (PreparedStatement) conn.prepareStatement(
                                    SQL, 
                                    Statement.RETURN_GENERATED_KEYS);
                            int rs1 = stmt.executeUpdate();   
                            SQL = "UPDATE users SET balance = balance + " + nominal 
                                    +" WHERE id = "+ tempid;
                            stmt = (PreparedStatement) conn.prepareStatement(
                                    SQL, 
                                    Statement.RETURN_GENERATED_KEYS);
                            int rs2 = stmt.executeUpdate();   
                            return "first update row affected "+
                                    Integer.toString(rs1) + " second update row affected "
                                    + Integer.toString(rs2);
                        }
                        
                    }
                    else {
                        String SQL = "UPDATE users SET balance = balance - " + nominal 
                        +" WHERE account_number = " + asal;
                PreparedStatement stmt = (PreparedStatement) conn.prepareStatement(
                        SQL, 
                        Statement.RETURN_GENERATED_KEYS);
                int rs1 = stmt.executeUpdate();   
                SQL = "UPDATE users SET balance = balance + " + nominal 
                        +" WHERE account_number = "+ tujuan;
                stmt = (PreparedStatement) conn.prepareStatement(
                        SQL, 
                        Statement.RETURN_GENERATED_KEYS);
                int rs2 = stmt.executeUpdate();   
                return "first update row affected "+
                        Integer.toString(rs1) + " second update row affected "
                        + Integer.toString(rs2);
                    }
                } 
        } catch (Exception ex) {
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            ex.printStackTrace(pw);
            String sStackTrace = sw.toString();
            return sStackTrace;
        }
    }
}
