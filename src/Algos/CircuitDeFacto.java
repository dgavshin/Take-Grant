package Algos;

import Entities.IEntity;
import Entities.Subject;
import Main.Edge;
import Main.Main;
import Rules.RuleDeJure;

import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import Main.Debug;

import static Main.Permission.*;
import static Rules.RuleDeFacto.*;
import static Rules.RuleDeJure.grant;
import static Rules.RuleDeJure.take;

public class CircuitDeFacto{

    private static Set<Edge>    F;
    private static Set<Edge>    addedF;
    private static Set<Edge>    removedF;

    private static void spread() {
        Set<IEntity> N;

        N = new HashSet<>();
        for (Edge f: F)
        {
            removedF.add(f);
            N.add(f.getE1());
            N.add(f.getE2());
            for (IEntity n: N)
                if (n != f.getE2() && n != f.getE1())
                    check(f, n);
        }
        F.addAll(addedF);
        F.removeAll(removedF);

        F.forEach(x -> System.out.println(x.getE1().getName() + x.getE2().getName()));
        addedF.clear();
        removedF.clear();
    }

    private static void check(Edge l, IEntity n) {
        List<Edge> e;

        if ((e = spy(l, n)) != null)
            addedF.addAll(e);
        if ((e = find(l, n)) != null)
            addedF.addAll(e);
        if ((e = post(l, n)) != null)
            addedF.addAll(e);
        if ((e = pass(l, n)) != null)
            addedF.addAll(e);
    }

    private static void prepare() {
        F = new LinkedHashSet<>();
        removedF = new HashSet<>();
        addedF = new HashSet<>();

        // Choose edges that contain READ,FLOW READ, FLOW WRITE or/and WRITE
        for (Edge j: Main.edgeMap)
            if (j.hasPermission(FLOW_READ) || j.hasPermission(FLOW_WRITE) ||
                    j.hasPermission(READ)  || j.hasPermission(WRITE))
                F.add(j);

        // Apply first and second rules to each edge of F
        for (Edge e: F)
        {
            List<Edge> lst;
            if ((lst  = first(e)) != null)
                addedF.addAll(lst);
            if ((lst = second(e)) != null)
                addedF.addAll(lst);
        }
        F.addAll(addedF);
        addedF.clear();
    }

    public static void start()
    {
        prepare();
        while (!F.isEmpty()) {
            spread();
        }
    }
}
