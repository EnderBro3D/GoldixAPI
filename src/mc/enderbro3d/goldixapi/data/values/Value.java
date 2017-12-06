package mc.enderbro3d.goldixapi.data.values;

public class Value implements IVal {
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
        if(value == null) return 0;
        String s = stringValue();
        return Integer.parseInt(s);
    }

    public short shortValue() {
        if(value == null) return 0;
        String s = stringValue();
        return Short.parseShort(s);
    }

    public long longValue() {
        if(value == null) return 0;
        String s = stringValue();
        return Long.parseLong(s);
    }

    public boolean booleanValue() {
        if(value == null) return false;
        String s = stringValue();
        return Boolean.parseBoolean(s);
    }

    public float floatValue() {
        if(value == null) return 0;
        String s = stringValue();
        return Float.parseFloat(s);
    }

    public double doubleValue() {
        if(value == null) return 0;
        String s = stringValue();
        return Double.parseDouble(s);
    }

    @Override
    public Object value() {
        return value;
    }
}
