package Tools;

public class Appropriator {
    private static int currentID = 1000;
    private static int currentPID = 2;
    private static char name = 'a' - 1;

    public static int getCurrentID() {
        return (currentID++);
    }

    public static int getCurrentPID() {
        return (currentPID++);
    }

    public static String getName() {
        return String.valueOf(name++);
    }
}
