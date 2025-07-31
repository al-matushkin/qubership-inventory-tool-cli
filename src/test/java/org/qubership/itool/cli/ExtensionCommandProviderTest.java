/*
 * Copyright 2024-2025 NetCracker Technology Corporation
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.qubership.itool.cli;

import org.junit.jupiter.api.Test;
import java.util.ServiceLoader;
import java.util.concurrent.Callable;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test for the ExtensionCommandProvider SPI mechanism.
 */
public class ExtensionCommandProviderTest {

    @Test
    public void testExtensionCommandProviderInterface() {
        // Test that the interface can be implemented
        ExtensionCommandProvider provider = new TestExtensionCommandProvider();

        assertNotNull(provider);
        assertEquals("test-command", provider.getCommandName());

        Callable<Integer> command = provider.createCommand();
        assertNotNull(command);
    }

    @Test
    public void testServiceLoaderDiscovery() {
        // Test that ServiceLoader can discover extension providers
        ServiceLoader<ExtensionCommandProvider> providers =
                ServiceLoader.load(ExtensionCommandProvider.class);

        assertNotNull(providers);

        // Count discovered providers (should be 0 in test environment)
        int providerCount = 0;
        for (ExtensionCommandProvider provider : providers) {
            providerCount++;
            assertNotNull(provider);
            assertNotNull(provider.getCommandName());
            assertNotNull(provider.createCommand());
        }

        // In test environment, no providers should be found
        assertEquals(0, providerCount,
                "No extension providers should be found in test environment");
    }

    /**
     * Test implementation of ExtensionCommandProvider for testing purposes.
     */
    private static class TestExtensionCommandProvider implements ExtensionCommandProvider {

        @Override
        public Callable<Integer> createCommand() {
            return new TestCommand();
        }

        @Override
        public String getCommandName() {
            return "test-command";
        }
    }

    /**
     * Test command implementation for testing purposes.
     */
    private static class TestCommand implements Callable<Integer> {

        @Override
        public Integer call() throws Exception {
            return 0;
        }
    }
}
