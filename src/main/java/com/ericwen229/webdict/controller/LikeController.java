package com.ericwen229.webdict.controller;

import com.ericwen229.webdict.model.Status;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.sql.*;

@RestController
public class LikeController {

    static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
    static final String DB_URL = "jdbc:mysql://localhost/webdict";

    static final String USER = "eric";
    static final String PWD = "professional*";

    @RequestMapping(value = "/like", method = RequestMethod.POST)
    public Status like(@RequestParam(value = "word") String word,
                       @RequestParam(value = "source") String source,
                       @RequestParam(value = "dislike", defaultValue = "false") String dislike) {
        Connection conn = null;
        Statement stmt = null;
        boolean success = true;
        String message = "";
        try {
            Class.forName(JDBC_DRIVER);
            conn = DriverManager.getConnection(DB_URL, USER, PWD);
            stmt = conn.createStatement();
            word = word.replace(" ", "");
            ResultSet rs = stmt.executeQuery(String.format("SELECT * FROM likes WHERE word='%s' LIMIT 1", word));
            if (rs.next()) {
                int num = rs.getInt(source);
                if (dislike.equals("true")) {
                    -- num;
                }
                else {
                    ++ num;
                }
                stmt.executeUpdate(String.format("UPDATE likes SET %s=%d WHERE word='%s'", source, num, word));
            }
        }
        catch (Exception e) {
            success = false;
            message = "internal exception";
            e.printStackTrace();
        }
        finally {
            try {
                if (stmt != null) {
                    conn.close();
                }
            }
            catch (SQLException se) {
                success = false;
                message = "internal exception";
            }
            try {
                if (conn != null) {
                    conn.close();
                }
            }
            catch (SQLException se) {
                success = false;
                message = "internal exception";
                se.printStackTrace();
            }
        }

        return new Status(success, message);
    }

}
