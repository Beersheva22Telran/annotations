package telran.annotation;

import java.lang.reflect.Field;

public class AnnotationsProcessor {
	public static final String NO_ID_MESSAGE = "A class has to have field annotated by Id";
	public static final String SEVERAL_IDS_MESSAGE = "Field annotated by Id must be only one";
	public static Object getId(Object obj) throws Exception{
		Class<?> clazz = obj.getClass();
		Field[] fields = clazz.getDeclaredFields();
		Field field = getFieldId(fields);
		return field.get(obj);
	}

	private static Field getFieldId(Field[] fields) {
		Field res = null;
		for(Field field: fields) {
			field.setAccessible(true);
			if(field.isAnnotationPresent(Id.class)) {
				if (res != null) {
					throw new IllegalArgumentException(SEVERAL_IDS_MESSAGE);
				}
				res = field;
			}
		}
		if (res == null) {
			throw new IllegalArgumentException(NO_ID_MESSAGE);
		}
		return res;
	}
}
