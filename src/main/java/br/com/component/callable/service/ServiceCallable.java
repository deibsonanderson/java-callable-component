package br.com.component.callable.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import br.com.component.callable.config.Properties;
import br.com.component.callable.dto.PayloadDTO;
import br.com.component.callable.exception.ValidationException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class ServiceCallable {


	@Autowired
	private BeanFactory beanFactory;
	
	@Autowired
	private Properties properties;
	
	private static final int TIME_OUT = 5;

	private static final ExecutorService executors = Executors.newCachedThreadPool();

	/**
	 * Examble of a validacao asynchronous with callable
	 * @param payload
	 * @return List<String> messages
	 * @throws ValidationException 
	 */
	public List<String> validate(PayloadDTO payload) throws ValidationException {
		List<String> validations = null;
		try {
			validations = mountResponseByCalled(callValidates(payload));
		} catch (Exception ex) {
			log.error("error while processing message:{}", ex.getMessage(), ex);
			throw new ValidationException("error while processing message:"+ ex.getMessage());
		}
		return validations;
	}

	/**
	 * Call validate class actives 
	 * @param payload
	 * @return Collection<Callable<List<String>>>
	 */
	private Collection<Callable<List<String>>> callValidates(PayloadDTO payload) {
		Collection<Callable<List<String>>> tasks = new ArrayList<>();
		for (String validationName : properties.getActives()) {
			tasks.add(new Callable<List<String>>() {
				@Override
				public List<String> call() throws Exception {
					return beanFactory.getBean(validationName, Validate.class).validate(payload);
				}
			});
		}
		return tasks;
	}

	/**
	 * Get callable messages for Rest response
	 * @param tasks
	 * @return List<String> messages
	 * @throws InterruptedException
	 * @throws ExecutionException
	 */
	private List<String> mountResponseByCalled(Collection<Callable<List<String>>> tasks) throws InterruptedException, ExecutionException {
		List<String> response = new ArrayList<>();
		List<Future<List<String>>> results = executors.invokeAll(tasks, TIME_OUT, TimeUnit.SECONDS);
		if (!CollectionUtils.isEmpty(results)) {
			for (Future<List<String>> future : results) {
				if (!CollectionUtils.isEmpty(future.get())) {
					response.addAll(future.get());
				}
			}
		}
		return response;
	}

}
