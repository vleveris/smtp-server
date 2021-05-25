package smtp.server.core;

import smtp.server.core.constants.Code;

public class InvalidCommand implements IClientCommand {
    private final Reply reply = new Reply(Code.COMMAND_UNRECOGNISED.getCode());

    public boolean isCorrect() {
        return true;
    }

    public Reply getReply() {
        return reply;
    }
}
