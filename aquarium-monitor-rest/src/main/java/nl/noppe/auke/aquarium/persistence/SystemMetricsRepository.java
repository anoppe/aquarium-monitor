package nl.noppe.auke.aquarium.persistence;

import java.util.Date;
import java.util.List;

import nl.noppe.auke.aquarium.metrics.system.SystemMetrics;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Generic MongoDb {@link CrudRepository} for the {@link SystemMetrics} entity.
 * @version $Id$
 * @author aukenoppe
 *
 */
@Repository
public interface SystemMetricsRepository extends CrudRepository<SystemMetrics, String> {

	@Query("FROM SystemMetrics s where s.occuredDatetime > :from")
	List<SystemMetrics> findByDateTimeGreaterThan(@Param("from") Date from);
}
