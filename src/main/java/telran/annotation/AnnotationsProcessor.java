package telran.annotation;


import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.*;

import telran.validation.annotation.Pattern;
import telran.validation.annotation.Validation;


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
	public static List<String> validate(Object obj) {
		Class<?> clazz = obj.getClass();
		Field[] fields = clazz.getDeclaredFields();
		List<String> errorMessages = new ArrayList<>();
		for(Field field : fields) {
			validateField(field, errorMessages, obj);
		}
		
		return errorMessages;
				
	}
	private static void validateField(Field field, List<String> errorMessages, Object obj) {
		Annotation [] annotations = field.getAnnotations();
		List<Annotation> validationAnnotations = Arrays.stream(annotations)
				.filter(a -> a.annotationType().isAnnotationPresent(Validation.class))
		.toList();
		validationAnnotations.forEach(a -> validateAnnotation(a, field, errorMessages, obj));
		
		
	}

	private static void validateAnnotation(Annotation a, Field field, List<String> errorMessages, Object obj) {
		// TODO Auto-generated method stub
		//define Validator class name according to the Annotation name
		System.out.println(a.annotationType().getName()+"Validator");
		String message = getMessage(field, obj);
		if(!message.isEmpty()) {
			errorMessages.add(message);
		}
		//
	}

	private static String getMessage(Field field, Object obj) {
		String res = "";
		field.setAccessible(true);
		try {
			String value = field.get(obj).toString();
			Pattern pattern = field.getAnnotation(Pattern.class);
			String regex = pattern.regex();
			if(!value.matches(regex)) {
				res = pattern.errorMessage();
			}
		} catch (Exception e) {
			res = e.getMessage();
		}
		return res;
	}
}
