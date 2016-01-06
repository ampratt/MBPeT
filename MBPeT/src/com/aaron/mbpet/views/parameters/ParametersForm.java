package com.aaron.mbpet.views.parameters;

import java.util.List;

import com.kbdunn.vaadin.addons.fontawesome.FontAwesome;
import com.aaron.mbpet.domain.Parameters;
import com.aaron.mbpet.services.FlotUtils;
import com.aaron.mbpet.ui.RampFlotWindow;
import com.vaadin.data.Validator;
import com.vaadin.data.fieldgroup.PropertyId;
import com.vaadin.data.validator.BeanValidator;
//import com.vaadin.server.FontAwesome;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;

public class ParametersForm extends FormLayout implements Button.ClickListener {

    List<String> dstatList;
    String[] dstats = {"None", "\"files\"", "\"ssh\""};
    Button rampbutton;
    
	// DSTAT settings
	@PropertyId("dstat_mode")
	private ComboBox dstat_mode = new ComboBox();
	
	@PropertyId("host")
	private TextField host = new TextField("Host:");
	
	@PropertyId("username")
	private TextField username = new TextField("Username:");
	
	@PropertyId("password")
	private TextField password = new TextField("Password:");
	
	@PropertyId("user_types")
	private TextField user_types = new TextField("User types:");
	
	@PropertyId("models_folder")
	private TextField models_folder = new TextField("Models folder:");
	
	@PropertyId("test_report_folder")
	private TextField test_report_folder = new TextField("Test report folder:");
	
	@PropertyId("ip")
	private TextField ip = new TextField("Ip address:");

	
	// TEST settings
	@PropertyId("test_duration")
	private TextField test_duration = new TextField("Test duration (secs):");		//test_duration
	
	@PropertyId("monitoring_interval")
	private TextField monitoring_interval = new TextField("Monitoring interval (secs):");
	
	@PropertyId("mean_user_think_time")
	private TextField mean_user_think_time = new TextField("Global think time (secs):");
	
	@PropertyId("standard_deviation")
	private TextField standard_deviation = new TextField("Standard deviation (secs):");
	
	@PropertyId("ramp_list")
	private TextField ramp_list = new TextField();
	
//	@PropertyId("target_response_times")
//	private TextField target_response_times = new TextField("Target Response Time");

	
	
	public ParametersForm() {
		this.setMargin(true);
//		this.setSpacing(true);

		host.addValidator(new BeanValidator(Parameters.class, "host"));
		username.addValidator(new BeanValidator(Parameters.class, "username"));
		password.addValidator(new BeanValidator(Parameters.class, "password"));
		user_types.addValidator(new BeanValidator(Parameters.class, "user_types"));
		models_folder.addValidator(new BeanValidator(Parameters.class, "models_folder"));
		test_report_folder.addValidator(new BeanValidator(Parameters.class, "test_report_folder"));
		ip.addValidator(new BeanValidator(Parameters.class, "ip"));
		
		test_duration.addValidator(new MyIntegerValidator());			//(new BeanValidator(Parameters.class, "test_duration"));
		monitoring_interval.addValidator(new MyIntegerValidator());		//(new BeanValidator(Parameters.class, "monitoring_interval"));
		mean_user_think_time.addValidator(new MyIntegerValidator());	//(new BeanValidator(Parameters.class, "mean_user_think_time"));
		standard_deviation.addValidator(new MyDoubleValidator());		//(new BeanValidator(Parameters.class, "standard_deviation"));
		ramp_list.addValidator(new BeanValidator(Parameters.class, "ramp_list"));
//		TargetResponseTime.addValidator(new BeanValidator(Parameters.class, "TargetResponseTime"));
		
		
		host.setNullRepresentation("");
		username.setNullRepresentation("");
		password.setNullRepresentation("");
		user_types.setNullRepresentation("");
		models_folder.setNullRepresentation("");
		test_report_folder.setNullRepresentation("");
		ip.setNullRepresentation("");
		
		test_duration.setNullRepresentation("0");
		monitoring_interval.setNullRepresentation("0");
		mean_user_think_time.setNullRepresentation("0.0");
		standard_deviation.setNullRepresentation("0.0");
		ramp_list.setNullRepresentation("");
//		TargetResponseTime.setNullRepresentation("");
				
		disableValidationMessages();
		
        Label section = new Label("DSTAT CONFIG");
        section.addStyleName("h4");
        section.addStyleName("colored");
        	addComponent(section);
        
        HorizontalLayout wrap = new HorizontalLayout();
        wrap.setCaption("DSTAT mode:");

        dstat_mode.setTextInputAllowed(false);
        dstat_mode.addItem("None");
        dstat_mode.addItem("\"file\"");
        dstat_mode.addItem("\"ssh\"");
        dstat_mode.setNullSelectionAllowed(false);
        dstat_mode.setValue("None");
        dstat_mode.addStyleName("tiny");
        dstat_mode.setWidth(7, Unit.EM);
	        wrap.addComponent(dstat_mode);
	        addComponent(wrap);
	        
	        addComponent(host);
			addComponent(username);
			addComponent(password);
			addComponent(user_types);
			addComponent(models_folder);
			addComponent(test_report_folder);
			addComponent(ip);
		
		
		section = new Label("TEST CONFIG");
        section.addStyleName("h4");
        section.addStyleName("colored");
	        addComponent(section);
	        
			addComponent(test_duration);
			addComponent(monitoring_interval);
			addComponent(mean_user_think_time);
			addComponent(standard_deviation);
        
		wrap = new HorizontalLayout();
		wrap.setSpacing(false);
		wrap.setWidth("100%");
		wrap.setCaption("Ramp list:");

			ramp_list.addStyleName("light");
			ramp_list.addStyleName("borderless");
			ramp_list.addStyleName("small");
	    	ramp_list.setInputPrompt("[(0,0), (100,200), ..]");
		    wrap.addComponent(ramp_list);
		
			rampbutton = new Button("");	//, this);
			rampbutton.setIcon(FontAwesome.LINE_CHART);
			rampbutton.addStyleName("borderless");
			rampbutton.addStyleName("colored");
			rampbutton.setDescription("Plot ramp function on chart");
			wrap.addComponent(rampbutton);
			wrap.setComponentAlignment(rampbutton, Alignment.MIDDLE_RIGHT);
			wrap.setExpandRatio(ramp_list, 1);
		addComponent(wrap);
	        
//			addComponent(TargetResponseTime);
		
		for (Component c : this.components) {
			c.addStyleName("tiny");
		}
//        dstat_mode.removeStyleName("tiny");

	}
	
	public void disableValidationMessages() {
		host.setValidationVisible(false);
		username.setValidationVisible(false);
		password.setValidationVisible(false);
		user_types.setValidationVisible(false);
		models_folder.setValidationVisible(false);
		test_report_folder.setValidationVisible(false);
		
		ip.setValidationVisible(false);
		test_duration.setValidationVisible(false);
		monitoring_interval.setValidationVisible(false);
		mean_user_think_time.setValidationVisible(false);
		standard_deviation.setValidationVisible(false);
		ramp_list.setValidationVisible(false);
//		TargetResponseTime.setValidationVisible(false);

	}
	
	public void enableValidationMessages() {
		host.setValidationVisible(true);
		username.setValidationVisible(true);
		password.setValidationVisible(true);
		user_types.setValidationVisible(true);
		models_folder.setValidationVisible(true);
		test_report_folder.setValidationVisible(true);
		
		ip.setValidationVisible(true);
		test_duration.setValidationVisible(true);
		monitoring_interval.setValidationVisible(true);
		mean_user_think_time.setValidationVisible(true);
		standard_deviation.setValidationVisible(true);
		ramp_list.setValidationVisible(true);
//		TargetResponseTime.setValidationVisible(true);
	}

	
	class MyIntegerValidator implements Validator {
	    @Override
	    public void validate(Object value)
	            throws InvalidValueException {
	        if (!(value instanceof Integer ))	//&& ((String)value).equals("hello")
	            throw new InvalidValueException("Value must be an integer");
	    }
	}
	
	class MyDoubleValidator implements Validator {
	    @Override
	    public void validate(Object value)
	            throws InvalidValueException {
	        if (!(value instanceof Double ))	//&& ((String)value).equals("hello")
	            throw new InvalidValueException("Value must be a double");
	    }
	}
//	ip
//	test_duration
//	ramp_list
//	TargetResponseTime
//	monitoring_interval
//	mean_user_think_time
//	standard_deviation

	public TextField getRampList(){
		return ramp_list;
	}
	public Button getRampButton(){
		return rampbutton;
	}
	
	@Override
	public void buttonClick(ClickEvent event) {
		if (event.getButton() == rampbutton) {
			String formatted = FlotUtils.formatRampToFlot(ramp_list.getValue());
			Notification.show("Formatted for flot: " + formatted);
//			FlotUtils.formatFlotToRamp(formatted);
			if (ramp_list.getValue().toString() == null){
				UI.getCurrent().addWindow(new RampFlotWindow("[(0,0)]"));
			} else
				UI.getCurrent().addWindow(new RampFlotWindow(ramp_list.getValue().toString()));
		}
		
	}

}
