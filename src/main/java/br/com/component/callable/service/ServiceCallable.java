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

import br.com.component.callable.dto.PayloadDTO;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class ServiceCallable {

	@Autowired
	private BeanFactory beanFactory;

	private static final ExecutorService checkers = Executors.newCachedThreadPool();

	public List<String> validate(PayloadDTO payload) {
		List<String> validations = null;
		try {
			validations = mountResponseByCalled(callValidates(payload));
		} catch (Exception ex) {
			log.error("error while processing message:{}", ex.getMessage(), ex);
		}
		return validations;
	}

	private List<String> getActiveValidations() {
		List<String> validates = new ArrayList<>();
		validates.add("ValidateName");
		validates.add("ValidateAge");
		validates.add("ValidateGender");
		return validates;
	}

	private Collection<Callable<List<String>>> callValidates(PayloadDTO payload) {
		Collection<Callable<List<String>>> tasks = new ArrayList<>();
		for (String checkerName : getActiveValidations()) {
			tasks.add(new Callable<List<String>>() {
				@Override
				public List<String> call() throws Exception {
					return beanFactory.getBean(checkerName, Validate.class).validate(payload);
				}
			});
		}
		return tasks;
	}

	private List<String> mountResponseByCalled(Collection<Callable<List<String>>> tasks) throws InterruptedException, ExecutionException {
		List<String> response = new ArrayList<>();
		List<Future<List<String>>> results = checkers.invokeAll(tasks, 19, TimeUnit.SECONDS);
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
