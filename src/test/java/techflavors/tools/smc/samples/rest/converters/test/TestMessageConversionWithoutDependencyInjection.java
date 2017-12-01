package techflavors.tools.smc.samples.rest.converters.test;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;

import java.util.ArrayList;
import java.util.List;

import org.testng.annotations.Test;

import techflavors.tools.smc.converters.MessageConverter;
import techflavors.tools.smc.converters.MessageConverters;
import techflavors.tools.smc.samples.rest.converters.AddressConvertor;
import techflavors.tools.smc.samples.rest.converters.PersonConvertor;
import techflavors.tools.smc.samples.rest.data.Address;
import techflavors.tools.smc.samples.rest.data.Person;

public class TestMessageConversionWithoutDependencyInjection {

	@Test
	public void testMessageConversion() {
		MessageConverters messageConverters = new MessageConverters("techflavors.tools.smc.samples.rest.converters");
		List<MessageConverter<?, ?>> messageConverterList = new ArrayList<>();
		messageConverterList.add(new PersonConvertor());
		messageConverterList.add(new AddressConvertor());

		messageConverters.setMessageConverters(messageConverterList);

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
