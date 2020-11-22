package ex1.src;

import java.io.*;
import java.util.*;

public class WGraph_Algo implements weighted_graph_algorithms {
    private weighted_graph ga;
    private HashMap<Integer, node_info> re;
    private HashMap<Integer, Double> dist;
    private HashMap<Integer, Boolean> visited;

    public WGraph_Algo()
    {
        ga=new WGraph_DS();
        re= new HashMap<Integer, node_info>();
        dist = new HashMap<Integer, Double>();
        visited =new HashMap<Integer, Boolean>();
    }
    /**
     * Init the graph on which this set of algorithms operates on.
     * @param g
     */
    @Override
    public void init(weighted_graph g) {
     ga=g;
    }
    /**
     * Return the underlying graph of which this class works.
     * @return
     */
    @Override
    public weighted_graph getGraph() {
        return ga;
    }
    /**
     * Compute a deep copy of this weighted graph.
     * @return
     */
    @Override
    public weighted_graph copy() {
        weighted_graph newG = new WGraph_DS();
        for (node_info n : ga.getV()) { //create new nodes (same like in the graph we have) and add them to the new graph
            newG.addNode(n.getKey());
        }
        for (node_info n : ga.getV()) { //go over all the "old" graph's nodes
            int nKey=n.getKey();
            for (node_info n1 :ga.getV(n.getKey()))
            {
                int n1Key= n1.getKey();
                newG.connect(nKey, n1Key, ga.getEdge(nKey,n1Key));//make a connection between the node and his neighbor in the new gragh
            }
        }
        return newG;
    }
    /**
     * Compute the Dijkstra algorithm:
     * create a reconstruct HashMap for the ShortestPath method - from which node
     * we arrived to each node.
     * create a visited HashMap for the isConnected method - tells us which node
     * has been visited.
     * changed the tag to the distance from a ex1.ex1.src node for each node - infinity
     * if the node is unreachable.
     * @param src
     */
    private void Dijkstra(int src)
    {
        PriorityQueue<node_info> dist= new PriorityQueue<>();
        re = new HashMap<Integer, node_info>(); //save the node that we came for to each key
        visited = new HashMap<Integer, Boolean>(); //store "true" or "false" to each node due to if it have been visited
        for(node_info n: ga.getV()) // go over all the graph's nodes
        {
            n.setTag((double)(Integer.MAX_VALUE)); //reset the tag to infinity
            visited.put(n.getKey(), false); //reset to all key "false" (not visited)
            re.put(n.getKey(), null); //reset all nodes to null (the "father")
        }
        node_info tmp=ga.getNode(src);
        tmp.setTag(0.0); //set the tag-the distance of the ex1.ex1.src from the ex1.ex1.src to 0
        dist.add(tmp); //add him to the the queue of the distances
        while (!dist.isEmpty())
        {
            tmp= dist.poll(); //poll and save the node with the lowest distance from the ex1.ex1.src in the queue
            for(node_info i: ga.getV(tmp.getKey())) //go over temp's neighbor
            {
                int tmpKey=tmp.getKey();
                int iKey=i.getKey();
                if(!visited.get(iKey)) //if the neighbor hasn't been visited
                {
                    double NewDist=ga.getEdge(iKey,tmpKey)+tmp.getTag(); //temp's distance from the ex1.ex1.src+ the edge between the tmp and the neighbor
                    if(NewDist<i.getTag()) { //if the NewDist < from the present distance of the neighbor
                        i.setTag(NewDist);
                        re.replace(iKey, tmp); //save tmp ad the node that we came for to this neighbor
                        dist.add(i);
                    }
                }
            }
            visited.replace(tmp.getKey(), true); // mark the tmp as visited
        }
    }
    /**
     * Returns true if and only if (iff) there is a valid path from EVREY node to each
     * other node.
     * @return
     */
    @Override
    public boolean isConnected() {
        if(ga.nodeSize()==0)//if the graph is empty return true
            return true;
      int start= ga.getV().stream().findAny().get().getKey(); //set the start to be a random key from the graph
        Dijkstra(start);
       return !(visited.containsValue(false)); //if the visited HashMap from the Dijkstra contains false return false
    }
    /**
     * returns the length of the shortest path between ex1.ex1.src to dest
     * Note: if no such path --> returns -1
     * @param src - start node
     * @param dest - end (target) node
     * @return
     */
    @Override
    public double shortestPathDist(int src, int dest) {
        if(ga.getNode(src)==null || ga.getNode(dest)==null) //if the ex1.ex1.src or the dest isn't at the graph return -1
            return -1;
        Dijkstra(src);
        if(ga.getNode(dest).getTag()==(double)(Integer.MAX_VALUE)) //if the dest's tag remain infinity- he has no path from the ex1.ex1.src
            return -1;
        return ga.getNode(dest).getTag();//return the dest's tag(his distance from the ex1.ex1.src)
    }
    /**
     * returns the the shortest path between ex1.ex1.src to dest - as an ordered List of nodes:
     * ex1.ex1.src--> n1-->n2-->...dest
     * see: https://en.wikipedia.org/wiki/Shortest_path_problem
     * Note if no such path --> returns null;
     * @param src - start node
     * @param dest - end (target) node
     * @return
     */
    @Override
    public List<node_info> shortestPath(int src, int dest) {
        if(shortestPathDist(src, dest)==-1) //if there is no nodes with ex1.ex1.src or dest key return null
            return null;
        List<node_info> path = new ArrayList<>();
        if (src==dest) {
            path.add(ga.getNode(src));
            return path;
        }
        Dijkstra(src);
        path.add(ga.getNode(dest));
        for (node_info n = re.get(dest); n != null; n = re.get(n.getKey())) //reconstruct the path from dest to ex1.ex1.src
            path.add(n);
        Collections.reverse(path); //reverse the path so ex1.ex1.src to dest
        return path;
    }
    /**
     * Saves this weighted (undirected) graph to the given
     * file name
     * @param file - the file name (may include a relative path).
     * @return true - iff the file was successfully saved
     */
    @Override
    public boolean save(String file) {
        try {
            FileWriter fw = new FileWriter(new File(file));
            StringBuilder s = new StringBuilder();
            for (node_info n : ga.getV()) { //go over all the graph's nodes
                //add the node to the start of the line
                int nKey = n.getKey();
                    s.append(nKey);
                    s.append(";");
                    for (node_info ni : ga.getV(nKey)) {
                        //add the node's neighbor to the same line
                        s.append(ni.getKey());
                        s.append(";");
                        s.append(ga.getEdge(nKey, ni.getKey())); //add the weight between both
                    }
                         s.append("\n");
                }

                fw.write(s.toString());
                s.setLength(0);
                fw.close();
                return true;
            }
        catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
    /**
     * This method load a graph to this graph algorithm.
     * if the file was successfully loaded - the underlying graph
     * of this class will be changed (to the loaded one), in case the
     * graph was not loaded the original graph should remain "as is".
     * @param file - file name
     * @return true - iff the graph was successfully loaded.
     */
    @Override
    public boolean load(String file) {
        try {
            weighted_graph ng=new WGraph_DS();
            BufferedReader br = new BufferedReader(new FileReader(file));
            String st;
            int v1=0,v2 = 0;
            double w;
            while ((st = br.readLine()) != null) {//scan line by line
                //the size of the variable at the line (1 if there is no neighbors to the first node at the line or 3 if ther is -node, neighbor, weight)
                int size=st.split(";").length;
                String[] spl=new String[size];
                spl=st.split(";");
                v1 = Integer.parseInt(spl[0]); //the node's key
                if (ng.getNode(v1)==null) { //if the node hasn't been added to the graph already, add him
                    ng.addNode(v1);
                }
                if(size==3) //if the nde v1 have a neighbor
                {
                    v2 = Integer.parseInt(spl[1]); //the neighbor's key
                    w = Double.parseDouble(spl[2]); //the weight of the edge between v1 and v2
                    if (ng.getNode(v2)==null) { //if the neighbor hasn't been added to the graph already, add hom
                        ng.addNode(v2);
                    }
                    ng.connect(v1,v2, w); //connect v1 and v2 with their weight
                }
            }
            init(ng);
            return true;
    }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
            return false;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
    /**
     * return a string with all the nodes in the graph
     * and their neighbors
     * @return
     */
    public String toString() {
        return ga.toString();
    }

}
