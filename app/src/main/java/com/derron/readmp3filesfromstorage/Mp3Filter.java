package com.derron.readmp3filesfromstorage;

import java.io.File;
import java.io.FilenameFilter;

class Mp3Filter implements FilenameFilter {
    @Override
    public boolean accept(File file, String name) {
        return name.endsWith(".mp3");
    }
}
