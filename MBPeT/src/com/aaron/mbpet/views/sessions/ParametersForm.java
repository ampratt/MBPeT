package com.aaron.mbpet.views.sessions;

import com.aaron.mbpet.domain.Parameters;
import com.aaron.mbpet.domain.TestSession;
import com.vaadin.data.fieldgroup.PropertyId;
import com.vaadin.data.validator.BeanValidator;
import com.vaadin.ui.Component;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.TextField;

public class ParametersForm extends FormLayout {


	@PropertyId("ip")
	private TextField ip = new TextField("ip");

	@PropertyId("test_duration")
	private TextField test_duration = new TextField("test_duration");

	@PropertyId("ramp_list")
	private TextField ramp_list = new TextField("ramp_list");
	
	@PropertyId("TargetResponseTime")
	private TextField TargetResponseTime = new TextField("TargetResponseTime");
	
	@PropertyId("monitoring_interval")
	private TextField monitoring_interval = new TextField("monitoring_interval");
	
	@PropertyId("mean_user_think_time")
	private TextField mean_user_think_time = new TextField("mean_user_think_time");
	
	@PropertyId("rampstandard_deviation_list")
	private TextField standard_deviation = new TextField("standard_deviation");

	public ParametersForm() {
		this.setMargin(true);
		this.setSpacing(true);

		ip.addValidator(new BeanValidator(Parameters.class, "ip"));
		test_duration.addValidator(new BeanValidator(Parameters.class, "test_duration"));
		ramp_list.addValidator(new BeanValidator(Parameters.class, "ramp_list"));
		TargetResponseTime.addValidator(new BeanValidator(Parameters.class, "TargetResponseTime"));
		monitoring_interval.addValidator(new BeanValidator(Parameters.class, "monitoring_interval"));
		mean_user_think_time.addValidator(new BeanValidator(Parameters.class, "mean_user_think_time"));
		standard_deviation.addValidator(new BeanValidator(Parameters.class, "standard_deviation"));
		
		ip.setNullRepresentation("");
		test_duration.setNullRepresentation("");
		ramp_list.setNullRepresentation("");
		TargetResponseTime.setNullRepresentation("");
		monitoring_interval.setNullRepresentation("");
		mean_user_think_time.setNullRepresentation("");
		standard_deviation.setNullRepresentation("");
		
		disableValidationMessages();


		addComponent(ip);
		addComponent(test_duration);
//		addComponent(ramp_list);
//		addComponent(TargetResponseTime);
		addComponent(monitoring_interval);
		addComponent(mean_user_think_time);
		addComponent(standard_deviation);
		
		for (Component c : this.components) {
			c.addStyleName("tiny");
		}
	}
	
	public void disableValidationMessages() {
		ip.setValidationVisible(false);
		test_duration.setValidationVisible(false);
		ramp_list.setValidationVisible(false);
		TargetResponseTime.setValidationVisible(false);
		monitoring_interval.setValidationVisible(false);
		mean_user_think_time.setValidationVisible(false);
		standard_deviation.setValidationVisible(false);

	}
	
	public void enableValidationMessages() {
		ip.setValidationVisible(true);
		test_duration.setValidationVisible(true);
		ramp_list.setValidationVisible(true);
		TargetResponseTime.setValidationVisible(true);
		monitoring_interval.setValidationVisible(true);
		mean_user_think_time.setValidationVisible(true);
		standard_deviation.setValidationVisible(true);
	}
	
//	ip
//	test_duration
//	ramp_list
//	TargetResponseTime
//	monitoring_interval
//	mean_user_think_time
//	standard_deviation

}
