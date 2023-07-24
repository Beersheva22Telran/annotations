package telran.annotation;

import static org.junit.jupiter.api.Assertions.*;
import static telran.annotation.AnnotationsProcessor.*;
import org.junit.jupiter.api.Test;

class AnnotationProcessorTest {

	@Test
	void getIdTest() throws Exception {
		String email = "stam@gmail.com";
		assertEquals(email, getId(new Xright(email, email)));
		assertThrowsExactly(IllegalArgumentException.class, ()->getId(new XnoId(email, email)), NO_ID_MESSAGE);
		assertThrowsExactly(IllegalArgumentException.class, ()->getId(new XtwoId(email, email)), SEVERAL_IDS_MESSAGE);
		
	}

}
class Xright {
	public Xright(String email, String name) {
		this.email = email;
		this.name = name;
	}
	@Id
	private String email;
	String name;
}
class XnoId {
	public XnoId(String email, String name) {
		this.email = email;
		this.name = name;
	}
	
	private String email;
	String name;
}
class XtwoId {
	public XtwoId(String email, String name) {
		
		this.email = email;
		this.name = name;
	}
	@Id
	private String email;
	@Id
	String name;
}