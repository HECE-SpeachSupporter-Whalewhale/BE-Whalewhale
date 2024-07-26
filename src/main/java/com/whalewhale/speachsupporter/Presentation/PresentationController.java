package com.whalewhale.speachsupporter.Presentation;

import com.whalewhale.speachsupporter.Item;
import com.whalewhale.speachsupporter.ItemRepository;
import com.whalewhale.speachsupporter.ItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor

public class PresentationController {
    private final PresentationRepository presentationRepository;//db입출력 함수 들어있다


    @GetMapping("/remember")
    String remember(Model model){
        //JPA로 데이터 입출력 하려면 1. repository만들기, 2.DB입출력 원하는 클래스에 repository 등록, 3. 사용
        List<Presentation> result = presentationRepository.findAll();
        System.out.println(result);
        model.addAttribute("presentations", result);

        return "remember.html";
    }

    @GetMapping("/writePresentation")
    String writePresentation(){
        return "writePresentation.html";
    }
    @PostMapping("/addPresentation")
    String addPresentation(@RequestParam(name = "title") String title, @RequestParam(name = "body") String body ){
Presentation presentation = new Presentation();
       presentation.setTitle(title);
       presentation.setBody(body);
       presentationRepository.save(presentation);
        System.out.println(title);
        System.out.println(body);


        return "redirect:/remember";
    }

    @PostMapping("/deletePresentation/{id}")
    String deletePresentation(@PathVariable Integer id) {
        presentationRepository.deleteById(id);
        return "redirect:/remember";
    }
}
