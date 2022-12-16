package tools;

public class IntPoint2d {
    public int x;
    public int y;

    public static IntPoint2d of(int x, int y) {
        IntPoint2d point = new IntPoint2d();
        point.x = x;
        point.y = y;
        return point;
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
    public String toString() {
        return "[" + x + "," + y + "]";
    }
}
