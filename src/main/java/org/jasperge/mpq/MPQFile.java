package org.jasperge.mpq;

import org.jasperge.sfmpq.FILELISTENTRY;

public class MPQFile {
    public final String name;
    public final int locale; // Locale ID of file
    public final int compressedSize; // Compressed size of file
    public final int fullSize; // Uncompressed size of file
    public final int flags;

    MPQFile(FILELISTENTRY entry) {
        this.name = new String(entry.szFileName).trim();
        this.locale = entry.lcLocale;
        this.compressedSize = entry.dwCompressedSize;
        this.fullSize = entry.dwFullSize;
        this.flags = entry.dwFlags;
    }
}
