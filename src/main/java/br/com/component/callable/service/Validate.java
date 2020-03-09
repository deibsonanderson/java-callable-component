package br.com.component.callable.service;

import java.util.List;

import br.com.component.callable.dto.PayloadDTO;

public interface Validate {

	List<String> validate(PayloadDTO payload);
}
