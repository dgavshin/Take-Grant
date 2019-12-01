package Rules;

import Entities.IEntity;
import Entities.Object;
import Main.Edge;
import Main.Main;
import Main.Permission;
import Main.Debug;

import java.util.Set;

import static Main.Main.edgeMap;
import static Main.Permission.*;

public class                RuleDeJure extends Rule {

    private static Set<Permission> sx;

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

    private static Edge appendPermissions(IEntity s, IEntity o, Edge source, String f)
    {
        Edge e;
        try {
            e = s.getEdge(o);
            e.getReal().addAll(source.getReal());
            e.getImaginary().addAll(source.getImaginary());
        } catch (NullPointerException ignored) {
            e = new Edge(s, o, source);
            edgeMap.add(e);
        }
        Debug.print(s, o, f);
        return (e);
    }

    public static void create(IEntity s) {
        Object o;

        o = new Object(s.getName() + "'");
        edgeMap.add(new Edge(s, o, TAKE, GRANT, READ, WRITE));
        Main.objects.add(o);
        Main.vertexMap.add(o);
        Debug.print(s, o, "create");
    }
}
