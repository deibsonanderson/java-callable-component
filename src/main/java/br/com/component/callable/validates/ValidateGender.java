package br.com.component.callable.validates;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import br.com.component.callable.dto.PayloadDTO;
import br.com.component.callable.service.Validate;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component("ValidateGender")
public class ValidateGender implements Validate {

	private static final String MALE = "M";
	private static final String FEMALE = "F";

	@Override
	public List<String> validate(PayloadDTO payload) {
		String response = "GENDER - ERROR";
		if (!StringUtils.isEmpty(payload.getGender())
				&& (FEMALE.equalsIgnoreCase(payload.getGender()) || MALE.equalsIgnoreCase(payload.getGender()))) {
			response = "GENDER - SUCCESS";
		}
		log.info("ValidateGender is processing message:{}",response);
		return Stream.of(response).collect(Collectors.toList());
	}

}
