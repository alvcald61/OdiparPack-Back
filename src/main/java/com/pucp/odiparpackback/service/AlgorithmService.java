package com.pucp.odiparpackback.service;

import com.pucp.odiparpackback.service.json.AlgorithmServerRequestJson;
import com.pucp.odiparpackback.service.json.AlgorithmServerResponseJson;

public interface AlgorithmService {
    AlgorithmServerResponseJson getPath(AlgorithmServerRequestJson algorithmServerRequestJson);
}
