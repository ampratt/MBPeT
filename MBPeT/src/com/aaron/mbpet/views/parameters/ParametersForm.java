package com.aaron.mbpet.views.parameters;

import com.aaron.mbpet.domain.Parameters;
import com.aaron.mbpet.domain.TestSession;
import com.vaadin.data.Property;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.fieldgroup.PropertyId;
import com.vaadin.data.validator.BeanValidator;
import com.vaadin.event.FieldEvents.TextChangeListener;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.TextField;

public class ParametersForm extends FormLayout {


	@PropertyId("ip")
	private TextField ip = new TextField("Ip address:");

	@PropertyId("test_duration")
	private TextField test_duration = new TextField("Test duration (secs):");		//test_duration
	
	@PropertyId("monitoring_interval")
	private TextField monitoring_interval = new TextField("Monitoring interval (secs):");
	
	@PropertyId("mean_user_think_time")
	private TextField mean_user_think_time = new TextField("Global think time (secs):");
	
	@PropertyId("standard_deviation_list")
	private TextField standard_deviation = new TextField("Standard deviation (secs):");
	
	@PropertyId("ramp_list")
	private TextField ramp_list = new TextField("Ramp list:");
	
//	@PropertyId("target_response_times")
//	private TextField target_response_times = new TextField("Target Response Time");

	
	
	public ParametersForm() {
		this.setMargin(true);
//		this.setSpacing(true);

		ip.addValidator(new BeanValidator(Parameters.class, "ip"));
		test_duration.addValidator(new BeanValidator(Parameters.class, "test_duration"));
//		ramp_list.addValidator(new BeanValidator(Parameters.class, "ramp_list"));
//		TargetResponseTime.addValidator(new BeanValidator(Parameters.class, "TargetResponseTime"));
//		monitoring_interval.addValidator(new BeanValidator(Parameters.class, "monitoring_interval"));
//		mean_user_think_time.addValidator(new BeanValidator(Parameters.class, "mean_user_think_time"));
//		standard_deviation.addValidator(new BeanValidator(Parameters.class, "standard_deviation"));
		
//		ip.setNullRepresentation("");
//		test_duration.setNullRepresentation("");
//		ramp_list.setNullRepresentation("");
//		TargetResponseTime.setNullRepresentation("");
//		monitoring_interval.setNullRepresentation("");
//		mean_user_think_time.setNullRepresentation("");
//		standard_deviation.setNullRepresentation("");
		
//		ip.setCaption("ip address:");
//		test_duration.setCaption("Test duration (secs):");
//		monitoring_interval.setCaption("Monitoring interval (secs):");
//		mean_user_think_time.setCaption("Global think time (secs):");
//		standard_deviation.setCaption("Standard deviation (secs):");
		
		ramp_list.setCaption("Ramp list:");
//		TargetResponseTime.setCaption("Target response time:");
		
		disableValidationMessages();


		addComponent(ip);
		addComponent(test_duration);
		addComponent(monitoring_interval);
		addComponent(mean_user_think_time);
		addComponent(standard_deviation);
		addComponent(ramp_list);
//		addComponent(TargetResponseTime);
		
		for (Component c : this.components) {
			c.addStyleName("tiny");
		}
	}
	
	public void disableValidationMessages() {
		ip.setValidationVisible(false);
		test_duration.setValidationVisible(false);
		monitoring_interval.setValidationVisible(false);
		mean_user_think_time.setValidationVisible(false);
		standard_deviation.setValidationVisible(false);
		ramp_list.setValidationVisible(false);
//		TargetResponseTime.setValidationVisible(false);

	}
	
	public void enableValidationMessages() {
		ip.setValidationVisible(true);
		test_duration.setValidationVisible(true);
		monitoring_interval.setValidationVisible(true);
		mean_user_think_time.setValidationVisible(true);
		standard_deviation.setValidationVisible(true);
		ramp_list.setValidationVisible(true);
//		TargetResponseTime.setValidationVisible(true);
	}

	
	
//	ip
//	test_duration
//	ramp_list
//	TargetResponseTime
//	monitoring_interval
//	mean_user_think_time
//	standard_deviation

}
