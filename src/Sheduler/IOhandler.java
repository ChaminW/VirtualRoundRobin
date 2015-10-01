/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Sheduler;

import computer.ProcessQueue;
import Processes.Process;
import computer.MainMemmory;

/**
 *
 * @author Malith
 */
public class IOhandler implements Runnable {

    ProcessQueue readyQueue;
    ProcessQueue IoWaiting;
    ProcessQueue auxiliary;
    int factor = 1;
    int stopwatch;
    Process chosenProcess;
    int blockingPeriod;

    public IOhandler(MainMemmory mainMemmory, int stopwatch, int factor) {
        this.readyQueue = mainMemmory.getReadyQueue();
        this.IoWaiting = mainMemmory.getIoWaiting();
        this.auxiliary = mainMemmory.getAuxiliary();

        this.stopwatch = stopwatch;
        this.factor = factor;
    }

    @Override
    public void run() {
        while (true) {

            if (!IoWaiting.isEmpty()) {

                //chosenProcess = IoWaiting.dequeue();
                chosenProcess = IoWaiting.peek();

                blockingPeriod = chosenProcess.getIOCostTime();

                // wait until IOrequest complete
                try {
                    Thread.sleep((long) (blockingPeriod * 900 * factor)); // 1000 because of sleep() takes milliseconds.              

                } catch (InterruptedException ex) {
                    System.out.println("interrupted"); //continue sending new processes to the ready queue
                }
                IoWaiting.dequeue();
                IoWaiting.displayDequeue(2, chosenProcess);

                // transfer the IO request gained process to the ready queue
                auxiliary.enqueue(chosenProcess);
                auxiliary.dislayEnqueue(1, chosenProcess);

                System.out.println("the blocked process " + chosenProcess.getName() + " is sent to the Auxiliary queue at " + stopwatch + "s");
            } else {
            }

        }
    }

}
