package nl.noppe.auke.aquarium.metrics;

import java.util.Date;

public interface Metrics {

	Long getId();

	void setId(Long id);

	Date getOccuredDatetime();

	void setOccuredDatetime(Date occuredDatetime);
}
