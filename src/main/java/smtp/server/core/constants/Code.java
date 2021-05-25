package smtp.server.core.constants;

public enum Code {
    HELP(211),
    HELP_NONSTANDARD(214),
    READY(220),
    EXIT(221),
    OK(250),
    USER_NOT_LOCAL_WARNING(251),
    USER_NOT_VERIFIED(252),
    INVALID_DOMAIN(301),
    MAIL_INPUT(354),
    SERVICE_UNAVAILABLE(421),
    MAILBOX_UNAVAILABLE(450),
    PROCESS_ERROR(451),
    INSUFFICIENT_STORAGE(452),
    PARAMETERS_UNAVAILABLE(455),
    COMMAND_UNRECOGNISED(500),
    PARAMETER_ERROR(501),
    COMMAND_NOT_IMPLEMENTED(502),
    BAD_SEQUENCE(503),
    PARAMETER_NOT_IMPLEMENTED(504),
    MAILBOX_NOT_FOUND(550),
    USER_NOT_LOCAL_ERROR(551),
    STORAGE_EXCESS(552),
    MAILBOX_NAME(553),
    TRANSACTION_FAILURE(554),
    MAILBOX_PARAMETER_UNRECOGNISED(555);

    private final int code;

    Code(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }
}
