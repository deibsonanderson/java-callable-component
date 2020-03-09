package br.com.component.callable.config;

import java.util.List;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Data;

@Component
@ConfigurationProperties(prefix = "validators")
@Data
public class Properties {

	private List<String> actives;
}
