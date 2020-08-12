/*
 * javahexeditor, a java hex editor
 * Copyright (C) 2006, 2009 Jordi Bergenthal, pestatije(-at_)users.sourceforge.net
 * The official javahexeditor site is sourceforge.net/projects/javahexeditor
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
package net.sourceforge.javahexeditor;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.*;

/**
 * Central factory to track creation of RandomAccessFile instance.
 *
 * @author Peter Dell
 */
public final class RandomAccessFileFactory {

	private static final List<RandomAccessFile> instances = new ArrayList<RandomAccessFile>(3);

	public static RandomAccessFile createRandomAccessFile(final File file, final String mode) throws FileNotFoundException {
		RandomAccessFile raf = new RandomAccessFile(file, mode) {
			@Override
			public void close() throws IOException {
				super.close();
				synchronized (instances) {
					instances.remove(this);
				}
				log("Closed random access file for '" + file.getAbsolutePath() + "' in mode '" + mode + "'");
				Thread.dumpStack();

			}
		};
		synchronized (instances) {
			instances.add(raf);
		}
		log("Created random access file for '" + file.getAbsolutePath() + "' in mode '" + mode + "'");
		Thread.dumpStack();
		return raf;
	}

	public static void log(String message) {
		System.out.println("RandomAccessFileFactory: " + message);
	}
}
