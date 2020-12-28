package Algos;

import Entities.IEntity;
import Main.Edge;
import Main.Main;

import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.Set;

import static Main.Permission.GRANT;
import static Main.Permission.TAKE;
import static Rules.RuleDeJure.grant;
import static Rules.RuleDeJure.take;

public class CircuitDeJure{

    private static Set<Edge>    L;
    private static Set<IEntity> N;

    // это замыкание де юре
    // чтобы лучше понять, как оно работает, лучше посмотри в доки
    // ./docs/Algos/CurcuitDeJure.md
    private static void spread(String type) {

        // перебираем все ребра из множества L
        // которое подготавливали в методе prepare
        for (Edge l: L)
            // перебираем все вершины в графе (берем из списка N), который
            // тоже подготовили в методе prepare
            for (IEntity n: N)
            {
                // Если вершина n не равна ниодной вершине из ребра l
                // тогда входим в условие
                if (n != l.getE2() && n != l.getE1())
                {
                    // если текущее замыкание Take (вспомни что было в start!)
                    if (type.equals("take"))
                        // тогда применяем правило take к паре вершин ребра l
                        // и к вершине n
                        take(l.getE1(), l.getE2(), n);
                    // если grant
                    else if (type.equals("grant"))
                        // то применяем grant
                        grant(l.getE1(), l.getE2(), n);
                }
            }
    }

    // Подготавливаем множество L (множество - это структура в джаве, которая не содержит повторящихся элементов)
    private static void prepare() {
        L = new LinkedHashSet<>();
        // в множестве N содержатся сейчас все существующие вершины графа
        N = Main.vertexMap;

       // перебираем все ребра из списка edgeMap
        for (Edge j: Main.edgeMap)
            // Выбираем ребра, которые содержат право take и/или grant
            if (j.hasPermission(TAKE) || j.hasPermission(GRANT))
                // Если есть такое ребро (у которого есть право тейк, грант, или все вместе)
                // добавляем во множество L
                L.add(j);
    }

    public static void start() {
        prepare();
        // запускаем замыканием именно в таком порядке
        // сначало применяем ко всем ребра и вершинам правило take
        // затем грант, затем снова take
        for (String type: Arrays.asList("take", "grant", "take"))
            spread(type);
    }
}
