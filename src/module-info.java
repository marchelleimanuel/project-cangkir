module Project_BAD {
	opens main;
	opens view.homepage;
	
	requires javafx.graphics;
	requires javafx.controls;
	requires java.sql;
	requires jfxtras.labs.samples;
}