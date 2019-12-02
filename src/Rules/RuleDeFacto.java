package Rules;

import Entities.IEntity;
import Main.Edge;
import Main.Debug;
import Main.Permission;

import java.util.*;

import static Main.Permission.*;

public class RuleDeFacto extends Rule {

    public static List<Edge> first(Edge xy)
    {
        List<Edge>  lst;
        Edge        yx;

        lst = new ArrayList<>();
        if (xy.hasPermission(READ))
            if (append(xy, FLOW_READ, "first")) lst.add(xy);
        if (xy.hasPermission(FLOW_READ)) {
            yx = getEdge(xy.getE2(), xy.getE1());
            if (append(yx, FLOW_WRITE, "first")) lst.add(yx);
        }
        return (lst);
    }

    public static List<Edge> second(Edge xy)
    {
        Edge        yx;
        List<Edge>  lst;

        lst = new ArrayList<>();
        if (xy.hasPermission(WRITE))
            if (append(xy, FLOW_WRITE, "second")) lst.add(xy);
        if (xy.hasPermission(FLOW_WRITE))
        {
            yx = getEdge(xy.getE2(), xy.getE1());
            if (append(yx, FLOW_READ, "second")) lst.add(xy);
        }
        return (lst);
    }

    public static List<Edge> spy(Edge xy, IEntity z)
    {
        List<Edge>  lst;
        Edge        xz;
        Edge        yz;
        Edge        zx;

        lst = new ArrayList<>();
        if (canShare(xy.getE2(), z))
        {
            yz = xy.getE2().getEdge(z);
            if (xy.hasPermission(FLOW_READ) && yz.hasPermission(FLOW_READ)) {
                zx = getEdge(yz.getE2(), xy.getE1());
                xz = getEdge(xy.getE1(), yz.getE2());

                if (append(zx, FLOW_WRITE, "spy")) lst.add(zx);
                if (append(xz, FLOW_READ, "spy"))  lst.add(xz);
            }
        }
        return (lst);
    }

    public static List<Edge> find(Edge xy, IEntity z)
    {
        List<Edge>  lst;
        Edge        yz;
        Edge        xz;
        Edge        zx;

        lst = new ArrayList<>();
        if (canShare(xy.getE2(), z))
        {
            yz = xy.getE2().getEdge(z);
            if (xy.hasPermission(FLOW_WRITE) && yz.hasPermission(FLOW_WRITE)) {
                zx = getEdge(yz.getE2(), xy.getE1());
                xz = getEdge(xy.getE1(), yz.getE2());

                if (append(zx, FLOW_READ, "find")) lst.add(zx);
                if (append(xz, FLOW_WRITE, "find"))  lst.add(xz);
            }
        }
        return (lst);
    }

    public static List<Edge> pass(Edge yx, IEntity z)
    {
        List<Edge>  lst;
        Edge        yz;
        Edge        xz;
        Edge        zx;

        lst = new ArrayList<>();
        if (canShare(yx.getE1(), z)) {
            yz = yx.getE1().getEdge(z);
            if (yx.hasPermission(FLOW_READ) && yz.hasPermission(FLOW_WRITE)) {
                zx = getEdge(yz.getE2(), yx.getE2());
                xz = getEdge(yx.getE2(), yz.getE2());

                if (append(zx, FLOW_READ, "pass")) lst.add(zx);
                if (append(xz, FLOW_WRITE, "pass"))  lst.add(xz);
            }
        }
        return (lst);
    }

    public static List<Edge> post(Edge xy, IEntity z)
    {
        List<Edge>  lst;
        Edge        zy;
        Edge        xz;
        Edge        zx;

        lst = new ArrayList<>();
        if (canShare(z, xy.getE2())) {
            zy = z.getEdge(xy.getE2());
            if (xy.hasPermission(FLOW_WRITE) && zy.hasPermission(FLOW_READ)) {
                xz = getEdge(xy.getE1(), zy.getE1());
                zx = getEdge(zy.getE1(), xy.getE1());

                if (append(zx, FLOW_WRITE, "post")) lst.add(zx);
                if (append(xz, FLOW_READ, "post"))  lst.add(xz);
            }
        }
        return (lst);
    }

    private static boolean append(Edge e, Permission p, String f)
    {
        if (e.getImaginary().add(p)) {
            Debug.print(e, f, p);
            return (true);
        }
        return (false);
    }
}
