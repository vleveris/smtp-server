package smtp.server.core;

import smtp.server.core.constants.Code;


public class MailCommand implements IClientCommand {
    private final Reply reply = new Reply();
    private final String[] cmdParams;
    private String sender = "";

    public MailCommand(String[] cmdParams) {
        cmdParams[1] = cmdParams[1].toLowerCase();
        this.cmdParams = cmdParams;
    }

    public boolean isCorrect() {
        if (!cmdParams[1].matches("from:[\\d\\D]*")) {
            reply.setCode(Code.MAILBOX_NOT_FOUND.getCode());
            reply.setText("Invalid syntax. Syntax should be MAIL FROM:<mailbox@domain>[crlf]");
            return false;
        }
        if (!cmdParams[1].matches("from:(<([\\d\\D]+@[\\d\\D]+)?>)?")) {
            reply.setCode(Code.MAILBOX_NOT_FOUND.getCode());
            reply.setText("The address is not valid.");
            return false;
        }
        if (cmdParams[1].length() > 7)
            sender = cmdParams[1].substring(6, cmdParams[1].length() - 1);

        reply.setCode(Code.OK.getCode());
        return true;
    }

    public Reply getReply() {
        return reply;
    }

    public String getSender() {
        return sender;
    }
}
