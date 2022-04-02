package ex2;

class SharedData {

    private int globalCount;

    SharedData() {
        this.globalCount = 0;
    }

    public int getGlobalCount() {
        return globalCount;
    }

    public synchronized void setGlobalCount(int sum) {
        this.globalCount = sum;
    }
}
