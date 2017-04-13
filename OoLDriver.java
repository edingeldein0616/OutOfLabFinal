import java.util.*;
import java.io.*;

public class OoLDriver
{
    public static String randomWord(int length) 
    {
        //Use the char value of 'a' for the base
        char base = 'a';
        Random random = new Random();
        String word = "";
        for(int j = 0; j < length; j++){
            //Int and chars can be added to to get the integer value
            //for another character
            char letter = (char)(random.nextInt(26) + base);
            //append the letter.
            word = word + letter;
        }
        return word;

    }
     
    public static void main(String[] args) throws FileNotFoundException
    {
        File inputNames = new File("LegNames.txt");
        File inputStates = new File("States.txt");
        Scanner inName = new Scanner(inputNames);
		Scanner inState = new Scanner(inputStates);
		ArrayList <Legislator> legs = new ArrayList<Legislator>();
		ArrayList <Legislator> house = new ArrayList<Legislator>();
		ArrayList <Legislator> senate = new ArrayList<Legislator>();
		ArrayList <Bill> trash = new ArrayList<Bill>();
		ArrayList <Bill> lawBook = new ArrayList<Bill>();
		final int LEGS = 535;
		Random rand = new Random();

		//Testing Data Structures...Only needed for testing!
		ArrayList <String> names = new ArrayList<String>();
		ArrayList <String> states = new ArrayList<String>();
		ArrayList <PartyType> partys = new ArrayList<PartyType>();

		//Generate Legislators
		String prevState = "";
		int stateCount = 0;
		for (int x = 0; x < LEGS; x++)
		{
			String name = inName.nextLine();
			names.add(name);
			String state = inState.nextLine();
			states.add(state);
			PartyType party = PartyType.nextParty();
			partys.add(party);
			legs.add(new Legislator(name, StateType.valueOf(state.replaceAll("\\W","")), party));
			if (state.equals(prevState))
			{
	            stateCount++;
			}
			else
			{
            	stateCount = 0;
            }
               
            if (stateCount < 2)
            {
                senate.add(legs.get(legs.size()-1));
            }
            else
            {
                house.add(legs.get(legs.size() -1 ));
            }
            prevState = state;
        }
          
        //Generate President
        String presName = inName.nextLine();
        PartyType presParty = PartyType.nextParty();
        President pres = new President(presName, presParty);
              
        inName.close();
        inState.close();
          
        //Set Leaders
        int[] partyCount = new int[PartyType.SIZE];
          
        for (Legislator l : senate)
        {
            if (l.getParty() == PartyType.Republican)
            {
                partyCount[0]++;
            }
            else if (l.getParty() == PartyType.Democrat)
            {
                partyCount[1]++;
            }
            else
            {
                partyCount[2]++;
            }
        }
          
        int max = 0;
        for (int x = 1; x < PartyType.SIZE; x++)
        {
            if (partyCount[max] < partyCount[x])
                max = x;
        }
          
        int r = rand.nextInt(senate.size());
        while (!(PartyType.VALUES[max] == senate.get(r).getParty()))
        {
            r= rand.nextInt(senate.size());
        }
          
        senate.set(r, new Leader(senate.get(r)));
         
        partyCount = new int[PartyType.SIZE];
          
        for (Legislator l : house)
        {
            if (l.getParty() == PartyType.Republican)
            {
                partyCount[0]++;
            }
            else if (l.getParty() == PartyType.Democrat)
            {
                partyCount[1]++;
            }
            else
            {
                partyCount[2]++;
            }
        }
          
        max = 0;
        for (int x = 1; x < PartyType.SIZE; x++)
        {
            if (partyCount[max] < partyCount[x])
                max = x;
        }
          
        r = rand.nextInt(house.size());
        while (!(PartyType.VALUES[max] == house.get(r).getParty()))
        {
            r= rand.nextInt(house.size());
        }
          
        house.set(r, new Leader(house.get(r)));
          
        //Divide up Committees
        Legislator[] committeesH = new Legislator[BillType.SIZE]; //(in order of ordinal)
        Legislator[] committeesS = new Legislator[BillType.SIZE]; //(in order of ordinal)
        for (int x = 0; x < house.size(); x++)
        {
            committeesH[x%BillType.SIZE] = (house.get(x));
            house.get(x).setCommittee(BillType.VALUES[x%BillType.SIZE]);
        }

        for (int x = 0; x < senate.size(); x++)
        {
            committeesS[x%BillType.SIZE] = (senate.get(x));
            senate.get(x).setCommittee(BillType.VALUES[x%BillType.SIZE]);
        }
          
          
        //****************************************************************************************************************
        //****************************************************************************************************************
        //***********************************MAIN LOOP********************************************************************
        //****************************************************************************************************************
        //****************************************************************************************************************
        final int YEAR = 1; //365;

        for (int day = 0; day < YEAR; day++)
        {
            //Who wants to author bills
            //Also sends those bills to head of their units
            for (Legislator l : senate)
            {
                Bill b = l.draftBill(senate);
                if (b != null)
                {
                    
                    int count = -1;
                    while(!(senate.get(++count) instanceof Leader));
                    ((Leader) senate.get(count)).newBill(b);
                }
            }
            for (Legislator l :  house)
            {
                Bill b = l.draftBill(house);
                if (b != null)
                {
                    int count = -1;
                    while(!(house.get(++count) instanceof Leader));
                    ((Leader) house.get(count)).newBill(b);
                }
            }

            //Announce Bills
            int countS = -1;
            while(!(senate.get(++countS) instanceof Leader));
            ArrayList<Bill> temp = ((Leader) senate.get(countS)).announceNewBills();
            int countH = -1;
            while(!(house.get(++countH) instanceof Leader));
            temp.addAll(((Leader) house.get(countH)).announceNewBills());
            
            //Proccess List to send to all Committees
            for (Bill b : temp)
            {
                int votes = 0;
                int yes = 0;
                boolean isHouse = true;
                if (senate.indexOf(b.getAuthor(0)) != -1) //Author In Senate
                {
                    
                    if (b.isHalfway()) //Bill is in House
                    {
                        System.out.println("line 218");
                        for (Legislator l : house)
                        {
                            if (l.getCommittee() == b.getType())
                            {
                                if (l.vote(b))
                                    yes++;
                                votes++;
                                System.out.println("House committe votes: " + votes);
                            }
                        }
                    }
                    else // Bill in Senate
                    {
                        
                        isHouse = false;
                        for (Legislator l : senate)
                        {
                            if (l.getCommittee() == b.getType())
                            {
                                if (l.vote(b))
                                    yes++;
                                votes++;
                            }           
                        }
                        System.out.println("Senate committe 1 votes: " + yes);
                    }
                }
                else //Author in House
                {
                    if (b.isHalfway()) //Bill in Senate
                    {
                        isHouse = false;
                        for (Legislator l : senate)
                        {
                            if (l.getCommittee() == b.getType())
                            {
                                if (l.vote(b))
                                    yes++;
                                votes++;
                            }                    
                        }
                    }
                    else // Bill in House
                    {
                        for (Legislator l : house)
                        {
                            if (l.getCommittee() == b.getType())
                            {
                                if (l.vote(b))
                                    yes++;
                                votes++;
                            }                                 
                        }
                        System.out.println("House committe 1 votes: " + yes);
                    }
                }
                    
                //Tally votes
                //System.err.println("Votes:\t" + votes);
                //System.err.println("Yes:\t" + yes);
                if ( (votes / 2) < yes) //Passes
                {
                    b.setStatus(BillStatus.OnTheFloor);
                    if (isHouse)
                    {
                        ((Leader) house.get(countH)).passedCommittee(b);
                    }
                    else
                    {
                        ((Leader) senate.get(countS)).passedCommittee(b);
                    }
                    //System.err.println("Pass");
                    }
                    else
                        trash.add(b);
                }
               
                //Floor Votes
                temp = new ArrayList<Bill>();
                temp = ((Leader) senate.get(countS)).callForVote();
                for (int x =0; x <temp.size();x++)
                {
                    int votes = 0;
                    int yes = 0;
                    for (Legislator l : senate)
                    {
                        if (l.vote(temp.get(x)))
                            yes++;
                        votes++;
                    }
                    if ( (votes/2 < yes))
                    {
                        if (senate.indexOf(temp.get(x).getAuthor(0)) != -1)
                            ((Leader) house.get(countH)).passedCommittee(temp.get(x));
                        else
                        {
                            temp.get(x).setStatus(BillStatus.Pending);
                            pres.getBill(temp.get(x));
                        }
                    }
                    else
                        trash.add(temp.get(x));
                }
               
                temp = new ArrayList<Bill>();
                temp = ((Leader) house.get(countH)).callForVote();
                for (int x =0; x <temp.size();x++)
                {
                    int votes = 0;
                    int yes = 0;
                    for (Legislator l : house)
                    {
                        if (l.vote(temp.get(x)))
                            yes++;
                        votes++;
                    }
                    if ( (votes/2 < yes))
                    {
                        if (house.indexOf(temp.get(x).getAuthor(0)) != -1)
                            ((Leader) senate.get(countS)).passedCommittee(temp.get(x));
                        else
                        {
                            temp.get(x).setStatus(BillStatus.Pending);
                            pres.getBill(temp.get(x));
                        }
                    }
                    else
                        trash.add(temp.get(x));
                }
               
                //President
                temp = pres.decide();
                for (Bill b : temp)
                {
                    if (b.getStatus() == BillStatus.Rejected)
                    {
                        if (house.indexOf(b.getAuthor(0)) != -1)
                            ((Leader) house.get(countH)).getRejected(b);
                        else
                        {
                            ((Leader) senate.get(countS)).getRejected(b);
                        }
                    }
                    else
                        lawBook.add(b);
                }
               
                //Overrides
                temp = ((Leader) house.get(countH)).callOverrides();
                temp.addAll(((Leader) senate.get(countS)).callOverrides());
                for (Bill b : temp)
                {
                    int votes = 0;
                    int yes = 0;
                    boolean senateOverride = false;
                    for (Legislator l : senate)
                    {
                        if (l.vote(b))
                            yes++;
                        votes++;
                    }
                    if ( (votes * 2)/3 < yes)
                        senateOverride = true;
                    
                    boolean houseOverride = false;
                    for (Legislator l : house)
                    {
                        if (l.vote(b))
                            yes++;
                        votes++;
                    }
                    if ( (votes * 2)/3 < yes)
                        houseOverride = true;
                    
                    if (houseOverride && senateOverride)
                    {
                        lawBook.add(b);
                    }
                    else
                        trash.add(b);
                }
        	}

        	System.out.println("Trash size: " + trash.size());
        	System.out.println("Law Book size: " + lawBook.size());

        	
    }
}