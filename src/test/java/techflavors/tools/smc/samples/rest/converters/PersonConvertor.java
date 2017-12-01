package techflavors.tools.smc.samples.rest.converters;

import techflavors.tools.smc.converters.MessageConverter;
import techflavors.tools.smc.samples.rest.data.Person;

public class PersonConvertor extends MessageConverter<Person, techflavors.tools.smc.samples.persist.data.Person> {
	@Override
	protected Person toSourceType(techflavors.tools.smc.samples.persist.data.Person data) {
		Person person = new Person();
		String[] names = data.name.split("-");
		person.firstName = names[0];
		person.lastName = names[1];
		person.address = messageConverters.toSourceType(data.address);
		return person;
	}

	@Override
	protected techflavors.tools.smc.samples.persist.data.Person toTargetType(Person data) {
		techflavors.tools.smc.samples.persist.data.Person person = new techflavors.tools.smc.samples.persist.data.Person();
		person.name = data.firstName + "-" + data.lastName;
		person.address = messageConverters.toTargetType(data.address);
		return person;
	}

}
