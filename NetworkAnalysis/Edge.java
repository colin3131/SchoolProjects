//Colin Spratt
//Edge class for Project 4
//4-3-18

import java.util.*;
public class Edge{
  //Variables
  private int v; //Starting vertex
  private int w; //Ending vertex
  private final int l; //Length
  private final int b; //Bandwidth
  private final boolean copper; //True for copper, false for fiber

  //Constructor
  public Edge(int start, int end, int length, int bw, boolean cop){
    v = start;
    w = end;
    l = length;
    b = bw;
    copper = cop;
  }

  //Copy Constructor
  public Edge(Edge e){
    v = e.either();
    w = e.other(v);
    l = e.length();
    b = e.bandwidth();
    copper = e.isCopper();
  }

  //Accessor methods
  public int either(){ return v; }
  public int length(){ return l; }
  public int bandwidth(){ return b; }
  public boolean isCopper(){ return copper; }

public int other(int ver){
  if(ver == w){return v;}
  else{return w;}
}

  public double time(){
    if(copper){
      return (double)(l) / 230000000.0;
    }
    else{
      return (double)(l) / 200000000.0;
    }
  }

  //Mutator methods
  public void setStart(int ver){v = ver;}
  public void setEnd(int ver){w = ver;}

  //Compare methods
  public int compareLength(Edge o){
    if(l > o.length()){ return 1;}
    else if(l == o.length()){ return 0;}
    else{ return -1;}
  }

  public int compareBandwidth(Edge o){
    if(b > o.bandwidth()){ return 1;}
    else if(b == o.bandwidth()){ return 0;}
    else{ return -1;}
  }

  //To String method
  public String toString(){
    String ret = v + " - " + w + "\n";
    ret += "Length: " + l + " meters\n";
    ret += "Bandwidth: " + b + "mbps\nType: ";
    if(copper){ret += "Copper";}
    else{ret += "Fiber Optic";}
    return ret;
  }
}
