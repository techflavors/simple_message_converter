package techflavors.tools.smc.converters;

import java.util.Map;

public abstract class MessageConverter<SourceType, TargetType> {

	// Injected by MessageConverters, because we may have different type of
	// converters
	protected MessageConverters messageConverters;

	@SuppressWarnings("unchecked")
	final TargetType convertToTargetType(Object data) {
		if (data == null)
			return null;
		return toTargetType((SourceType) data);
	}

	@SuppressWarnings("unchecked")
	final SourceType convertToSourceType(Object data) {
		if (data == null)
			return null;
		return toSourceType((TargetType) data);
	}

	@SuppressWarnings("unchecked")
	final TargetType convertToTargetType(Object data, Map<String, Object> param) {
		if (data == null)
			return null;
		return toTargetType((SourceType) data, param);
	}

	@SuppressWarnings("unchecked")
	final SourceType convertToSourceType(Object data, Map<String, Object> param) {
		if (data == null)
			return null;
		return toSourceType((TargetType) data, param);
	}

	// Methods to override
	/**
	 * 
	 * @param data
	 * @param param
	 *            - additional parameters
	 * @return
	 */
	protected TargetType toTargetType(SourceType data, Map<String, Object> param) {
		throw new UnsupportedOperationException("Expecting child classes to implement this method");
	}

	/**
	 * 
	 * @param data
	 * @param param
	 *            - additional parameters
	 * @return
	 */
	protected SourceType toSourceType(TargetType data, Map<String, Object> param) {
		throw new UnsupportedOperationException("Expecting child classes to implement this method");
	}

	protected TargetType toTargetType(SourceType data) {
		throw new UnsupportedOperationException("Expecting child classes to implement this method");
	}

	protected SourceType toSourceType(TargetType data) {
		throw new UnsupportedOperationException("Expecting child classes to implement this method");
	}
}
