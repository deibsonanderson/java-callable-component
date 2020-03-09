package br.com.component.callable.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import br.com.component.callable.dto.PayloadDTO;
import br.com.component.callable.exception.ValidationException;
import br.com.component.callable.service.ServiceCallable;

@RestController
public class ControllerCallable {

	@Autowired
	ServiceCallable services;

	@PostMapping(value = {"/validate","/validation"}, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<String>> errors(@RequestBody PayloadDTO payload) throws ValidationException {
		return ResponseEntity.status(HttpStatus.OK).body(services.validate(payload));
	}
}
