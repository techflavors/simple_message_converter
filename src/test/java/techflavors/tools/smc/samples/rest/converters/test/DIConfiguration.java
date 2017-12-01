package techflavors.tools.smc.samples.rest.converters.test;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import techflavors.tools.smc.converters.MessageConverter;
import techflavors.tools.smc.converters.MessageConverters;
import techflavors.tools.smc.samples.rest.converters.AddressConvertor;
import techflavors.tools.smc.samples.rest.converters.PersonConvertor;

@Configuration
@ComponentScan(basePackages = { "techflavors.tools.smc" })
public class DIConfiguration {
	@Bean(name = "MyProjectConverter")
	public MessageConverters converters() {
		return new MessageConverters("techflavors.tools.smc.samples.rest.converters");
	}

	/** OR annotate PersonConvertor with @Component **/
	@Bean(name = "PersonConverter")
	public MessageConverter<?,?> personConverter() {
		return new PersonConvertor();
	}

	@Bean(name = "AddressConverter")
	public MessageConverter<?,?> addressConverter() {
		return new AddressConvertor();
	}

}
