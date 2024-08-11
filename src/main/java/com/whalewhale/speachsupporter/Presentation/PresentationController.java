package com.whalewhale.speachsupporter.Presentation;

import com.whalewhale.speachsupporter.Speed.Speed;
import com.whalewhale.speachsupporter.Speed.SpeedRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class PresentationController {
    private final PresentationRepository presentationRepository;
    private final SpeedRepository speedRepository;

    @GetMapping("/remember")
    String remember(Model model) {
        List<Presentation> result = presentationRepository.findAll();
        model.addAttribute("presentations", result);
        return "remember.html";
    }

    @GetMapping("/writePresentation")
    String writePresentation() {
        return "writePresentation.html";
    }

    @PostMapping("/addPresentation")
    public String addPresentation(@RequestParam(name = "title") String title,
                                  @RequestParam(name = "body") String body,
                                  @RequestParam(name = "speed_check", defaultValue = "false") Boolean speedCheck,
                                  @RequestParam(name = "speed_minute") Integer speedMinute,
                                  @RequestParam(name = "speed_second") Integer speedSecond,
                                  Model model) {

        // Presentation 생성 및 저장
        Presentation presentation = new Presentation();
        presentation.setTitle(title);
        presentation.setBody(body);
        presentationRepository.save(presentation);

        // Speed 생성 및 저장
        Speed speed = new Speed();
        speed.setPresentation(presentation);
        speed.setSpeed_check(speedCheck);
        speed.setSpeed_minute(speedMinute);
        speed.setSpeed_second(speedSecond);
        speedRepository.save(speed);

        model.addAttribute("successMessage", "발표가 성공적으로 추가되었습니다.");
        return "writePresentation.html";
    }
}
