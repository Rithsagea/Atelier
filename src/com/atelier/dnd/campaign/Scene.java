package com.atelier.dnd.campaign;

import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Scene {
  @JsonProperty("_id")
  private UUID id = UUID.randomUUID();

  public UUID getId() {
    return id;
  }

  @Override
  public String toString() {
    return String.format("Scene [%s]", id);
  }
}
