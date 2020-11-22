package ex1.tests;


import ex1.src.WGraph_Algo;
import ex1.src.node_info;
import ex1.src.weighted_graph;
import ex1.src.weighted_graph_algorithms;
import org.junit.jupiter.api.*;

import java.io.IOException;
import java.time.Duration;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTimeout;


public class WGraph_AlgoTest {

    private static int[] nodes(weighted_graph g) { //create an array with all the graph's keys
        int size = g.nodeSize();
        Collection<node_info> V = g.getV();
        node_info[] nodesArray = new node_info[size];
        V.toArray(nodesArray); // O(n) operation
        int[] ans = new int[size];
        for(int i=0;i<size;i++) {ans[i] = nodesArray[i].getKey();}
        Arrays.sort(ans);
        return ans;
    }
    @BeforeAll
    public static void begin()
    {
        System.out.println("Running tests for Ex1, WGraph_Algo class");
    }

    @Test
    public void init_copy_Test()
    {
        Random rnd = new Random(1);
        weighted_graph g1 = WGraph_DSTest.graphCreator(20);
        int[] nodes = nodes(g1);
        weighted_graph_algorithms ga= new WGraph_Algo();
        ga.init(g1);
        assertEquals(g1, ga.getGraph());
        for(int i=0; i<=(nodes.length)/2;i++) //remove half of the nodes of g1
            g1.removeNode(i);
        assertEquals(g1, ga.getGraph()); //check if the nodes removed from ga too and the graphs is steel equals
        weighted_graph copyOFga= ga.copy(); //create a copy of ga
        assertEquals(copyOFga, ga.getGraph());
        ga.getGraph().removeNode(nodes.length-2); //remove a node from ga
        boolean equals4=copyOFga.equals(ga.getGraph());
        assertEquals(false, equals4); //check that the remove doesn't happened on the copy graph too
        ga.getGraph().addNode(nodes.length-2); //adding the node that has been remove back
        copyOFga.removeNode(nodes.length-3); //remove a node from the copy graph
        boolean equals5=copyOFga.equals(ga.getGraph());
        assertEquals(false, equals5); //check that the remove doesn't happened on ga too
    }
    @Test
    public void save_load_Test() throws IOException {
        int v=3, e=3;
        Random rnd = new Random(1);
        weighted_graph graph=WGraph_DSTest.graphCreator(v);
        int n1=0,n2=0;
        for (int i=0;i<e;i++){
            double w = Math.random()*20;
            n1 = rnd.nextInt((v-1));
            n2 = rnd.nextInt((v-1));
            graph.connect(n1,n2, w);
        }
        weighted_graph_algorithms ga1 = new WGraph_Algo();
        weighted_graph_algorithms ga2 = new WGraph_Algo();
        ga1.init(graph);
        ga1.save("ga1save"); //save g1
        ga2.load("ga1save");//load the g1 text file to g2
        assertEquals(graph, ga2.getGraph()); //check if g2=graph(that g1 init to) like supposed to
        assertEquals(ga1.getGraph(), ga2.getGraph());//check if g2=g1 like supposed to

    }
    @Test //create a graph with million vertices and edges and check how much time it takes to save and load it
    public void save_load_TimeTest() throws IOException {
        assertTimeout(Duration.ofSeconds(10), () -> {
            int v=1000000, e=1000000;
            Random rnd = new Random(1);
            weighted_graph graph= WGraph_DSTest.graphCreator(v);
            int n1=0,n2=0;
            for (int i=0;i<e;i++){
                double w = Math.random()*20;
                n1 = rnd.nextInt((v-1));
                n2 = rnd.nextInt((v-1));
                graph.connect(n1,n2, w);
            }
            weighted_graph_algorithms ga1 = new WGraph_Algo();
            weighted_graph_algorithms ga2 = new WGraph_Algo();
            ga1.init(graph);
            ga1.save("saveOfga1");
            ga2.load("saveOfga1");
        });
    }
    @Test //create a graph with million vertices and edges and check how much time it takes to copy it
    public void copy_TimeTest()  {
        assertTimeout(Duration.ofSeconds(10), () -> {
            int v=1000000, e=1000000;
            Random rnd = new Random(1);
            weighted_graph graph= WGraph_DSTest.graphCreator(v);
            int n1=0,n2=0;
            for (int i=0;i<e;i++){
                double w = Math.random()*20;
                n1 = rnd.nextInt((v-1));
                n2 = rnd.nextInt((v-1));
                graph.connect(n1,n2, w);
            }
            weighted_graph_algorithms ga1 = new WGraph_Algo();
            weighted_graph_algorithms ga2 = new WGraph_Algo();
            ga1.init(graph);
            ga1.save("saveOfga1");
            ga2.load("saveOfga1");
        });
    }
    @Test
    public void Path_dist_connected()
    {
        weighted_graph g= WGraph_DSTest.graphCreator(10);
        g.removeNode(0);
        g.connect(1,2,3.0);
        g.connect(1,5, 2.5);
        g.connect(2,5, 7.4);
        g.connect(6,5, 1.7);
        g.connect(2,4, 8.2);
        g.connect(5,7, 26.6);
        g.connect(4,7,4.9);
        g.connect(8,9,0.5);
        g.connect(3,8,11.3);
        weighted_graph_algorithms ga = new WGraph_Algo();
        ga.init(g);
        boolean connected = ga.isConnected();
        assertEquals(false, connected); //8,3,9 isn't connected to the rest of the graph
        List<node_info> path= ga.shortestPath(6,7);
        int[] arr={6,5,1,2,4,7}; //the path that supposed to be from 6 to 7
        boolean flag=true;
        for (int i=0; i<path.size();i++)
        {
            if(path.get(i).getKey()!=arr[i])
                flag=false;
        }
        assertEquals(true, flag);
        double dist67 =ga.shortestPathDist(6,7);
        assertEquals(20.3, dist67, 0.0001);
        List<node_info> path2= ga.shortestPath(3,9);
        int[] arr2={3,8,9}; //the path that supposed to be from 3 to 9
        flag=true;
        for (int i=0; i<path2.size();i++)
        {
            if(path2.get(i).getKey()!=arr2[i])
                flag=false;
        }
        assertEquals(true, flag); //check if the path list equals to the path that should be (the array)
        double dist39 =ga.shortestPathDist(3,9);
        assertEquals(11.8, dist39, 0.0001);
        List<node_info> path3= ga.shortestPath(8,5);
        assertEquals(null, path3);//check if the path list equals to the path that should be -  null
        double dist85 = ga.shortestPathDist(8,5);
        assertEquals(-1, dist85);
        g.connect(4,9,1.6);
        connected = ga.isConnected();
        assertEquals(true, connected); //check if the graph is connected after connecting 4 and 9 like supposed to
        path3= ga.shortestPath(8,5);
        int[] arr3={8,9,4,2,1,5}; //the path that supposed to be from 8 to 5
        flag=true;
        for (int i=0; i<path3.size();i++)
        {
            if(path3.get(i).getKey()!=arr3[i])
                flag=false;
        }
        assertEquals(true, flag);
        double dist852 = ga.shortestPathDist(8,5);
        assertEquals(15.8, dist852, 0.0001);
        g.removeEdge(1,5);
        List<node_info> path4= ga.shortestPath(6,7);
        int[] arr4={6,5,2,4,7}; //the path that supposed to be from 6 to 7
        flag=true;
        for (int i=0; i<path4.size();i++)
        {
            if(path4.get(i).getKey()!=arr4[i])
                flag=false;
        }
        assertEquals(true, flag);
        dist67 =ga.shortestPathDist(6,7);
        assertEquals(22.2, dist67, 0.0001);
        g.removeEdge(2,5);
        g.removeEdge(7,5);
        List<node_info> path5= ga.shortestPath(6,7);
        assertEquals(null, path5);
        connected=ga.isConnected();
        assertEquals(false, connected);
    }
    @Test
    public void Path_dist_connectedTime() //check the time to shortestPath, shortestPathDist, isConnected
    {
        assertTimeout(Duration.ofSeconds(10), () -> {
            int v = 100000, e = 100000;
            Random rnd = new Random(1);
            weighted_graph graph = WGraph_DSTest.graphCreator(v);
            int n1 = 0, n2 = 0;
            for (int i = 0; i < e; i++) {
                double w = Math.random() * 30;
                n1 = rnd.nextInt((v - 1));
                n2 = rnd.nextInt((v - 1));
                graph.connect(n1, n2, w);
            }
            weighted_graph_algorithms ga1 = new WGraph_Algo();
            ga1.init(graph);
            ga1.isConnected();
            n1 = 1 + rnd.nextInt((v - 1));
            ga1.shortestPathDist(n1, n1 - 1);
            ga1.shortestPath(n1, n1 - 1);
        });
    }
}
