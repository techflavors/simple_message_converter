package techflavors.tools.smc.samples.persist.data;

public class Address {
	public String street;
	public String city;

	@Override
	public String toString() {
		return street + "," + city;
	}
}
