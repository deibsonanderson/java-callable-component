package br.com.component.callable;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.web.config.EnableSpringDataWebSupport;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import br.com.component.callable.controller.ControllerCallable;
import br.com.component.callable.dto.PayloadDTO;
import br.com.component.callable.service.ServiceCallable;

@RunWith(SpringRunner.class)
@EnableSpringDataWebSupport
@SpringBootTest
@AutoConfigureMockMvc
public class ControllerCallableTest {

	@MockBean
	ServiceCallable service;

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	@InjectMocks
	private ControllerCallable controller;

	@Autowired
	private WebApplicationContext wac;

	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
		this.mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
	}
	
	private static final String URI = "/validate";
	
	private static final String JSON_REQUEST_SUCCESS = "json/request_success.json";
	private static final String JSON_RESPONSE_SUCCESS = "json/response_success.json";
	
	@Test
	public void validateSuccessTest() throws Exception {

		Mockito.when(
				service.validate(Mockito.any(PayloadDTO.class)))
				.thenReturn(getResponse());

		RequestBuilder requestBuilder = MockMvcRequestBuilders.post(URI).accept(MediaType.APPLICATION_JSON)
				.content(parseToJson(JSON_REQUEST_SUCCESS)).contentType(MediaType.APPLICATION_JSON);

		mockMvc.perform(requestBuilder).andExpect(status().isOk())
				.andExpect(content().json(parseToJson(JSON_RESPONSE_SUCCESS))).andReturn();

	}
	
	private String parseToJson(String path) throws IOException {
		File file = new ClassPathResource(path).getFile();
		return new String(Files.readAllBytes(Paths.get(file.getPath())));
	}
	
	private List<String> getResponse(){
		List<String> response = new ArrayList<>();
		response.add("AGE - SUCCESS");
		response.add("NAME - SUCCESS");
		response.add("GENDER - SUCCESS");
		return response;
	}
}
