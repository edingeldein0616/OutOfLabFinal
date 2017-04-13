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
          return "This Bill is:\t" + getName() + "\n\tType:\t" + getType()+"\n\tAuthors:\t"+getAuthorsString()+"\n\tStatus:\t" +getStatus()+"\n\tText:\t"+getText()+"\n";

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

     public ArrayList<Legislator> getAuthorsArray()
     {
          return authors;
     }

     public String getAuthorsString() {
          String output = "";
          output += getAuthor(0).getName();
          for(int i = 1; i < getAuthorsArray().size(); i++) {
               output += ", " + getAuthor(i).getName();
          }
          return output;
     }
     
     public void setAuthors(ArrayList<Legislator> a)
     {
          authors = a;
     }

     public void addAuthor(Legislator author) {
          authors.add(author);
     }
}