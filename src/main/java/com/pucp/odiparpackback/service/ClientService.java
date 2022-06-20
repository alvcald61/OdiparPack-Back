package com.pucp.odiparpackback.service;

import com.pucp.odiparpackback.request.ClientRequest;
import com.pucp.odiparpackback.request.ClientResponse;
import com.pucp.odiparpackback.response.StandardResponse;

import java.util.List;

public interface ClientService {
  StandardResponse<List<ClientResponse>> listAll();
  StandardResponse<Long> create(ClientRequest request);
  StandardResponse<Long> update(ClientRequest request);
  StandardResponse<String> delete(Long clientId);
}
