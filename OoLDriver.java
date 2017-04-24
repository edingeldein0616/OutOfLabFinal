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
    public static void PrintOutputFileInfo(int signedBills) throws FileNotFoundException, IOException {
        String input = "";
        Scanner userInput = new Scanner(System.in);
        System.out.println("Name of file without extension: ");
        input = userInput.nextLine();
        File output = new File(input + ".txt");
        FileWriter outputFile = new FileWriter(output, true);
        double co = ((double) committeeOneBills) / ((double) totalBillsCreated) * 100;
        double ct = ((double) committeeTwoBills) / ((double) totalBillsCreated) * 100;
        double fo = ((double) floorOneBills) / ((double) totalBillsCreated) * 100;
        double ft = ((double) floorTwoBills) / ((double) totalBillsCreated) * 100;
        double v = ((double) vetoedBills) / ((double) totalBillsCreated) * 100;
        double o = ((double) overriddenBills) / ((double) totalBillsCreated) * 100;
        double s = ((double) signedBills) / ((double) totalBillsCreated) * 100;

        outputFile.write("ARO " + beginingARating + "/ARE " + aRating + "/D " + totalBillsCreated + "/CO " + co + "/CT " + ct + "/FO " + fo + "/FT " + ft + "/V " + v + "/O " + o + "/S " + s + "\n");
        outputFile.close();
    }

    public static double aRating = 0.2;
    public static double beginingARating = 0.2;
    public static int draftedBills = 0;
    //Bill counts (number of bills that have made it to given levels)
    public static int totalBillsCreated = 0;
    public static int committeeOneBills = 0;
    public static int committeeTwoBills = 0;
    public static int floorOneBills = 0;
    public static int floorTwoBills = 0;
    public static int vetoedBills = 0;
    public static int overriddenBills = 0;
    public static int sLeader = -1;
    public static int hLeader = -1;
     
    public static void main(String[] args) throws FileNotFoundException
    {
        //Regular variables
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

        //Bipartisanship Variables
        int mode = 0;
        double avgRating = 0.0;
        double maxRating = 0.0;
        double minRating = 0.0;
        int highCount = 0;
        int lowCount = 0;
        
        int totalPassedBills = 0;
        int totalBills = 0;

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
        final int YEAR = 365; //365;

        for (int day = 0; day < YEAR; day++)
        {
            System.out.println("\t\t\t\tDAY " + day);
            for(int row = 0; row < 5; row++) { 
                for(int col = 0; col < 100; col++) {
                    System.out.print("*");
                }
                System.out.println();
            }

            int passedCount = 0;
            int voteCount = 0;            
            //Who wants to author bills
            //Also sends those bills to head of their units
            for (Legislator l : senate)
            {
                Bill b = l.draftBill(senate);
                if (b != null)
                {
                    //Update variables
                    totalBillsCreated++;
                    b.creationDate = day;
                    //Send to leader
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
                    totalBillsCreated++;
                    b.creationDate = day;
                    //Send to leader
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
            sLeader = countS;
            hLeader = countH;
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
                        committeeTwoBills++;
                        for (Legislator l : house)
                        {
                            if (l.getCommittee() == b.getType())
                            {
                                if (l.vote(b, mode, presParty)) {
                                    yes++;
                                }
                                votes++;
                            }
                        }
                       // System.out.println(printResults(b, 'H', votes, yes));
                    }
                    else // Bill in Senate
                    {
                        
                        isHouse = false;
                        for (Legislator l : senate)
                        {
                            if (l.getCommittee() == b.getType())
                            {
                                if (l.vote(b, mode, presParty)) {
                                    yes++;
                                }
                                votes++;
                            }           
                        }
                       //System.out.println(printResults(b, 'S', votes, yes));
                    }
                }
                else //Author in House
                {
                    if (b.isHalfway()) //Bill in Senate
                    {
                        isHouse = false;
                        committeeTwoBills++;
                        for (Legislator l : senate)
                        {
                            if (l.getCommittee() == b.getType())
                            {
                                if (l.vote(b, mode, presParty)) {
                                    yes++;
                                }
                                votes++;
                            }                    
                        }
                        //System.out.println(printResults(b, 'S', votes, yes));
                    }
                    else // Bill in House
                    {
                        for (Legislator l : house)
                        {
                            if (l.getCommittee() == b.getType())
                            {
                                if (l.vote(b, mode, presParty)) {
                                    yes++;
                                }
                                votes++;
                            }                                 
                        }
                        //System.out.println(printResults(b, 'H', votes, yes));
                    }
                }
                    
                //Tally votes
                //System.err.println("Votes:\t" + votes);
                //System.err.println("Yes:\t" + yes);
                voteCount += votes;
                if ( (votes / 2) < yes) //Passes
                {
                    committeeOneBills++;
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
                    passedCount++;
                }
                else {
                    b.trashDate = day;
                    trash.add(b);
                }
            }
               
                //Floor Votes
                temp = new ArrayList<Bill>();
                temp = ((Leader) senate.get(countS)).callForVote();
                
                    //senate floor vote loop
                for (int x =0; x <temp.size();x++)
                {
                    int votes = 0;
                    int yes = 0;
                    for (Legislator l : senate)
                    {
                        if (l.vote(temp.get(x), mode, presParty)) {
                            yes++;
                        }
                        votes++;
                    }
                    voteCount += votes;
                    if ( (votes/2 < yes))
                    {
                        if (senate.indexOf(temp.get(x).getAuthor(0)) != -1) {
                            floorOneBills++;
                            passedCount++;
                            ((Leader) house.get(countH)).passedCommittee(temp.get(x));
                        }
                        else
                        {
                            temp.get(x).setStatus(BillStatus.Pending);
                            pres.getBill(temp.get(x));
                            floorTwoBills++;
                            passedCount++;
                        }
                    }
                    else{
                        temp.get(x).trashDate = day;
                        trash.add(temp.get(x));
                    }
                    System.out.println("Vote " + x);
                    System.out.println(printResults(temp.get(x), 'S', votes, yes));
                }
               
                temp = new ArrayList<Bill>();
                temp = ((Leader) house.get(countH)).callForVote();

                    //house floor vote loop
                for (int x =0; x <temp.size();x++)
                {
                    int votes = 0;
                    int yes = 0;
                    for (Legislator l : house)
                    {
                        if (l.vote(temp.get(x), mode, presParty)) {
                            yes++;
                        }
                        votes++;
                    }
                    if ( (votes/2 < yes))
                    {
                        if (house.indexOf(temp.get(x).getAuthor(0)) != -1) {
                            floorOneBills++;
                            passedCount++;
                            ((Leader) senate.get(countS)).passedCommittee(temp.get(x));
                        }
                        else
                        {
                            temp.get(x).setStatus(BillStatus.Pending);
                            pres.getBill(temp.get(x));
                            floorTwoBills++;
                            passedCount++;
                        }
                    }
                    else {
                        temp.get(x).trashDate = day;
                        trash.add(temp.get(x));
                    }

                    System.out.println("Vote " + x);
                    System.out.println(printResults(temp.get(x), 'H', votes, yes));
                }
               
                //President
                temp = pres.decide();
                for (Bill b : temp)
                {
                    if (b.getStatus() == BillStatus.Rejected)
                    {   
                        vetoedBills++;
                        if (house.indexOf(b.getAuthor(0)) != -1)
                            ((Leader) house.get(countH)).getRejected(b);
                        else
                        {
                            ((Leader) senate.get(countS)).getRejected(b);
                        }
                    }
                    else {
                        b.signDate = day;
                        lawBook.add(b);
                        passedCount++;
                    }
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
                        if (l.vote(b, mode, presParty))
                            yes++;
                        votes++;
                    }
                    if ( (votes * 2)/3 < yes)
                        senateOverride = true;
                    
                    boolean houseOverride = false;
                    for (Legislator l : house)
                    {
                        if (l.vote(b, mode, presParty))
                            yes++;
                        votes++;
                    }

                    voteCount += votes;

                    if ( (votes * 2)/3 < yes)
                        houseOverride = true;
                    
                    if (houseOverride && senateOverride)
                    {   
                        overriddenBills++;
                        b.signDate = day;
                        lawBook.add(b);
                        passedCount++;
                    }
                    else {
                        b.trashDate = day;
                        trash.add(b);
                    }
                }
        	    
                double passRate = 0;
                passRate = ((double)passedCount / (double)voteCount);
                
                if((passRate > 0.1) || (passRate < 0.01))
                {
                    if(aRating > 0)
                        aRating -= 0.01;
                    // Approval Rating -1%
                }
                else if (passRate >= 0.01 && passRate <= 0.1)
                {
                    if(aRating < 1)
                        aRating += 0.01;
                    // Approval Rating +1%
                }
                
                if(passRate > 0.65)
                {
                    mode = 1;
                    // Pres's party 50% less likely to vote for different party bills
                    // Non-Pres's parties 50% more likely to vote for president's party's bills
                }
                else if(passRate < 0.45)
                {
                    mode = 2;
                    // Pres's party 50% more likely to vote for different party bills
                    // Non-Pres's parties 50% less likely to vote for president's party's bills
                }
                else {
                    mode = 0;
                }
                
                if(aRating > 0.65)
                    highCount++;
                else if(aRating < 0.45)
                    lowCount++;
                
                if(aRating > maxRating)
                    maxRating = aRating;
                else if(aRating < minRating)
                    minRating = aRating;
                else if(minRating == 0)
                    minRating = aRating;
                
                avgRating += aRating;
                
            } //Outside of day loop
            
            avgRating = avgRating / YEAR;
            
            //Bill statistics
            System.out.println("Total Bills Created: " + totalBillsCreated);
            committeeTwoBills += ((Leader) house.get(hLeader)).pc.size() + ((Leader) senate.get(sLeader)).pc.size();
            try {
                PrintOutputFileInfo(lawBook.size());
            } catch(IOException e) {
                System.err.println("Print Error: " + e.getMessage());
            }
            //Aproval rating output
            System.out.println("\nAverage Approval Rating: " + (int)(avgRating*100) + "%");
            System.out.println("Max Approval Rating: " + (int)(maxRating*100) + "%");
            System.out.println("Min Approval Rating: " + (int)(minRating*100) + "%");
            System.out.println("# of Days > 65%: " + highCount);
            System.out.println("# of Days < 45%: " + lowCount);
            System.out.println("Approval Rating: " + (int)(aRating*100) + "%");
        	System.out.println("Trash size: " + trash.size());
        	System.out.println("Law Book size: " + lawBook.size());

        	
    }

    public static String printResults(Bill b, char chamber, int votes, int yes) {
        if(chamber == 'H') {
            return "House Committe Vote results...\n" + b + "\nNum Votes: " + votes + "\nNum Yes: " + yes + "\n\n";
        } else {
            return "Senate Committe Vote results...\n" + b + "\nNum Votes: " + votes + "\nNum Yes: " + yes + "\n\n";
        }
    }
}