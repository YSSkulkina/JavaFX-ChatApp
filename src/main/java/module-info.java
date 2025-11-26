module com.chat {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires org.kordamp.bootstrapfx.core;

    opens com.chat to javafx.fxml;
    exports com.chat;
    exports com.chat.server;
    opens com.chat.server to javafx.fxml;

    opens com.chat.client to javafx.fxml;
    exports com.chat.client;
}