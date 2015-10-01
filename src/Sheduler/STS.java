/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Sheduler;

import Processes.Process;
import app.OSGUI;

import computer.CPU;
import computer.MainMemmory;
import computer.ProcessQueue;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

//  STS = Short Time Sheduler
/**
 *
 * @author Malith
 */
public class STS implements Runnable {

    ArrayList<Process> processList;
    CPU cpu;

    ProcessQueue readyQueue;
    ProcessQueue Auxiliary;
    ProcessQueue IoWaiting;

    int factor; // controlls the speed of the simulator(1= realtime -  2,3,4,5 makes it slower)
    int stopwatch; // contains the current time of the simulator
    int queueIndex; // index name of 3 queues (0=readyQueue, 1=AuxiliaryQueue, 2=IOWaitingQueue)

    Process chosenProcess; // the process chosen to send in to the CPU
    Process preemptedProcess;
    Process blockedProcess;

    int nextCPUtime; // time that the chosen process will be in the CPU ( equal or smaller than timeslice)
    int releaseCount = 0; //count the released processes
    OSGUI osgui;

    public STS(ArrayList<Process> processList, CPU cpu, MainMemmory mainMemmory, int stopwatch, int factor, OSGUI osgui) {
        this.processList = processList;
        this.cpu = cpu;

        this.readyQueue = mainMemmory.getReadyQueue();
        this.IoWaiting = mainMemmory.getIoWaiting();
        this.Auxiliary = mainMemmory.getAuxiliary();

        this.stopwatch = stopwatch;
        this.factor = factor;
        this.osgui = osgui;
    }

    @Override
    public void run() {
        while (readyQueue.isEmpty()) {
        } //wait until LTS sends a new process to the ready queue

        while (true) {

            // If there isn't any process in ready or Auxiliary queue to dispatch into the CPU
            if (readyQueue.isEmpty() && Auxiliary.isEmpty()) {
                try {
                    cpu.getGui().skip(); // skip painting the current progressbar in the gui

                    Thread.sleep(1000 * factor);
                    stopwatch++;

                } catch (InterruptedException ex) {
                    Logger.getLogger(STS.class.getName()).log(Level.SEVERE, null, ex);
                }

            } else {

                System.out.println("");
                // if there's a process in the Auxiliary queue, take it! (because we give priority to this queue over the ready queue)
                if (!Auxiliary.isEmpty()) {
                    chosenProcess = Auxiliary.dequeue();
                    queueIndex = 1;
                    Auxiliary.displayDequeue(queueIndex, chosenProcess);

                } else {
                    chosenProcess = readyQueue.dequeue();
                    queueIndex = 0;
                    readyQueue.displayDequeue(queueIndex, chosenProcess);
                }

                // send the selected process to the CPU
                cpu.dispatch(chosenProcess);
                System.out.println("the chosen process " + chosenProcess.getName() + " is dispatched to the CPU at " + stopwatch);

                nextCPUtime = chosenProcess.getNextCPUtime(stopwatch); // get the time, the proces will be in the CPU (always less than or equal to time slice)

                // debuging Exception
                if (nextCPUtime == 0) {
                    nextCPUtime = chosenProcess.getTimeSlice();
                    System.out.println("++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
                }

                //execute the process
                cpu.execute(nextCPUtime, chosenProcess);

                // if this chosen process will get blocked due to an IO request after dispatching
                if (chosenProcess.isIOhappens()) {
                    // wait before blocking the running process (no more than a timeslice)
                    try {
                        Thread.sleep((long) (nextCPUtime * 1000 * factor)); // 1000 because of sleep() takes milliseconds.   
                        stopwatch += nextCPUtime;

                    } catch (InterruptedException ex) {
                        System.out.println("interrupted"); //continue sending new processes to the ready queue
                    }

                    //block the running process 
                    blockedProcess = cpu.block();
                    System.out.println("the running process " + blockedProcess.getName() + " is blocked at " + stopwatch + "s");

                    // add the blocked process in to the IO queue 
                    IoWaiting.enqueue(blockedProcess, stopwatch);
                    IoWaiting.dislayEnqueue(2, blockedProcess);

                    System.out.println("the blocked process " + blockedProcess.getName() + " is enqueed to the IO Queue at " + stopwatch + "s");

                } // if this chosen process will get preempted, without being blocked dueto an IO request after dispatching
                else {

                    // wait before preempting the running process
                    try {
                        Thread.sleep((long) (nextCPUtime * 1000 * factor)); // 1000 because of sleep() takes milliseconds.   
                        stopwatch += nextCPUtime;

                    } catch (InterruptedException ex) {
                        System.out.println("interrupted"); //continue sending new processes to the ready queue
                    }

                    //preempt the running process 
                    preemptedProcess = cpu.preempt();
                    System.out.println("the running process " + preemptedProcess.getName() + " is preempted at " + stopwatch + "s");

                    // add the preempted process in to the ready queue if there is remainig service time to do, else relese the process
                    if (preemptedProcess.getNextCPUtime(stopwatch) > 0) {
                        if (!readyQueue.isEmpty() || !Auxiliary.isEmpty()) {
                            readyQueue.dislayEnqueue(0, chosenProcess);
                        }
                        readyQueue.enqueue(preemptedProcess);

                        System.out.println("the preempted process " + preemptedProcess.getName() + " is enqueed backck to the ready Queue at " + stopwatch + "s");
                    } else {
                        System.out.println("the preempted process " + preemptedProcess.getName() + " is released at " + stopwatch + "s");
                        preemptedProcess.setReleaseTime(stopwatch);
                        releaseCount++;

                        // when all the processes are finished 
                        if (releaseCount == processList.size()) {

                            // to store details about processes(turn around time waiting time)
                            int[][] data = new int[processList.size()][2];

                            for (int i = 0; i < processList.size(); i++) {
                                int releaseTime = processList.get(i).getReleaseTime();
                                if (processList.get(0).getArrivalTime() != 0) {
                                    releaseTime += processList.get(0).getArrivalTime();
                                }
                                int turnoroundTime = releaseTime - processList.get(i).getArrivalTime();
                                data[i][0] = turnoroundTime;
                                data[i][1] = turnoroundTime - processList.get(i).getServiceTime();
                            }
                            osgui.FinalResultDisplay(data);

                            cpu.getGui().skip();

                            // sleep the thread
                            while (true) {
                                try {
                                    Thread.sleep(10000);
                                } catch (InterruptedException ex) {
                                    Logger.getLogger(STS.class.getName()).log(Level.SEVERE, null, ex);
                                }
                            }
                        }
                    }

                }

            }
        }
    }

}
