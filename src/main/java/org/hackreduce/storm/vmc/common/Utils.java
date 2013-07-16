package org.hackreduce.storm.vmc.common;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import backtype.storm.tuple.Values;

public final class Utils
{
  
  public final static Comparator<Values> topNComparator = new Comparator<Values>()
  {
    @Override
    public int compare(Values o1, Values o2)
    {return retrieveX(o1) - retrieveX(o2);}
  }; 
  
  public static int retrieveX(Values v)
  {
    Integer n = Integer.MIN_VALUE;
    if ((v != null) && (v.size() >= 4)) 
      try
      {n = (Integer)(v.get(3));}
      catch(ClassCastException cce)
      {n = Integer.MIN_VALUE;}
    return n != null?n:Integer.MIN_VALUE;
  }

  /**
   * Given a tuple represented v and top n list represented by q, insertion sort on just v.  It should also not add v
   * if it is already in q (since feeds can retrieve the same data when polled several times  in short intervals.
   * 
   * pre: q is sorted, n > 0, q.size <= n
   * post: q is size n and sorted.  Return true if (v was added to q and v in q). 
   * @param q
   * @param v
   * @param n
   * @return
   */
  public static boolean setTopN(List<Values> q, Values v, int n)
  {
    if (v == null) return false;
    assert(q.size() <= n);
    if (q.size() == 0)
    {
      q.add(v);
      return true;
    }
    int i = 0;
    for (Values cur: q)
    {
      if (v.get(2).equals(cur.get(2))) return false;
      if (topNComparator.compare(v, cur) < 0)
      {
        q.add(i, v);
        if (q.size() > n)  q.remove(n);
        return true;
      }
      i++;  
    }
    if (q.size() < n)
    {
      q.add(v);
      return true;
    }
    return false;
  }
  
  public static Values[] mapToTuples(Map<String, String> lookup)
  {
    Set<String> keys = lookup.keySet();
    Values[] tuples = new Values[keys.size()];
    int i = 0;
    for (String key: keys)
      tuples[i++] = new Values(key, lookup.get(key));
    return tuples;
  }
  
  public static Map<String, String> loadFileFromUrl(URL url) throws Exception
  {
    Map<String, String> lookup = new HashMap<String, String>();
    BufferedReader in = null;
    InputStreamReader isr = null;
    try
    {
      isr = new InputStreamReader(url.openStream());
      in = new BufferedReader(isr);
      
      String line;
      while ((line = in.readLine()) != null)
      {
        String[] kv = line.split("\t");
        lookup.put(kv[0], kv[1]);
      }
      return lookup;
    }
    finally
    {if (in != null) in.close();}
  }
  
  public static String displayTopN(Map<String, List<Values>> lookup, String descr)
  {
    Set<String> keys = lookup.keySet();
    List<String> keysSorted = new ArrayList<String>();
    keysSorted.addAll(keys);
    Collections.sort(keysSorted);
    StringBuilder sb = new StringBuilder();
    for (String key: keysSorted)
    {
      sb.append("\n**************************\n [" + key + "] for " + descr + "\n");
      List<Values> values = lookup.get(key);
      for (Values value: values)
        sb.append(String.format("[%s], %s: [%d]  with title [%s] and link [%s].\n", value.get(0), descr, value.get(3), value.get(1), value.get(2)));
    }
    return sb.toString();
  }
  
  public static String displayHelloCounts(Map<String, String> lookup, Map<String, Integer> counts)
  {
    Set<String> keys = lookup.keySet();
    List<String> keysSorted = new ArrayList<String>();
    keysSorted.addAll(keys);
    Collections.sort(keysSorted);
    StringBuilder sb = new StringBuilder();
    for (String key: keysSorted)
      sb.append(String.format("[%d3] people said \"[%s]\" (Hello!) in [%s].\n", counts.get(key), lookup.get(key), key));
    return sb.toString();
  }

}
