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
import java.sql.ResultSet;
import java.sql.Statement;

/**
 *
 * @author Yanichi
 */
@WebService(serviceName = "ValidasiNoRekening")
public class ValidasiNoRekening {

    /** This is a sample web service operation */
    @WebMethod(operationName = "validasinorekening")
    public String cekNoRek(@WebParam(name = "norek") String nomor) {
        try{ 
        Connection conn = null;
        conn =
       DriverManager.getConnection("jdbc:mysql://localhost/bank_transaction?" +
                                   "user=root&password=1234");
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("select * from users where accoun"
                    + "t_number = "+nomor);
            if (!rs.next()){
                return "tidak valid";
            }
            else{
                return "valid";
            }
        }catch (Exception e){
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            e.printStackTrace(pw);
            String sStackTrace = sw.toString();
            return sStackTrace;  
        }
    }
}
