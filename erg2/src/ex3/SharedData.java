package ex3;

import java.util.concurrent.ThreadLocalRandom;

class SharedData {

    private final int[] mainArray;
    private final int[] countArray;
    private final int systemCores;
    private final int size;
    private int myPlace;

    SharedData(int size, int systemCores) {
        this.myPlace = 0;
        this.size = size;
        this.systemCores = systemCores;
        this.mainArray = new int[size];
        this.countArray = new int[size];
        initializeTables(mainArray, countArray, size, systemCores);
    }

    private static void initializeTables(int[] a, int[] b, int size, int systemCores) {

        int myFirstIndex = size / systemCores;

        Thread[] threads = new Thread[systemCores];
        for (int i = 1; i <= systemCores; i++) {
            int finalI = i;
            threads[i - 1] = new Thread() {
                @Override
                public void run() {
                    if (finalI != systemCores) {
                        for (int j = myFirstIndex * (finalI - 1); j < myFirstIndex * finalI; j++) {
                            a[j] = ThreadLocalRandom.current().nextInt(size + 1);
                            b[j] = 0;
                        }
                    } else {
                        for (int j = myFirstIndex * (finalI - 1); j < size; j++) {
                            a[j] = ThreadLocalRandom.current().nextInt(size + 1);
                            b[j] = 0;
                        }
                    }
                }
            };
            threads[i - 1].start();
        }

        /* wait for threads to finish */
        for (int i = 0; i < systemCores; i++) {
            try {
                threads[i].join();
            } catch (InterruptedException e) {
                System.err.println("this should not happen");
            }
        }
    }

    public int[] getMainArray() {
        return mainArray;
    }

    public int[] getCountArray() {
        return countArray;
    }

    public int getPlace() {
        return myPlace;
    }

    public synchronized void setPlace(int value) {
        this.myPlace = value;
    }

    public synchronized void incr() {
        this.myPlace++;
    }

    public int getSystemCores() {
        return systemCores;
    }

    public int getSize() {
        return size;
    }

    public synchronized void setMainArray(int place, int value) {
        this.mainArray[place] = value;
    }

    public synchronized void setCountArray(int place, int value) {
        this.countArray[place] = value;
    }
}
