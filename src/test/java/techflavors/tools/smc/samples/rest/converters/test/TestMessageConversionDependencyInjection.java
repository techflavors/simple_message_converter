package techflavors.tools.smc.samples.rest.converters.test;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;

import javax.inject.Inject;
import javax.inject.Named;

import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.Test;

import techflavors.tools.smc.converters.MessageConverters;
import techflavors.tools.smc.samples.rest.data.Address;
import techflavors.tools.smc.samples.rest.data.Person;

@ContextConfiguration(classes= {DIConfiguration.class})
public class TestMessageConversionDependencyInjection extends AbstractTestNGSpringContextTests{
	
	@Inject
	@Named("MyProjectConverter")
	MessageConverters messageConverters;

	@Test
	public void testMessageConversion() {
		Person person = new Person();
		person.firstName = "Biju";
		person.lastName = "Nair";
		person.address = new Address();
		person.address.street = "My Street";
		person.address.city = "San Jose";

		techflavors.tools.smc.samples.persist.data.Person personToPersist = messageConverters.toTargetType(person);

		assertEquals(personToPersist.name, "Biju-Nair");
		assertNotNull(personToPersist.address);
		assertEquals(personToPersist.address.street, "My Street");
		assertEquals(personToPersist.address.city, "San Jose");

		person = messageConverters.toSourceType(personToPersist);

		assertEquals(person.firstName, "Biju");
		assertEquals(person.lastName, "Nair");
		assertNotNull(person.address);
		assertEquals(person.address.street, "My Street");
		assertEquals(person.address.city, "San Jose");
	}

}
