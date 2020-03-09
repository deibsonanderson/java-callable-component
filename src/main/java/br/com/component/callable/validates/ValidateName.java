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
@Component("ValidateName")
public class ValidateName implements Validate {

	@Override
	public List<String> validate(PayloadDTO payload) {
		String response = "NAME - SUCCESS";
		
		if(StringUtils.isEmpty(payload.getName()) || payload.getName().length() < 3 ){
			response = "NAME - ERROR";
		}
		log.info("ValidateName is processing message:{}",response);
		return Stream.of(response).collect(Collectors.toList());
	}

}
