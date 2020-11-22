package ex1.src;

import java.util.Collection;
import java.util.HashMap;

public class WGraph_DS implements weighted_graph {
    private HashMap<Integer, node_info> graf;
    private int MC;
    private int Edge;
    private HashMap<Integer,  HashMap<node_info, Double>> EdgesWeight;

    /**
     *build a new WGraph_DS
     */
    public WGraph_DS() {
        graf= new HashMap<Integer, node_info>();
        MC=0;
        Edge=0;
        EdgesWeight = new HashMap<Integer, HashMap<node_info, Double>>();

    }
    @Override
    /**
     * return the node_data by the node_id,
     * @param key - the node_id
     * @return the node_data by the node_id, null if none.
     */
    public node_info getNode(int key) {
        if(!graf.containsKey(key))
            return null;
        return graf.get(key);
    }

    /**
     * return true iff (if and only if) there is an edge between node1 and node2
     * Note: this method should run in O(1) time.
     * @param node1
     * @param node2
     */
    @Override
    public boolean hasEdge(int node1, int node2) {
        if(!graf.containsKey(node1)||!graf.containsKey(node2))
            return false; //if one of the nodes isn't in the graph return false
        if (EdgesWeight.get(node1).containsKey(getNode(node2)))
                return true;
        return false;
    }
    /**
     * return the weight if the edge (node1, node1). In case
     * there is no such edge - should return -1
     * Note: this method should run in O(1) time.
     * @param node1
     * @param node2
     */
    @Override
    public double getEdge(int node1, int node2) {
        if(!hasEdge(node1, node2)) //return -1 if the nodes isn't connected
            return -1;
        return EdgesWeight.get(node1).get(getNode(node2));
    }
    /**
     * add a new node to the graph with the given key.
     * Note: this method should run in O(1) time.
     * Note2: if there is already a node with such a key -> no action should be performed.
     * @param key
     */
    @Override
    public void addNode(int key) {
        if(graf.containsKey(key)) //if the graph already contains this key do nothing
            return;
        MC++;
        node_info n=new NodeInfo(key); //create a new node with this key
        graf.put(key, n); //add the new node to the graph
        EdgesWeight.put(key, new HashMap<node_info, Double>()); //add the new node to the Edges(with new empty neighbors hashmap)
    }
    /**
     * Connect an edge between node1 and node2, with an edge with weight >=0.
     * Note: this method should run in O(1) time.
     * Note2: if the edge node1-node2 already exists - the method simply updates the weight of the edge.
     */
    @Override
    public void connect(int node1, int node2, double w) {
        if (!graf.containsKey(node1) || !graf.containsKey(node2) ||node1==node2)
        {
            return; //do nothing if one of the nodes isn't in the graph
        }
        if (hasEdge(node1, node2)) { //if they already connected
            if(getEdge(node1, node2)!=w) { //change the weight only if he different from the present weight
                EdgesWeight.get(node1).replace(getNode(node2), w);
                EdgesWeight.get(node2).replace(getNode(node1), w);
                MC++;
            }
            return;
        }
        //the case that they hasn't been connected before
        MC++;
        Edge++;
        EdgesWeight.get(node1).put(getNode(node2), w);
        EdgesWeight.get(node2).put(getNode(node1), w);
    }
    /**
     * This method return a pointer (shallow copy) for a
     * Collection representing all the nodes in the graph.
     * Note: this method should run in O(1) tim
     * @return Collection<node_data>
     */
    @Override
    public Collection<node_info> getV() {
        return graf.values();
    }
    /**
     * This method returns a Collection containing all the
     * nodes connected to node_id
     * Note: this method can run in O(k) time, k - being the degree of node_id.
     * @return Collection<node_data>
     */
    @Override
    public Collection<node_info> getV(int node_id) {
        return EdgesWeight.get(node_id).keySet();
    }
    /**
     * Delete the node (with the given ID) from the graph -
     * and removes all edges which starts or ends at this node.
     * This method should run in O(n), |V|=n, as all the edges should be removed.
     * @return the data of the removed node (null if none).
     * @param key
     */
    @Override
    public node_info removeNode(int key) {
        if(graf.containsKey(key)==false) //if the node isn't in the graph return null
            return null;
        node_info node=getNode(key); //save this node at a temporary variable
        for(node_info n: getV(node.getKey())) //scan all the neighbors of this node
        {
         EdgesWeight.get(n.getKey()).remove(node); //remove the node from the neighbor's list
         Edge--;
         MC++;
        }
        MC++;
        graf.remove(key); //remove the node from the graph
        return node;
    }
    /**
     * Delete the edge from the graph,
     * Note: this method should run in O(1) time.
     * @param node1
     * @param node2
     */
    @Override
    public void removeEdge(int node1, int node2) {
        if(!hasEdge(node1, node2))//if they not connected do nothing
            return;
        MC++;
        Edge--;
        EdgesWeight.get(node1).remove(getNode(node2)); //remove node2 from node1's neighbor list
        EdgesWeight.get(node2).remove(getNode(node1));//remove node1 from node2's neighbor list
    }

    /** return the number of vertices (nodes) in the graph.
     * Note: this method should run in O(1) time.
     * @return
     */
    @Override
    public int nodeSize() {
        return graf.size();
    }
    /**
     * return the number of edges (undirectional graph).
     * Note: this method should run in O(1) time.
     * @return
     */
    @Override
    public int edgeSize() {
        return Edge;
    }
    /**
     * return the Mode Count - for testing changes in the graph.
     * Any change in the inner state of the graph should cause an increment in the ModeCount
     * @return
     */
    @Override
    public int getMC() {
        return MC;
    }
    /**
     * return a string with all the nodes in the graph
     * and their neighbors
     * @return
     */
    public String toString(){
        String g="";
        for ( node_info n: getV())
            g+=n+ ", neighbors: "+ getV(n.getKey())+"\n";
        return g;
    }
    /** return true if the graph is equal to the
     * graph in the class.
     * @param other
     * @return
     */
        public boolean equals(Object other)
    {
        if(!(other instanceof weighted_graph))
            return false;
        weighted_graph g= (weighted_graph)(other);
        if(g.nodeSize()!=nodeSize() || g.edgeSize()!=edgeSize())
            return false;
        for (node_info n: getV()) {
            node_info gNode = g.getNode(n.getKey());
            if (gNode == null)
                return false;
            if(g.getV(n.getKey()).size()!=getV(n.getKey()).size())
                return false;
            for (node_info ni: getV(n.getKey())) {
                if(g.getEdge(ni.getKey(), n.getKey())!=getEdge(n.getKey(), ni.getKey()))
                    return false;
            }
            }
        return true;
    }

    private static class NodeInfo implements node_info, Comparable<NodeInfo>
    {
        private int key;
        private String info;
        private double tag;
        private HashMap<Integer, NodeInfo> ni;

        public NodeInfo(int Key) {
            this.key = Key;
            info = null;
            tag = 0;
        }
        /**
         * Return the key (id) associated with this node.
         * Note: each node_data should have a unique key.
         * @return
         */
        @Override
        public int getKey() {
            return key;
        }
        /**
         * return the remark (meta data) associated with this node.
         * @return
         */
        @Override
        public String getInfo() {
            return info;
        }
        /**
         * Allows changing the remark (meta data) associated with this node.
         * @param s
         */
        @Override
        public void setInfo(String s) {
            info = s;
        }
        /**
         * Temporal data (aka distance, color, or state)
         * which can be used be algorithms
         * @return
         */
        @Override
        public double getTag() {
            return tag;
        }
        /**
         * Allow setting the "tag" value for temporal marking an node - common
         * practice for marking by algorithms.
         * @param t - the new value of the tag
         */
        @Override
        public void setTag(double t) {
            tag = t;
        }
        /**
         * return a string with the key of this node
         */
        public String toString()
        {
          return ""+key;
        }

        @Override
        public int compareTo(NodeInfo o) {
            node_info n =(node_info)(o);
            if(this.tag-n.getTag()>0) return 1;
            return -1;
        }
        /**
         * return 1 if this node in the class is bigger
         * then the taf of the node the method excepted
         * and -1 else
         * @param o
         */
    }

}
