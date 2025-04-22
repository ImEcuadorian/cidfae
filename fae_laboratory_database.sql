create table Batches
(
    batch_id          int auto_increment
        primary key,
    work_order        varchar(50)                                         not null,
    test_date         date                                                not null,
    fiber_layer_count int                                                 not null,
    maintenance_type  enum ('manufacturing', 'repair')                    not null,
    orientation       varchar(100)                                        not null,
    test_type         enum ('tension', 'compression', 'bending', 'shear') not null,
    resin_type        varchar(100)                                        not null,
    curing_details    text                                                not null,
    fiber_type        varchar(100)                                        not null,
    batch_title       varchar(255)                                        not null,
    specimen_count    int default 0                                       null,
    constraint work_order
        unique (work_order)
);

create definer = root@`%` trigger prevent_manual_specimen_count_update
    before update
    on Batches
    for each row
BEGIN
    IF OLD.specimen_count <> NEW.specimen_count THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Error: You cannot manually modify the number of specimens';
    END IF;
END;

create table Results
(
    result_id               int auto_increment
        primary key,
    batch_id                int   not null,
    average_max_force       float not null,
    average_elastic_modulus float not null,
    constraint batch_id
        unique (batch_id),
    constraint Results_ibfk_1
        foreign key (batch_id) references Batches (batch_id)
            on delete cascade
);

create table Specimens
(
    specimen_id     int auto_increment
        primary key,
    batch_id        int   not null,
    max_force       float not null,
    elastic_modulus float not null,
    constraint Specimens_ibfk_1
        foreign key (batch_id) references Batches (batch_id)
            on delete cascade
);

create index batch_id
    on Specimens (batch_id);

create definer = root@`%` trigger update_results_after_delete
    after delete
    on Specimens
    for each row
BEGIN
    IF (SELECT COUNT(*) FROM Specimens WHERE batch_id = OLD.batch_id) > 0 THEN
        UPDATE Results 
        SET average_max_force = (SELECT AVG(max_force) FROM Specimens WHERE batch_id = OLD.batch_id),
            average_elastic_modulus = (SELECT AVG(elastic_modulus) FROM Specimens WHERE batch_id = OLD.batch_id)
        WHERE batch_id = OLD.batch_id;
    ELSE
        DELETE FROM Results WHERE batch_id = OLD.batch_id;
    END IF;
END;

create definer = root@`%` trigger update_results_after_insert
    after insert
    on Specimens
    for each row
BEGIN
    INSERT INTO Results (batch_id, average_max_force, average_elastic_modulus)
    SELECT NEW.batch_id, AVG(max_force), AVG(elastic_modulus)
    FROM Specimens
    WHERE batch_id = NEW.batch_id
    GROUP BY batch_id
    ON DUPLICATE KEY UPDATE 
        average_max_force = VALUES(average_max_force),
        average_elastic_modulus = VALUES(average_elastic_modulus);
END;

create definer = root@`%` trigger update_results_after_insert_duplicate
    after insert
    on Specimens
    for each row
BEGIN
    INSERT INTO Results (batch_id, average_max_force, average_elastic_modulus)
    SELECT NEW.batch_id, AVG(max_force), AVG(elastic_modulus)
    FROM Specimens
    WHERE batch_id = NEW.batch_id
    GROUP BY batch_id
    ON DUPLICATE KEY UPDATE 
        average_max_force = VALUES(average_max_force),
        average_elastic_modulus = VALUES(average_elastic_modulus);
END;

create definer = root@`%` trigger update_specimen_count_after_delete
    after delete
    on Specimens
    for each row
BEGIN
    UPDATE Batches
    SET specimen_count = GREATEST(specimen_count - 1, 0)
    WHERE batch_id = OLD.batch_id;
END;

create definer = root@`%` trigger update_specimen_count_after_insert
    after insert
    on Specimens
    for each row
BEGIN
    UPDATE Batches
    SET specimen_count = COALESCE(specimen_count, 0) + 1
    WHERE batch_id = NEW.batch_id;
END;

create definer = root@`%` trigger update_specimen_count_after_update
    after update
    on Specimens
    for each row
BEGIN
    IF OLD.batch_id <> NEW.batch_id THEN
        UPDATE Batches 
        SET specimen_count = GREATEST(specimen_count - 1, 0)  
        WHERE batch_id = OLD.batch_id;

        UPDATE Batches 
        SET specimen_count = COALESCE(specimen_count, 0) + 1  
        WHERE batch_id = NEW.batch_id;
    END IF;
END;

