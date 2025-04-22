package io.github.imecuadorian.cidfae.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Specimen {
    private int specimenId;
    private double maxForce;
    private double elasticModulus;
    private int batchId;

    public Specimen(double maxForce, double elasticModulus, int batchId) {
        this.maxForce = maxForce;
        this.elasticModulus = elasticModulus;
        this.batchId = batchId;
    }
}
