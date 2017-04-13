//Java Doc Comments are left as an exercise to the students
import java.util.*;
public class Leader extends Legislator
{
     ArrayList<Bill> newBills = new ArrayList<Bill>();
     ArrayList<Bill> pc = new ArrayList<Bill>();//Passed Committee
     ArrayList<Bill> rejected = new ArrayList<Bill>();//vetoed
     Random rand = new Random();
     
     public ArrayList<Bill> callForVote()
     {
          ArrayList<Bill> call= new ArrayList<Bill>();
          for (int x = pc.size() - 1; x >= 0; x--)
          {
               if (rand.nextInt(10) < 8)
               {
                    //System.out.println("Call For Vote:\n" + pc.get(x));
                    call.add(pc.get(x));
                    pc.remove(pc.get(x));  
               }
          }
          return call;
     }
     
     public ArrayList<Bill> callOverrides()
     {
          for (Bill b: rejected)
          {
               System.out.println("Calling for override of Veto of Bill\n"+b);
          }
          ArrayList<Bill> temp = rejected;
          rejected = new ArrayList<Bill>();
          return temp;
     }
     
     public Leader(Legislator l)
     {
          super(l);
     }
     
     public void newBill(Bill b)
     {
          newBills.add(b);
     }
     
     public void passedCommittee(Bill b)
     {
          pc.add(b);
     }
     
     public void getRejected(Bill b)
     {
          rejected.add(b);
     }
     
     public ArrayList<Bill> announceNewBills()
     {
          ArrayList<Bill> announced = new ArrayList<Bill>();
          for (int x = newBills.size() - 1; x >=0; x--)
          {
               if (rand.nextInt(10) < 7)
               {
                    //System.out.println("New Bill:\n" + newBills.get(x));
                    announced.add(newBills.get(x));
                    newBills.get(x).setStatus(BillStatus.InCommittee);
                    newBills.remove(newBills.get(x)); 
               }
          }
          return announced;
          
     }
}