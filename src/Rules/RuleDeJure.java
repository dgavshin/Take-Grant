package Rules;

import Entities.IEntity;
import Entities.Object;
import Main.Edge;
import Main.Main;
import Main.Permission;
import Main.Debug;

import java.util.*;

import static Main.Main.edgeMap;
import static Main.Permission.*;

public class                RuleDeJure extends Rule {

    private static Set<Permission> sx;

    /**
     * правило take, смотри в доках как оно работает
     * docs/Rules/RuleDeJure.md
     */
    public static Edge   take(IEntity s, IEntity x, IEntity y)
    {
        Edge xy;


        if (canShare(s, x) && canShare(x, y))
        {
            sx = s.getEdge(x).getReal();
            xy = x.getEdge(y);
            if (sx.contains(TAKE))
                return (appendPermissions(s, y, xy, "take"));
        }
        return (null);
    }

    public static Edge   grant(IEntity s, IEntity x, IEntity y)
    {
        Edge sy;

        if (canShare(s, x) && canShare(s, y))
        {
            sx = s.getEdge(x).getReal();
            sy = s.getEdge(y);
            if (sx.contains(GRANT))
                return (appendPermissions(x, y, sy, "grant"));
        }
        return (null);
    }

    public static void create(IEntity s) {
        Object o;

        o = new Object(s.getName() + "'");
        edgeMap.add(new Edge(s, o, TAKE, GRANT, READ, WRITE));
        Main.objects.add(o);
        Main.vertexMap.add(o);
        Debug.print(s, o, "create", Arrays.asList(TAKE, GRANT, READ, WRITE));
    }

    private static Edge appendPermissions(IEntity s, IEntity o, Edge source, String f)
    {
        Edge e;
        List<Permission> permissions = new ArrayList<>();

        e = getEdge(s, o);
        for (Permission p: source.getImaginary())
            if (e.getImaginary().add(p))
                permissions.add(p);
        for (Permission p: source.getReal())
            if (e.getReal().add(p))
                permissions.add(p);
        if (!permissions.isEmpty())
        {
            Debug.print(s, o, f, permissions);
            return (e);
        }
        return (null);
    }
}
