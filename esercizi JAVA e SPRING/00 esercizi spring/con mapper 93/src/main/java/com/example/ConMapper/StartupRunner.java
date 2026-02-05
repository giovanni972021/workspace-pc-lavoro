package com.example.ConMapper;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
public class StartupRunner implements ApplicationRunner {

  @Override
  public void run(ApplicationArguments args) {
  }

  /*
   * @Override
   * public void run(ApplicationArguments args) {
   * throw new RuntimeException("Errore di prova: sto testando il crash");
   * }
   */
}
