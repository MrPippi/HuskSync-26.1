/*
 * This file is part of HuskSync, licensed under the Apache License 2.0.
 *
 *  Copyright (c) William278 <will27528@gmail.com>
 *  Copyright (c) contributors
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package space.arim.morepaperlib.scheduling;

/**
 * Patched version of StandardFoliaDetection for Java 25 compatibility.
 *
 * <p>On Java 25, calling {@link Class#forName(String)} inside a plugin's static
 * initializer can throw {@link IllegalStateException} ("zip file closed") because
 * the {@link java.util.zip.ZipFile} backing Paper's {@code PluginClassLoader} may
 * already be closed by the time the static block executes.  The upstream version
 * only catches {@link ClassNotFoundException}, so the exception escapes and the
 * class is permanently poisoned, breaking all scheduler calls.
 *
 * <p>This replacement adds a {@code Throwable} catch that retries the lookup
 * through the server class loader (which is not subject to the plugin-JAR
 * lifecycle), so Folia detection works correctly on all supported JVM versions.
 */
final class StandardFoliaDetection implements FoliaDetection {

    private static final boolean IS_FOLIA;
    static final StandardFoliaDetection INSTANCE = new StandardFoliaDetection();

    static {
        boolean isFolia;
        try {
            Class.forName("io.papermc.paper.threadedregions.RegionizedServer");
            isFolia = true;
        } catch (ClassNotFoundException ignored) {
            isFolia = false;
        } catch (Throwable ignored) {
            // Java 25+: plugin class loader's ZipFile may be closed at static-init time.
            // Retry via the server class loader, which remains open for the server lifetime.
            isFolia = detectViaServerClassLoader();
        }
        IS_FOLIA = isFolia;
    }

    private static boolean detectViaServerClassLoader() {
        try {
            ClassLoader serverClassLoader = org.bukkit.Bukkit.class.getClassLoader();
            Class.forName("io.papermc.paper.threadedregions.RegionizedServer", false, serverClassLoader);
            return true;
        } catch (Throwable ignored) {
            return false;
        }
    }

    private StandardFoliaDetection() {}

    @Override
    public boolean isUsingFolia() {
        return IS_FOLIA;
    }

}
