package info.lynxnet.warmups;


public class BetterProgrammerTask {

    public static Change getCorrectChange(int cents) {
        if (cents < 0) {
            return null;
        }
        int dollars = cents / 100;
        int rest = cents % 100;

        int quarters = rest / 25;
        rest = rest % 25;
        int dimes = rest / 10;
        rest = rest % 10;
        int nickels = rest / 5;
        rest = rest % 5;

        Change change = new Change(dollars, quarters, dimes, nickels, rest);

        return change;

        /*
          Please implement this method to
          take cents as a parameter
          and return an equal amount in dollars and coins using the minimum number of
          coins possible.
          For example: 164 cents = 1 dollar, 2 quarters, 1 dime and 4 cents.
          Return null if the parameter is negative.

         */
    }


    // Please do not change this class
    static class Change {
        private final int _dollars;
        private final int _quarters; //25 cents
        private final int _dimes; // 10 cents
        private final int _nickels; // 5 cents
        private final int _cents; // 1 cent


        public Change(int dollars, int quarters, int dimes, int nickels, int cents) {
            _dollars = dollars;
            _quarters = quarters;
            _dimes = dimes;
            _nickels = nickels;
            _cents = cents;
        }


        public int getDollars() {
            return _dollars;
        }


        public int getQuarters() {
            return _quarters;
        }


        public int getDimes() {
            return _dimes;
        }


        public int getNickels() {
            return _nickels;
        }


        public int getCents() {
            return _cents;
        }

        @Override
        public String toString() {
            final StringBuilder sb = new StringBuilder();
            sb.append("Change");
            sb.append("{_dollars=").append(_dollars);
            sb.append(", _quarters=").append(_quarters);
            sb.append(", _dimes=").append(_dimes);
            sb.append(", _nickels=").append(_nickels);
            sb.append(", _cents=").append(_cents);
            sb.append('}');
            return sb.toString();
        }
    }

    public static void main(String[] argv) {
        System.out.println(getCorrectChange(100));

        System.out.println(getCorrectChange(144));

        System.out.println(getCorrectChange(102));

        System.out.println(getCorrectChange(256));
    }
}
