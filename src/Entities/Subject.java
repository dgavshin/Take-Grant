package Entities;

import Main.Edge;
import Tools.Appropriator;
import Tools.RandomName;

import java.util.ArrayList;
import java.util.List;

import static Main.Main.edgeMap;

public class Subject extends Object implements IEntity {

    private String subjectName;

    public Subject() {
        // subjectName = RandomName.get(); =(
        subjectName = Appropriator.getName();
    }

    public Subject(String name) {
        subjectName = name;
    }

    @Override
    public String getName() {
        return (subjectName);
    }

    @Override
    public String toString() {
        return ("Subject");
    }

    @Override
    public List<IEntity> getChildren() {
        List<IEntity> children = new ArrayList<>();
        for (Edge edge : edgeMap)
            if (this == edge.getE1())
                children.add(edge.getE2());
        return (children);
    }

    @Override
    public Edge getEdge(IEntity o) {
        for (Edge edge : edgeMap)
            if (this == edge.getE1() && o == edge.getE2())
                return (edge);
        return (null);
    }
}
