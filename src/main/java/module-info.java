module com.example.domineering {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires org.kordamp.bootstrapfx.core;
    requires com.almasb.fxgl.all;

    opens com.example.domineering to javafx.fxml;
    exports com.example.domineering;
    exports com.example.domineering.Agent;
    opens com.example.domineering.Agent to javafx.fxml;
    exports com.example.domineering.State;
    opens com.example.domineering.State to javafx.fxml;
}