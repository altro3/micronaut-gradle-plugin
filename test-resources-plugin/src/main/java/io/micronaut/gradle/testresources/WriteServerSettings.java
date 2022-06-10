/*
 * Copyright 2003-2021 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.micronaut.gradle.testresources;

import org.gradle.api.DefaultTask;
import org.gradle.api.file.DirectoryProperty;
import org.gradle.api.provider.Property;
import org.gradle.api.tasks.Input;
import org.gradle.api.tasks.Optional;
import org.gradle.api.tasks.OutputDirectory;
import org.gradle.api.tasks.TaskAction;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;

public abstract class WriteServerSettings extends DefaultTask {
    private static final String SERVER_URI = "server.uri";
    private static final String SERVER_ACCESS_TOKEN = "server.access.token";
    private static final String SERVER_CLIENT_READ_TIMEOUT = "server.client.read.timeout";

    @OutputDirectory
    public abstract DirectoryProperty getOutputDirectory();

    @Input
    @Optional
    public abstract Property<String> getToken();

    @Input
    public abstract Property<Integer> getPort();

    @Input
    @Optional
    public abstract Property<Integer> getClientTimeout();

    @TaskAction
    void writeProperties() throws FileNotFoundException {
        File propertiesFile = getOutputDirectory().file("test-resources.properties").get().getAsFile();
        try (PrintWriter prn = new PrintWriter(new FileOutputStream(propertiesFile))) {
            prn.println(SERVER_URI + "=http\\://localhost\\:" + getPort().get());
            if (getToken().isPresent()) {
                prn.println(SERVER_ACCESS_TOKEN + "=" + getToken().get());
            }
            if (getClientTimeout().isPresent()) {
                prn.println(SERVER_CLIENT_READ_TIMEOUT + "=" + getClientTimeout().get());
            }
        }
    }
}
