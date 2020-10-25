package com.fozatkardouh.io.multiplechoiceengine.internal.service.impl;

import com.fozatkardouh.io.multiplechoiceengine.api.model.Choice;
import com.fozatkardouh.io.multiplechoiceengine.internal.model.YamlChoiceNode;
import com.fozatkardouh.io.multiplechoiceengine.internal.service.api.MessagesResource;
import com.fozatkardouh.io.multiplechoiceengine.internal.service.api.ProcessedChoiceRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Log
@Component
@RequiredArgsConstructor
public class ProcessedChoiceRepositoryImpl implements ProcessedChoiceRepository {

    private final static String YAML_FILES_LOCATION_PATH = "yaml/";

    private final MessagesResource messagesResource;

    private final ThreadLocal<Yaml> yamlParser = ThreadLocal.withInitial(() -> new Yaml(new Constructor(YamlChoiceNode.class)));
    private final Map<String, Choice> choiceRepository = new ConcurrentHashMap<>();

    @PostConstruct
    private void init() throws IOException {
        PathMatchingResourcePatternResolver resourceResolver = new PathMatchingResourcePatternResolver();
        final Resource[] resources = resourceResolver.getResources(YAML_FILES_LOCATION_PATH + "*");

        for (Resource resource : resources) {
            YamlChoiceNode parentYamlChoiceNode = yamlParser.get()
                                                            .load(resource.getInputStream());
            final String key = resource.getFilename();
            final Choice value = mapToChoice(parentYamlChoiceNode);
            choiceRepository.put(key, value);
        }
    }

    @Override
    public @Nullable
    Choice retrieveProcessedChoiceFromYaml(String yamlFileName) {
        StopWatch stopWatch = new StopWatch("STOPWATCH_retrieveProcessedChoiceFromYaml");
        stopWatch.start();
        try {
            return choiceRepository.computeIfAbsent(yamlFileName, this::parseYamlToChoice);
        } catch (Exception e) {
            log.severe("Exception happened!: " + e.getMessage());
            return null;
        } finally {
            log.info("retrieveProcessedChoiceFromYaml elapsedTime=" + stopWatch.getTotalTimeNanos());
        }
    }

    private Choice parseYamlToChoice(String yamlFileName) {
        InputStream inputStream = this.getClass()
                                      .getClassLoader()
                                      .getResourceAsStream(YAML_FILES_LOCATION_PATH + yamlFileName);
        YamlChoiceNode parentYamlChoiceNode = yamlParser.get()
                                                        .load(inputStream);
        return mapToChoice(parentYamlChoiceNode);
    }

    private Choice mapToChoice(YamlChoiceNode yamlChoiceNode) {
        return Choice.builder()
                     .id(yamlChoiceNode.getId())
                     .message(messagesResource.retrieveMessage(yamlChoiceNode.getMessageKey()))
                     .active(yamlChoiceNode.getActive())
                     .availableSubChoices(yamlChoiceNode.getAvailableSubChoices()
                                                        .stream()
                                                        .filter(YamlChoiceNode::getActive)
                                                        .map(this::mapToChoice)
                                                        .collect(Collectors.toList()))
                     .build();
    }

}
