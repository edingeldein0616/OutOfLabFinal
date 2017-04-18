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

     /**
     * Voting method for the Legislator Class.
     * @param b The input bill that the legislator is to vote on.
     * @return boolean Returns true if the legislator decides to vote yes, returns false if voting no.
     */
     public boolean vote(Bill b)
     {
          //Set important variables
          int index = b.getAuthorsArray().size();
          ArrayList<Legislator> authors = b.getAuthorsArray();
          int sameParty = 0, diffParty = 0;
          double samePartyOdds = 0.0, diffPartyOdds = 0.0;
          
          //Count same and different party authors
          for(Legislator leg : authors) {
               if(leg.getParty() == getParty()) {
                    sameParty++;
               } else {
                    diffParty++;
               }
          }
          
          //Calculate sameParty odds
          samePartyOdds = recursion(sameParty, true); //To calculate same party odds, enter second parameter as true
          diffPartyOdds = recursion(diffParty, false);
          
          //Decide what voting outcome is
          double factor = ((samePartyOdds * sameParty) + (diffPartyOdds * diffParty)) / ((double)(sameParty + diffParty)); //From OOL3 pdf.
          factor *= 10;
          //System.out.println(getName() + ", " + getParty() + ": " + factor);
          if (rand.nextInt(10) < factor)
               return true;
          return false;
     }

     /**
     * The recursion method that calculates the voting odds for the individual legislator based on the party compositon
     * of the bill that they are voting on.
     * @param n The number of bill authors that are either the same or different party as the voting legislator
     * @param same Decides whether the method is calculating same voting odds or different voting odds.
     * (meaning same legislator or different legislator voting odds)
     * @return double Returns a double that will be added to the factor that represents the voting odds of the legislator.
     */
     public double recursion(int n, boolean same) {
          double factor = 0.0;
          if(same) {
               if(n == 0)
                    return 0.0;
               if(n > 0) {
                    factor = 0.6 + (1.0 - (recursion(n - 1, true)) * 0.6);
                    return factor;
               }
          } else {
               if(n == 0) {
                    return 0.0;
               } if(n > 0) {
                    factor = 0.1 - Math.pow(0.1, n) - recursion(n-1, false);
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

               //adds random number (2 - 6) of randomly picked authors to bill (must be in same committee)
               int numAuthors = rand.nextInt(4) + 2;
               for(int i = 1; i <= numAuthors; i++) {
                    boolean picked = false;                    
                    while(!picked){     
                         int index = rand.nextInt(chamber.size());
                         Legislator temp = chamber.get(index);
                         if(temp.getCommittee() == getCommittee()) {
                              authors.add(temp); //Same legislator may be multiple authors
                              picked = true;
                         }
                    }
               }

               return new Bill(getCommittee(), authors);
          }
          return null;
     }
}