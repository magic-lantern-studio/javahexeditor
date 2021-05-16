/*
 * javahexeditor, a java hex editor
 * Copyright (C) 2006, 2009 Jordi Bergenthal, pestatije(-at_)users.sourceforge.net
 * Copyright (C) 2018 - 2021 Peter Dell, peterdell(-at_)users.sourceforge.net
 * The official javahexeditor site is https://sourceforge.net/projects/javahexeditor
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 675 Mass Ave, Cambridge, MA 02139, USA.
 */

package net.sourceforge.javahexeditor.common;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;

/**
 * Utility class to access resources in the class path.
 *
 * @author Peter Dell
 */
public final class ResourceUtility {

	/**
	 * Loads a resource as string.
	 *
	 * @param path
	 *            The resource path, not empty, not <code>null</code>.
	 * @return The resource content or <code>null</code> if the resource was not
	 *         found.
	 */
	public static String loadResourceAsString(String path) {
		if (path == null) {
			throw new IllegalArgumentException("Parameter 'path' must not be null.");
		}
		if (path.isEmpty()) {
			throw new IllegalArgumentException("Parameter 'path' must not be empty.");
		}
		final InputStream inputStream = getInputStream(path);
		if (inputStream == null) {
			return null;
		}
		StringBuilder builder = new StringBuilder();
		try {
			InputStreamReader reader = new InputStreamReader(inputStream);
			char[] buffer = new char[8192];
			int actualLength;
			while ((actualLength = reader.read(buffer, 0, buffer.length)) != -1) {
				builder.append(buffer, 0, actualLength);
			}
			reader.close();

		} catch (IOException ex) {
			Log.logError("Cannot load resource '{0}'.", new Object[] { path }, ex);
		} finally {

			try {
				inputStream.close();
			} catch (IOException ignore) {
			}
		}
		return builder.toString();
	}

	/**
	 * Self implemented logic to bypass the bug described in
	 * <a href="https://bugs.sun.com/view_bug.do?bug_id=4523159">JDK-4523159 :
	 * getResourceAsStream on jars in path with "!"</a>. Note that this is not the
	 * full logic. The rest was removed to reduced dependencies.
	 *
	 * @param path
	 *            The path of the resource to load, not <code>null</code>.
	 * @return The input stream or <code>null</code> if the source was not found.
	 */
	private static InputStream getInputStream(String path) {
		if (path == null) {
			throw new IllegalArgumentException("Parameter 'path' must not be null.");
		}
		// If there is no loader, the program was launched using the Java
		// boot class path and the system class loader must be used.
		ClassLoader loader = ResourceUtility.class.getClassLoader();
		URL url = (loader == null) ? ClassLoader.getSystemResource(path) : loader.getResource(path);
		InputStream result = null;
		try {
			if (url != null) {
				result = url.openStream();
			}
		} catch (IOException ex) {
			Log.logError("Cannot get input stream for path '{0}'", new Object[] { path }, ex);
		}
		return result;
	}
}
