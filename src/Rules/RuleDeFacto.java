package Rules;

import Entities.IEntity;
import Main.Edge;
import Main.Debug;

import java.util.*;

import static Main.Permission.*;

public class RuleDeFacto extends Rule {

    public static List<Edge> first(Edge xy)
    {
        List<Edge>  lst;
        Edge        yx;

        lst = new ArrayList<>();
        if (xy.hasPermission(READ))
        {
            xy.getImaginary().add(FLOW_READ);
            lst.add(xy);
        }
        if (xy.hasPermission(FLOW_READ)) {
            yx = getEdge(xy.getE2(), xy.getE1());
            yx.getImaginary().add(FLOW_WRITE);
            lst.add(yx);
        }
        if (!lst.isEmpty())
            Debug.print(lst, "first");
        return (lst.isEmpty() ? null : lst);
    }

    public static List<Edge> second(Edge xy)
    {
        Edge    yx;
        List<Edge> lst;

        lst = new ArrayList<>();
        if (xy.hasPermission(WRITE))
        {
            xy.getImaginary().add(FLOW_WRITE);
            lst.add(xy);
        }
        if (xy.hasPermission(FLOW_WRITE))
        {
            yx = getEdge(xy.getE2(), xy.getE1());
            yx.getImaginary().add(FLOW_READ);
            lst.add(yx);
        }
        if (!lst.isEmpty())
            Debug.print(lst, "second");
        return (lst.isEmpty() ? null : lst);
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

                xz.getImaginary().add(FLOW_READ);
                zx.getImaginary().add(FLOW_WRITE);

                lst.add(xz);
                lst.add(zx);
                Debug.print(lst, "spy");
            }
        }
        return (lst.isEmpty() ? null : lst);
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

                xz.getImaginary().add(FLOW_WRITE);
                zx.getImaginary().add(FLOW_READ);

                lst.add(xz);
                lst.add(zx);
                Debug.print(lst, "find");
            }
        }
        return (lst.isEmpty() ? null : lst);
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
                xz = getEdge(yx.getE2(), yz.getE2());
                zx = getEdge(yz.getE2(), yx.getE2());

                xz.getImaginary().add(FLOW_WRITE);
                zx.getImaginary().add(FLOW_READ);

                lst.add(xz);
                lst.add(zx);
                Debug.print(lst, "pass");
            }
        }
        return (lst.isEmpty() ? null : lst);
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

                xz.getImaginary().add(FLOW_READ);
                zx.getImaginary().add(FLOW_WRITE);

                lst.add(xz);
                lst.add(zx);
                Debug.print(lst, "post");
            }
        }
        return (lst.isEmpty() ? null : lst);
    }
}
