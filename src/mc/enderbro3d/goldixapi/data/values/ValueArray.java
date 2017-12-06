package mc.enderbro3d.goldixapi.data.values;

import java.util.Arrays;

public class ValueArray implements IVal {
    public Object[] value;

    public ValueArray(Object[] value) {
        this.value = value;
    }

    public String[] stringValue() {
        if(value == null) return new String[]{"null"};
        String[] arr = new String[value.length];
        for(int i = 0; i < arr.length ; i++) {
            arr[i] = value[i].toString();
        }
        return arr;
    }

    public String toString() {
        return Arrays.toString(stringValue());
    }

    public int[] integerValue() {
        if(value == null) return new int[]{0};
        String[] arr0 = stringValue();
        int[] arr = new int[arr0.length];
        for(int i = 0;i < arr.length;i++) {
            arr[i] = Integer.parseInt(arr0[i]);
        }
        return arr;
    }

    public short[] shortValue() {
        if(value == null) return new short[]{0};
        String[] arr0 = stringValue();
        short[] arr = new short[arr0.length];
        for(int i = 0;i < arr.length;i++) {
            arr[i] = Short.parseShort(arr0[i]);
        }
        return arr;
    }

    public long[] longValue() {
        if(value == null) return new long[]{0};
        String[] arr0 = stringValue();
        long[] arr = new long[arr0.length];
        for(int i = 0;i < arr.length;i++) {
            arr[i] = Long.parseLong(arr0[i]);
        }
        return arr;
    }

    public boolean[] booleanValue() {
        if(value == null) return new boolean[]{false};
        String[] arr0 = stringValue();
        boolean[] arr = new boolean[arr0.length];
        for(int i = 0;i < arr.length;i++) {
            arr[i] = Boolean.valueOf(arr0[i]);
        }
        return arr;
    }

    public float[] floatValue() {
        if(value == null) return new float[]{0};
        String[] arr0 = stringValue();
        float[] arr = new float[arr0.length];
        for(int i = 0;i < arr.length;i++) {
            arr[i] = Float.parseFloat(arr0[i]);
        }
        return arr;
    }

    public double[] doubleValue() {
        if(value == null) return new double[]{0};
        String[] arr0 = stringValue();
        double[] arr = new double[arr0.length];
        for(int i = 0;i < arr.length;i++) {
            arr[i] = Double.parseDouble(arr0[i]);
        }
        return arr;
    }

    @Override
    public Object[] value() {
        return value;
    }
}
