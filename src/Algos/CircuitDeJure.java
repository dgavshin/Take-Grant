package Algos;

import Entities.IEntity;
import Main.Edge;
import Main.Main;

import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.Set;

import static Main.Permission.GRANT;
import static Main.Permission.TAKE;
import static Rules.RuleDeJure.grant;
import static Rules.RuleDeJure.take;

public class CircuitDeJure{

    private static Set<Edge>    L;
    private static Set<IEntity> N;

    private static void spread(String type) {
        for (Edge l: L)
            for (IEntity n: N)
            {
                if (n != l.getE2() && n != l.getE1())
                {
                    if (type.equals("take"))
                        take(l.getE1(), l.getE2(), n);
                    else if (type.equals("grant"))
                        grant(l.getE1(), l.getE2(), n);
                }
            }
    }

    private static void prepare() {
        L = new LinkedHashSet<>();
        N = Main.vertexMap;

        // Choose edges that contain TAKE or/and GRANT
        for (Edge j: Main.edgeMap)
            if (j.hasPermission(TAKE) || j.hasPermission(GRANT))
                L.add(j);
    }

    public static void start() {
        prepare();
        for (String type: Arrays.asList("take", "grant", "take"))
            spread(type);
    }
}
