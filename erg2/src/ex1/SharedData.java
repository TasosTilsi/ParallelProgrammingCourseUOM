package ex1;

import java.util.Arrays;

class SharedData {

    int[] histogram;
    int size;
    int cores;

    SharedData(int size) {
        this.size = size;
        histogram = new int[size];
        initializeTable();
    }

    SharedData(int size, int cores) {
        this.size = size;
        this.cores = cores;
        histogram = new int[size];
        initializeTable(cores);
    }

    private void initializeTable() {
        for (int i = 0; i < size; i++) {
            histogram[i] = 0;
        }
    }

    private void initializeTable(int cores) {
        int myAlphabetSizeIndex = size / cores;

        Thread[] threads = new Thread[cores];

        for (int i = 1; i <= cores; i++) {
            int finalI = i;
            threads[i - 1] = new Thread() {
                @Override
                public void run() {
                    if (finalI != cores) {
                        for (int j = myAlphabetSizeIndex * (finalI - 1); j < myAlphabetSizeIndex * finalI; j++) {
                            histogram[j] = 0;
                        }
                    } else {
                        for (int j = myAlphabetSizeIndex * (finalI - 1); j < myAlphabetSizeIndex; j++) {
                            histogram[j] = 0;
                        }
                    }
                }
            };

            threads[i - 1].start();
        }

        /* wait for threads to finish */
        for (int i = 0; i < cores; i++) {
            try {
                threads[i].join();
            } catch (InterruptedException e) {
                System.err.println("this should not happen");
            }
        }
    }

    public synchronized void count(int character) {
        histogram[character]++;
    }

    public void printData() {
        Arrays.stream(histogram).distinct().forEach(System.out::println);
    }
}
