package com.example.ConMapper.dto;

public class ProdottoDTO {
  private String nomeProdotto;
  private String prezzoInfo;

  public void setNomeProdotto(String n) {
    this.nomeProdotto = n;
  }

  public void setPrezzoInfo(String p) {
    this.prezzoInfo = p;
  }

  @Override
  public String toString() {
    return "DTO { Nome: " + nomeProdotto + ", Info: " + prezzoInfo + " }";
  }
}