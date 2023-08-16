ALTER TABLE `airport`
    MODIFY COLUMN `location_point` POINT NOT NULL;
ALTER TABLE `ipscmatch`
    MODIFY COLUMN `location_point` POINT NOT NULL;

ALTER TABLE `airport`
    ADD SPATIAL INDEX `spatial_index_location_point` (`location_point`);
ALTER TABLE `ipscmatch`
    ADD SPATIAL INDEX `spatial_index_location_point` (`location_point`);

