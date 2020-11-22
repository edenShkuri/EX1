package ex1.tests;


import ex1.src.WGraph_DS;
import ex1.src.node_info;
import ex1.src.weighted_graph;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collection;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

class WGraph_DSTest {
    private static long startTest;
    public static weighted_graph graphCreator(int v_size) { //create graph with v nodes
        weighted_graph g = new WGraph_DS();
        for (int i = 0; i < v_size; i++) {
            g.addNode(i);
        }
        return g;
    }
    private static int[] nodes(weighted_graph g) { //create an array with all the graph's keys
        int size = g.nodeSize();
        Collection<node_info> V = g.getV();
        node_info[] nodesArray = new node_info[size];
        V.toArray(nodesArray);
        int[] ans = new int[size];
        for(int i=0;i<size;i++) {ans[i] = nodesArray[i].getKey();}
        Arrays.sort(ans);
        return ans;
    }
@BeforeAll
public static void begin()
{
    System.out.println("Running tests for Ex1, WGraph_DS class");
    startTest = System.currentTimeMillis();
}
@AfterAll
public static void end()
{
    long endTest = System.currentTimeMillis();
    double timeout= (endTest-startTest)/1000.0;
    System.out.println("time for all test: "+ timeout);
}

@Test
public void add_nodes_and_edges()
{
    Random rnd = new Random(1);
    weighted_graph g1 = graphCreator (6);
    int inRange= rnd.nextInt(6),outRange=7+rnd.nextInt(12);
    node_info b1 = g1.getNode(outRange);// get node that isn't in the graph
    assertEquals(b1, null); //check if the node related to the key "outRange" is null (the node isn't in the graph)
    int[] nodes = nodes(g1);
    double r1, r2;
    for(int i=2;i<6;i++) {
        r1= Math.random()*20;
        r2=Math.random()*20;
        int n0 = nodes[i-2];
        int n1 = nodes[i-1];
        int n2 = nodes[i];
        //connect every node to the node before him and the node 2 before him
        g1.connect(n0, n1, r1);
        g1.connect(n0, n2, r2);
    }
    int node=(int)(Math.random()*4);
    try{g1.connect(node, outRange, 7.6);} //make a connection with node that isn't in the graph
    catch (Exception e)
    {
        System.out.println("cannot connect "+node+" and "+outRange+", "
                           +outRange+" does not exist at the graph");
    }
    boolean edge1 = g1.hasEdge(node, node+2);
    boolean edge2 = g1.hasEdge(node, node+1);
    boolean edge3 = g1.hasEdge(nodes[0], nodes[nodes.length-1]);
    boolean edge4 = g1.hasEdge(inRange, outRange); // checking an edge with node that isn't in the graph
    try{ g1.removeNode(node);}
    catch (Exception e)
    {
        System.out.println("problem with removing node number: "+node);
        System.out.println(e.getCause());
    }
    try{ node_info afterRemove= g1.removeNode(node);//remove node that isn't in the graph
        assertEquals( null, afterRemove);
    }
    catch (Exception e)
    {
        System.out.println("problem with removing node number: "+node);
        System.out.println(e.getCause());
    }

    boolean edge5 = g1.hasEdge(node , node+1); // checking an edge with node that has been remove
    assertEquals(true, edge1);
    assertEquals(true, edge2);
    assertEquals(false, edge3);
    assertEquals(false, edge4);
    assertEquals(false, edge5);

}
@Test
public void edgesTest() {
    Random rnd = new Random(2);
    weighted_graph g2 = graphCreator(6);
    int[] nodes = nodes(g2);
    for (int i = 1; i <=5; i++) { //connecting 0 with all the other nodes except 6
        int n1 = nodes[i];
        g2.connect(0, n1, i + 0.5);
    }
    int edgesize= g2.edgeSize();
    assertEquals(5, edgesize); // 5 edges between 0 and 1-5
    int node1= 1+rnd.nextInt(5); //random key from 1 to 5
    int node2=1+ rnd.nextInt(5);
    double edge1 = g2.getEdge(0, node1);
    double edge2 = g2.getEdge(0, node2);
    double edge3 = g2.getEdge(0, 6); //get edge of edge that doesn't exist
    boolean b1= g2.hasEdge(0, node1);
    g2.removeEdge(0,node1);
    g2.removeEdge(0,node1);
    boolean b2= g2.hasEdge(0, node1);
    int MC=g2.getMC();
    assertEquals(12, MC); //adding 6 vertices, 5 edges, removing 1 edge =12 actions
    g2.connect(0, node2, g2.getEdge(0, node2)); //supposed to do nothing -  there is already edge with the same weight
    int MCnoCange=g2.getMC();
    assertEquals(MC,MCnoCange);
    try{ g2.removeEdge(6,node2);//try to remove an edge that doesn't exist
        g2.removeEdge(12,node2); //try to remove an edge with node that doesn't exist in the graph
       }
    catch (Exception e)
    {
        System.out.println("edge not found");
    }
    assertEquals(node1+0.5, edge1);
    assertEquals(node2+0.5, edge2);
    assertEquals(-1, edge3);
    assertEquals(true, b1);
    assertEquals(false, b2);

}
@Test
public void getCollections()
{
    Random rnd = new Random(1);
    weighted_graph g3 = graphCreator(0);
    int[] nodes3 = nodes(g3); //function that use at getV
    int size3=g3.nodeSize();
    assertEquals(size3, nodes3.length);
    weighted_graph g4 = graphCreator(8);
    int[] nodes4 = nodes(g4);
    int size4=g4.nodeSize();
    assertEquals(size4, nodes4.length);
    int n1= rnd.nextInt(6);
    //add to n1 2 neighbors
    g4.connect(n1, n1+1, 1.0);
    g4.connect(n1, n1+2, 2.0);
    int neiSize=g4.getV(n1).size();
    assertEquals(2,neiSize);
    boolean isNei1= g4.getV(n1).contains(g4.getNode(n1+1));
    boolean isNei2= g4.getV(n1).contains(g4.getNode(n1+2));
    assertEquals(true, isNei1);
    assertEquals(true, isNei2);
    g4.removeEdge(n1, n1+2);
    int neiSize2=g4.getV(n1).size();
    assertEquals(1,neiSize2);
    boolean isNei3= g4.getV(n1).contains(g4.getNode(n1+2));
    assertEquals(false, isNei3);
}

@Test
public void TimeTest() //check the time to create a graph with million vertices and edges
{
    Random rnd = new Random(1);
    int v=1000000, e=v;
    double start = System.currentTimeMillis();
    weighted_graph graph= new WGraph_DS();
    for(int i=0;i<v;i++)
    {
        graph.addNode(i);
    }
    int n1=0,n2=0;
    for (int i=0;i<e;i++){
        double w = Math.random()*20;
         n1 = rnd.nextInt((v-1));
         n2 = rnd.nextInt((v-1));
        graph.connect(n1,n2, w);
    }
   double end = System.currentTimeMillis();
   double timeout= (end-start)/1000.0;
   if (timeout>10)
       fail("Exception in times");
   else{
       System.out.println("time to build a graph with 1 Million vertices and 1 Million edges: "+ timeout);
   }

}

}