package com.zerulus.game.util;

import com.zerulus.game.entity.GameObject;
import com.zerulus.game.math.AABB;

import java.util.HashMap;
import java.util.Stack;
import java.util.ArrayList;

public class AABBTree {

    private ArrayList<AABBNode> nodeList;
    private HashMap<GameObject, AABBNode> nodeIndex;

    private int rootIndex = 0;

    public AABBTree() {
        this.nodeList = new ArrayList<AABBNode>();
        this.nodeIndex = new HashMap<GameObject, AABBNode>();
    }

    public void insert(GameObject go) {
		AABBNode node = new AABBNode(go, nodeList.size());
		System.out.println("Adding: game object to tree...");
		nodeList.add(node);

        AABBNode index = insertLeaf(node);
        nodeIndex.put(go, index);
    }

	// TODO: fix inifite loop. check if tree is correctly created
    public AABBNode insertLeaf(AABBNode newNode) {

        // make sure we are inserting a new leaf?

        if(nodeList.size() == 1) {
            rootIndex = newNode.index;
            System.out.println("WARNING: This is the root node");
            return newNode;
		}
		
		// looking for a suitable leaf node
        int treeIndex = rootIndex;
        while(!nodeList.get(treeIndex).isLeaf()) {

			AABB aabb = newNode.aabb; // node to insert
            AABBNode treeNode = nodeList.get(treeIndex); // node that could be root or parent/grand
            AABBNode rightNode = nodeList.get(treeNode.right); // node that could be siblings / cousins or nil
            AABBNode leftNode = nodeList.get(treeNode.left); // node that could be siblings / cousins or nil

            AABB combinedAabb = treeNode.aabb.merge(aabb); // new node that could be parent/grand of node to insert

            float parentCost = 2.0f * combinedAabb.getSurfaceArea(); // cost of being a parent (lol)
            float minPushCost = 2.0f * (combinedAabb.getSurfaceArea() - treeNode.aabb.getSurfaceArea()); // cost of being the next descendant

            float costLeft = 0;
            float costRight = 0;

            if(leftNode.isLeaf()) {
                costLeft = aabb.merge(leftNode.aabb).getSurfaceArea() + minPushCost;
            } else {
                costLeft = (aabb.merge(leftNode.aabb).getSurfaceArea() - leftNode.aabb.getSurfaceArea()) + minPushCost;
            }

            if(rightNode.isLeaf()) {
                costRight = aabb.merge(rightNode.aabb).getSurfaceArea() + minPushCost;
            } else {
                costRight = (aabb.merge(rightNode.aabb).getSurfaceArea() - rightNode.aabb.getSurfaceArea()) + minPushCost;
            }

            if(parentCost < costLeft && parentCost < costRight) {
                break;
            }

            if(costLeft < costRight) {
                treeIndex = treeNode.left;
            } else {
                treeIndex = treeNode.right;
			}
        }

		// suitable sibling leaf node found
        AABBNode sibling = nodeList.get(treeIndex);
        int oldParentIndex = sibling.parent;

        AABBNode newParent = new AABBNode(sibling.parent, treeIndex, newNode.index, nodeList.size(), newNode.height + 1);
        newParent.aabb = newNode.aabb.merge(sibling.aabb);

        nodeList.get(newNode.index).parent = newParent.index;
		nodeList.get(treeIndex).parent = newParent.index;

        if(oldParentIndex == -1) {
            rootIndex = newParent.index;
        } else {
            AABBNode oldParent = nodeList.get(oldParentIndex);
            if(oldParent.left == sibling.index) {
                nodeList.get(oldParentIndex).left = newParent.index;
            } else {
                nodeList.get(oldParentIndex).right = newParent.index;
            }
		}
		
		nodeList.add(newParent);

        fixUpwardsTree(newNode.parent);
        return newNode;
    }

    public void removeObject(GameObject go) {
        AABBNode node = nodeIndex.get(go);
        removeLeaf(node);
        nodeIndex.remove(go);

    }

    public void update(GameObject go) {
        AABBNode node = nodeIndex.get(go);
        updateLeaf(node, node.aabb);
    }

    public ArrayList<GameObject> queryOverlaps(GameObject go) {
        ArrayList<GameObject> result = new ArrayList<GameObject>();
        Stack<AABBNode> stack = new Stack<AABBNode>();
        AABB test = go.getBounds();

        stack.push(nodeList.get(rootIndex));
        while(!stack.empty()) {
            AABBNode node = stack.pop();

            if(nodeIndex == null) continue;

            if(node.aabb.collides(test)) {
                if(node.isLeaf() && node.go != go) {
                    result.add(node.go);
                } else {
                    stack.push(nodeList.get(node.left));
                    stack.push(nodeList.get(node.right));
                }
            }
        }

        return result;
    }

    public void removeLeaf(AABBNode node) {
        if(node.index == rootIndex) {
            rootIndex = -1;
            return;
        }

        int parentIndex = node.parent;
        AABBNode parent = nodeList.get(parentIndex);
        int grandParentIndex = parent.parent;
        
        int siblingIndex = parent.left == node.index ? parent.right : parent.left;
        AABBNode sibling = nodeList.get(siblingIndex);

        if(grandParentIndex != -1) {
            AABBNode grandParent = nodeList.get(grandParentIndex);
            if(grandParent.left == parent.index) {
                nodeList.get(grandParentIndex).left = sibling.index;
            }  else {
                nodeList.get(grandParentIndex).right = sibling.index;
            }
            nodeList.get(siblingIndex).parent = grandParent.index;

            fixUpwardsTree(grandParent.index);
            
        } else {
            rootIndex = siblingIndex;
            nodeList.get(siblingIndex).parent = -1;
        }

        nodeList.get(node.index).parent = -1;
    }

    public void updateLeaf(AABBNode node, AABB aabb) {
        
        if(node.aabb == aabb) return;

        removeLeaf(node);
        node.aabb = aabb;
        insertLeaf(node);
    }

    public void fixUpwardsTree(int nodeIndex) {
        while(nodeIndex != -1) {
            AABBNode node = nodeList.get(nodeIndex);
            
            AABBNode leftNode = nodeList.get(node.left);
            AABBNode rightNode = nodeList.get(node.right);
            nodeList.get(nodeIndex).aabb = leftNode.aabb.merge(rightNode.aabb);

            nodeIndex = node.parent;
        }
	}
	
	public String toString() {
		String result = "[\n";
		ArrayList<AABBNode> tree = nodeList;

		for(int i = 0; i < tree.size(); i++) {
			result += "  " + i + ":{ " + tree.get(i).go + ", " +
				tree.get(i).left + ", " + tree.get(i).right + 
				", " + tree.get(i).parent + " }\n";
		}

		return result + "]";
	}

    private class AABBNode {

        public AABB aabb;
        public GameObject go;

        public int parent = -1;
        public int left = -1;
        public int right = -1;

        public int index;
        public int height = -1; // not in use

        public boolean isLeaf() { return left == -1; }

        public AABBNode(GameObject go, int index) {
            this.go = go;
            this.aabb = go.getBounds();
            this.index = index;
        }

        public AABBNode(int parent, int left, int right, int index, int height) {
            this.parent = parent;
            this.left = left;
            this.right = right;
            this.index = index;
            this.height = height;
        }
	}
}