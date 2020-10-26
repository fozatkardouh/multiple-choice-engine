package com.fozatkardouh.io.multiplechoiceengine.internal.service.impl;

import com.fozatkardouh.io.multiplechoiceengine.api.model.Choice;
import com.fozatkardouh.io.multiplechoiceengine.internal.service.api.ProcessedChoiceRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static com.fozatkardouh.io.multiplechoiceengine.internal.service.impl.YamlChoiceNodeProcessor.YAML_FILES_LOCATION_PATH;

@Log
@Component
@RequiredArgsConstructor
public class ProcessedChoiceRepositoryImpl implements ProcessedChoiceRepository {

    private final YamlChoiceNodeProcessor yamlChoiceNodeProcessor;

    private final Map<String, Choice> choiceRepository = new ConcurrentHashMap<>();

    @Override
    public @Nullable
    Choice retrieveProcessedChoiceFromYaml(String yamlFileName) {
        StopWatch stopWatch = new StopWatch();
        try {
            stopWatch.start();
            return choiceRepository.computeIfAbsent(yamlFileName, yamlChoiceNodeProcessor::parseYamlChoiceNodeToChoice);
        } catch (Exception e) {
            log.severe("Exception happened! " + e.getMessage());
            return null;
        } finally {
            stopWatch.stop();
            log.info("retrieveProcessedChoiceFromYaml elapsedTime=" + stopWatch.getTotalTimeNanos());
        }
    }

    @PostConstruct
    private void parseAllYamlFilesAndAddToLocalRepo() throws IOException {
        PathMatchingResourcePatternResolver resourceResolver = new PathMatchingResourcePatternResolver();
        Arrays.stream(resourceResolver.getResources(YAML_FILES_LOCATION_PATH + "*"))
              .map(Resource::getFilename)
              .forEach(this::parseAndAddYamlFileToRepository);
    }

    private void parseAndAddYamlFileToRepository(String yamlFileName) {
        final Choice choice = yamlChoiceNodeProcessor.parseYamlChoiceNodeToChoice(yamlFileName);
        choiceRepository.put(yamlFileName, choice);
    }

}
