module tiendaElectronicaQuiz {
	requires javafx.controls;
	requires javafx.fxml;
	requires java.sql;
	requires javafx.base;
	requires java.base;
	requires javafx.graphics;
	requires poi;
	requires poi.ooxml;
	requires com.oracle.database.jdbc;
	
	opens controller;
	opens application to javafx.graphics, javafx.fxml;
	opens model to javafx.base, javafx.controls;
}
