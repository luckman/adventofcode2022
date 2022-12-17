package tools;

import java.util.Objects;

public class IntPoint2d {
    public int x;
    public int y;

    public IntPoint2d(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public static IntPoint2d of(int x, int y) {
        return new IntPoint2d(x, y);
    }

    public static IntPoint2d fromStringWithComma(String s) {
        String[] split = s.split(",");
        int x = Integer.parseInt(split[0]);
        int y = Integer.parseInt(split[1]);
        return IntPoint2d.of(x, y);
    }

    // x=..., y=...
    public static IntPoint2d fromStringWithXAndYLetters(String s) {
        int x = Integer.parseInt(s.split("x=")[1].split(",")[0].trim());
        int y = Integer.parseInt(s.split("y=")[1].trim());
        return IntPoint2d.of(x, y);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        IntPoint2d that = (IntPoint2d) o;
        return x == that.x && y == that.y;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }

    @Override
    public String toString() {
        return "[" + x + "," + y + "]";
    }
}
