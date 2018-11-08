import java.util.*;
import java.security.MessageDigest;
import java.math.BigInteger;

// A test UI which will prompt the user to provide the OTP,
// show access granted message only if the right OTP is entered.
public class OTP_Server {

    private final static String SECRET_KEY = "808670FF00FF08812";
    private final static int INBOUNDS = 0;
    private final static int OUTBOUNDS = 1;

    private static Vector<String> keys;
    private static int HASH_ROUND;
    private static int CLIENT_ROUND;
    private static int SERVER_ROUND;
    private static String FEEDBACK;

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

    public static void validate(String otp)
    {
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
        syncAttempt(otp);
    }

    public static void syncAttempt(String otp)
    {
        for (int i = 0; i < 10; i++)
        {
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
            else if (otp.equals(generateFotp()))
                validateSync(0, OUTBOUNDS);
            else
                continue;
            return;
        }
        System.out.println("Invalid one-time password. Access denied.");
    }

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

    public static String generateFotp()
    {
        if(HASH_ROUND == 0)
            FEEDBACK = SECRET_KEY;

        try
        {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hashArr = digest.digest(FEEDBACK.getBytes("UTF-8"));

            Formatter hex = new Formatter();
            for (byte b : hashArr)
                hex.format("%02x", b);
            FEEDBACK = hex.toString();

            String truncateKey = new BigInteger(FEEDBACK, 16).toString().substring(0, 6);
            keys.add(truncateKey);
        }
        catch(Exception e) { throw new RuntimeException(e); }

        HASH_ROUND++;
        return keys.get(HASH_ROUND - 1);
    }

    public static void doubleCollision()
    {
        System.out.println("Performing consecutive double-variable collision analysis...");

        String [] test_keys = new String[10000];
        for(int i = 0; i < test_keys.length; i++)
            test_keys[i] = generateFotp();

        int doubleCollision = 0;
        HashMap<String, String> pairs = new HashMap<>();
        for(int i = 0; i < test_keys.length - 1; i++)
        {

            if(pairs.get(test_keys[i]) != null && pairs.get(test_keys[i]).equals(test_keys[i+1]))
                continue;
            pairs.put(test_keys[i], test_keys[i+1]);

            for(int j = i+1; j < test_keys.length - 1; j++)
            {
                //System.out.println(test_keys[i] + " = " + test_keys[j] + " | " +
                        //test_keys[i+1] + " = " + test_keys[j+1]);
                if (test_keys[i].equals(test_keys[j]) && test_keys[i + 1].equals(test_keys[j + 1]))
                    doubleCollision++;
            }
        }
        System.out.println("Number of double-variable collisions: " + doubleCollision);
    }
}
