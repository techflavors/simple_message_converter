package techflavors.tools.smc.samples.rest.data;

public class Address {
	public String street;
	public String city;

	@Override
	public String toString() {
		return street + "," + city;
	}
}
