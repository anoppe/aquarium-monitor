SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='TRADITIONAL,ALLOW_INVALID_DATES';

DROP SCHEMA IF EXISTS `mydb` ;
CREATE SCHEMA IF NOT EXISTS `mydb` DEFAULT CHARACTER SET latin1 COLLATE latin1_swedish_ci ;
DROP SCHEMA IF EXISTS `aquarium_monitor` ;
CREATE SCHEMA IF NOT EXISTS `aquarium_monitor` DEFAULT CHARACTER SET latin1 ;
USE `mydb` ;
USE `aquarium_monitor` ;

-- -----------------------------------------------------
-- Table `aquarium_monitor`.`tbl_aqua_metrics`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `aquarium_monitor`.`tbl_aqua_metrics` ;

CREATE  TABLE IF NOT EXISTS `aquarium_monitor`.`tbl_aqua_metrics` (
  `aqua_metrics_id` BIGINT(20) NOT NULL AUTO_INCREMENT ,
  `occured_datetime` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP ,
  `ph` DOUBLE NOT NULL DEFAULT '1' ,
  `temperature` DOUBLE NOT NULL DEFAULT '1' ,
  `current` DOUBLE NULL DEFAULT '1' ,
  PRIMARY KEY (`aqua_metrics_id`) ,
  UNIQUE INDEX `aqua_metrics_id_UNIQUE` (`aqua_metrics_id` ASC) )
ENGINE = InnoDB
AUTO_INCREMENT = 147
DEFAULT CHARACTER SET = latin1;


-- -----------------------------------------------------
-- Table `aquarium_monitor`.`tbl_system_metrics`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `aquarium_monitor`.`tbl_system_metrics` ;

CREATE  TABLE IF NOT EXISTS `aquarium_monitor`.`tbl_system_metrics` (
  `system_metrics_id` BIGINT(20) NOT NULL AUTO_INCREMENT ,
  `occured_datetime` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP ,
  `used_memory` DOUBLE NOT NULL DEFAULT '1' ,
  `free_memory` DOUBLE NOT NULL DEFAULT '1' ,
  `cpu_utilization` DOUBLE NOT NULL DEFAULT '1' ,
  `used_swap` DOUBLE NOT NULL DEFAULT '1' ,
  `available_swap` DOUBLE NOT NULL DEFAULT '1' ,
  PRIMARY KEY (`system_metrics_id`) ,
  UNIQUE INDEX `system_metrics_id_UNIQUE` (`system_metrics_id` ASC) )
ENGINE = InnoDB
AUTO_INCREMENT = 292
DEFAULT CHARACTER SET = latin1;



SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
