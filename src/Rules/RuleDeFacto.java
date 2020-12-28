package Rules;

import Entities.IEntity;
import Main.Edge;
import Main.Debug;
import Main.Permission;

import java.util.*;

import static Main.Permission.*;

public class RuleDeFacto extends Rule {

    /**
     * Первое правило, смотри в доках как оно работает
     * docs/Rules/RuleDefacto.md
     */
    public static List<Edge> first(Edge xy)
    {
        List<Edge>  lst;
        Edge        yx;

        // Создаем список ребер, туда занесем те ребра
        // у которых появились новые права
        lst = new ArrayList<>();

        // Если переданное ребро xy содержит право READ
        // (x ----- read -----> y)
        if (xy.hasPermission(READ))
            // тогда создаем информационный поток от x к y
            // и если такого права еще не было на этом ребре,
            // то добавляем это ребро в список lst
            if (append(xy, FLOW_READ, "first")) lst.add(xy);

        // если ребро xy уже содержит информационный поток на чтение
        if (xy.hasPermission(FLOW_READ)) {
            // тогда создаем информационный поток на запись, но уже в другую сторону
            // от y к x
            yx = getEdge(xy.getE2(), xy.getE1());


            // если такого права еще не было на этом ребре,
            // то добавляем это ребро в список lst
            if (append(yx, FLOW_WRITE, "first")) lst.add(yx);
        }
        return (lst);
    }

    /**
     * Второе правило, смотри в доках как оно работает
     * docs/Rules/RuleDefacto.md
     */
    public static List<Edge> second(Edge xy)
    {
        Edge        yx;
        List<Edge>  lst;

        // Создаем список ребер, туда занесем те ребра
        // у которых появились новые права
        lst = new ArrayList<>();

        // если переданное нами ребро xy содержит право WRITE
        if (xy.hasPermission(WRITE))
            // тогда добавляем информационный поток на запись к этому ребру
            // и если его не было раньше, то добавляем ребро xy в список lst
            if (append(xy, FLOW_WRITE, "second")) lst.add(xy);
        if (xy.hasPermission(FLOW_WRITE))
        {
            // ищем ребро, и если не находим, создаем
            // но поменяем направление
            // было xy x -----> y, теперь ребро yx y -----> x
            // (думаем логически, это же джава)
            yx = getEdge(xy.getE2(), xy.getE1());

            // добавляем к ребру информационный поток на чтение
            // и если его раньше не было у ребра, тогда заносим ребро yx
            // в список lst
            if (append(yx, FLOW_READ, "second")) lst.add(yx);
        }
        return (lst);
    }

    /**
     * Правило spy, смотри в доках как оно работает
     * docs/Rules/RuleDefacto.md
     */
    public static List<Edge> spy(Edge xy, IEntity z)
    {
        List<Edge>  lst;
        Edge        xz;
        Edge        yz;
        Edge        zx;

        // Создаем список ребер, туда занесем те ребра
        // у которых появились новые права
        lst = new ArrayList<>();
        // тут заглушка в виде canShare
        // в данном случае он всегда возвращает True
        // если ребро yz есть в списке edgeMap (списке всех ребер)
        if (canShare(xy.getE2(), z))
        {
            // Ищем ребро yz
            yz = xy.getE2().getEdge(z);

            // если ребро xy и yz содержат информационные потоки на чтение
            if (xy.hasPermission(FLOW_READ) && yz.hasPermission(FLOW_READ)) {

                // тогда ищем ребро zx и xz, и если их нет, то создаем
                zx = getEdge(yz.getE2(), xy.getE1());
                xz = getEdge(xy.getE1(), yz.getE2());

                // добавляем к ребру информационный поток на запись
                // и если его раньше не было у ребра, тогда заносим ребро zx
                // в список lst
                if (append(zx, FLOW_WRITE, "spy")) lst.add(zx);
                // добавляем к ребру информационный поток на чтение
                // и если его раньше не было у ребра, тогда заносим ребро xz
                // в список lst
                if (append(xz, FLOW_READ, "spy"))  lst.add(xz);
            }
        }
        return (lst);
    }


    /**
     * Правило find, смотри в доках как оно работает
     * docs/Rules/RuleDefacto.md
     */
    public static List<Edge> find(Edge xy, IEntity z)
    {
        List<Edge>  lst;
        Edge        yz;
        Edge        xz;
        Edge        zx;

        // Создаем список ребер, туда занесем те ребра
        // у которых появились новые права
        lst = new ArrayList<>();

        // тут заглушка в виде canShare
        // в данном случае он всегда возвращает True
        // если ребро yz есть в списке edgeMap (списке всех ребер)
        if (canShare(xy.getE2(), z))
        {
            // находим ребро yz
            yz = xy.getE2().getEdge(z);

            // Если у ребер xy и yz есть информационные потоки на запись
            if (xy.hasPermission(FLOW_WRITE) && yz.hasPermission(FLOW_WRITE)) {

                // то ищем ребра, и если их нет, создаем
                zx = getEdge(yz.getE2(), xy.getE1());
                xz = getEdge(xy.getE1(), yz.getE2());

                // и добавляем к ребру информационный поток на чтение
                // и если его раньше не было у ребра, тогда заносим ребро zx
                // в список lst
                if (append(zx, FLOW_READ, "find")) lst.add(zx);
                // дальше добавляем к ребру xz информационный поток на запись
                // и если его раньше не было у ребра, тогда заносим ребро xz
                // в список lst
                if (append(xz, FLOW_WRITE, "find"))  lst.add(xz);
            }
        }
        return (lst);
    }

    /**
     * Правило pass, смотри в доках как оно работает
     * docs/Rules/RuleDefacto.md
     */
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


        // Создаем список ребер, туда занесем те ребра
        // у которых появились новые права
        lst = new ArrayList<>();

        // тут заглушка в виде canShare
        // в данном случае он всегда возвращает True
        // если ребро yz есть в списке edgeMap (списке всех ребер)
        if (canShare(z, xy.getE2())) {

            // ищем ребро zy
            zy = z.getEdge(xy.getE2());

            // если у ребра ху есть информационный поток на запись, а у
            // ребра zу информационный поток на чтение
            if (xy.hasPermission(FLOW_WRITE) && zy.hasPermission(FLOW_READ)) {

                // то ищем ребра, и если их нет, создаем
                xz = getEdge(xy.getE1(), zy.getE1());
                zx = getEdge(zy.getE1(), xy.getE1());

                // добавляем к ребру информационный поток на запись
                // и если его раньше не было у ребра, тогда заносим ребро zx
                // в список lst
                if (append(zx, FLOW_WRITE, "post")) lst.add(zx);
                // и также добавляем к ребру информационный поток на чтение
                // и если его раньше не было у ребра, тогда заносим ребро xz
                // в список lst
                if (append(xz, FLOW_READ, "post"))  lst.add(xz);
            }
        }
        return (lst);
    }

    // этот метод добавляет в список информационных потоков рЕбра новые потоки.
    // Возвращает тру, если это ребро было добавлено
    // фолс, если оно уже было в списке
    private static boolean append(Edge e, Permission p, String f)
    {
        if (e.getImaginary().add(p)) {
            Debug.print(e, f, p);
            return (true);
        }
        return (false);
    }
}
