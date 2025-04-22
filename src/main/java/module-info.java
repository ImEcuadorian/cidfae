module io.github.imecuadorian.cidfae {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires java.desktop;
    requires static lombok;
    requires io.github.cdimascio.dotenv.java;
    requires mysql.connector.j;

    opens io.github.imecuadorian.cidfae.controller to javafx.fxml;

    exports io.github.imecuadorian.cidfae;
    exports io.github.imecuadorian.cidfae.controller;
    exports io.github.imecuadorian.cidfae.controller.view;

    opens io.github.imecuadorian.cidfae to javafx.fxml;
    opens io.github.imecuadorian.cidfae.controller.view to javafx.fxml;
}
