package Main;

import Entities.IEntity;

import java.util.List;

import static Main.Main.edgeMap;
import static Tools.Color.RED;
import static Tools.Color.RESET;

public class Debug {

    private static boolean ALL = false;

    private static boolean DE_JURE = false;
    private static boolean DE_JURE_RULES = false;
    private static boolean DE_JURE_CIRCUIT = false;

    private static boolean DE_FACTO = false;
    private static boolean DE_FACTO_RULES = false;
    private static boolean DE_FACTO_CIRCUIT = false;

    private static boolean MAIN = false;

    public static void print(IEntity a, IEntity b, String f, List<Permission> permissions)
    {
        StringBuilder ps = new StringBuilder();
        if (ALL || DE_JURE || DE_JURE_RULES)
            for (Permission p : permissions)
                ps.append(p.getRight()).append(" ");
            debug(a, b, f, ps.toString());
    }

    public static void print(Edge s, String f, Permission ... permissions)
    {
        StringBuilder builder = new StringBuilder();
        if (ALL || DE_FACTO || DE_FACTO_RULES)
            for (Permission p : permissions)
                builder.append(p.getRight()).append(" ");
            debug(s.getE1(),s.getE2(),f, builder.toString());

    }

    public static void printEdgeMap(String type)
    {
        if (MAIN || ALL) {
            System.out.println("\n" + type);
            System.out.println("================EDGE MAP================");
            for (Edge e : edgeMap) {
                System.out.println(RED + e.getE1().getName() + RESET + " has edge with " +
                        RED + e.getE2().getName() + RESET + ". Permissions - " + RED +
                        e.permissionsToString() + RESET);
            }
            System.out.println("================EDGE MAP================\n");
        }
    }

    private static void debug(IEntity a, IEntity b, String f, String permission)
    {
        System.out.println("[+] Added edge between " + RED + a.getName() + RESET + " and "
                + RED + b.getName() + RESET + " by " + f + " - " + RED + permission + RESET);
    }

    static void printUsage()
    {
        System.out.println(RED + "\nUsage: java -jar Take-Grant.jar --config={absolute path to config} --debug a,j,f,m,jr,fr,jc,fc\n" + RESET +
                "\t\tDebug keys:\n" +
                "\t\t\ta  = all\n" +
                "\t\t\tj  = all de jure debug\n" +
                "\t\t\tf  = all de facto debug\n" +
                "\t\t\tm  = main debug\n" +
                "\t\t\tjr = only de jure rules\n" +
                "\t\t\tfr = only de facto rules\n" +
                "\t\t\tjc = only de jure circuit\n" +
                "\t\t\tfc = only de facto circuit\n");
    }

    static void setDebugKeys(String[] keys)
    {
        for (String key: keys)
        {
            switch (key) {
                case "a":  Debug.ALL = true; break;
                case "m":  Debug.MAIN = true; break;
                case "j":  Debug.DE_JURE = true; break;
                case "f":  Debug.DE_FACTO = true; break;
                case "jr": Debug.DE_JURE_RULES = true; break;
                case "fr": Debug.DE_FACTO_RULES = true; break;
                case "jc": Debug.DE_JURE_CIRCUIT = true; break;
                case "fc": Debug.DE_FACTO_CIRCUIT = true; break;
            }
        }
    }
}
