package smtp.server.core;

import smtp.server.core.constants.Code;

public class Reply {
    private int code;
    private String text;
    private boolean multiLine = false;

    public Reply() {

    }

    public Reply(int code, String text) {
        this.code = code;
        this.text = text;
    }

    public Reply(int code) {
        setCode(code);
    }

    public void setCode(int code) {
        this.code = code;
        if (code == Code.OK.getCode())
            text = "OK";
        else if (code == Code.EXIT.getCode())
            text = "Goodbye";
        else if (code == Code.BAD_SEQUENCE.getCode())
            text = "Bad sequence of commands";
        else if (code == Code.MAIL_INPUT.getCode())
            text = "Start mail input; end with <CRLF>.<CRLF>";
        else if (code == Code.USER_NOT_VERIFIED.getCode())
            text = "Cannot verify user, but will accept message and attempt delivery";
        else if (code == Code.SERVICE_UNAVAILABLE.getCode())
            text = "Service not available, closing transmission channel";
        else if (code == Code.MAILBOX_UNAVAILABLE.getCode())
            text = "Requested mail action not taken: mailbox unavailable";
        else if (code == Code.PROCESS_ERROR.getCode())
            text = "Requested action aborted: local error in processing";
        else if (code == Code.INSUFFICIENT_STORAGE.getCode())
            text = "Requested action not taken: insufficient system storage";
        else if (code == Code.PARAMETERS_UNAVAILABLE.getCode())
            text = "Server unable to accommodate parameters";
        else if (code == Code.COMMAND_UNRECOGNISED.getCode())
            text = "Syntax error, command unrecognised";
        else if (code == Code.PARAMETER_ERROR.getCode())
            text = "Syntax error in parameters or arguments";
        else if (code == Code.COMMAND_NOT_IMPLEMENTED.getCode())
            text = "Command not implemented";
        else if (code == Code.PARAMETER_NOT_IMPLEMENTED.getCode())
            text = "Command parameter not implemented";
        else if (code == Code.MAILBOX_NOT_FOUND.getCode())
            text = "Requested action not taken: mailbox not found";
        else if (code == Code.STORAGE_EXCESS.getCode())
            text = "Requested mail action aborted: exceeded storage allocation";
        else if (code == Code.MAILBOX_NAME.getCode())
            text = "Requested action not taken: mailbox name not allowed";
        else if (code == Code.TRANSACTION_FAILURE.getCode())
            text = "Transaction failed";
        else
            text = "Unknown";

    }

    public void setText(String text) {
        this.text = text;
    }

    public int getCode() {
        return code;
    }

    public void setMultiLine(boolean multiLine) {
        this.multiLine = multiLine;
    }

    public String toString() {
        String reply;
        if (code == 0)
            reply = "";
        else {
            String[] lines = text.split("\r\n", 10);
            StringBuilder prepareReply = new StringBuilder();
            for (int i = 0; i < lines.length; i++) {
                prepareReply.append(code);
                if (i < lines.length - 1)
                    prepareReply.append("-");
                else
                    prepareReply.append(" ");
                prepareReply.append(lines[i]);
                if (i < lines.length - 1)
                    prepareReply.append("\r\n");
            }
            reply = prepareReply.toString();
        }
        return reply;
    }
}