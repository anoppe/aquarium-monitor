package nl.noppe.auke.aquarium.metrics;

import java.util.Date;

public interface Metrics {

	String getId();

	void setId(String id);

	Date getOccuredDatetime();

	void setOccuredDatetime(Date occuredDatetime);
}
