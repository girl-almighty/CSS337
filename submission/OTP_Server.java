import java.util.*;
import java.security.MessageDigest;
import java.math.BigInteger;

// A test UI which will prompt the user to provide the OTP,
// show access granted message only if the right OTP is entered.
public class OTP_Server {

    private final static String SECRET_KEY = "808670FF00FF08812";   // hash seed
    private final static int INBOUNDS = 0;      // specifies that there are pre-generated keys to be checked
    private final static int OUTBOUNDS = 1;     // specifies that there are no pre-generated keys to be checked

    private static Vector<String> keys;         // container storing generated keys
    private static int HASH_ROUND;              // current round of the OTP-generating hashing algorithm
    private static int CLIENT_ROUND;            // current client OTP round
    private static int SERVER_ROUND;            // current OTP round that server is expecting
    private static String FEEDBACK;             // stores feedback to be fed into the next round of hashing


    public static void main(String[] args)
    {
        System.out.println("---OTP Server---");
        CLIENT_ROUND = HASH_ROUND = SERVER_ROUND = 0;
        keys = new Vector<String>(1);

        Scanner reader = new Scanner(System.in);

        while(true)
        {
            System.out.println("Type in your one-time password (or 0 to exit; 1 to perform double collision):");
            String otp = "";
            if(reader.hasNextLine())
                otp = reader.nextLine();

            if(otp.equals("1"))
                doubleCollision();
            if(otp.equals("0") || otp.equals("") || otp.equals("1"))
                return;

            System.out.print("client key: " + otp  + " == server key: ");
            validate(otp);
        }
    }

    // purpose: validates that the user input OTP is a valid OTP.
    //          this also utilizes the synchronization mechanism to see if
    //          the client OTP matches the next 10 keys, if it doesn't match the current one.
    public static void validate(String otp)
    {
        // if the client round of OTP is synced with the server round of OTP,
        // then there are no pre-generated keys to be checked.
        // thus we have to generate a new one and compare the client OTP with it.
        if(CLIENT_ROUND >= keys.size())
        {
            String cur = generateFotp();

            if (otp.equals(cur))
            {
                System.out.println(cur);
                System.out.println("Access granted.");
                CLIENT_ROUND++;
                SERVER_ROUND++;
                return;
            }
        }

        // otherwise, we must perform a synchronization attempt
        // to check if the client OTP matches the next 10 keys of the server.
        syncAttempt(otp);
    }

    // purpose: checks the next 10 keys of the server to see if the client OTP finds a match.
    public static void syncAttempt(String otp)
    {
        for (int i = 0; i < 10; i++)
        {
            // first, we check the pre-generated keys if any.
            if(CLIENT_ROUND + i < keys.size())
            {
                if(otp.equals(keys.get(CLIENT_ROUND + i)) && CLIENT_ROUND + i == SERVER_ROUND)
                {
                    System.out.println(keys.get(CLIENT_ROUND + i));
                    System.out.println("Access granted.");
                    CLIENT_ROUND++;
                    SERVER_ROUND++;
                }
                else if(otp.equals(keys.get(CLIENT_ROUND + i)))
                    validateSync(CLIENT_ROUND + i + 1, INBOUNDS);
                else
                    continue;
            }

            // if there are no pre-generated keys to be checked, we generate a new one for comparison.
            else if (otp.equals(generateFotp()))
                validateSync(0, OUTBOUNDS);

            // if there are no pre-generated keys to be checked, and generating a new one did not match,
            // we must continue the for loop and keep generating a key until we match or until we make 10 attempts.
            else
                continue;

            return;
        }
        System.out.println("Invalid one-time password. Access denied.");
    }

    // purpose: once a match out of the 10 attempts is found, we prompt the user to type in the next OTP
    //          as means of extra validation.
    public static void validateSync(int index, int bounds)
    {
        System.out.println("Please type in the next one-time password below:");

        Scanner reader = new Scanner(System.in);  // Reading from System.in
        String nextOTP = reader.nextLine(); // Scans the next token of the input as an int.

        String curOTP = bounds == INBOUNDS ? keys.get(index) : generateFotp();

        if(nextOTP.equals(curOTP))
        {
            System.out.println(curOTP);
            System.out.println("Access granted.");
            CLIENT_ROUND = SERVER_ROUND = bounds == INBOUNDS ? index + 1 : HASH_ROUND;
            return;
        }

        System.out.println("Invalid one-time password. Access denied.");
    }

    // purpose: generates a key by feeding a string to sha-256, and truncating it to 6 digits.
    public static String generateFotp()
    {
        // if it's the first hash round, we set the feedback to our seed.
        if(HASH_ROUND == 0)
            FEEDBACK = SECRET_KEY;

        try
        {
            // we hash the feedback using sha-256, thus generateing a byte array.
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hashArr = digest.digest(FEEDBACK.getBytes("UTF-8"));

            // we convert this byte array to a hex string.
            Formatter hex = new Formatter();
            for (byte b : hashArr)
                hex.format("%02x", b);
            FEEDBACK = hex.toString();

            // we convert the hex string to a base-10 decimal, and slice the first 6 digits as the key.
            String truncateKey = new BigInteger(FEEDBACK, 16).toString().substring(0, 6);
            keys.add(truncateKey);
        }
        catch(Exception e) { throw new RuntimeException(e); }

        HASH_ROUND++;
        return keys.get(HASH_ROUND - 1);
    }

    // purpose: analyzes the number of times 2 consecutive keys appear again in a dataset.
    public static void doubleCollision()
    {
        System.out.println("Performing consecutive double-variable collision analysis...");

        // we generate 10,000 keys so that our test size n = 10000
        // and store it in an array.
        String [] test_keys = new String[10000];
        for(int i = 0; i < test_keys.length; i++)
            test_keys[i] = generateFotp();

        // we create a hashmap to store all the unique consecutive pairs that we check.
        int doubleCollision = 0;
        HashMap<String, String> pairs = new HashMap<>();
        for(int i = 0; i < test_keys.length - 1; i++)
        {
            // if the consecutive pair is in the hashmap, that means we've checked its occurence already so we skip it.
            if(pairs.get(test_keys[i]) != null && pairs.get(test_keys[i]).equals(test_keys[i+1]))
                continue;

            // otherwise, we store it in the hashmap
            pairs.put(test_keys[i], test_keys[i+1]);

            // we then compare each consecutive pair with all the consecutive pairs in the array
            for(int j = i+1; j < test_keys.length - 1; j++)
                if (test_keys[i].equals(test_keys[j]) && test_keys[i + 1].equals(test_keys[j + 1]))
                    doubleCollision++;
        }
        System.out.println("Number of double-variable collisions: " + doubleCollision);
    }
}
