package com.whalewhale.speachsupporter.Presentation;

import com.whalewhale.speachsupporter.Item;
import com.whalewhale.speachsupporter.ItemRepository;
import com.whalewhale.speachsupporter.ItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor

public class PresentationController {
    private final PresentationRepository presentationRepository;//db입출력 함수 들어있다
    private final PresentationService presentationService;


    @GetMapping("/remember")
    String remember(Model model){
        //JPA로 데이터 입출력 하려면 1. repository만들기, 2.DB입출력 원하는 클래스에 repository 등록, 3. 사용
        List<Presentation> result = presentationRepository.findAll();
        model.addAttribute("items", result);

        return "remember.html";
    }

    @PostMapping("/presentationAdd")
    String presentationaddPost(String title, String body){
        presentationService.savePresentation(title, body);
        return "redirect:/remember";
    }
}
