package com.mush.weirdo.space;

import java.util.ArrayList;

/**
 * Created by Cic on 19.1.2017.
 */
public class SpaceNode {
    public Point3F localPosition;

    private SpaceNode parent = null;
    private ArrayList<SpaceNode> subNodes;
    private Point3F globalPosition;
    private boolean isGlobalPositionValid;

    public SpaceNode() {
        localPosition = new Point3F();
        globalPosition = localPosition;
        invalidateGlobalPosition();
    }

    public ArrayList<SpaceNode> getSubnodes() {
        return subNodes;
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

    public void removeAllSubnodes(){
        if (subNodes == null) {
            return;
        }
        for (SpaceNode node : subNodes) {
            node.setParent(null);
        }
        subNodes.clear();
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
        if (!isGlobalPositionValid) {
            globalPosition = new Point3F(localPosition);

            SpaceNode ancestor = parent;

            while (ancestor != null) {
                globalPosition.offset(ancestor.localPosition);
                ancestor = ancestor.getParent();
            }

            isGlobalPositionValid = true;
        }

        return globalPosition;
    }

    public void invalidateGlobalPosition(){
        isGlobalPositionValid = false;
    }
}
