package io.github.invvk.seniorparkour.event;

public enum PlateType {

    START,
    CHECKPOINT,
    END;

    public static PlateType fromInt(int result) {
        return switch (result) {
            case 1 -> PlateType.START;
            case 2 -> PlateType.END;
            case 3 -> PlateType.CHECKPOINT;
            default -> null;
        };
    }

}
