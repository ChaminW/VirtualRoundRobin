
package computer;
import Processes.Process;
import app.OSGUI;
import java.util.LinkedList;



/**
 *
 * @author Malith
 */
public class ProcessQueue{
    LinkedList<Process> queue;
    OSGUI osgui;
    
    //this can be removed later
     public ProcessQueue() {
        this.queue = new LinkedList<>();
       
    }
    public ProcessQueue(OSGUI osgui) {
        this.queue = new LinkedList<>();
        this.osgui = osgui;
    }
    
  
    public synchronized void enqueue(Process process){
        queue.add(process);
      
    }
    
    public synchronized void enqueue(Process process, int stopwatch){
        process.setIoInTime(stopwatch+0); // to coppy the value, not to send the reference??
        queue.add(process);
      
    }
    public synchronized Process dequeue(){
        
        return queue.remove();
        
    }
    public synchronized Process peek(){
        return queue.peek();
    }
    
    public synchronized void dislayEnqueue(int queueIndex, Process process){
        //display in the queue in the GUI
        osgui.enqueue(queueIndex, process.getName(), process.getRemainingServiceTime(), process.getServiceTime());
    }
    
    public synchronized void displayDequeue(int queueIndex, Process process){
        osgui.dequeue(queueIndex, process.getName());
        System.out.println("DDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDdqueue!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!! "+process.getName());
    }
    public synchronized boolean isEmpty(){
        return queue.size()==0;
    }
}
