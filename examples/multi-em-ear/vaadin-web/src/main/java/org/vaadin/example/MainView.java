package org.vaadin.example;

import com.vaadin.cdi.annotation.CdiComponent;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import jakarta.annotation.PostConstruct;
import jakarta.ejb.EJB;
import org.example.modulea.FooAService;
import org.example.moduleb.FooBService;

@Route("")
@CdiComponent
public class MainView extends VerticalLayout {

    @EJB
    private FooAService fooAService;

    @EJB
    private FooBService fooBService;

    @PostConstruct
    public void init() {
        Button button = new Button("Add FooA and FooB objects", e -> Notification
                .show(fooAService.saveAndCountFooA() + ", "
                        + fooBService.saveAndFindAllFooB()));

        button.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        button.addClickShortcut(Key.ENTER);

        add(button);
    }

}
