package Rules;

import Entities.IEntity;
import Main.Edge;
import Main.Main;
import Main.Permission;

import java.util.Objects;

public class Rule {
    static boolean canShare(IEntity e1, IEntity e2)
    {
        return !Objects.isNull(e1.getEdge(e2));
    }

    public static Edge getEdge(IEntity s, IEntity o)
    {
        Edge e;

        e = s.getEdge(o);
        if (Objects.isNull(e))
        {
            e = new Edge(s,o);
            Main.edgeMap.add(e);
            return (e);
        }
        return (s.getEdge(o));
    }
}
