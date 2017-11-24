package mc.enderbro3d.goldixapi.data;

public class Value {
    public Object value;

    public Value(Object value) {
        this.value = value;
    }

    public String stringValue() {
        if(value == null) return "null";
        return String.valueOf(value);
    }

    public String toString() {
        return stringValue();
    }

    public int integerValue() {
        String s = stringValue();
        if(s == null || s.equals("null")) return 0;
        return Integer.parseInt(s);
    }

    public short shortValue() {
        String s = stringValue();
        if(s == null || s.equals("null")) return 0;
        return Short.parseShort(s);
    }

    public long longValue() {
        String s = stringValue();
        if(s == null || s.equals("null")) return 0;
        return Long.parseLong(s);
    }

    public boolean booleanValue() {
        String s = stringValue();
        if(s == null || s.equals("null")) return false;
        return Boolean.parseBoolean(s);
    }

    public float floatValue() {
        String s = stringValue();
        if(s == null || s.equals("null")) return 0;
        return Float.parseFloat(s);
    }

    public double doubleValue() {
        String s = stringValue();
        if(s == null || s.equals("null")) return 0;
        return Double.parseDouble(s);
    }
}
