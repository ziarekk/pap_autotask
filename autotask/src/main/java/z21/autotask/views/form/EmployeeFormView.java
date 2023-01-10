package z21.autotask.views.form;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;

import org.springframework.beans.factory.annotation.Autowired;
import z21.autotask.entities.EmpStatus;
import z21.autotask.entities.Position;
import z21.autotask.service.DataService;
import z21.autotask.views.MainLayout;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;

@RolesAllowed("ROLE_ADMIN")
@Route(value = "/employeeForm", layout = MainLayout.class)
public class EmployeeFormView extends VerticalLayout {

    private final DataService dataService;
    private final ComboBox<Position> CBposition;
    private final ComboBox<EmpStatus> CBempStatus;

    private final ComboBox<String> CBgender;
    private final DatePicker DTPdateOfBirth;

    private final TextField firstNameTF;
    private final TextField lastNameTF;

    HorizontalLayout buttons;


    @Autowired
    public EmployeeFormView(DataService dataService) {
        this.dataService = dataService;

        FormLayout employeeForm = new FormLayout();

        firstNameTF = new TextField("First name:");
        lastNameTF = new TextField("Last name:");
        CBgender = prepareGenderComboBox();
        CBposition = preparePositionsComboBox();
        CBempStatus = prepareEmpStatusComboBox();
        DTPdateOfBirth = prepareDatePicker();

        buttons=  prepareButtons(); //new Button("Add");

        employeeForm.add(firstNameTF,lastNameTF, CBgender, CBposition,CBempStatus, DTPdateOfBirth, buttons);

        H1 title = new H1("Add Employee");
        add(title, employeeForm);
        setWidth("auto");
        setMargin(true);
        setJustifyContentMode(FlexComponent.JustifyContentMode.START);
        setAlignItems(FlexComponent.Alignment.STRETCH);
    }

    private HorizontalLayout prepareButtons() {
        Button addButton = new Button("Add");
        Button BClear = new Button("Clear");
        HorizontalLayout buttons = new HorizontalLayout();
        buttons.add(addButton, BClear);

        addButton.addClickListener(click -> {
            String firstName = firstNameTF.getValue();
            String lastName = lastNameTF.getValue();

            Integer positionId = CBposition.getValue().getPositionId();
            Integer empStatusId = CBempStatus.getValue().getStatusId();
            char gender = CBgender.getValue().charAt(0);
            Date birthDate = Date.from(DTPdateOfBirth.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant());
            dataService.addEmployee(firstName, lastName,  gender, birthDate, positionId, empStatusId, null);

            Notification.show("Successfully added new Employee to database!");
        });

        BClear.addClickListener(click -> {
            firstNameTF.clear();
            lastNameTF.clear();
            CBposition.clear();
            CBempStatus.clear();
            CBgender.clear();;
            DTPdateOfBirth.setValue(LocalDate.now());

            Notification.show("All info cleared.");
        });
        return buttons;
    }

    private ComboBox<Position> preparePositionsComboBox() {
        ComboBox<Position> CBposition = new ComboBox<>("Position");
        CBposition.setItems(dataService.getAllPositions());
        CBposition.setItemLabelGenerator(Position::getName);
        return CBposition;
    }

    private ComboBox<EmpStatus> prepareEmpStatusComboBox() {
        ComboBox<EmpStatus> CBempStatus = new ComboBox<>("Employee Status");
        CBempStatus.setItems(dataService.getAllEmpStatuses());
        CBempStatus.setItemLabelGenerator(EmpStatus::getDescription);
        return CBempStatus;
    }

    private ComboBox<String> prepareGenderComboBox() {
        ComboBox<String> CBspecies = new ComboBox<>("Gender");
        String[] genders = new String[]{"Woman", "Man", "Unknown"};
        CBspecies.setItems(genders);
        return CBspecies;
    }

    private DatePicker prepareDatePicker() {
        DatePicker DTPwhen = new DatePicker();
        DTPwhen.setLabel("Date of birth");
        DTPwhen.setValue(LocalDate.now());
        return DTPwhen;
    }

}
