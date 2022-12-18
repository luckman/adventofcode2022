package tools;

import java.util.Objects;

public class IntPoint3d {
    public int x;
    public int y;
    public int z;

    public IntPoint3d(int x, int y, int z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public static IntPoint3d of(int x, int y, int z) {
        return new IntPoint3d(x, y, z);
    }

    public static IntPoint3d fromStringWithComma(String s) {
        String[] split = s.split(",");
        int x = Integer.parseInt(split[0]);
        int y = Integer.parseInt(split[1]);
        int z = Integer.parseInt(split[2]);
        return IntPoint3d.of(x, y, z);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        IntPoint3d that = (IntPoint3d) o;
        return x == that.x && y == that.y && z == that.z;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y, z);
    }

    @Override
    public String toString() {
        return String.format("[%s,%s,%s]", x, y, z);
    }
}
