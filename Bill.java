import java.util.*;

//Java Doc Comments are left as an exercise to the students
public class Bill
{
     private BillStatus status = null;
     private String name = "";
     private String text = "";
     private BillType type = null;
     private ArrayList<Legislator> authors = null;
     private static int billCount = 0;
     private boolean c1 = false; //Passed first Committee
     public int creationDate = -1;
     public int trashDate = -1;
     public int signDate = -1;
     
     public boolean isHalfway()
     {
          return c1;
     }
     
     public Bill(String n, String t, BillType bt, ArrayList<Legislator> a)
     {
          setStatus(BillStatus.Drafted);
          setName(n);
          setText(t);
          setType(bt);
          setAuthors(a);
          setStatus(BillStatus.Drafted);
     }
     
     public String toString()
     {    
          int[] partyCount = getPartyNumbers();
          String insert = "\tParty Counts:\tDems: "+partyCount[0]+"\tReps: "+partyCount[1]+"\tLibs: "+partyCount[2];
          return "This Bill is:\t" + getName() + "\n\tType:\t" + getType()+"\n\tAuthors: "+getAuthorsString()+"\n"+insert+"\n\tStatus:\t" +getStatus()+"\n\tText:\t"+getText()+"\n";

     }
     
     
     public Bill(BillType bt, ArrayList<Legislator> a)
     {
          setName("This is bill " + billCount++ + ".");
          String billText = "";
          billText = billText + "Yadda Yadda ";
          setText(billText);
          setType(bt);
          setAuthors(a);
          setStatus(BillStatus.Drafted);
     }
     
     public BillStatus getStatus()
     {
          return status;
     }
     
     public void setStatus(BillStatus bs)
     {
          status = bs;
          if (bs == BillStatus.OnTheFloor)
               c1 = true;
     }
     
     public String getName()
     {
          return name;
     }
     
     public void setName(String n)
     {
          name = n;
     }
     
     public String getText()
     {
          return text;
     }
     
     public void setText(String t)
     {
          text = t;
     }
     
     public BillType getType()
     {
          return type;
     }
     
     public void setType(BillType bt)
     {
          type = bt;
     }
     
     public Legislator getAuthor(int i) {
          return authors.get(i);
     }

     /**
     * Returns the array of Legislator objects that are authors of the bill. The "main" author is the author in index 0.
     * @return authors array belonging to the bill.
     */
     public ArrayList<Legislator> getAuthorsArray()
     {
          return authors;
     }

     /**
     * Returns a string containing all of the authors names seperated by a comma.
     * @return A single string containing all of the authors names.
     */
     public String getAuthorsString() {
          String output = "";
          output += getAuthor(0).getName();
          for(int i = 1; i < getAuthorsArray().size(); i++) {
               output += ", " + getAuthor(i).getName();
          }
          return output;
     }
     
     /**
     * Adds an whole array of authors to the bill. Overwrites current author array.
     * @param a An array of authors.
     */
     public void setAuthors(ArrayList<Legislator> a)
     {
          authors = a;
     }

     /**
     * Adds a single author to the end of the authors array of the bill.
     * @param author An author legislator to add to authors array.
     */
     public void addAuthor(Legislator author) {
          authors.add(author);
     }

     /**
     * Returns an int array that contains the number of authors belonging to each party. 0 = Democrats, 1 = Republicans, 2 = Libertarians
     * @return int[] Containing the number of authors belonging to each party.
     */
     public int[] getPartyNumbers() {
          int[] nums = new int[3];
          int dems = 0, reps = 0, libs = 0;
          for(Legislator leg : authors) {
               if(leg.getParty() == PartyType.Democrat)
                    dems++;
               else if(leg.getParty() == PartyType.Republican)
                    reps++;
               else
                    libs++;
          }
          nums[0] = dems;
          nums[1] = reps;
          nums[2] = libs;
          return nums;
     }
}