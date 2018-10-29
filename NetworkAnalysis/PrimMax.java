//Colin Spratt
//Prim from textbook authors modified to find Max spanning Tree
//4-3-18

import java.util.*;
public class PrimMax{
  private Edge[] edgeTo;
  private double[] distTo;
  private boolean[] marked;
  private IndexMinPQ<Double> pq;

  public PrimMax(Graph G){
    edgeTo = new Edge[G.V()];
    distTo = new double[G.V()];
    marked = new boolean[G.V()];
    pq = new IndexMinPQ<Double>(G.V());

    for(int v = 0; v < G.V(); v++)
      distTo[v] = 0;

    for(int v = 0; v < G.V(); v++)
      if(!marked[v]) prim(G, v);
  }

  private void prim(Graph G, int s){
    distTo[s] = 0.0;
    pq.insert(s, distTo[s]);
    while(!pq.isEmpty()){
      int v = pq.delMin();
      scan(G, v);
    }
  }

  private void scan(Graph G, int v){
    marked[v] = true;
    //System.out.println("\nGetting edge list for vertex " + v);
    ArrayList<Edge> adj = G.getEdgeList(v);
    for(int i = 0; i < adj.size(); i++){
      Edge e = adj.get(i);
      double weight = (double)e.bandwidth(); //Negate bandwidth to find max
      //System.out.println("\nFound edge between " + v + " and " + e.other(v) + " with bandwidth " + weight);
      int w = e.other(v);
      if(marked[w]) continue;
      if(weight > distTo[w]){
        //System.out.println("\nBandwidth " + weight + " > " + distTo[w]);
        distTo[w] = weight;
        edgeTo[w] = e;
        //System.out.println();
        //for(Edge ed: edgeTo)
        //  if(ed != null) System.out.println(ed.toString());
        //System.out.println();
        if(pq.contains(w)) pq.decreaseKey(w, distTo[w]);
        else               pq.insert(w, distTo[w]);
      }
      else{
        //System.out.println("\nBandwidth " + weight + " < " + distTo[w]);
      }
    }
  }

  public Queue<Edge> edges() {
    Queue<Edge> mst = new Queue<Edge>();
    for(int v = 0; v < edgeTo.length; v++){
      Edge e = edgeTo[v];
      if(e != null){
        //System.out.println("\nQueueing edge: " + e.toString());
        mst.enqueue(e);
      }
    }
    return mst;
  }

  public Graph returnTree(){
    Graph newG = new Graph(edgeTo.length);
    Queue<Edge> mst = edges();
    int size = mst.size();
    for(int i = 0; i < size; i++){
      Edge ed = mst.dequeue();
      newG.addEdge(ed);
    }
    //System.out.println("Returning Graph: \n" + newG.toString());
    return newG;
  }
}
