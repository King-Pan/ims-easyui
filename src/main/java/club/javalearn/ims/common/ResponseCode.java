package club.javalearn.ims.common;

public enum ResponseCode {
    SUCCESS(200, "SUCCESS"),
    ERROR(500, "ERROR"),
    UNAUTHORIZED(401,"UNAUTHORIZED"),
    ILLEGAL_ARGUMENT(2, "ILLEGAL_ARGUMENT");

    private final int code;
    private final String desc;


    ResponseCode(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public int getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }
}
