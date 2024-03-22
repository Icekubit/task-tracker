package icekubit.tasktrackerbackend.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("prohibited_endpoint")
public class TestController {

    @GetMapping
    public String getSecretString() {
        return "Hello World";
    }

}
