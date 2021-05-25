package smtp.server.core;

import smtp.server.core.constants.Code;

public class HelpCommand implements IClientCommand {
    private String[] cmdParams;
    private final Reply reply = new Reply(Code.HELP.getCode());

    public HelpCommand(String[] cmdParams) {
        this.cmdParams = cmdParams;
    }

    public boolean isCorrect() {
        return true;
    }

    public Reply getReply() {
        reply.setText("EHLO HELO MAIL RCPT DATA VRFY EXPN RSET QUIT");
        return reply;
    }
}
