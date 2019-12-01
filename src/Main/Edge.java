package Main;

import Entities.IEntity;

import java.util.*;

import static Main.Permission.ANY;

public class Edge {

    private IEntity             e1;
    private IEntity             e2;
    private Set<Permission>     rePermissions = new HashSet<>();
    private Set<Permission>     imPermissions = new HashSet<>();

    public                      Edge(IEntity e1, IEntity e2, Permission ... p)
    {
        this.e1 = e1;
        this.e2 = e2;
        for (Permission entry: p)
        {
            if (entry.getType().equals("im"))
                imPermissions.add(entry);
            else if (entry.getType().equals("re"))
                rePermissions.add(entry);
        }
    }

    public                      Edge(IEntity e1, IEntity e2, Edge p)
    {
        this.e1 = e1;
        this.e2 = e2;
        imPermissions.addAll(p.getImaginary());
        rePermissions.addAll(p.getReal());
    }

    public boolean              hasPermission(Permission p)
    {
        for (Permission e: rePermissions)
            if (e == p)
                return (true);
        for (Permission e: imPermissions)
            if (e == p)
                return (true);
        return (false);
    }

    public Set<Permission>      getReal() { return rePermissions; }
    public Set<Permission>      getImaginary() { return imPermissions; }
    public IEntity              getE1() { return (e1); }
    public IEntity              getE2() { return (e2); }

    public String               permissionsToString()
    {
        StringBuilder str = new StringBuilder();
        for (Permission p: rePermissions)
            str.append(p.getRight()).append(" ");
        for (Permission p: imPermissions)
            str.append(p.getRight()).append(" ");
        return (str.toString());
    }
    public void                 addPermission(Permission p)
    {
        if (p.getType().equals("im"))
            this.getImaginary().add(p);
        else if (p.getType().equals("re"))
            this.getReal().add(p);
    }


}
