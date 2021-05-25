package smtp.server.core;

import smtp.server.core.constants.Code;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class RcptCommand implements IClientCommand {
    private final Reply reply = new Reply();
    private final String[] cmdParams;
    private int accountId = -1;
    private final Connection con;

    public RcptCommand(String[] cmdParams, Connection con) {
        cmdParams[1] = cmdParams[1].toLowerCase();
        this.cmdParams = cmdParams;
        this.con = con;
    }

    public boolean isCorrect() {
        if (!cmdParams[1].matches("to:([\\d\\D]*)?")) {
            reply.setCode(Code.MAILBOX_NOT_FOUND.getCode());
            reply.setText("Invalid syntax. Syntax should be RCPT TO:<mailbox@domain>[crlf]");
            return false;
        }
        if (!cmdParams[1].matches("to:<[\\d\\D]+@[\\d\\D]+>")) {
            reply.setCode(Code.MAILBOX_NOT_FOUND.getCode());
            reply.setText("A valid address is required.");
            return false;
        }
        String receiver = cmdParams[1].substring(4, cmdParams[1].length() - 1);
        String[] parts = receiver.split("@", 2);
        try {
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT ID from DOMAIN WHERE NAME='" + parts[1] + "';");
            int domainId = -1;
            while (rs.next())
                domainId = rs.getInt("id");
            rs = stmt.executeQuery("SELECT ID, DOMAIN_ID from ACCOUNT WHERE USERNAME='" + parts[0] + "';");
            int accountDomain;
            while (rs.next()) {
                accountDomain = rs.getInt("domain_id");
                if (accountDomain == domainId)
                    accountId = rs.getInt("id");
            }
        } catch (SQLException s) {
            reply.setCode(Code.MAILBOX_NOT_FOUND.getCode());
            return false;
        }
        if (accountId < 0) {
            reply.setCode(Code.MAILBOX_NOT_FOUND.getCode());
            reply.setText("Unknown user");
            return false;
        }
        reply.setCode(Code.OK.getCode());
        return true;
    }

    public Reply getReply() {
        return reply;
    }

    public int getAccountId() {
        return accountId;
    }
}
