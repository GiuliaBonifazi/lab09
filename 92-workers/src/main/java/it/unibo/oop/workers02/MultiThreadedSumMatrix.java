package it.unibo.oop.workers02;

import java.util.List;

public class MultiThreadedSumMatrix implements SumMatrix {

    private final int nthread;

    public MultiThreadedSumMatrix(int n) {
        this.nthread = n;
    }

    private class Worker extends Thread {
        private final List<Integer> list;
        private final int startpos;
        private final int nelem;
        private long res;

        private Worker(final List<Integer> list, final int startpos, final int nelem) {
            super();
            this.list = list;
            this.startpos = startpos;
            this.nelem = nelem;
        }

        @Override
        public void run() {
            
        }
    }

    @Override
    public double sum(double[][] matrix) {
        
        return 0;
    }
    
}
