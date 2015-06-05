package info.lynxnet.warmups;

/**
 *
 */
public class DecToBinConverter {
    private static final String ERROR = "ERROR";

    public static String toBinary(String decimal) {
        if (decimal == null) {
            return ERROR;
        }
        if (!decimal.matches("\\d+(\\.\\d+)?")) {
            return ERROR;
        }
        String intPart = null;
        String fracPart = null;
        int point = decimal.indexOf(".");
        if (point >= 0) {
            intPart = decimal.substring(0, point);
            fracPart = decimal.substring(point + 1);
        } else {
            intPart = decimal;
        }
        int intVal = Integer.parseInt(intPart);
        StringBuilder result = new StringBuilder();
        if (intVal == 0) {
            result.append("0");
        } else {
            while (intVal != 0) {
                int remainder = intVal % 2;
                intVal >>= 1;
                result.append(remainder == 0 ? "0" : "1");
            }
            result.reverse();
        }
        if (fracPart != null) {
            int frac = Integer.parseInt(fracPart);
            int closestTen = 1;
            for (int i = 0; i < fracPart.length(); i++) {
                closestTen *= 10;
            }
            result.append(".");
            int stopper = 50;
            while (frac > 0) {
                int digit = (frac << 1) / closestTen;
                frac = (frac << 1) % closestTen;
                if (stopper-- == 0) {
                    return ERROR;
                }
                result.append(digit == 0 ? "0" : "1");
            }
        }
        return result.toString();
    }

    public static void main(String[] args) {
        System.out.println(toBinary("127"));
        System.out.println(toBinary("6"));
        System.out.println(toBinary("8.0"));
        System.out.println(toBinary("8.5"));
        System.out.println(toBinary("8.75"));
        System.out.println(toBinary("8.25"));
        System.out.println(toBinary("8.00869140625"));
        System.out.println(toBinary("8a.75"));
        System.out.println(toBinary("10.766"));
    }
}
