package smtp.server.core;

import smtp.server.core.constants.Code;

public class NotImplementedCommand implements IClientCommand {
    private final Reply reply = new Reply(Code.COMMAND_NOT_IMPLEMENTED.getCode());

    public boolean isCorrect() {
        return true;
    }

    public Reply getReply() {
        return reply;
    }
}
