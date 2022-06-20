package com.pucp.odiparpackback.controller;

import com.pucp.odiparpackback.request.ClientRequest;
import com.pucp.odiparpackback.request.ClientResponse;
import com.pucp.odiparpackback.response.StandardResponse;
import com.pucp.odiparpackback.service.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/client")
public class ClientController {
  @Autowired
  private ClientService clientService;

  @GetMapping("/list")
  public ResponseEntity<StandardResponse<List<ClientResponse>>> listAll() {
    StandardResponse<List<ClientResponse>> response = clientService.listAll();
    return ResponseEntity.status(response.getStatus()).body(response);
  }

  @PostMapping("/create")
  public ResponseEntity<StandardResponse<Long>> createClient(@RequestBody ClientRequest request) {
    StandardResponse<Long> response = clientService.create(request);
    return ResponseEntity.status(response.getStatus()).body(response);
  }

  @PutMapping("/update")
  public ResponseEntity<StandardResponse<Long>> updateClient(@RequestBody ClientRequest request) {
    StandardResponse<Long> response = clientService.update(request);
    return ResponseEntity.status(response.getStatus()).body(response);
  }

  @DeleteMapping("/delete/{id}")
  public ResponseEntity<StandardResponse<String>> createClient(@PathVariable Long id) {
    StandardResponse<String> response = clientService.delete(id);
    return ResponseEntity.status(response.getStatus()).body(response);
  }
}
