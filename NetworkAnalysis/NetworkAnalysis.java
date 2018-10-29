//Colin Spratt
//Main Driver for Project 4
//4-3-2018
import java.util.*;
import java.io.*;

public class NetworkAnalysis{
  public static void main(String args[]){
    int verts;
    Graph G = new Graph(1);
    Scanner sc;
    //Pull in File
    try{
      File netData = new File(args[0]);
      sc = new Scanner(netData);
      verts = Integer.parseInt(sc.nextLine());
      G = new Graph(verts);
      while(sc.hasNextLine()){ //Pull in every variable, add edge
        String ln = sc.nextLine();
        String[] ar = ln.split(" ");
        int vone = Integer.parseInt(ar[0]);
        int vtwo = Integer.parseInt(ar[1]);
        String typ = ar[2];
        boolean cop;
        if(typ.equals("copper")){cop = true;}
        else{cop = false;}
        int bnw = Integer.parseInt(ar[3]);
        int len = Integer.parseInt(ar[4]);
        Edge edg = new Edge(vone, vtwo, len, bnw, cop);
        G.addEdge(edg);
      }
    }
    catch(Exception e){
      System.out.println("File input failed.");
      System.out.println("Error: " + e);
      System.exit(0);
    }

    //menu
    sc = new Scanner(System.in);
    while(true){
      System.out.println("\n\n---Network Analysis Menu---");
      System.out.println("1. Find Lowest Latency Path");
      System.out.println("2. Determine Copper-only Connection");
      System.out.println("3. Find Max Data Transmission");
      System.out.println("4. Find Lowest Average Latency Spanning Tree");
      System.out.println("5. Determine if Fail-Prone");
      System.out.println("0. Quit");
      System.out.print("Please enter a valid number: ");
      int ch = Integer.parseInt(sc.nextLine());
      if(ch == 0){break;}
      else if(ch == 1) LowestLatency(G);
      else if(ch == 2) CheckCopper(G);
      else if(ch == 3) MaxData(G);
      else if(ch == 4) LAvgL(G);
      else if(ch == 5) FailTest(G);
    }
  }

  public static void FailTest(Graph G){
    boolean test = G.isConnectedAfterFailure();
    if(test) System.out.println("\nGraph is Connected after failure of any two vertices.\n");
    else     System.out.println("\nGraph is NOT Connected after failure of two vertices.\n");
  }

  public static void LAvgL(Graph G){

    PrimMin prim = new PrimMin(G);
    Graph newGraph = prim.returnTree();
    System.out.println("\nEdge List of New Spanning Tree: \n" + newGraph.toString());
    System.out.println("\nAverage Latency of the Tree: " + newGraph.averageLatency());
    System.out.println("\nAverage Latency of the Full Graph: " + G.averageLatency());

  }

  public static void MaxData(Graph G){
      PrimMax prim = new PrimMax(G);
      Graph newGraph = prim.returnTree();

      Scanner sc = new Scanner(System.in);
      System.out.print("\nEnter starting Vertex from 0 to " + (G.V()-1) + ": ");
      int start = Integer.parseInt(sc.nextLine());
      System.out.print("Enter end Vertex from 0 to " + (G.V()-1) + ": ");
      int end = Integer.parseInt(sc.nextLine());

      Dijkstra dij = new Dijkstra(newGraph, start);
      Stack<Edge> path = dij.pathTo(end);
      Edge e = path.pop();
      int lowBW = e.bandwidth();
      while(!path.empty()){
        e = path.pop();
        if(lowBW > e.bandwidth()){lowBW = e.bandwidth();}
      }

      System.out.println("\nMax Bandwidth between " + start + " and " + end + ": " + lowBW + "mbps");
  }

  public static void LowestLatency(Graph G){//Finds the lowest Latency Path using Dijkstra's Algorithm
    Scanner sc = new Scanner(System.in);
    System.out.print("\nEnter starting Vertex from 0 to " + (G.V()-1) + ": ");
    int start = Integer.parseInt(sc.nextLine());
    System.out.print("Enter end Vertex from 0 to " + (G.V()-1) + ": ");
    int end = Integer.parseInt(sc.nextLine());

    //System.out.println("Graph:\n" + G.toString());

    Dijkstra dij = new Dijkstra(G, start);
    Stack<Edge> path = dij.pathTo(end);
    System.out.print("\nPath: ");
    Edge e = path.pop();
    int lastV = e.either();
    System.out.print(start);
    int lowBW = e.bandwidth();
    while(!path.empty()){
      System.out.print(" -> " + e.other(lastV));
      e = path.pop();
      if(lowBW > e.bandwidth()){lowBW = e.bandwidth();}
      lastV = e.either();
    }
    System.out.print(" -> " + e.other(lastV));
    System.out.println(" Bandwidth: " + lowBW + "mbps");
  }

  public static void CheckCopper(Graph G){
    boolean result = G.isCopperConnected();
    if(result){
      System.out.println("\nGraph is Copper-Only connected");
    }
    else{
      System.out.println("\nGraph is not Copper-Only connected");
    }
  }
}
