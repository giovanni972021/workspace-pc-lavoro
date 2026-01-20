- per TESTARLO
- nel TERMINALE
-

- mvn spring-boot:run
- su CHROME
  http://localhost:8081/api/saluto

perche api e saluto?
vedi nella classe controller

@RequestMapping("/api")

@GetMapping(value = "/saluto", produces = "text/plain;charset=UTF-8")
