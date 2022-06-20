package com.pucp.odiparpackback.service.impl;

import com.pucp.odiparpackback.model.Client;
import com.pucp.odiparpackback.repository.ClientRepository;
import com.pucp.odiparpackback.request.ClientRequest;
import com.pucp.odiparpackback.request.ClientResponse;
import com.pucp.odiparpackback.response.ErrorResponse;
import com.pucp.odiparpackback.response.StandardResponse;
import com.pucp.odiparpackback.service.ClientService;
import com.pucp.odiparpackback.utils.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class ClientServiceImpl implements ClientService {
  @Autowired
  ClientRepository clientRepository;

  @Override
  public StandardResponse<List<ClientResponse>> listAll() {
    StandardResponse<List<ClientResponse>> response;
    try {
      List<Client> list = clientRepository.findAll();
      List<ClientResponse> responseList = new ArrayList<>();
      for (Client c : list) {
        ClientResponse client = ClientResponse.builder().id(c.getId()).name(c.getName()).ruc(c.getRuc()).build();
        responseList.add(client);
      }
      response = new StandardResponse<>(responseList);
    } catch (Exception ex) {
      ErrorResponse error = new ErrorResponse(ex.getMessage());
      response = new StandardResponse<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }
    return response;
  }

  @Override
  public StandardResponse<Long> create(ClientRequest request) {
    StandardResponse<Long> response;
    ErrorResponse error;
    error = validateRequired(request, request.getId());
    if (Objects.nonNull(error)) {
      response = new StandardResponse<>(error, HttpStatus.BAD_REQUEST);
      return response;
    }

    Client client = Client.builder()
            .name(request.getName())
            .ruc(request.getRuc())
            .build();
    try {
      Long clientId = clientRepository.save(client).getId();
      response = new StandardResponse<>(clientId);
    } catch (Exception ex) {
      error = new ErrorResponse(ex.getMessage());
      response = new StandardResponse<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }
    return response;
  }

  @Override
  public StandardResponse<Long> update(ClientRequest request) {
    StandardResponse<Long> response;
    ErrorResponse error;
    error = validateRequired(request, request.getId());
    if (Objects.nonNull(error)) {
      response = new StandardResponse<>(error, HttpStatus.BAD_REQUEST);
      return response;
    }
    Client client = Client.builder()
            .id(request.getId())
            .name(request.getName())
            .ruc(request.getRuc())
            .build();
    try {
      Long clientId = clientRepository.save(client).getId();
      response = new StandardResponse<>(clientId);
    } catch (Exception ex) {
      error = new ErrorResponse(ex.getMessage());
      response = new StandardResponse<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }
    return response;
  }

  @Override
  public StandardResponse<String> delete(Long clientId) {
    StandardResponse<String> response;

    try {
      clientRepository.deleteById(clientId);
      response = new StandardResponse<>("Exito");
    } catch (Exception ex) {
      ErrorResponse error = new ErrorResponse(ex.getMessage());
      response = new StandardResponse<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }
    return response;
  }

  private ErrorResponse validateRequired(ClientRequest request, Long id) {
    ErrorResponse errorResponse = null;
    if (Objects.isNull(request.getName()) || request.getName().isBlank()) {
      errorResponse = new ErrorResponse(String.format(Message.REQUIRED_FIELD, "name"));
    } else if (Objects.isNull(request.getRuc()) || request.getRuc().isBlank()) {
      errorResponse = new ErrorResponse(String.format(Message.REQUIRED_FIELD, "ruc"));
    } else if (Objects.isNull(id)) {
      errorResponse = new ErrorResponse(String.format(Message.REQUIRED_FIELD, "id"));
    }

    return errorResponse;
  }
}
