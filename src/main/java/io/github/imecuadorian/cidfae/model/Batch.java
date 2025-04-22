package io.github.imecuadorian.cidfae.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.*;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Batch {
    private int batchId;
    private String workOrder;
    private String batchTitle;
    private LocalDate testDate;
    private int fiberLayerCount;
    private String maintenanceType;
    private String orientation;
    private String testType;
    private String resinType;
    private String fiberType;
    private String curingDetails;
    private List<Specimen> specimens;

    public Batch(String workOrder, String batchTitle) {
        this.workOrder = workOrder;
        this.batchTitle = batchTitle;
    }

    public int getSpecimenCount() {
        return (specimens != null) ? specimens.size() : 0;
    }
}
