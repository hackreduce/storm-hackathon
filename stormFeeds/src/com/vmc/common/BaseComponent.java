package com.vmc.common;

import java.util.Map;

import backtype.storm.task.TopologyContext;
import backtype.storm.topology.IComponent;

public abstract class BaseComponent implements IComponent
{

  protected Map conf;
  protected TopologyContext context;

  @Override
  public Map<String, Object> getComponentConfiguration() {return this.conf;}

}
