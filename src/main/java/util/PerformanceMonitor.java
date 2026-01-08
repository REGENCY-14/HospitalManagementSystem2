package util;

public class PerformanceMonitor {
    private long startTime;
    private long endTime;
    private String operationName;
    
    /**
     * Constructor for PerformanceMonitor
     */
    public PerformanceMonitor(String operationName) {
        this.operationName = operationName;
        this.startTime = System.currentTimeMillis();
    }
    
    /**
     * Starts the timer
     */
    public void start() {
        this.startTime = System.currentTimeMillis();
    }
    
    /**
     * Stops the timer and returns the elapsed time in milliseconds
     */
    public long stop() {
        this.endTime = System.currentTimeMillis();
        return getElapsedTime();
    }
    
    /**
     * Returns the elapsed time in milliseconds
     */
    public long getElapsedTime() {
        return endTime - startTime;
    }
    
    /**
     * Prints the operation name and elapsed time
     */
    public void printElapsedTime() {
        System.out.println(operationName + " took " + getElapsedTime() + " ms");
    }
    
    /**
     * Returns formatted string with operation name and elapsed time
     */
    public String getReport() {
        return operationName + " - Execution Time: " + getElapsedTime() + " ms";
    }
    
    /**
     * Resets the monitor
     */
    public void reset() {
        this.startTime = System.currentTimeMillis();
        this.endTime = 0;
    }
}
