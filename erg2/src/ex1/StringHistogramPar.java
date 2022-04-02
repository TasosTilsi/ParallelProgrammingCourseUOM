package ex1;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class StringHistogramPar {

    public static void main(String[] args) throws IOException {

        double startTime = System.currentTimeMillis();

        String fileString = new String(Files.readAllBytes(Paths.get("/home/tasostilsi/Development/Projects/pamak/parallelProgramming/erg2/resources/chromosome.txt")));//, StandardCharsets.UTF_8);
        int systemCores = Runtime.getRuntime().availableProcessors();
        System.out.println(String.format("This system has [%d] Cores", systemCores));
        Thread[] threads = new Thread[systemCores];

        char[] text = new char[fileString.length()];

        int fileStringLength = fileString.length();

        int myFileStringLengthIndex = fileStringLength / systemCores;

        int alphabetSize = 256;

        SharedData histogram = new SharedData(alphabetSize, systemCores);

        for (int i = 1; i <= systemCores; i++) {
            int finalI = i;
            threads[i - 1] = new Thread() {
                @Override
                public void run() {
                    if (finalI != systemCores) {
                        for (int j = myFileStringLengthIndex * (finalI - 1); j < myFileStringLengthIndex * finalI; j++) {
                            text[j] = fileString.charAt(j);
                            histogram.count(text[j]);
                        }
                    } else {
                        for (int j = myFileStringLengthIndex * (finalI - 1); j < fileStringLength; j++) {
                            text[j] = fileString.charAt(j);
                            histogram.count(text[j]);
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

        histogram.printData();

        double endTime = System.currentTimeMillis();

        System.out.println("The execution time was: " + (endTime - startTime) / 1000.0 / 60.0 + " seconds");
    }

}