package ex3;

class CountSortArgsTimeSeq {
    public static void main(String[] args) {

        int size = 10000;

        //        letUserDecideTheSize(args);

        int[] a = new int[size];
        int[] b = new int[size];
        for (int i = 0; i < size; i++) {
//            a[i] = i;
            a[i] = (int) (Math.random() * size);
            b[i] = 0;
        }

        /* for debugging */
//        for (int i = 0; i < size; i++)
//            System.out.println(a[i]);

        // get current time
        long start = System.currentTimeMillis();

        for (int i = 0; i < size; i++) {
            int mynum = a[i];
            int myplace = 0;
            for (int j = 0; j < size; j++)
                if (mynum < a[j] || (mynum == a[j] && j < i))
                    myplace++;
            b[myplace] = mynum;
        }

        // get current time and calculate elapsed time
        long elapsedTimeMillis = System.currentTimeMillis() - start;
        System.out.println("time in ms = " + elapsedTimeMillis);

        /* for debugging */
//        for (int i = 0; i < size; i++)
//            System.out.println(b[i]);
    }

    private static int letUserDecideTheSize(String[] args) {
        int size = 0;
        if (args.length != 1) {
            System.out.println("Usage: java CountSortArgsTime <vector size>");
            System.exit(1);
        }

        try {
            size = Integer.parseInt(args[0]);
        } catch (NumberFormatException nfe) {
            System.out.println("Integer argument expected");
            System.exit(1);
        }
        if (size <= 0) {
            System.out.println("size should be positive integer");
            System.exit(1);
        }
        return size;
    }

}
