package Main;

import Algos.CircuitDeFacto;
import Algos.CircuitDeJure;
import Algos.CircuitTG;
import Entities.IEntity;
import Entities.Object;
import Entities.Subject;
import Rules.Rule;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

import static Main.Debug.printEdgeMap;
import static Main.Permission.getByRight;

public class Main {

    public static char name = 'a' - 1;
    public static List<Edge> edgeMap = new ArrayList<>();
    public static Set<IEntity> vertexMap = new HashSet<>();
    public static List<Subject> subjects = new ArrayList<>();
    public static List<Object> objects = new ArrayList<>();

    private static void getConfig(String configPath) throws IOException {
        // Создаем хешмап (это такая структура, которая состоит из элементов типа Ключ:Значение)
        // в данном случае ключом будет какая-либо строка, а значением IEntity (интерфейс для объекта и субъекта)
        Map<String, IEntity> entities = new HashMap<>();

        // Создаем экземпляр класса BufferedReader, чтобы построчно читать файл переданный
        // в аргументе configPath
        BufferedReader reader = new BufferedReader(new FileReader(configPath));
        // читаем первую строку - количество всех субъектов и объектов
        int size = Integer.parseInt(reader.readLine());
        // Теперь заходим в цикл, итераций у которого будет такое количество
        // какое указано в size
        // тоесть перебирать будем столько строк конфига,
        // сколько указано в самой первой строке этого же конфига
        for (int i = 0; i < size; i++) {
            // читаем строку, разделяем ее по пробелу и получаем массив
            // из максимум двух элементов
            // строка состоит из "имени вершины" и ее типа "s" либо "o"
            // подробнее в доках
            String[] entity = reader.readLine().split(" ", 2);
            // если второй элемент этого массива entity равен букве 's'
            if (entity[1].equals("s"))
                // тогда создаем новых субъект и даем ему имя
                // которое лежит на первом месте в этом массиве
                entities.put(entity[0], new Subject(entity[0]));
            else
                // иначе создаем объект
                entities.put(entity[0], new Object(entity[0]));
        }

        // теперь читаем до тех пор, пока у нас остались строчки
        while (reader.ready()) {
            // читаем строку и кладем в переменную str
            String str = reader.readLine();
            // делим строку по двоеточию
            // s1-o1:t, r, w
            // строка выглядит как сверху, поэтому мы отделяем права t, r, w от остальной части
            String[] edge = str.split(":");

            // теперь отделяем s1 от o1 по дефису
            String[] vertexes = edge[0].split("-");

            // дальше отделяем все права по запятой
            String[] permissions = edge[1].split(",");

            // В vertexes лежат имена вершин, это наши ключи, чтобы
            // получить экземпляры классов объектов и субъектов
            // полученные экземпляры классов entities.get(...) (их два)
            // мы отправляем в функции getEdge, которая найдет нам существующее ребро в графе
            // а если не найдет, то создаст
            Edge e = Rule.getEdge(entities.get(vertexes[0]), entities.get(vertexes[1]));

            // дальше, перебираем все права из массива permissions
            for (String p : permissions)
                // и в фнукции getByRight сопоставляем строчное название права
                // с его реальным экземпляром
                // а потом реальный экзепляр кладем в соответствующий список ребра
                e.addPermission(getByRight(p));
        }
        // Теперь перебираем все пары ключ-значение из хешмапа e
        // конструкция ниже называется foreach, выполнить действия Для Каждого элемента (фор ич)
        // форыч сам берет значения из списков, множеств, в данном случае из хешмапы
        // и кладет пару ключ-значение в переменную -e-
        // e - ЭТО ПАРА КЛЮЧ:ЗНАЧЕНИЕ
        // КЛЮЧ В ДАННОМ СЛУЧАЕ ИМЯ ОБЪЕКТА (или субъекта)
        // ЗНАЧЕНИЕ ЭТО ЭКЗЕМПЛЯР КЛАССА (либо объекта либо субъекта) чтобы это не значило
        for (Map.Entry<String, IEntity> e: entities.entrySet()) {
            // если значение (помни, что есть еще ключ) пары -e-
            // это субъект, тогда добавляем этот субъект в список всех субъектов
            if (e.getValue() instanceof Subject)
                // в скобках (Subject) мы приводим тип ЗНАЧЕНИЯ к типу Subject (это ж субъект)
                Main.subjects.add((Subject) e.getValue());
            else
                // иначе в список всех объектов
                Main.objects.add((Object) e.getValue());
        }
        reader.close();
    }

    public static void main(String[] args) throws IOException {
        // Читаем конфиг из указанной папки
        getConfig(".\\configs\\config");
        // выводим текущий граф
        printEdgeMap("Start map");
        // запускаем тж замыкание
        CircuitTG.start();
        // снова выводим граф
        printEdgeMap("TG");
        // запускаем де юре замыкание
        CircuitDeJure.start();
        // опять принтим граф
        printEdgeMap("DE JURE");
        // теперь де факто замыкание
        CircuitDeFacto.start();
        printEdgeMap("DE FACTO");
    }
}$