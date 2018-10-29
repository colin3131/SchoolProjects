//Colin Spratt
//Graph class for project 4
//4-3-18

import java.util.*;
public class Graph{
  //Variables
  private final int v;
  private int e;
  private ArrayList<ArrayList<Edge>> adj = new ArrayList<ArrayList<Edge>>();

  //Constructors
  public Graph(int verts){
    v = verts;
    e = 0;
    for(int i = 0; i < v; i++){
      adj.add(new ArrayList<Edge>());
    }
  }

  public Graph(Graph G){
    v = G.V();
    e = G.E();
    for(int i = 0; i < v; i++)
      adj.add(G.getEdgeList(i));
  }

  //Accessor methods
  public ArrayList<Edge> getEdgeList(int index){ //get EdgeList at i
    return adj.get(index);
  }

  public double getEdgeTime(int v, int w){ //Get time given v + w
    Edge te = adj.get(v).get(w);
    return te.time();
  }

  public double averageLatency(){
    double avg = 0.0;
    for(int i = 0; i < adj.size(); i++){
      for(int j = 0; j < adj.get(i).size(); j++){
        avg += adj.get(i).get(j).time();
      }
    }
    avg = avg / (2 * E());
    return avg;
  }

  public String toString(){
    String ret = "";
    for(int i = 0; i < adj.size(); i++){
      ret += "\n---Edges for Vertex " + i + ":\n";
      for(int j = 0; j < adj.get(i).size(); j++){
        ret += adj.get(i).get(j).toString() + "\n";
      }
    }
    return ret;
  }

  public int V(){return v;}
  public int E(){return e;}

  //Mutator methods
  public void addEdge(Edge ed){
    int eV = ed.either();
    int eW = ed.other(eV);
    Edge otherEdge = new Edge(ed);
    otherEdge.setStart(eW);
    otherEdge.setEnd(eV);
    adj.get(eV).add(ed);
    adj.get(eW).add(otherEdge);
    e++;
  }

  //Check if copper only connected
  public boolean isCopperConnected(){ //Uses DFS search to visit all vertices
    int count = 1;                    //If vertex isn't visited, not connected
    Stack<ArrayList<Edge>> stack = new Stack<ArrayList<Edge>>();
    boolean[] visited = new boolean[adj.size()];
    boolean[] connected = new boolean[adj.size()];
    connected[0] = true;
    for(int i = 0; i < visited.length; i++)
      visited[i] = false;

    stack.push(adj.get(0));
    while(!stack.empty()){
      ArrayList<Edge> vertex = stack.pop();
      int u = adj.indexOf(vertex);
      //System.out.println("Checking Vertex " + u);
      if(visited[u] == false){
        visited[u] = true;
        //System.out.println("Vertex  " + u + " now visited");
        for(int i = 0; i < vertex.size(); i++){
          Edge eg = vertex.get(i);
          int neighbor = eg.other(u);
          //System.out.println("Checking edge from " + u + " to " + neighbor);
          if(eg.isCopper() && connected[neighbor] == false){
            count++;
            connected[neighbor] = true;
            //System.out.println("--Vertexes conntected: " + count);
            stack.push(adj.get(neighbor));
          }
        }
      }
    }
    System.out.println("\nVertexes connected: " + count);
    System.out.println("Total Vertexes: " + v);
    if(count < v){return false;}
    else{return true;}
  }

  //Method to check if, after 2 failures, graph still functions
  public boolean isConnectedAfterFailure(){
      for(int i = 0; i < v-1; i++){
        for(int j = i+1; j < v; j++){
          ArrayList<Edge> start = null;
          ArrayList<Edge> failOne = adj.get(i);
          ArrayList<Edge> failTwo = adj.get(j);
          boolean[] visited = new boolean[v];

          //Ignore the failed points
          visited[i] = true;
          visited[j] = true;

          if(i != 0){
            start = adj.get(0);
          }
          else{
            if(j != v-1) start = adj.get(j+1);
            else if(j-i != 1) start = adj.get(j-1);
            else return false;
          }

          withoutTwo(visited, start);

          for(int k = 0; k < visited.length; k++){
            if(!visited[k]) return false;
          }
        }
      }
      return true;
  }

  //BFS helper for checking connectivity, attempts to visit every node
  private void withoutTwo(boolean[] visited, ArrayList<Edge> check){
    if(visited[adj.indexOf(check)]) return; //If check was already visited, go back
    visited[adj.indexOf(check)] = true;

    for(int i = 0; i < check.size(); i++){
      Edge e = check.get(i);
      int end = e.other(e.either());
      if(visited[end]) continue;

      withoutTwo(visited, adj.get(end));
    }
  }

}
