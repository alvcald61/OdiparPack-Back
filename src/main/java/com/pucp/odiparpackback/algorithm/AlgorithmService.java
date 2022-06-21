package com.pucp.odiparpackback.algorithm;

import com.pucp.odiparpackback.algorithm.request.AlgorithmRequest;
import com.pucp.odiparpackback.algorithm.response.AlgorithmResponse;

public interface AlgorithmService {
    AlgorithmResponse getPath(AlgorithmRequest algorithmServerRequestJson);
}
