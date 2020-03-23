package org.jasperge.mpq;

import static org.jasperge.sfmpq.SFMPQ.*;

public enum WaveQuality {
    LOW(MAWA_QUALITY_LOW),
    MEDIUM(MAWA_QUALITY_MEDIUM),
    HIGH(MAWA_QUALITY_HIGH);

    public final int value;

    WaveQuality(int value) {
        this.value = value;
    }
}
