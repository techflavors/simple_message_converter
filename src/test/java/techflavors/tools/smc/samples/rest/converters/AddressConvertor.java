package techflavors.tools.smc.samples.rest.converters;

import techflavors.tools.smc.converters.MessageConverter;
import techflavors.tools.smc.samples.rest.data.Address;

public class AddressConvertor extends MessageConverter<Address, techflavors.tools.smc.samples.persist.data.Address> {
	@Override
	protected Address toSourceType(techflavors.tools.smc.samples.persist.data.Address data) {
		Address address = new Address();
		address.city = data.city;
		address.street = data.street;
		return address;
	}

	@Override
	protected techflavors.tools.smc.samples.persist.data.Address toTargetType(Address data) {
		techflavors.tools.smc.samples.persist.data.Address address = new techflavors.tools.smc.samples.persist.data.Address();
		address.city = data.city;
		address.street = data.street;
		return address;
	}
}
