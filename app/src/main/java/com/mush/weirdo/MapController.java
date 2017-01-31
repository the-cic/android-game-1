package com.mush.weirdo;

import com.mush.weirdo.space.SpaceNode;
import com.mush.weirdo.space.SpaceObject;
import com.mush.weirdo.space.SpaceObjectBody;

import java.util.ArrayList;

/**
 * Created by Cic on 31.1.2017.
 */
public class MapController {

    private SpaceNode rootNode;
    // Foreground objects
    private ArrayList<SpaceObject> objects;

    private SpaceNode[] chunkNodes;
    private ArrayList<SpaceObject>[] chunkObjects;
    private int currentChunkIndex;
    private MapProvider mapProvider;

    public MapController(SpaceNode rootNode1, ArrayList<SpaceObject> objects1, MapProvider mapProvider1) {
        this.mapProvider = mapProvider1;
        this.rootNode = rootNode1;
        this.objects = objects1;
        this.chunkNodes = new SpaceNode[3];
        this.chunkObjects = new ArrayList[3];
        setChunk(0);
    }

    private void setChunk(int index) {
        this.currentChunkIndex = index;
        for (int i = 0; i < chunkNodes.length; i++) {
            addChunk(index + i, i);
        }
    }

    private void addChunk(int chunkIndex, int chunkOrder) {
        int GROUND_Y = 43; // fix this

        SpaceNode chunkNode = getNodeForChunk(chunkIndex);
        chunkNodes[chunkOrder] = chunkNode;

        ArrayList<SpaceObject> aChunkObjects = mapProvider.getChunk(chunkIndex);
        chunkObjects[chunkOrder] = aChunkObjects;

        for (SpaceObject object : aChunkObjects) {
//            object.spaceNode.localPosition.offset(0, 0, GROUND_Y);
            object.spaceNode.addToNode(chunkNode);
            objects.add(object);
        }
    }

    private void clearChunk(int chunkOrder){
        SpaceNode chunkNode = chunkNodes[chunkOrder];
        chunkNode.removeAllSubnodes();

        ArrayList<SpaceObject> aChunkObjects = chunkObjects[chunkOrder];
        for (SpaceObject object : aChunkObjects) {
            objects.remove(object);
        }
    }

    private SpaceNode getNodeForChunk(int chunk) {
        SpaceNode chunkNode = new SpaceNode();
        chunkNode.localPosition.offset(chunk * mapProvider.chunkLength * mapProvider.objectWidth, 0, 0);
        chunkNode.addToNode(rootNode);
        return chunkNode;
    }

    /**
     * @param ofs -1 | +1
     */
    public void advanceChunk(int ofs) {
        int newChunkIndex = currentChunkIndex + ofs;
        if (newChunkIndex < 0) {
            return;
        }
        // clear old chunk info
        if (ofs == 1) {
            clearChunk(0);
        } else {
            clearChunk(2);
        }

        // shift chunk info
        if (ofs == 1) {
            chunkNodes[0] = chunkNodes[1];
            chunkNodes[1] = chunkNodes[2];
            chunkObjects[0] = chunkObjects[1];
            chunkObjects[1] = chunkObjects[2];
        } else {
            chunkNodes[2] = chunkNodes[1];
            chunkNodes[1] = chunkNodes[0];
            chunkObjects[2] = chunkObjects[1];
            chunkObjects[1] = chunkObjects[0];
        }

        // add new chunk info
        if (ofs == 1) {
            addChunk(newChunkIndex + 2, 2);
        } else {
            addChunk(newChunkIndex, 0);
        }

        currentChunkIndex = newChunkIndex;
    }

    public int getCurrentChunkIndex(){
        return currentChunkIndex;
    }
}
