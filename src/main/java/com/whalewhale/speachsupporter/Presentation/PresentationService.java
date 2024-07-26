package com.whalewhale.speachsupporter.Presentation;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class PresentationService {
    private final PresentationRepository presentationRepository;

    public void savePresentation(String title, String body){

        Presentation presentation = new Presentation();
        presentation.setTitle(title);
        presentation.setBody(body);

        presentationRepository.save(presentation);
    }


}
