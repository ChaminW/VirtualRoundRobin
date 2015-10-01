package Sheduler;

import Processes.Process;
import app.OSGUI;
import computer.MainMemmory;
import java.util.ArrayList;

import computer.ProcessQueue;

/**
 *
 * @author Malith
 */
public class LTS implements Runnable {

    ArrayList<Processes.Process> processList;
    ProcessQueue readyQueue;
    MainMemmory mainMemmory;
    int interval = 0;
    int stopwatch = 0;
    int factor = 1;
    OSGUI osgui;

    public LTS(ArrayList<Process> processList, MainMemmory mainMemmory, int stopwatch, int factor, OSGUI osgui) {
        this.processList = processList;
        this.readyQueue = mainMemmory.getReadyQueue();
        this.mainMemmory = mainMemmory;
        this.stopwatch = stopwatch;
        this.factor = factor;
        this.osgui = osgui;

    }

    @Override
    public void run() {

        for (Process process : processList) {

            interval = process.getArrivalTime() - stopwatch; //get the time that the LTS must wait before releasing a new process to the ready queue

            //if the first procecss doesn't addmitted at 0 second.
            if (processList.indexOf(process) == 0 && interval > 0) {
                osgui.skip();
                stopwatch++;
            }

            // wait before releasing the new process
            try {
                Thread.sleep((long) (interval * 1000 * factor)); // 1000 because of sleep() takes milliseconds.   
                stopwatch += interval;

                //release the new process to the ready queue
                readyQueue.enqueue(process);
                readyQueue.dislayEnqueue(0, process);

                System.out.println("a new process " + process.getName() + " is added to the ready Queue at " + stopwatch + "s");

            } catch (InterruptedException ex) {
                System.out.println("interrupted"); //continue sending new processes to the ready queue
            }
        }

    }
}
