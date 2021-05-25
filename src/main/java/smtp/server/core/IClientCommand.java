package smtp.server.core;

interface IClientCommand {
    boolean isCorrect();

    Reply getReply();
}
