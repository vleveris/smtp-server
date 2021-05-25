package smtp.server.core;

import smtp.server.core.constants.Code;

public class NoopCommand implements IClientCommand {
    private final Reply reply = new Reply(Code.OK.getCode());

    public boolean isCorrect() {
        return true;
    }

    public Reply getReply() {
        return reply;
    }
}
