package network;

public enum ProtocolType {

    EXIT((byte) 0x00),  STUDENT((byte) 0x01), PROFESSOR((byte) 0x02),ADMIN((byte) 0x03),UNDEFINED((byte) 0x99);

    /*
    0x00: 종료  EXIT
    0x01: 학생 STUDENT
    0x02: 교수 PROFESSOR
    0x03: 관리자 ADMIN
    */

    private final byte type;

    public byte getType() {
        return type;
    }

    ProtocolType(byte type) {
        this.type = type;
    }

    public static ProtocolType get(int type) {
        switch (type) {
            case 0x00:
                return EXIT;
            case 0x01:
                return STUDENT;
            case 0x02:
                return PROFESSOR;
            case 0x03:
                return ADMIN;
            default:
                return UNDEFINED;

        }
    }
}
