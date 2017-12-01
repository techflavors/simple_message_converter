package techflavors.tools.smc.samples.rest.data;

public class Person {
	public String firstName;
	public String lastName;
	public Address address;

	public String toString() {
		return firstName + "," + lastName + "," + address;

	}
}
