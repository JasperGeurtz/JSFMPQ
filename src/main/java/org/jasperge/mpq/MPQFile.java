package org.jasperge.mpq;

import org.jasperge.sfmpq.FILELISTENTRY;

public class MPQFile {
    public String name;
    public FILELISTENTRY entry;

    MPQFile(FILELISTENTRY entry) {
        this.entry = entry;
        this.name = new String(entry.szFileName).trim();
    }
}
