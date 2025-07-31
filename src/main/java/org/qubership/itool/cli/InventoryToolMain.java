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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ServiceLoader;

/**
 * Main entry point for the inventory tool CLI application.
 *
 * MIGRATION STATUS: This is a placeholder for the Picocli migration.
 *
 * Current Status: - Vert.x CLI is being migrated to Picocli due to breaking changes in Vert.x 5.0.1
 * - Migration guide created: PICOCLI_MIGRATION_GUIDE.md - Proof of concept completed
 *
 * TODO: 1. Add Picocli dependency to pom.xml 2. Implement Picocli-based main class 3. Convert all
 * commands from Vert.x CLI to Picocli 4. Update Maven configuration
 *
 * For now, this delegates to the existing Vert.x launcher.
 */
public class InventoryToolMain {

    private static final Logger LOGGER = LoggerFactory.getLogger(InventoryToolMain.class);

    public static void main(String[] args) {
        LOGGER.info("=== Inventory Tool CLI ===");
        LOGGER.info("Migration Status: Vert.x CLI -> Picocli (in progress)");
        LOGGER.info("See PICOCLI_MIGRATION_GUIDE.md for details");

        if (args.length == 0) {
            LOGGER.info(
                    "Available commands: exec, query, ci-exec, ci-assembly, obfuscate, extract");
            LOGGER.info("Example: java -jar inventory-tool.jar exec -l your_login");
            System.exit(1);
        }

        // For now, delegate to existing Vert.x launcher
        // This will be replaced with Picocli implementation
        try {
            // Use reflection to avoid compilation issues during migration
            Class<?> launcherClass = Class.forName("io.vertx.core.Launcher");
            java.lang.reflect.Method mainMethod = launcherClass.getMethod("main", String[].class);
            mainMethod.invoke(null, (Object) args);
        } catch (Exception e) {
            LOGGER.error("Failed to delegate to Vert.x launcher", e);
            System.exit(1);
        }
    }

    /**
     * Discovers and registers extension commands using ServiceLoader. This method will be used when
     * Picocli is implemented.
     *
     * @param commandLine the CommandLine instance to add subcommands to
     */
    private static void discoverExtensionCommands(Object commandLine) {
        LOGGER.debug("Discovering extension commands...");

        try {
            ServiceLoader<ExtensionCommandProvider> extensionProviders =
                    ServiceLoader.load(ExtensionCommandProvider.class);

            int extensionCount = 0;
            for (ExtensionCommandProvider provider : extensionProviders) {
                try {
                    String commandName = provider.getCommandName();
                    Object extensionCommand = provider.createCommand();

                    LOGGER.debug("Found extension command: {}", commandName);

                    // TODO: When Picocli is implemented, add the command to CommandLine
                    // commandLine.addSubcommand(commandName, extensionCommand);

                    extensionCount++;
                } catch (Exception e) {
                    LOGGER.warn("Failed to load extension command from provider: {}",
                            provider.getClass().getName(), e);
                }
            }

            if (extensionCount > 0) {
                LOGGER.info("Loaded {} extension command(s)", extensionCount);
            } else {
                LOGGER.debug("No extension commands found");
            }

        } catch (Exception e) {
            LOGGER.warn("Failed to discover extension commands", e);
        }
    }

    /**
     * Gets the list of available commands for help display. This includes both core commands and
     * discovered extension commands.
     *
     * @return array of available command names
     */
    private static String[] getAvailableCommands() {
        // Core commands
        String[] coreCommands = {"exec", "query", "ci-exec", "ci-assembly", "obfuscate", "extract"};

        // TODO: When Picocli is implemented, also include discovered extension commands
        return coreCommands;
    }
}
