package smtp.server.core;

import smtp.server.core.constants.Code;
import smtp.server.core.constants.Command;
import smtp.server.core.constants.Mode;

import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class CommandManager {
    private Mode mode = Mode.AUTHENTICATION;
    private Command commandType;
    private boolean keepAlive = true;
    private String[] cmdParams;
    private final List<Integer> accountIds = new ArrayList<>();
    private final String hostname;
    private final Connection con;
    private String message = "";
    private String clientCommand;
    private String from;

    public CommandManager(String hostname, Connection con) {
        this.hostname = hostname;
        this.con = con;
    }

    public boolean isKeepAlive() {
        return keepAlive;
    }

    public String execute(String clientCommand) {
        commandType = parseCommand(clientCommand);
        this.clientCommand = clientCommand;
        Reply reply = makeReply();
        return reply.toString();
    }

    private Command parseCommand(String clientCommand) {
        if (mode != Mode.DATA_STARTED) {
            cmdParams = clientCommand.split("[ ]+");
            cmdParams[0] = cmdParams[0].toUpperCase();
            switch (cmdParams[0]) {
                case "EHLO":
                    return Command.EHLO;
                case "HELO":
                    return Command.HELO;
                case "QUIT":
                    return Command.QUIT;
                case "NOOP":
                    return Command.NOOP;
                case "VRFY":
                    return Command.VRFY;
                case "EXPN":
                    return Command.EXPN;
                case "RSET":
                    return Command.RSET;
                case "DATA":
                    return Command.DATA;
                case "MAIL":
                    return Command.MAIL;
                case "RCPT":
                    return Command.RCPT;
                default:
                    return Command.UNKNOWN;
            }
        } else {
            return Command.DATA_CONTENTS;
        }
    }

    private Reply makeReply() {
        switch (commandType) {
            case QUIT:
                return processQuit();
            case EHLO:
            case HELO:
                return processEhlo();
            case RSET:
                return processRset();
            case HELP:
                return processHelp();
            case MAIL:
                return processMail();
            case RCPT:
                return processRcpt();
            case NOOP:
                return processNoop();
            case VRFY:
                return processVrfy();
            case EXPN:
                return processExpn();
            case DATA:
                return processData();
            case DATA_CONTENTS:
                return processDataContents();
            default:
                return processUnknown();
        }
    }

    private Reply processEhlo() {
        EhloCommand ehlo = new EhloCommand(cmdParams, hostname);
        if (ehlo.isCorrect())
            mode = Mode.SENDER;
        return ehlo.getReply();
    }

    private Reply processMail() {
        Reply reply;
        if (mode == Mode.AUTHENTICATION)
            reply = new Reply(Code.BAD_SEQUENCE.getCode());
        else if (mode == Mode.SENDER) {
            MailCommand mailCommand = new MailCommand(cmdParams);
            if (mailCommand.isCorrect()) {
                from = mailCommand.getSender();
                mode = Mode.RECEIVER;
            }
            reply = mailCommand.getReply();
        } else
            reply = new Reply(Code.BAD_SEQUENCE.getCode(), "Issue a reset if you want to start over");
        return reply;
    }

    private Reply processRcpt() {
        Reply reply = new Reply(Code.BAD_SEQUENCE.getCode());
        if (mode == Mode.AUTHENTICATION)
            return reply;
        else if (mode == Mode.SENDER) {
            reply.setText("Must have sender first");
            return reply;
        } else {
            RcptCommand rcpt = new RcptCommand(cmdParams, con);
            if (rcpt.isCorrect()) {
                accountIds.add(rcpt.getAccountId());
                if (mode != Mode.DATA_READY)
                    mode = Mode.DATA_READY;
            }
            reply = rcpt.getReply();
            return reply;
        }
    }

    private Reply processUnknown() {
        return new InvalidCommand().getReply();
    }

    private Reply processQuit() {
        IClientCommand quit = new QuitCommand(cmdParams);
        if (quit.isCorrect())
            keepAlive = false;
        return quit.getReply();
    }

    private Reply processRset() {
        IClientCommand rset = new RsetCommand(cmdParams);
        if (rset.isCorrect())
            mode = Mode.SENDER;
        return rset.getReply();
    }

    private Reply processHelp() {
        return new HelpCommand(cmdParams).getReply();
    }

    private Reply processNoop() {
        return new NoopCommand().getReply();
    }

    private Reply processVrfy() {
        return new NotImplementedCommand().getReply();
    }

    private Reply processExpn() {
        return new NotImplementedCommand().getReply();
    }

    private Reply processData() {
        switch (mode) {
            case AUTHENTICATION:
                return new Reply(Code.BAD_SEQUENCE.getCode());
            case SENDER:
            case RECEIVER:
                return new Reply(Code.BAD_SEQUENCE.getCode(), "Must have sender and recipient first");
            default:
                IClientCommand data = new DataCommand(cmdParams);
                if (data.isCorrect())
                    mode = Mode.DATA_STARTED;
                return data.getReply();
        }
    }

    private Reply processDataContents() {
        if (clientCommand.startsWith(".")) {
            if (clientCommand.length() > 1)
                clientCommand = clientCommand.substring(1);
            else {
                try {
                    Statement stmt = con.createStatement();
                    Timestamp datetime = new Timestamp(new Date().getTime());
                    int newRow = stmt.executeUpdate("INSERT INTO MAIL (REVERSE_PATH, DATE, MESSAGE) VALUES('" + from + " ', ' " +
                            datetime + "', '" + message + "');");
                    if (newRow != 1)
                        throw new SQLException();
                    ResultSet rs = stmt.executeQuery("SELECT ID FROM MAIL WHERE DATE = '" + datetime + "';");
                    int mailId = -1;
                    while (rs.next())
                        mailId = rs.getInt("id");

                    if (mailId < 0)
                        throw new SQLException();

                    for (Integer accountId : accountIds) {
                        newRow = stmt.executeUpdate("INSERT INTO ACCOUNT_MAIL VALUES(" + accountId + ", " + mailId + ");");
                        if (newRow != 1)
                            throw new SQLException();
                    }
                } catch (SQLException s) {
                    return new Reply(Code.TRANSACTION_FAILURE.getCode());
                } finally {
                    mode = Mode.SENDER;
                }
                return new Reply(Code.OK.getCode());
            }
        }
        message = message + clientCommand + "\r\n";
        return new Reply();
    }


}