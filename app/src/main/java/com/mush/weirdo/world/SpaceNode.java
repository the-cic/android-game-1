package com.mush.weirdo.world;

import android.graphics.Point;
import android.graphics.PointF;

import java.util.ArrayList;

/**
 * Created by Cic on 19.1.2017.
 */
public class SpaceNode {
    public Point3F localPosition;

    private SpaceNode parent = null;
    private ArrayList<SpaceNode> subNodes;

    public SpaceNode() {
        localPosition = new Point3F();
    }

    public void addSubNode(SpaceNode node) {
        if (subNodes == null) {
            subNodes = new ArrayList<>();
        }
        if (!subNodes.contains(node)) {
            subNodes.add(node);
            node.setParent(this);
        }
    }

    public void removeSubNode(SpaceNode node) {
        if (subNodes == null) {
            return;
        }
        if (subNodes.contains(node)) {
            subNodes.remove(node);
            node.setParent(null);
        }
    }

    protected void setParent(SpaceNode node) {
        this.parent = node;
    }

    public void addToNode(SpaceNode node) {
        node.addSubNode(this);
    }

    public void remove() {
        if (this.parent == null) {
            return;
        }
        this.parent.removeSubNode(this);
    }

    public SpaceNode getParent() {
        return parent;
    }

    public Point3F localToGlobal() {
        Point3F g = new Point3F(localPosition);

        SpaceNode ancestor = parent;

        while (ancestor != null) {
            g.offset(ancestor.localPosition);
            ancestor = ancestor.getParent();
        }

        return g;
    }
}
