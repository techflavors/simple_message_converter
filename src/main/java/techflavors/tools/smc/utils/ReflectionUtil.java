package  techflavors.tools.smc.utils;

import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

public abstract class ReflectionUtil {
	public static Class<?> getSuperClassGenericType(Class<?> clz, int paramindex) {
		Type type = clz.getGenericSuperclass();
		return getGenericType(type, paramindex);
	}

	public static Class<?> getGenericType(Type type, int paramindex) {
		  Class<?> genericType = null;
		  if (type instanceof ParameterizedType) {
		   genericType = (Class<?>) ((ParameterizedType) type).getActualTypeArguments()[paramindex];
		  }
		  return genericType;
	}	

	public static Method findMethod(Class<?> clazz, String name,
			Class<?>... paramTypes) {
		Method method = null;
		try {
			method = clazz.getDeclaredMethod(name, paramTypes);
		} catch (NoSuchMethodException | SecurityException e) {
		}
		return method;
	}
}
