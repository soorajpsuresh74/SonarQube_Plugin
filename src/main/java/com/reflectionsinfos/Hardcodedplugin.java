package com.reflectionsinfos;

import com.reflectionsinfos.repository.HardcodedpluginRepository;
import org.sonar.api.Plugin;

public class Hardcodedplugin implements Plugin {
    @Override
    public void define(Context context) {
        context.addExtension(HardcodedpluginRepository.class);
    }
}
