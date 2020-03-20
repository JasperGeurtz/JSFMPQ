package org.jasperge.mpq;

import com.sun.jna.Pointer;

import java.util.List;
import java.util.stream.Collectors;

import static org.jasperge.sfmpq.SFMPQ.*;

public class MPQEditor {
    final SFMPQWrapper sfmpq = new SFMPQWrapper();
    Pointer archive;

    /**
     * fails if doesn't exist
     */
    public MPQEditor(String filePath) {
        // last param ignored anyway
        archive = sfmpq.openArchiveForUpdate(filePath, MOAU_OPEN_EXISTING, 262144);
    }

    /**
     * Add file to Archive: WILL REPLACE!
     */
    boolean addFile(String sourceFileName, String destFileName) {
        return sfmpq.addFileToArchive(archive, sourceFileName, destFileName, MAFA_REPLACE_EXISTING);
    }

    List<MPQFile> getFiles() {
        return sfmpq.listFiles(archive).stream()
                .map(MPQFile::new)
                .collect(Collectors.toList());
    }

    boolean close() {
        return sfmpq.closeUpdatedArchive(archive) == 1;
    }

}
