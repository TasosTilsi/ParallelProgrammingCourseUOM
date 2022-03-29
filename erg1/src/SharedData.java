import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class SharedData {

    private double globalSum;
    private Lock globalSumLock = new ReentrantLock();

    public SharedData() {
        globalSum = 0.0;
    }

    public double getGlobalSum() {
        return globalSum;
    }

    public void setGlobalSum(double globalSum) {
        this.globalSum = globalSum;
    }

    public Lock getGlobalSumLock() {
        return globalSumLock;
    }

    public void setGlobalSumLock(Lock globalSumLock) {
        this.globalSumLock = globalSumLock;
    }
}
