package br.com.component.callable;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.annotation.Order;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.util.ReflectionTestUtils;

import br.com.component.callable.config.Properties;
import br.com.component.callable.dto.PayloadDTO;
import br.com.component.callable.exception.ValidationException;
import br.com.component.callable.service.ServiceCallable;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ServiceCallableTest {

	@Autowired
	ServiceCallable service;

	@Test
	@Order(1)
	public void validateSuccessTest() throws ValidationException {
		ReflectionTestUtils.setField(service, "properties", getValidationActives(false));
		List<String> actual = service.validate(getPayloadDTO());
		assertNotNull(actual);
	}

	@Test
	@Order(2)
	public void validateFailedTest() {
		ReflectionTestUtils.setField(service, "properties", getValidationActives(true));
		assertThatThrownBy(() -> service.validate(getPayloadDTO())).isInstanceOf(ValidationException.class);
	}

	private PayloadDTO getPayloadDTO() {
		PayloadDTO payload = new PayloadDTO();
		payload.setName("Name");
		payload.setAge(20);
		payload.setGender("M");
		return payload;
	}

	private Properties getValidationActives(boolean isNull) {
		Properties properties = new Properties();
		ReflectionTestUtils.setField(service, "properties", properties);
		if (!isNull) {
			List<String> actives = new ArrayList<>();
			actives.add("ValidateName");
			actives.add("ValidateAge");
			actives.add("ValidateGender");
			properties.setActives(actives);
		}
		return properties;
	}
}
