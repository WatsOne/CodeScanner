package org.kulikov.codescanner;

import com.orm.SugarRecord;

public class BarCodeEntity extends SugarRecord<BarCodeEntity> {

    private String barCodeResult;

    public BarCodeEntity() {
    }

    public BarCodeEntity(String barCodeResult) {
        this.barCodeResult = barCodeResult;
    }

    public String getBarCodeResult() {
        return barCodeResult;
    }
}
