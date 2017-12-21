# Simple Message or Object Converter

There are many components available to convert an object of type A to type B. Most of these converters are either runtime or generates codes. The converters are configured using some configuration or mapping files. These converters are good and easy for simple object conversion, but when it comes to complex object, we write complex mappings and configurations.

The biggest problem I have seen in such component is, if some value is not properly converted, it is difficult to debug and fix it, unless you are expert in using such component.

Here I am introducing a simple conversion component, which let developers to write conversion in much simple way and get full control over the conversion logic. The performance is far better because it uses direct conversion logic and no runtime api's. 

## Getting Started

It is easy to use the simple message converter. 

* Add maven dependency
* Create all POJO's
* Write converters for each Pojo Conversion. 
* Write an loader/factory to inject/load all converters.

### Maven Dependency
```xml
	<dependency>
		<groupId>com.github.techflavors</groupId>
		<artifactId>simple-message-converters</artifactId>
		<version>1.0.0</version>
	</dependency>
```

### Generating Converters and Loaders using Maven Plugin

We have provided a maven plugin to generate templates for converters and loaders.

```xml
<plugin>
<groupId>com.github.techflavors</groupId>
<artifactId>smc-maven-plugin</artifactId>
<version>1.0.0</version>
<executions>
    <execution>
        <id>smc</id>
        <goals>
            <goal>smc-generator</goal>
        </goals>
    </execution>
</executions>
<configuration>
    <sourceDirectory>${basedir}/src/main/java</sourceDirectory>
    <converters>
        <converter>
            <name>TestConverter</name>
            <packageName>techflavors.tools.smc.generators.converters</packageName>
            <sourceType>techflavors.tools.smc.generators.source.data.Person</sourceType>
            <targetType>techflavors.tools.smc.generators.target.data.Person</targetType>
        </converter>
    </converters>
    <injectionType>PLAIN</injectionType> <!-- use SPRING for spring based loaders -->
    <outputDirectory>target/generated-sources/smc</outputDirectory>
</configuration>
</plugin>
```
*The plugin generate conversion for direct convertable fields, for customization you have to modify the generated code*

You can refer to [sample test case](https://github.com/techflavors/simple-message-converters-maven-plugin/tree/master/simple-message-converters-maven-plugin-test) on how to use maven plugin. 

### Generating Converters manually

* Extend the converter class from techflavors.tools.smc.converters.MessageConverter
* MessageConverter takes source and target types as arguments
* Depending on the conversion you can override following
    * toTargetType
    * toTargetType with params (in case if you have external parameters)
    * toSourceType
    * toSourceType with params

```java
// PersonConverter.java
package techflavors.tools.smc.generators.converters;

import techflavors.tools.smc.converters.MessageConverter;

public class PersonConverter extends
		MessageConverter<techflavors.tools.smc.generators.source.data.Person, techflavors.tools.smc.generators.target.data.Person> {

	@SuppressWarnings({ "unchecked" })
	@Override
	protected techflavors.tools.smc.generators.target.data.Person toTargetType(
			techflavors.tools.smc.generators.source.data.Person data) {
		techflavors.tools.smc.generators.target.data.Person result = new techflavors.tools.smc.generators.target.data.Person();

		result.setName(data.getFirstname() +","+data.getLastname());
		
        result.setAddressList(
            (java.util.List<techflavors.tools.smc.generators.target.data.Address>) 
            (java.util.List<?>) 
            messageConverters.toTargetTypeAsCollection(data.getAddressList()));

		result.setAddressSet(
            (java.util.Set<techflavors.tools.smc.generators.target.data.Address>) 
            (java.util.Set<?>) 
            messageConverters.toTargetTypeAsCollection(data.getAddressSet()));

		result.setAddressArray(
            (techflavors.tools.smc.generators.target.data.Address[]) 
            messageConverters.toTargetTypeAsArray(data.getAddressArray()));

		result.setAddress(
            (techflavors.tools.smc.generators.target.data.Address) 
            messageConverters.toTargetType(data.getAddress()));

		result.setAnything(
				(java.util.List<?>) 
                (java.util.List<?>) 
                messageConverters.toTargetTypeAsCollection(data.getAnything()));

		return result;
	}

	@SuppressWarnings({ "unchecked" })
	@Override
	protected techflavors.tools.smc.generators.source.data.Person toSourceType(
			techflavors.tools.smc.generators.target.data.Person data) {
		techflavors.tools.smc.generators.source.data.Person result = new techflavors.tools.smc.generators.source.data.Person();
       
        String[] token = data.getName().split(",");
        result.setFirstname(token[0]);
		result.setLastname(token[1]);

		result.setAddressList((
            java.util.List<techflavors.tools.smc.generators.source.data.Address>) 
            (java.util.List<?>) 
            messageConverters.toSourceTypeAsCollection(data.getAddressList()));

		result.setAddressSet(
                (java.util.Set<techflavors.tools.smc.generators.source.data.Address>) 
                (java.util.Set<?>) 
                messageConverters.toSourceTypeAsCollection(data.getAddressSet()));

		result.setAddressArray(
            (techflavors.tools.smc.generators.source.data.Address[]) 
            messageConverters.toSourceTypeAsArray(data.getAddressArray()));

		result.setAddress(
            (techflavors.tools.smc.generators.source.data.Address) 
            messageConverters.toSourceType(data.getAddress()));

		result.setAnything(
				(java.util.List<?>) 
                (java.util.List<?>) 
                messageConverters.toSourceTypeAsCollection(data.getAnything()));

		return result;
	}

}
```
```java
//AddressConvertor.java
import techflavors.tools.smc.converters.MessageConverter;

public class AddressConvertor extends
		MessageConverter<techflavors.tools.smc.generators.source.data.Address, techflavors.tools.smc.generators.target.data.Address> {

	@SuppressWarnings({ "unchecked" })
	@Override
	protected techflavors.tools.smc.generators.target.data.Address toTargetType(
			techflavors.tools.smc.generators.source.data.Address data) {
		techflavors.tools.smc.generators.target.data.Address result = new techflavors.tools.smc.generators.target.data.Address();

		result.setStreet(data.getStreet());
		result.setCity(data.getCity());

		return result;
	}

	@SuppressWarnings({ "unchecked" })
	@Override
	protected techflavors.tools.smc.generators.source.data.Address toSourceType(
			techflavors.tools.smc.generators.target.data.Address data) {
		techflavors.tools.smc.generators.source.data.Address result = new techflavors.tools.smc.generators.source.data.Address();

		result.setStreet(data.getStreet());
		result.setCity(data.getCity());

		return result;
	}

}
```
### Generating Loaders/Injector's manually
#### For Spring based Java Project 
```java
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import techflavors.tools.smc.converters.MessageConverter;
import techflavors.tools.smc.converters.MessageConverters;

@Configuration
@ComponentScan(basePackages = { "techflavors.tools.smc.generators.converters" })
public class TestConverterFactory {

	@Bean(name = "techflavors.tools.smc.generators.converters")
	public MessageConverters converters() {
		return new MessageConverters("techflavors.tools.smc.generators.converters");
	}
	
		
	@Bean(name = "AddressConvertor")
	public MessageConverter<?,?> AddressConvertor() {
		return new AddressConvertor();
	}
		
	@Bean(name = "PersonConvertor")
	public MessageConverter<?,?> PersonConvertor() {
		return new PersonConvertor();
	}
}
```
Usage
```java
public class TestMessageConversionDependencyInjection extends AbstractTestNGSpringContextTests {

	@Inject
	@Named("techflavors.tools.smc.generators.converters")
	MessageConverters messageConverters;

	@Test
	public void testMessageConversion() {
		TestMessageConversion messageConversion = new TestMessageConversion();
		messageConversion.testMessageConversion(messageConverters);
	}

}
```
#### For Normal Java Project (non-Spring based)
```java
public final class TestConverterFactory {

	private static final MessageConverters messageConverters = new MessageConverters(
			"techflavors.tools.smc.generators.converters");

	static {
		List<MessageConverter<?, ?>> messageConverterList = new ArrayList<>();
		messageConverterList.add(new PersonConverter());
		messageConverterList.add(new AddressConverter());

		messageConverters.setMessageConverters(messageConverterList);
	}

	public static final MessageConverters messageConvertors() {
		return messageConverters;
	}

	private TestConverterFactory() {
	}

}
```
Usage
```java
public class TestMessageConversionWithoutDependencyInjection {

	@Test
	public void testMessageConversion() throws Exception {
		TestMessageConversion messageConversion = new TestMessageConversion();
		messageConversion.testMessageConversion(TestConverterFactory.messageConvertors());
	}

}
```
#### TestMessageConversion.java
```java
public class TestMessageConversion {

	public void testMessageConversion(MessageConverters messageConverters) {
		Person person = new Person();
		person.firstname = "Biju";
		person.lastname = "Nair";
		person.address = new Address();
		person.address.street = "My Street";
		person.address.city = "My City";

		techflavors.tools.smc.generators.target.data.Person personToPersist = messageConverters.toTargetType(person);

		assertEquals(personToPersist.firstname, "Biju");
		assertEquals(personToPersist.lastname, "Nair");
		assertNotNull(personToPersist.address);
		assertEquals(personToPersist.address.street, "My Street");
		assertEquals(personToPersist.address.city, "My City");

		person = messageConverters.toSourceType(personToPersist);

		assertEquals(person.firstname, "Biju");
		assertEquals(person.lastname, "Nair");
		assertNotNull(person.address);
		assertEquals(person.address.street, "My Street");
		assertEquals(person.address.city, "My City");
	}

}
```
## Comparison & Performance

will include soon. 

## Contributing

Please feel free to contribute.

## Authors

* **Biju Nair** 

See also the list of [contributors](https://github.com/techflavors/simple-message-converters/graphs/contributors) who participated in this project.

