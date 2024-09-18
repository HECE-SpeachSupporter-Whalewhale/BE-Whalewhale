package com.whalewhale.speachsupporter.Presentation;

import com.whalewhale.speachsupporter.Speed.Speed;
import com.whalewhale.speachsupporter.Speed.SpeedRepository;
import com.whalewhale.speachsupporter.Users.Users;
import com.whalewhale.speachsupporter.Users.UsersRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/presentations") // API 경로 설정
public class PresentationController {

    private static final Logger logger = LoggerFactory.getLogger(PresentationController.class);
    private final PresentationRepository presentationRepository;
    private final SpeedRepository speedRepository;
    private final UsersRepository usersRepository;

    // 발표 추가
    @PostMapping("/add")
    @Transactional
    public ResponseEntity<?> addPresentation(@RequestBody Presentation presentationRequest) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String currentUsername = authentication.getName();
            logger.info("현재 인증된 사용자 이름: {}", currentUsername);

            Users user = usersRepository.findByUsername(currentUsername)
                    .orElseThrow(() -> new IllegalArgumentException("해당 사용자가 없습니다: " + currentUsername));

            // Presentation 객체 설정
            presentationRequest.setUser(user);

            // Optional usage 설정
            if (presentationRequest.getUsage() == null) {
                presentationRequest.setUsage("default"); // 기본값 설정, 필요에 따라 조정
            }

            presentationRepository.save(presentationRequest);
            logger.info("Presentation 저장 완료: {}", presentationRequest);

            // Speed 객체 설정 및 저장
            Speed speed = presentationRequest.getSpeed();
            speed.setPresentation(presentationRequest);
            speedRepository.save(speed);
            logger.info("Speed 저장 완료: {}", speed);

            return new ResponseEntity<>(presentationRequest, HttpStatus.CREATED);
        } catch (Exception e) {
            logger.error("발표 추가 중 오류 발생", e);
            return new ResponseEntity<>("발표 추가 중 오류가 발생했습니다: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // 발표 삭제
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deletePresentation(@PathVariable Integer id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = authentication.getName();
        Users currentUser = usersRepository.findByUsername(currentUsername)
                .orElseThrow(() -> new IllegalArgumentException("해당 사용자가 없습니다: " + currentUsername));

        Presentation presentation = presentationRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 ID의 Presentation이 없습니다: " + id));

        if (!presentation.getUser().equals(currentUser)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("{\"message\": \"해당 Presentation을 삭제할 권한이 없습니다.\"}");
        }

        presentationRepository.delete(presentation);
        return ResponseEntity.ok().body("{\"message\": \"Presentation이 삭제되었습니다.\"}");
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> updatePresentation(@PathVariable Integer id, @RequestBody Presentation updatedPresentation) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String currentUsername = authentication.getName();
            Users currentUser = usersRepository.findByUsername(currentUsername)
                    .orElseThrow(() -> new IllegalArgumentException("해당 사용자가 없습니다: " + currentUsername));

            Presentation existingPresentation = presentationRepository.findById(id)
                    .orElseThrow(() -> new IllegalArgumentException("해당 ID의 Presentation이 없습니다: " + id));

            if (!existingPresentation.getUser().equals(currentUser)) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body("{\"message\": \"해당 Presentation을 수정할 권한이 없습니다.\"}");
            }

            // 수정 가능한 필드들을 업데이트
            existingPresentation.setTitle(updatedPresentation.getTitle());
            existingPresentation.setBody(updatedPresentation.getBody());
            existingPresentation.setIsBookmarked(updatedPresentation.getIsBookmarked());
            existingPresentation.setUsage(updatedPresentation.getUsage());

            // Speed 정보 업데이트
            if (updatedPresentation.getSpeed() != null) {
                Speed existingSpeed = existingPresentation.getSpeed();
                if (existingSpeed == null) {
                    existingSpeed = new Speed();
                    existingSpeed.setPresentation(existingPresentation);
                    existingPresentation.setSpeed(existingSpeed);
                }
                existingSpeed.setSpeed_check(updatedPresentation.getSpeed().getSpeed_check());
                existingSpeed.setSpeed_minute(updatedPresentation.getSpeed().getSpeed_minute());
                existingSpeed.setSpeed_second(updatedPresentation.getSpeed().getSpeed_second());
                speedRepository.save(existingSpeed);
            }

            Presentation savedPresentation = presentationRepository.save(existingPresentation);

            logger.info("Presentation 수정 완료: {}", savedPresentation);
            return ResponseEntity.ok(savedPresentation);
        } catch (Exception e) {
            logger.error("발표 수정 중 오류 발생", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("{\"message\": \"발표 수정 중 오류가 발생했습니다: " + e.getMessage() + "\"}");
        }
    }

}
