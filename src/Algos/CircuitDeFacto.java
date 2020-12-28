package Algos;
import Entities.IEntity;
import Main.Edge;
import Main.Main;

import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import static Main.Permission.*;
import static Rules.RuleDeFacto.*;

public class CircuitDeFacto{

    // это множество F (множество - это структура в джаве, которая не содержит повторящихся элементов)
    private static Set<Edge>    F;
    private static Set<Edge>    addedF;
    private static Set<Edge>    removedF;

    // метод замыкания прав по правилам де факто
    // смотри в доках, как работает это замыкание
    private static void spread() {
        Set<IEntity> N;

        // Cоздаем множество (это такая структура, где все элементы не повторяются)
        // здесь будут лежать все вершины, которые мы хотим обработать
        // обновляем этот список постепенно, во время замыкания
        N = new HashSet<>();

        // обходим все ребра из множества F
        for (Edge f: F)
        {
            // добавляем в множество removedF те ребра
            // которые мы потом удалим. Удалим мы их потому что
            // так по правилам замыкания нужно
            removedF.add(f);

            // Добавляем во множество N вершины этого ребра
            N.add(f.getE1());
            N.add(f.getE2());

            // перебираем каждую вершину из N
            for (IEntity n: N)
                // если эта вершина не равна ниодной вершине из ребра f
                if (n != f.getE2() && n != f.getE1())
                    // запускаем метод check, которые выполняет все
                    // правила де факто
                    check(f, n);
        }
        F.addAll(addedF);
        F.removeAll(removedF);

        addedF.clear();
        removedF.clear();
    }

    private static void check(Edge l, IEntity n) {
        // выполняем правило spy над ребро l и вершиной n
        // если появились новые права, то заносим их в спиоск addedF
        // ребро l в данном случае имеет вершины x --> y, а n это z
        // x ----> y ---->? z (ребро необязательно есь между yz).
        addedF.addAll(spy(l, n));
        // тоже самое с правилом find
        addedF.addAll(find(l, n));
        // post
        addedF.addAll(post(l, n));
        // pass
        addedF.addAll(pass(l, n));
    }

    // Подготавливаем множество F (множество - это структура, где элементы не повторяются)
    private static void prepare() {
        F = new LinkedHashSet<>();
        removedF = new HashSet<>();
        addedF = new HashSet<>();

        // Выбираем такие ребра, которые содержат одно (или несколько) из следующих прав:
        // информационный поток на чтение, информационный поток на запись,
        // право на чтение и/или право на запись
        for (Edge j: Main.edgeMap)
            if (j.hasPermission(FLOW_READ) || j.hasPermission(FLOW_WRITE) ||
                    j.hasPermission(READ)  || j.hasPermission(WRITE))
                F.add(j);

        // Применяем первое и второе правила ко всех этим ребрам
        for (Edge e: F)
        {
            List<Edge> lst;
            // если первое правило привело к появлению новых прав, то ...
            if ((lst  = first(e)) != null)
                // добавляем в новый список, иначе будет ошибка
                // и джава скажет, что мы изменяем F в то время, пока по нему
                // итерируемся. Так нельзя
                addedF.addAll(lst);
            // тут тоже самое
            if ((lst = second(e)) != null)
                addedF.addAll(lst);
        }
        // теперь добавляем к F все найденные ребра
        F.addAll(addedF);
        // очищаем временное множество
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
