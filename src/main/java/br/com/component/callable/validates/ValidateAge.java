package br.com.component.callable.validates;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.stereotype.Component;

import br.com.component.callable.dto.PayloadDTO;
import br.com.component.callable.service.Validate;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component("ValidateAge")
public class ValidateAge implements Validate {

	@Override
	public List<String> validate(PayloadDTO payload) {
		String response = "AGE - SUCCESS";
		if(payload.getAge() == null || payload.getAge() < 1){
			response = "AGE - ERROR";
		}
		log.info("ValidateAge is processing message:{}",response);
		return Stream.of(response).collect(Collectors.toList());
	}

}
