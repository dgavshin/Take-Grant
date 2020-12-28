package Entities;

import Main.Edge;
import Tools.Appropriator;
import Tools.RandomName;

import java.util.ArrayList;
import java.util.List;

import static Main.Main.edgeMap;

public class Object implements IEntity {

    private String objectName;

    public Object() {
        // objectName = RandomName.get(); =(
        objectName = Appropriator.getName();
    }

    public Object(String name) {
        objectName = name;
    }

    @Override
    public String getName() {
        return (objectName);
    }

    @Override
    public String toString() {
        return ("Object");
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
