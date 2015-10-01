/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Processes;

/**
 *
 * @author Malith
 */
public class Process {

    int index;
    final int arrivalTime;
    final int serviceTime;
    final int initialTimeSlice;
    int timeSlice;
    int remainingServiceTime;

    int IORequestTime = 10000; //  time after arrival which the process request an IO 
    int IOCostTime; // time duration that takes to get the IO request
    boolean IOhappens; // this will be tue if this process will blocked due to an IO request before timeout.
    int totalCPUburst = 0;
    boolean hasIOrequest;
    int ioInTime;
    int releaseTime;

    public Process(int arrivalTime, int serviceTime, int timeSlice, int index) {
        this.arrivalTime = arrivalTime;
        this.serviceTime = serviceTime;
        this.timeSlice = timeSlice;
        this.initialTimeSlice = timeSlice;
        this.remainingServiceTime = serviceTime;
        this.index = index;
        hasIOrequest = false;
        IOhappens = false;
    }

    // If user inputs an IO bound process
    public Process(int arrivalTime, int serviceTime, int timeSlice, int index, int IORequestTime, int IOCompetionTime) {
        System.out.println("******************************");
        this.arrivalTime = arrivalTime;
        this.serviceTime = serviceTime;
        this.timeSlice = timeSlice;
        this.initialTimeSlice = timeSlice;
        this.remainingServiceTime = serviceTime;
        this.index = index;

        hasIOrequest = true;
        this.IORequestTime = IORequestTime;
        this.IOCostTime = IOCompetionTime;

        IOhappens = true;
    }

    public void setIoInTime(int ioInTime) {
        this.ioInTime = ioInTime;
    }

    public synchronized void setIOhappens(boolean IOhappens) {
        this.IOhappens = IOhappens;
    }

    public int getIOCostTime() {
        return IOCostTime;
    }

    public synchronized int getRemainingServiceTime() {
        return remainingServiceTime;
    }

    public boolean isIOhappens() {
        return IOhappens;
    }

    public int getServiceTime() {
        return serviceTime;
    }

    public void setReleaseTime(int releaseTime) {
        this.releaseTime = releaseTime;
    }

    public int getReleaseTime() {
        return releaseTime;
    }

    public synchronized int getName() {
        return index;
    }

    public void setTimeSlice(int timeSlice) {
        this.timeSlice = timeSlice;
    }

    public synchronized int getTimeSlice() {
        return timeSlice;
    }

    public synchronized int getArrivalTime() {
        return arrivalTime;
    }

    public void runProcess(int nextCPUtime) {
        remainingServiceTime -= nextCPUtime;
        totalCPUburst += nextCPUtime;
    }

    //get the time period that the process will be in the CPU next
    public synchronized int getNextCPUtime(int stopwatch) {

        // if a process has chosen from the Auxiliary queue
        if (timeSlice != initialTimeSlice) {
            setIOhappens(false);
            int temp = timeSlice;
            timeSlice = initialTimeSlice;
            return temp;
        }

        int timeSpent = stopwatch - arrivalTime; // (cpu burst + io burst)
        int DueTimeToIORequest = IORequestTime - totalCPUburst; // the remainig time before the IO request happens
        System.out.println(DueTimeToIORequest + "Due Time to IORequest ");

        // if the process will be blocked before being timeout
        if (isIOhappens() && IORequestTime > totalCPUburst && DueTimeToIORequest <= timeSlice) {// watchout when using auxiliry queue 
            setIOhappens(true);
            timeSlice = timeSlice - DueTimeToIORequest;
           
            return DueTimeToIORequest;
        }

        // if the process's service time is over in between the next timeslice
        if (remainingServiceTime < timeSlice) {
            setIOhappens(false);//??? do I need this

            return remainingServiceTime;
            
        } else if (remainingServiceTime > 0) {
            setIOhappens(false);
            timeSlice = initialTimeSlice;

            return timeSlice;
        }
        return 0;
    }

    public boolean getHasIOrequest() {
        return hasIOrequest;
    }

    public int getIORequestTime() {
        return IORequestTime;
    }

    public void setHasIOrequest(boolean hasIOrequest) {
        this.hasIOrequest = hasIOrequest;
    }

}
