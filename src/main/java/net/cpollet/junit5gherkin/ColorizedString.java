package net.cpollet.junit5gherkin;

/**
 * Created by cpollet on 18.10.16.
 */
@SuppressWarnings("unused")
public class ColorizedString {
    private static final String PREFIX = "\u001b[";
    private static final String SUFFIX = "m";

    public static String COLOR_NC = "0";
    public static String COLOR_WHITE = "1;37";
    public static String COLOR_BLACK = "0;30";
    public static String COLOR_BLUE = "0;34";
    public static String COLOR_LIGHT_BLUE = "1;34";
    public static String COLOR_GREEN = "0;32";
    public static String COLOR_LIGHT_GREEN = "1;32";
    public static String COLOR_CYAN = "0;36";
    public static String COLOR_LIGHT_CYAN = "1;36";
    public static String COLOR_RED = "0;31";
    public static String COLOR_LIGHT_RED = "1;31";
    public static String COLOR_PURPLE = "0;35";
    public static String COLOR_LIGHT_PURPLE = "1;35";
    public static String COLOR_BROWN = "0;33";
    public static String COLOR_YELLOW = "1;33";
    public static String COLOR_GRAY = "0;30";
    public static String COLOR_LIGHT_GRAY = "0;37";

    private final String color;
    private final String string;

    public ColorizedString(String color, String string) {
        this.color = color;
        this.string = string;
    }

    @Override
    public String toString() {
        return PREFIX + color + SUFFIX + string + PREFIX + SUFFIX;
    }
}
