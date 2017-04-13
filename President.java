//Java Doc Comments are left as an exercise to the students
import java.util.*;

public class President
{
     private String name = "";
     private PartyType party = null;
     private ArrayList<Bill> pending = new ArrayList<Bill>();
     
     private Random rand = new Random();
     
     public President(String n, PartyType p)
     {
          setName(n);
          setParty(p);
     }
     
     public void getBill(Bill b)
     {
          pending.add(b);
          //System.err.println("got a bill");
     }
     
     public ArrayList<Bill> decide()
     {
          ArrayList<Bill> decided = new ArrayList<Bill>();
          for (int x = pending.size() - 1; x >= 0; x--)
          {
               if (rand.nextInt(100)<85)
               {
                    int factor = 75;
                    if (pending.get(x).getAuthor(0).getParty() == getParty())
                         factor = 95;
                    if (rand.nextInt(100) < factor)
                    {
                         pending.get(x).setStatus(BillStatus.Approved);
                        // System.err.println("signed");
                    }
                    else 
                    {
                         pending.get(x).setStatus(BillStatus.Rejected);
                        // System.err.println("rejected");
                    }
                    decided.add(pending.get(x));
                    pending.remove(pending.get(x));
               }
          }
          return decided;
     }
      
     public PartyType getParty()
     {
          return party;
     }

     public void setParty(PartyType p)
     {
          party = p;
     }
     
     public String getName()
     {
          return name;
     }

     public void setName(String n)
     {
          name = n;
     }
}