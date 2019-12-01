package Algos;

import Entities.IEntity;
import Entities.Subject;
import Main.Edge;
import Main.Main;
import Rules.RuleDeJure;

import java.util.*;

import Main.Debug;
import static Main.Main.subjects;
import static Main.Permission.GRANT;
import static Main.Permission.TAKE;
import static Rules.RuleDeJure.grant;
import static Rules.RuleDeJure.take;

public class CircuitTG {

    private static Set<Edge>       L;
    private static Set<Edge>       addedL;
    private static Set<Edge>       removedL;

    private static void spread() {
        Set<IEntity> N;

        N = new HashSet<>();
        for (Edge l: L)
        {
            removedL.add(l);
            N.add(l.getE1());
            N.add(l.getE2());
            for (IEntity n: N)
                if (n != l.getE2() && n != l.getE1())
                    check(l, n);
        }
        L.removeAll(removedL);
        L.addAll(addedL);
        addedL.clear();
        removedL.clear();
    }

    private static void check(Edge l, IEntity n) {
        Edge e;

        if ((e = take(l.getE1(), l.getE2(), n)) != null)
            if (e.hasPermission(TAKE) || e.hasPermission(GRANT))
                addedL.add(e);
        if ((e = grant(l.getE1(), l.getE2(), n)) != null)
            if (e.hasPermission(TAKE) || e.hasPermission(GRANT))
                addedL.add(e);
    }

    private static void prepare() {
        L = new LinkedHashSet<>();
        removedL = new HashSet<>();
        addedL = new HashSet<>();

        // Create objects
        for (Subject s : Main.subjects)
            RuleDeJure.create(s);

        // Choose edges that contain TAKE or/and GRANT
        for (Edge j: Main.edgeMap)
            if (j.hasPermission(TAKE) || j.hasPermission(GRANT))
                L.add(j);
    }

    public static void start() {
        prepare();
        while (!L.isEmpty()) {
            spread();
        }
    }
}
