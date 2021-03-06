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
package net.sourceforge.javahexeditor.unittest;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;

import junit.framework.Test;
import junit.framework.TestSuite;
import net.sourceforge.javahexeditor.RandomAccessFileFactory;

public class AllTests {

	static final String resourceData = "TestsData.hex";
	static final String resourceLongData = "TestsLongData.hex";
	static final String resourceUnicode = "TestsUnicode.hex";

	public static void main(String[] args) {
		junit.textui.TestRunner.run(AllTests.suite());
	}

	/**
	 * Helper method that creates a long test file
	 *
	 * @param size
	 * @return a test file of size size or null on error
	 */
	public static File setUpLongData(long size) {
		File longFile = new File(AllTests.class.getResource(resourceLongData).getPath());
		try {
			RandomAccessFile file = RandomAccessFileFactory.createRandomAccessFile(longFile, "rws");
			file.setLength(size);
			file.close();
		} catch (IOException e) {

			return null;
		}

		return longFile;
	}

	public static Test suite() {
		TestSuite suite = new TestSuite("Test for net.sourceforge.javahexeditor");

		// Note: gcj 4.4.0 won't let to memory-map twice the same file in the same
		// virtual machine.
		// BinaryContentTest and FinderTest both map TestsLongData.hex so if run
		// from gcj the test
		// crashes. Workaround: test first one, then the other.
		// Plus file TestsLongData.hex should be with length 0.
		// Plus gcc with text junit give an initializer exception (can be
		// ignored).
		// $JUnit-BEGIN$
		suite.addTestSuite(BinaryContentTest.class);
		suite.addTestSuite(HexTextsTest.class);
		suite.addTestSuite(UndoRedoTest.class);
		suite.addTestSuite(FinderTest.class);
		// $JUnit-END$
		return suite;
	}

	/**
	 * Helper method to tear down the file created in setUpLongData()
	 */
	public static void tearDownLongData() {
		setUpLongData(0);
	}
}
