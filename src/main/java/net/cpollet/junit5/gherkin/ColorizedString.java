package net.cpollet.junit5.gherkin;

/**
 * Created by cpollet on 18.10.16.
 */
@SuppressWarnings("unused")
public class ColorizedString {
    private static final String PREFIX = "\u001b[";
    private static final String SUFFIX = "m";
    private final Color color;
    private final String string;

    public ColorizedString(Color color, String string) {
        this.color = color;
        this.string = string;
    }

    @Override
    public String toString() {
        return PREFIX + color.code + SUFFIX + string + PREFIX + SUFFIX;
    }

    enum Color {
        NC("0"),
        WHITE("1;37"),
        BLACK("0;30"),
        BLUE("0;34"),
        LIGHT_BLUE("1;34"),
        GREEN("0;32"),
        LIGHT_GREEN("1;32"),
        CYAN("0;36"),
        LIGHT_CYAN("1;36"),
        RED("0;31"),
        LIGHT_RED("1;31"),
        PURPLE("0;35"),
        LIGHT_PURPLE("1;35"),
        BROWN("0;33"),
        YELLOW("1;33"),
        GRAY("0:30"),
        LIGHT_GRAY("0;37");

        private final String code;

        Color(String code) {
            this.code = code;
        }
    }
}
