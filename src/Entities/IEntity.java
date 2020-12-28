package Entities;

import Main.Edge;

import java.util.List;

public interface IEntity {

    String getName();

    List<IEntity> getChildren();

    Edge getEdge(IEntity o);
}
