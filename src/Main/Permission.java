package Main;

import Entities.IEntity;

public enum Permission {

    FLOW_READ("im", "fr"),
    FLOW_WRITE("im", "fw"),

    READ("re", "r"),
    WRITE("re", "w"),
    EXECUTE("re", "x"),

    TAKE("re", "t"),
    GRANT("re", "g"),

    ANY("any", "any");

    Permission(String type, String right) {
        this.type = type;
        this.right = right;
    }

    private String type;
    private String right;

    public String getType()
    {
        return (type);
    }
    public String getRight() {
        return (right);
    }

    public static Permission getByRight(String right)
    {
        switch (right)
        {
            case "fw": return (FLOW_WRITE);
            case "fr": return (FLOW_READ);
            case "r":  return (READ);
            case "w":  return (WRITE);
            case "t":  return (TAKE);
            case "g":  return (GRANT);
        }
        return (ANY);
    }
}
