package Main;

import Algos.CircuitDeFacto;
import Algos.CircuitDeJure;
import Algos.CircuitTG;
import Entities.IEntity;
import Entities.Object;
import Entities.Subject;
import Rules.Rule;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static Main.Debug.printEdgeMap;
import static Main.Debug.printUsage;
import static Main.Permission.*;

public class Main {

    public static List<Edge>        edgeMap = new ArrayList<>();
    public static Set<IEntity>      vertexMap = new HashSet<>();
    public static List<Subject>     subjects = new ArrayList<>();
    public static List<Object>      objects = new ArrayList<>();
    private static String           Path;

    private static String match(String regex, String line, int group)
    {
        String result;

        result = "";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(line);
        if (matcher.find())
            result = line.substring(matcher.start(group), matcher.end(group));
        return (result);
    }

    private static void getConfig() throws IOException {
        Edge         e;
        List<String> lst;
        String[]     a;
        String[]     permissions;
        Subject[]    subjects;
        Object[]     objects;

        lst = new ArrayList<>();
        Files.lines(Paths.get(Path), StandardCharsets.UTF_8).forEach(lst::add);
        int s_len = Integer.parseInt(match("Subjects:([0-9]+)", lst.get(0), 1));
        int o_len = Integer.parseInt(match("Objects:([0-9]+)", lst.get(1), 1));

        subjects = new Subject[s_len];
        objects = new Object[o_len];

        for (int i = 0; i < s_len; i++) { subjects[i] = new Subject(); }
        for (int i = 0; i < o_len; i++) { objects[i] = new Object(); }

        for (String str: lst)
        {
            if (str.contains("-"))
            {
                a = str.split(":")[0].split("-");
                permissions = str.split(":")[1].split(",");
                IEntity s = a[0].charAt(0) == 's' ? subjects[a[0].charAt(1) - '0' - 1] : objects[a[0].charAt(1) - '0' - 1];
                IEntity o = a[1].charAt(0) == 's' ? subjects[a[1].charAt(1) - '0' - 1] : objects[a[1].charAt(1) - '0' - 1];

                e = Rule.getEdge(s, o);
                for (String p: permissions)
                    e.addPermission(getByRight(p));
            }
        }
        Main.subjects.addAll(Arrays.asList(subjects));
        Main.objects.addAll(Arrays.asList(objects));
    }

    private static boolean checkArgs(String[] args)
    {
        String[]    debugKeys;
        boolean     hasPath;

        hasPath = false;
        for (int i = 0; i < args.length; i++)
        {
            switch (args[i])
            {
                case "--config":
                    Path = args[i + 1];
                    hasPath = true;
                    break;
                case "--debug":
                    try {
                        debugKeys = args[i + 1].split(",");
                        Debug.setDebugKeys(debugKeys);
                    }
                    catch (ArrayIndexOutOfBoundsException ignored){}
            }
        }
        return (hasPath);
    }

    public static void          main(String[] args) throws IOException {
        if (checkArgs(args)) {
            getConfig();
            printEdgeMap("Start map");
            CircuitTG.start();
            printEdgeMap("TG");
            CircuitDeJure.start();
            printEdgeMap("DE JURE");
            CircuitDeFacto.start();
            printEdgeMap("DE FACTO");
        }
        else
            printUsage();
    }
}


//
//        RuleDeFacto.first(a);
//        printEdgeMap("first");
//
//        RuleDeFacto.first(b);
//        printEdgeMap("first");
//
//        RuleDeFacto.second(a);
//        printEdgeMap("second");
//
//        RuleDeFacto.second(b);
//        printEdgeMap("second");
//
//        RuleDeFacto.spy(a,z);
//        printEdgeMap("spy");
//
//        RuleDeFacto.find(a,z);
//        printEdgeMap("find");
//
//        RuleDeFacto.post(a,z);
//        printEdgeMap("post");
//
//        RuleDeFacto.pass(a, z);
//        printEdgeMap("pass");