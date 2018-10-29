//Colin Spratt
//DijkstraSP from textbook modified for Project 4
//4-3-18

import java.util.*;
public class Dijkstra{
  private double[] distTo;
  private Edge[] edgeTo;
  private IndexMinPQ<Double> pq;

  //Finds shortest path using Time as weight
  public Dijkstra(Graph G, int s){
    distTo = new double[G.V()];
    edgeTo = new Edge[G.E()];
    if(G.E() <= G.V()){
      edgeTo = new Edge[G.V()];
    }

    for(int v = 0; v < G.V(); v++){
      distTo[v] = Double.POSITIVE_INFINITY;
    }
    distTo[s] = 0.0;

    pq = new IndexMinPQ<Double>(G.V());

    pq.insert(s, distTo[s]);
    //System.out.println("Inserting vertex " + s + " into pq");

    while(!pq.isEmpty()){

      int v = pq.delMin();
      //System.out.println("-Removing vertex " + v + " from pq");

      ArrayList<Edge> ag = G.getEdgeList(v);

      for(int i = 0; i < ag.size(); i++){
        Edge e = ag.get(i);
        //System.out.println("--Relaxing vertex " + i);
        relax(e);
      }
    }
  }

  //Relax e and update PQ if anything changed
  private void relax(Edge e){
    int v = e.either();
    int w = e.other(v);
    if(distTo[w] > distTo[v] + e.time()){
      distTo[w] = distTo[v] + e.time();
      //System.out.println("---Distance to " + w + " is " + distTo[w]);
      //System.out.println("----Edge's time is " + (double)(e.time()));
      edgeTo[w] = e;
      if(pq.contains(w)){pq.decreaseKey(w, distTo[w]);}
      else{
        pq.insert(w, distTo[w]);
        //System.out.println("Inserting vertex " + w + " into pq");
      }
    }
  }

  //Finds the distance from s to v
  public double distTo(int v){
    return distTo[v];
  }

  //Returns the path from s to v
  public Stack<Edge> pathTo(int v){
    Stack<Edge> path = new Stack<Edge>();
    for(Edge e = edgeTo[v]; e != null; e = edgeTo[e.either()]){
      //System.out.println("\nStacking edge: \n" + e.toString());
      path.push(e);
    }

    return path;
  }
}
