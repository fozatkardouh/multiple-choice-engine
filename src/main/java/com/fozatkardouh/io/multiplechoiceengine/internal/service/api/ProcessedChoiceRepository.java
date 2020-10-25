package com.fozatkardouh.io.multiplechoiceengine.internal.service.api;

import com.fozatkardouh.io.multiplechoiceengine.api.model.Choice;

public interface ProcessedChoiceRepository {

    Choice retrieveProcessedChoiceFromYaml(String yamlFileName);

}
