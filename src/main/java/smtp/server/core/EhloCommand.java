package smtp.server.core;

import smtp.server.core.constants.Code;

public class EhloCommand implements IClientCommand {
    private String[] cmdParams;
    private String hostname;

    public EhloCommand(String[] cmdParams, String hostname) {
        this.cmdParams = cmdParams;
        this.hostname = hostname;
    }

    public boolean isCorrect() {
        return cmdParams.length > 1;
    }

    public Reply getReply() {
        Reply reply = new Reply();
        if (isCorrect()) {
            reply.setCode(Code.OK.getCode());
            if (cmdParams[0].startsWith("H"))
                reply.setText("Hello " + cmdParams[1]);
            else {
                hostname = hostname.toUpperCase();
                reply.setMultiLine(true);
                reply.setText(hostname + " greets " + cmdParams[1]
                        + "\r\n8BITMIME\r\nSIZE\r\nHELP");
            }
        } else {
            reply.setCode(Code.INVALID_DOMAIN.getCode());
            reply.setText(cmdParams[0] + " Invalid domain address.");
        }
        return reply;
    }
}