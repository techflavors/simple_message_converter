package techflavors.tools.smc.converters;

import java.lang.reflect.Array;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.inject.Inject;

import techflavors.tools.smc.utils.ReflectionUtil;

/**
 * The core class for Message Conversion
 * 
 * <ol>
 * <li>If this component is used as part javax dependency injection framework
 * (like Spring), create a bean as shown below
 * 
 * <pre>
 * {@code @Bean("RestConvertors")} 
 * public MessageConverters converters() {
 *   return new MessageConverters(
 *    "techflavors.tools.smc.samples.rest.convertors"); 
 * }
 * </pre>
 * 
 * </li>
 * <li>For using this converter inject the converter as
 * 
 * <pre>
 * {@code @Inject @Named(name="RestConvertors") }
 *        MessageConverters messageConverters;
 * 
 *        public void somemethod(){
 *           messageConverters.toTargetType(obj); 
 *        }
 * </pre>
 * 
 * <li>If it is used with non dependency framework, create a singleton instance
 * of this class (per conversion) and invoke {@link #setMessageConverters(List)}
 * and <a target="_blank" href=
 * "https://github.com/techflavors/simple_message_converter/blob/master/src/test/java/techflavors/tools/smc/samples/rest/converters/test/TestMessageConversionWithoutDependencyInjection.java">TestMessageConversionWithoutDependencyInjection</a></li>
 * </ol>
 * 
 * 
 *
 */
public class MessageConverters {

	private final int TO_TARGET = 1;
	private final int TO_SOURCE = 2;

	private Map<Class<?>, MessageConverter<?, ?>> messageConverters = new HashMap<Class<?>, MessageConverter<?, ?>>();

	private String convertorsBasePackage;

	/**
	 * Base package name. This is to avoid conflict between multiple converters
	 * 
	 * @param convertorsBasePackage
	 */
	public MessageConverters(String convertorsBasePackage) {
		this.convertorsBasePackage = convertorsBasePackage;
	}

	@Inject
	public void setMessageConverters(List<MessageConverter<?, ?>> converters) {

		for (MessageConverter<?, ?> converter : converters) {

			if (!converter.getClass().getPackage().getName().startsWith(convertorsBasePackage))
				continue;

			converter.messageConverters = this;

			Class<?> sourceTypeClass = ReflectionUtil.getSuperClassGenericType(converter.getClass(), 0);
			Class<?> targetTypeClass = ReflectionUtil.getSuperClassGenericType(converter.getClass(), 1);

			Method toTargetTypeMethod = ReflectionUtil.findMethod(converter.getClass(), "toTargetType",
					sourceTypeClass);
			Method toTargetTypeMethodWithParam = ReflectionUtil.findMethod(converter.getClass(), "toTargetType",
					sourceTypeClass, Map.class);
			Method toSourceTypeMethod = ReflectionUtil.findMethod(converter.getClass(), "toSourceType",
					targetTypeClass);
			Method toSourceTypeWithParam = ReflectionUtil.findMethod(converter.getClass(), "toSourceType",
					targetTypeClass, Map.class);

			if (toTargetTypeMethod != null || toTargetTypeMethodWithParam != null) {
				messageConverters.put(sourceTypeClass, converter);
			}
			if (toSourceTypeMethod != null || toSourceTypeWithParam != null) {
				messageConverters.put(targetTypeClass, converter);
			}

		}
	}

	public <TargetType> TargetType toTargetType(Object data) {
		return convertToTargetType(data, null);
	}

	public <TargetType> TargetType toTargetType(Object data, Map<String, Object> param) {
		return convertToTargetType(data, param);
	}

	public <SourceType> SourceType toSourceType(Object data) {
		return convertoSourceType(data, null);
	}

	public <SourceType> SourceType toSourceType(Object data, Map<String, Object> param) {
		return convertoSourceType(data, param);
	}

	public <TargetType> Collection<TargetType> toTargetTypeAsCollection(Object data) {
		return convertAsCollection(convertToTargetType(data, null));
	}

	public <TargetType> Collection<TargetType> toTargetTypeAsCollection(Object data, Map<String, Object> param) {
		return convertAsCollection(convertToTargetType(data, param));
	}

	public <SourceType> Collection<SourceType> toSourceTypeAsCollection(Object data) {
		return convertAsCollection(convertoSourceType(data, null));
	}

	public <SourceType> Collection<SourceType> toSourceTypeAsCollection(Object data, Map<String, Object> param) {
		return convertAsCollection(convertoSourceType(data, param));
	}

	public <TargetType> TargetType[] toTargetTypeAsArray(Object data) {
		return convertAsArray(convertToTargetType(data, null));
	}

	public <TargetType> TargetType[] toTargetTypeAsArray(Object data, Map<String, Object> param) {
		return convertAsArray(convertToTargetType(data, param));
	}

	public <SourceType> SourceType[] toSourceTypeAsArray(Object data) {
		return convertAsArray(convertoSourceType(data, null));
	}

	public <SourceType> SourceType[] toSourceTypeAsArray(Object data, Map<String, Object> param) {
		return convertAsArray(convertoSourceType(data, param));
	}

	private <TargetType> TargetType convertToTargetType(Object data, Map<String, Object> param) {
		return convert(data, param, TO_TARGET);
	}

	private <SourceType> SourceType convertoSourceType(Object data, Map<String, Object> param) {
		return convert(data, param, TO_SOURCE);
	}

	@SuppressWarnings("unchecked")
	private <ReturnType> ReturnType convert(Object data, Map<String, Object> param, int dir) {

		ReturnType result = null;
		if (data instanceof Object[]) {
			result = (ReturnType) convertInternal((Object[]) data, param, dir);
		} else if (data instanceof List) {
			result = (ReturnType) convertInternal((List<?>) data, param, dir);
		} else if (data instanceof Set) {
			result = (ReturnType) convertInternal((Set<?>) data, param, dir);
		} else {
			result = (ReturnType) convertInternal(data, param, dir);
		}

		return result;
	}

	private List<?> convertInternal(List<?> data, Map<String, Object> param, int dir) {
		if (data == null || data.size() == 0) {
			return Collections.EMPTY_LIST;
		}

		List<Object> resultList = new ArrayList<Object>(data.size());
		for (Object dataObj : data) {
			resultList.add(convertInternal(dataObj, param, dir));
		}
		return resultList;
	}

	private Set<?> convertInternal(Set<?> data, Map<String, Object> param, int dir) {
		if (data == null || data.size() == 0) {
			return Collections.EMPTY_SET;
		}

		Set<Object> resultList = new HashSet<Object>(data.size());
		for (Object dataObj : data) {
			resultList.add(convertInternal(dataObj, param, dir));
		}
		return resultList;
	}

	@SuppressWarnings("unchecked")
	private <ReturnType> ReturnType[] convertInternal(Object[] data, Map<String, Object> param, int dir) {
		if (data == null || data.length == 0) {
			return null;
		}
		ReturnType[] resultArr = null;
		for (int index = 0; index < data.length; index++) {
			Object convertedObject = convertInternal(data[index], param, dir);
			if (index == 0) {
				resultArr = (ReturnType[]) Array.newInstance(convertedObject.getClass(), data.length);
			}
			resultArr[index] = (ReturnType) convertedObject;
		}
		return resultArr;
	}

	private Object convertInternal(Object data, Map<String, Object> param, int dir) {
		if (data == null)
			return null;
		MessageConverter<?, ?> messageConverter = messageConverters.get(data.getClass());

		if (messageConverter == null) {
			throw new MessageConversionException(String.format("No Converter/method found for %s", data.getClass()));
		}

		Object result = null;
		switch (dir) {
		case TO_TARGET:
			result = (param == null) ? messageConverter.convertToTargetType(data)
					: messageConverter.convertToTargetType(data, param);
			break;
		case TO_SOURCE:
			result = (param == null) ? messageConverter.convertToSourceType(data)
					: messageConverter.convertToSourceType(data, param);
			break;

		}
		return result;
	}

	@SuppressWarnings("unchecked")
	private <ReturnType> Collection<ReturnType> convertAsCollection(Object convertedObject) {
		if (convertedObject == null)
			return null;
		Collection<ReturnType> result = null;
		if (convertedObject instanceof Object[]) {
			result = (List<ReturnType>) Arrays.asList((Object[]) convertedObject);
		} else if (convertedObject instanceof List) {
			result = (List<ReturnType>) convertedObject;
		} else if (convertedObject instanceof Set) {
			result = (Set<ReturnType>) convertedObject;
		} else {
			result = new ArrayList<ReturnType>();
			result.add((ReturnType) convertedObject);
		}

		return result;
	}

	@SuppressWarnings("unchecked")
	private <ReturnType> ReturnType[] convertAsArray(Object convertedObject) {
		if (convertedObject == null)
			return null;
		ReturnType[] result = null;
		if (convertedObject instanceof Object[]) {
			result = (ReturnType[]) convertedObject;
		} else if (convertedObject instanceof List) {
			List<?> list = (List<?>) convertedObject;
			if (list.size() > 0) {
				result = (ReturnType[]) list
						.toArray((ReturnType[]) Array.newInstance(list.get(0).getClass(), list.size()));
			}

		} else {
			result = (ReturnType[]) Array.newInstance(convertedObject.getClass(), 1);
			result[0] = (ReturnType) convertedObject;
		}

		return result;
	}

}
