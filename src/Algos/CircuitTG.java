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


    // Это ТЖ замыкание, смотри как оно работает
    // в доках ./docs/Algos/CircuitTG.md
    private static void spread() {
        Set<IEntity> N;

        // Создаем множество N (множество - это структура в джаве, которая не содержит повторящихся элементов)
        // Сюда будем складывать все вершины, которые получим при переборе ребер
        // про перебор ребер ниже
        N = new HashSet<>();
        // Перебираем ребра из списка L
        // там лежат все ребра с правилом тейк и/или грант
        for (Edge l: L)
        {
            // добавляем наше ребро в список удаленных ребер
            // (потом из удалим из самого L, просто во время итерации по структуре
            // нельзя ее как-либо менять)
            removedL.add(l);

            // Добавляем во множество вершины от ребра l ( нарпример x ----> y)
            N.add(l.getE1()); // тогда это x
            N.add(l.getE2()); // а это y

            // теперь перебираем каждую вершины из множества N
            for (IEntity n: N)
                // Если эта вершина не входит в текущее ребро
                // не равне его иксу и игрику (вспоминаем пример x ----> y)
                if (n != l.getE2() && n != l.getE1())

                    // тогда вызываем функцию check
                    check(l, n);
        }
        L.addAll(addedL);
        L.removeAll(removedL);
        addedL.clear();
        removedL.clear();
    }

    // в функции check мы применяем к тому самому
    // ребру l и вершине n правило take, а потом grant
    // если получилось новое ребро с правилами тейк или грант в результате
    // этих операций, тогда заносим новосозданное ребро во множество addedL
    private static void check(Edge l, IEntity n) {
        Edge e;

        // если получилось ребро в результате операции take
        if ((e = take(l.getE1(), l.getE2(), n)) != null)
            // и оно имеет права тейк или грант
            if (e.hasPermission(TAKE) || e.hasPermission(GRANT))
                // заносим во множество addedL (все ребра оттуда мы потом добавим в наше другое множество - L)
                addedL.add(e);
        // тут тоже самое, но с правилом grant
        if ((e = grant(l.getE1(), l.getE2(), n)) != null)
            if (e.hasPermission(TAKE) || e.hasPermission(GRANT))
                addedL.add(e);
    }

    // Подготавливаем все необходимые множества (множество - это структура в джаве, которая не содержит повторящихся элементов),
    // создаем объекты для каждого субъекта
    private static void prepare() {

        // множество L с которым мы будем работать в методе spread
        L = new LinkedHashSet<>();
        // это множество мы будем использовать в методе spread
        // чтобы заносить туда ребра, которые хотим удалить
        // удалять прямо из L мы не сможем, потому что будем в этом время
        // по нему итерироваться ( а так в джаве нельзя, и ходить по массиву и изменять его одновременно)
        removedL = new HashSet<>();
        // с такой же целью создадим множество, но только сюда
        // будем добавлять ребра
        addedL = new HashSet<>();

        // Каждый субъект обладает функцией создания объекта
        // после создания, субъект имеет все права к этому объекту.
        // Поэтому перебираем все субъекты на нашем графе
        for (Subject s : Main.subjects)
            // и создаем для них объекты
            RuleDeJure.create(s);

        // Перебираем все ребра в нашем графе
        for (Edge j: Main.edgeMap)
            // если у этого ребра есть правило тейк или грант (или все вместе)
            if (j.hasPermission(TAKE) || j.hasPermission(GRANT))
                // добавляем в наше множество L (с которым будем работать в методе spread)
                L.add(j);
    }

    public static void start() {
        prepare();
        // до тех пор пока L не пуст, выполняем распространение прав
        // методом spread
        while (!L.isEmpty()) {
            spread();
        }
    }
}
