package aoc6;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Orbit {

    private String name;
    private Set<Orbit> children;
    private Orbit parent;

    public Orbit(String orbitee) {
        this.name = orbitee;
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public void addChild(Orbit orbit) {
        if(children == null) {
            children = new HashSet<>();
        }
        children.add(orbit);
    }

    public Set<Orbit> getChildren() {
        return children;
    }

    public void setChildren(final Set<Orbit> children) {
        this.children = children;
    }

    public Orbit getParent() {
        return parent;
    }

    public void setParent(final Orbit parents) {
        this.parent = parents;
    }

    public int countDistanceToParent(final Orbit parent) {
        int result = 1;
        Orbit currentParent = this.getParent();
        while(currentParent != parent) {
            result++;
            currentParent = currentParent.getParent();
        }
        return result;
    }

    public List<Orbit> getAllParents() {
        List<Orbit> resultList = new ArrayList<>();
        Orbit current = parent;
        while (current != null) {
            resultList.add(current);
            current = current.getParent();
        }
        return resultList;
    }
}
