package smtp.server.core.constants;

public enum Command {
    EHLO,
    HELO,
    QUIT,
    HELP,
    MAIL,
    RCPT,
    DATA,
    NOOP,
    VRFY,
    EXPN,
    RSET,
    UNKNOWN,
    DATA_CONTENTS
}
