package pro.itfray.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/security")
public class SecurityTestController {

  @GetMapping("/hello")
  public String getHello() {
    return "Hello!!!";
  }
}
