package ex3;

class CountSortArgsTimePar {
    public static void main(String[] args) {

        int systemCores = Runtime.getRuntime().availableProcessors();
        System.out.println(String.format("This system has [%d] Cores", systemCores));

//        letUserDecideTheSize(args);

        int size = 10000;

        SharedData myData = new SharedData(size, systemCores);
        Thread[] threads = new Thread[systemCores];

//        printTable(size, myData.getMainArray());

        // get current time
        long start = System.currentTimeMillis();

//        for (int i = 0; i < size; i++) {

        int myFirstIndex = size / systemCores;

        for (int k = 1; k <= systemCores; k++) {
            int finalk = k;
//                int finalI = i;
            threads[k - 1] = new Thread() {
                @Override
                public void run() {
                    for (int i = 0; i < size; i++) {
                        myData.setPlace(0);
                        if (finalk != systemCores) {
                            for (int j = myFirstIndex * (finalk - 1); j < myFirstIndex * finalk; j++) {
                                if (myData.getMainArray()[i] < myData.getMainArray()[j] || (myData.getMainArray()[i] == myData.getMainArray()[j] && j < i)) {
                                    myData.incr();
                                }
                            }
                        } else {
                            for (int j = myFirstIndex * (finalk - 1); j < size; j++) {
                                if (myData.getMainArray()[i] < myData.getMainArray()[j] || (myData.getMainArray()[i] == myData.getMainArray()[j] && j < i)) {
                                    myData.incr();
                                }
                            }
                        }
                        myData.setCountArray(myData.getPlace(), myData.getMainArray()[i]);
                    }
                }
            };
            threads[k - 1].start();
        }


        /* wait for threads to finish */
        for (int k = 0; k < systemCores; k++) {
            try {
                threads[k].join();
            } catch (InterruptedException e) {
                System.err.println("this should not happen");
            }
        }

//            for (int j = 0; j < size; j++) {
//                if (myData.getMainArray()[i] < myData.getMainArray()[j] || (myData.getMainArray()[i] == myData.getMainArray()[j] && j < i)) {
//                    myplace++;
//                    myData.setPlace(myplace);
//                }
//            }

//            myData.setCountArray(myplace, mynum);
//        }

        // get current time and calculate elapsed time
        long elapsedTimeMillis = System.currentTimeMillis() - start;
        System.out.println("time in ms = " + elapsedTimeMillis);

        printTable(size, myData.getCountArray());
    }

    private static void printTable(int size, int[] b) {
        /* for debugging */
        for (int i = 0; i < size; i++)
            System.out.println(b[i]);
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
