import java.util.*;

public enum PartyType {Republican, Democrat, Libertarian;
     
public static final PartyType[] VALUES = values();
public static final int SIZE = VALUES.length;
private static final Random RANDOM = new Random();

public static PartyType nextParty()  {
     return VALUES[RANDOM.nextInt(SIZE)];
}


}