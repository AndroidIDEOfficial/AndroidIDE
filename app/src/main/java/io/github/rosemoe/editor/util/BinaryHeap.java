/*
 *   Copyright 2020-2021 Rosemoe
 *
 *   Licensed under the Apache License, Version 2.0 (the "License");
 *   you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 */
package io.github.rosemoe.editor.util;

import android.util.SparseIntArray;

/**
 * A General implementation of big root heap
 *
 * @author Rose
 */
public class BinaryHeap {

    /**
     * Map from id to its position in heap array
     */
    private final SparseIntArray idToPosition;

    /**
     * Id allocator
     */
    private int idAllocator = 1;

    /**
     * Current node count in heap
     */
    private int nodeCount;

    /**
     * Node array for heap
     */
    private Node[] nodes;

    /**
     * Create a binary heap
     * This heap maintains its max value in heap
     */
    public BinaryHeap() {
        idToPosition = new SparseIntArray();
        nodeCount = 0;
        nodes = new Node[129];
    }

    /**
     * Ensure there is enough space
     *
     * @param capacity desired space size
     */
    public void ensureCapacity(int capacity) {
        capacity++;
        if (nodes.length < capacity) {
            Node[] origin = nodes;
            if (nodes.length << 1 >= capacity) {
                nodes = new Node[nodes.length << 1];
            } else {
                nodes = new Node[capacity];
            }
            System.arraycopy(origin, 0, nodes, 0, nodeCount + 1);
        }
    }

    /**
     * Get the max value in this heap
     *
     * @return Max value
     */
    public int top() {
        if (nodeCount == 0) {
            return 0;
        }
        return nodes[1].data;
    }

    /**
     * Internal implementation to move down nodes
     *
     * @param position target node's position in heap
     */
    private void heapifyDown(int position) {
        for (int child = position * 2; child <= nodeCount; child = position * 2) {
            Node parentNode = nodes[position], childNode;
            if (child + 1 <= nodeCount && nodes[child + 1].data > nodes[child].data) {
                child = child + 1;
            }
            childNode = nodes[child];
            if (parentNode.data < childNode.data) {
                idToPosition.put(childNode.id, position);
                idToPosition.put(parentNode.id, child);
                nodes[child] = parentNode;
                nodes[position] = childNode;
                position = child;
            } else {
                break;
            }
        }
    }

    /**
     * Internal implementation to move up nodes
     *
     * @param position target node's position in heap
     */
    private void heapifyUp(int position) {
        for (int parent = position / 2; parent >= 1; parent = position / 2) {
            Node childNode = nodes[position], parentNode = nodes[parent];
            if (childNode.data > parentNode.data) {
                idToPosition.put(childNode.id, parent);
                idToPosition.put(parentNode.id, position);
                nodes[position] = parentNode;
                nodes[parent] = childNode;
                position = parent;
            } else {
                break;
            }
        }
    }

    /**
     * Add a new node to the heap
     *
     * @return Id of node
     * @throws IllegalStateException when there is no new id available
     */
    public int push(int value) {
        ensureCapacity(nodeCount + 1);
        if (idAllocator == Integer.MAX_VALUE) {
            throw new IllegalStateException("unable to allocate more id");
        }
        int id = idAllocator++;
        nodeCount++;
        nodes[nodeCount] = new Node(id, value);
        idToPosition.put(id, nodeCount);
        heapifyUp(nodeCount);
        return id;
    }

    /**
     * Update the value of node with given id to newValue
     *
     * @param id       Id returned by push()
     * @param newValue new value for this node
     * @throws IllegalArgumentException when the id is invalid
     */
    public void update(int id, int newValue) {
        int position = idToPosition.get(id, 0);
        if (position == 0) {
            throw new IllegalArgumentException("trying to update with an invalid id");
        }
        int origin = nodes[position].data;
        nodes[position].data = newValue;
        if (origin < newValue) {
            heapifyUp(position);
        } else if (origin > newValue) {
            heapifyDown(position);
        }
    }

    /**
     * Remove node with given id
     *
     * @param id Id returned by push()
     * @throws IllegalArgumentException when the id is invalid
     */
    public void remove(int id) {
        int position = idToPosition.get(id, 0);
        if (position == 0) {
            throw new IllegalArgumentException("trying to remove with an invalid id");
        }
        idToPosition.delete(id);
        //Replace removed node with last node
        nodes[position] = nodes[nodeCount];
        //Release node
        nodes[nodeCount--] = null;
        //Do not update heap if it is just the last node
        if (position == nodeCount + 1) {
            return;
        }
        idToPosition.put(nodes[position].id, position);
        heapifyUp(position);
        heapifyDown(position);
    }

    /**
     * Print elements of this heap
     * They are printed layer by layer to system out
     */
    public void print() {
        int start = 1;
        int count = 1;
        while (start <= nodeCount) {
            printRegion(start, count);
            start += count;
            count *= 2;
        }
    }

    /**
     * Print elements and turn to new line
     *
     * @param start Start position in array
     * @param count Element count to print
     */
    private void printRegion(int start, int count) {
        for (int i = 0; i < count; i++) {
            if (start + i <= nodeCount) {
                System.out.print((nodes[start + i] != null ? nodes[start + i].data : -1) + ", ");
            } else {
                break;
            }
        }
        System.out.println();
    }

    /**
     * Data node of the heap
     *
     * @author Rose
     */
    private static class Node {

        /**
         * An final id of node
         */
        final int id;
        /**
         * Saved value of this node
         */
        int data;

        Node(int id, int value) {
            this.data = value;
            this.id = id;
        }

    }

}
