//Java Doc Comments are left as an exercise to the students
import java.util.*;
public class Legislator
{
     private String name = "";
     private StateType state = null;
     private PartyType party = null;
     private BillType committee = null;
     
     private Random rand = new Random();
     
     public Legislator(String n, StateType s, PartyType p)
     {
          setState(s);
          setParty(p);
          setName(n);
     }
     
     public Legislator(Legislator l)
     {
          setState(l.getState());
          setParty(l.getParty());
          setName(l.getName());
     }

     public boolean vote(Bill b)
     {
          int index = 0;
          double factor = 3.0;
          if (b.getAuthor(index).getParty() == getParty()) {
               factor = recursion(factor, index++, b);
               System.out.println(factor);
          }
          if (rand.nextInt(10) < factor)
               return true;
          return false;
          //return true;
     }

     public double recursion(double factor, int index, Bill b) {
          if(index < b.getAuthorsArray().size() - 1) {
               if(b.getAuthor(index).getParty() == getParty()) {
                    factor += (10.0 - 6.0) * 6.0;
                    index++;
                    factor = recursion(factor, index, b);
               } else {
                    factor -= Math.pow(1.0, index + 1);
                    index++;
                    factor = recursion(factor, index, b);
               }
          } else {
               if(b.getAuthor(index).getParty() == getParty()) {
                    factor += (10.0 - 6.0) * 6.0;
                    return factor;
               } else {
                    factor -= Math.pow(1.0, index + 1);
                    return factor;
               }
          }
          return factor;
     }
     
     public String getName()
     {
          return name;
     }


     public void setName(String n)
     {
          name = n;
     }

     public StateType getState()
     {
          return state;
     }


     public void setState(StateType s)
     {
          state = s;
     }

     public PartyType getParty()
     {
          return party;
     }

     public BillType getCommittee()
     {
          return committee;
     }
     
     public void setCommittee(BillType b)
     {
          committee = b;
     }

     public void setParty(PartyType p)
     {
          party = p;
     }
     
     public Bill draftBill(ArrayList<Legislator> chamber)
     {
          if (rand.nextInt(20) ==  0) { //5%

               //adding this author
               ArrayList<Legislator> authors = new ArrayList<Legislator>();
               authors.add(this);

               //adds 2 more randomly picked authors to bill (must be in same committee)
               for(int i = 1; i <= 3; i++) {
                    boolean picked = false;                    
                    while(!picked){     
                         int index = rand.nextInt(chamber.size());
                         Legislator temp = chamber.get(index);
                         
                         if(temp.getCommittee() == getCommittee()) {
                              authors.add(temp); //May be the same 2-3 legislators as authors
                              picked = true;
                         }
                    }
               }

               return new Bill(getCommittee(), authors);
          }
          return null;
     }
}