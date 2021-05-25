package smtp.server.core;

import smtp.server.core.constants.Code;

public class DataCommand implements IClientCommand {
    private final String[] cmdParams;
    private final Reply replyOK = new Reply(Code.MAIL_INPUT.getCode());
    private final Reply tooManyParams = new Reply(Code.PARAMETER_ERROR.getCode());

    public DataCommand(String[] cmdParams) {
        this.cmdParams = cmdParams;
    }

    public boolean isCorrect() {
        return cmdParams.length == 1;
    }

    public Reply getReply() {
        if (isCorrect())
            return replyOK;
        else
            return tooManyParams;
    }
}
