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
@WebService(serviceName = "MakeVirtual")
public class MakeVirtual {

    /** This is a sample web service operation */
    @WebMethod(operationName = "MakeVirtual")
    public String makevirtual(@WebParam(name = "account") String account) {
        Connection conn = null;
        try {
            Class.forName("com.mysql.jdbc.Driver").newInstance();
            conn =
       DriverManager.getConnection("jdbc:mysql://localhost/bank_transaction?" +
                                   "user=root&password=1234");
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("select * from users "
                    + "where account_number = "+account);
            if (!rs.next()){
                return "akun tidak ditemukan";
            }
            else{
                String tempid = rs.getString("id");
                rs = stmt.executeQuery("select (1000000000+count(*))as num "
                        + "from virtual_accounts where user_id = "+tempid+";");
                rs.next();
                String tempnum = rs.getString("num");
                String SQL = "INSERT INTO virtual_accounts(id, user_id) VALUES("
                        + tempnum +", "+tempid +");";
                
                PreparedStatement statement = (PreparedStatement) conn.prepareStatement(
                        SQL, 
                        Statement.RETURN_GENERATED_KEYS);
                int result = statement.executeUpdate();
//                rs = stmt.executeQuery("select max(id) as account "
//                        + "from virtual_accounts where user_id = "+ tempid);
//                return rs.getString("account");
                return tempnum;
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
