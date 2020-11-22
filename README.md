# Weighted graphs  
***Author:** eden shkuri* 

In this project I implemented algorithms for developing a data structure into a undirectional weighted graph.  
The graph contains vertices and edges between each 2 vertices.

### Data Structures:  
#### NodeInfo:  
The NodeInfo class implements the node_info interface as an inner class in the WGraph_DS:  
This class represents a vertex in a undirectional weighted graph.  
Each node has a unique id number (key), info and tag (that were used only during the algorithms).  

#### WGraph_DS:
The WGraph_DS implements the weighted_graph interface:  
This class represents a undirectional weighted graph.  
The nodes and edges are implemented in a "HashMap" data structure.  
There are functions for adding / removing nodes and edges, getting a collection of the nodes in the graph / collection of neighbors for each  
node. additionally, you can get the amount of nodes/edges that are in the graph and also get the amount of actions done on the graph (MC).  

### Algorithms:
The WGraph_Algo class implements the weighted_graph_algorithms:  
The WGraph_Algo class contains a graph to operate the algorithms on.  
This class represents the Graph algorithms including:  
1.	**init-** Initializes the graph a given graph.  
2.	**getGraph-** Return the underlying graph the class works on.  
3.	**copy-** Deep copy of the graph.  
4.	**isConnected-** Checks whether the graph is connected.  
5.	**shortestPathDist-** Calculates the shortest path distance between 2 given nodes.  
6.	**shortestPath-** Finds the shortest path (by list of nodes) between 2 given nodes in the graph.  
7.	**save-** Saves the graph into a file.  
8.	**load-** load a graph from a file to the graph that class works on.  
**isConnected, shortestPath and shortestPathDist using Dijkstra's algorithm method.**

### Tests:  
I have created 2 JUNIT test (using JUNIT 5 version) :  
WGraph_DSTest and WGraph_AlgoTest that build a graph and test some algorithms on it.  

#### An example for a graph this project work on:  

![piccc](https://user-images.githubusercontent.com/74586829/99406259-5cdd3980-28f6-11eb-9733-015f1b228cd1.JPG)

Shortest path from **6 -> 7** (marked in yellow)  

The distance of the shortest path : **20.3**  

This graph is **not** connected
