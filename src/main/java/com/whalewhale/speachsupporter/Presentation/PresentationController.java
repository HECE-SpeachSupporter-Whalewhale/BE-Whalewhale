package com.whalewhale.speachsupporter.Presentation;

import com.whalewhale.speachsupporter.Speed.Speed;
import com.whalewhale.speachsupporter.Speed.SpeedRepository;
import com.whalewhale.speachsupporter.Users.Users;
import com.whalewhale.speachsupporter.Users.UsersRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class PresentationController {

    private static final Logger logger = LoggerFactory.getLogger(PresentationController.class);
    private final PresentationRepository presentationRepository;
    private final SpeedRepository speedRepository;
    private final UsersRepository usersRepository;

    @GetMapping("/remember")
    public String remember(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = authentication.getName().toLowerCase(); // username을 소문자로 변환하여 사용
        Users currentUser = usersRepository.findByUsername(currentUsername)
                .orElseThrow(() -> new IllegalArgumentException("해당 사용자가 없습니다: " + currentUsername));

        List<Presentation> result = presentationRepository.findByUser(currentUser);
        model.addAttribute("presentations", result);
        return "remember.html";
    }


    @GetMapping("/writePresentation")
    String writePresentation() {
        return "writePresentation.html";
    }

    @PostMapping("/addPresentation")
    @Transactional
    public String addPresentation(@RequestParam(name = "title") String title,
                                  @RequestParam(name = "body") String body,
                                  @RequestParam(name = "speed_check", defaultValue = "false") Boolean speedCheck,
                                  @RequestParam(name = "speed_minute") Integer speedMinute,
                                  @RequestParam(name = "speed_second") Integer speedSecond,
                                  Model model) {

        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String currentUsername = authentication.getName();
            logger.info("현재 인증된 사용자 이름: {}", currentUsername);

            Users user = usersRepository.findByUsername(currentUsername)
                    .orElseThrow(() -> new IllegalArgumentException("해당 사용자가 없습니다: " + currentUsername));

            Presentation presentation = new Presentation();
            presentation.setTitle(title);
            presentation.setBody(body);
            presentation.setUser(user);
            presentationRepository.save(presentation);
            logger.info("Presentation 저장 완료: {}", presentation);

            Speed speed = new Speed();
            speed.setPresentation(presentation);
            speed.setSpeed_check(speedCheck);
            speed.setSpeed_minute(speedMinute);
            speed.setSpeed_second(speedSecond);
            speedRepository.save(speed);
            logger.info("Speed 저장 완료: {}", speed);

            model.addAttribute("successMessage", "발표가 성공적으로 추가되었습니다.");
            return "writePresentation.html";
        } catch (Exception e) {
            logger.error("발표 추가 중 오류 발생", e);
            model.addAttribute("errorMessage", "발표 추가 중 오류가 발생했습니다: " + e.getMessage());
            return "writePresentation.html";
        }
    }

    @PostMapping("/deletePresentation/{id}")
    public String deletePresentation(@PathVariable Integer id, HttpServletRequest request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = authentication.getName();
        Users currentUser = usersRepository.findByUsername(currentUsername)
                .orElseThrow(() -> new IllegalArgumentException("해당 사용자가 없습니다: " + currentUsername));

        Presentation presentation = presentationRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 ID의 Presentation이 없습니다: " + id));

        if (!presentation.getUser().equals(currentUser)) {
            throw new IllegalStateException("해당 Presentation을 삭제할 권한이 없습니다.");
        }

        presentationRepository.delete(presentation);

        String referer = request.getHeader("Referer");
        return "redirect:" + (referer != null ? referer : "/remember");
    }
}
