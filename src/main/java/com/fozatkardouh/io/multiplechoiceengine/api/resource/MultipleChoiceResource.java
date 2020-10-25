package com.fozatkardouh.io.multiplechoiceengine.api.resource;

import com.fozatkardouh.io.multiplechoiceengine.api.model.Choice;
import com.fozatkardouh.io.multiplechoiceengine.internal.service.api.ProcessedChoiceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class MultipleChoiceResource {

    private final ProcessedChoiceRepository processedChoiceRepository;

    @GetMapping("/{yamlFileName}")
    public @Nullable
    Choice retrieveChoices(@PathVariable String yamlFileName) {
        return processedChoiceRepository.retrieveProcessedChoiceFromYaml(yamlFileName);
    }

}
