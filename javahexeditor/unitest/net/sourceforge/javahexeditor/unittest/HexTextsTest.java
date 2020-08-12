package net.sourceforge.javahexeditor.unittest;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyleRange;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

import junit.framework.TestCase;
import net.sourceforge.javahexeditor.BinaryContent;
import net.sourceforge.javahexeditor.HexTexts;

public final class HexTextsTest extends TestCase {

	private BinaryContent content;
	private Display display;
	private Shell shell;
	private HexTexts hexTexts;

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		display = Display.getDefault();
		shell = new Shell(Display.getDefault(), SWT.MODELESS | SWT.SHELL_TRIM);
		hexTexts = new HexTexts(shell, SWT.DEFAULT);
		content = new BinaryContent();
		content.insert(ByteBuffer.allocate(200), 0L);
		hexTexts.setContentProvider(content);
	}

	@Override
	protected void tearDown() throws Exception {
		content.dispose();
		hexTexts.dispose();
		shell.dispose();
		display.dispose();
		super.tearDown();
	}

	/*
	 * Test method for
	 * 'net.sourceforge.javahexeditor.HexTexts.mergeRanges(ArrayList, int)'
	 */
	public void testMergeRanges() {
		List<Long> changes = new ArrayList<Long>();
		List<Integer> highlights = new ArrayList<Integer>();
		List<StyleRange> merged = hexTexts.mergeRanges(changes, highlights); // _
		// _
		// _
		// _
		assertTrue(merged == null || merged.isEmpty());

		changes.add(Long.valueOf(0));
		changes.add(Long.valueOf(1));
		merged = hexTexts.mergeRanges(changes, highlights); // C _ _ _
		assertEquals(1, merged.size());
		assertEquals(0, (merged.get(0)).start);
		assertEquals(1, (merged.get(0)).length);

		changes.set(0, Long.valueOf(1));
		changes.set(1, Long.valueOf(2));
		merged = hexTexts.mergeRanges(changes, highlights); // _ C C _
		assertEquals(1, merged.size());
		assertEquals(1, (merged.get(0)).start);
		assertEquals(2, (merged.get(0)).length);

		changes.clear();
		highlights.add(Integer.valueOf(0));
		highlights.add(Integer.valueOf(1));
		merged = hexTexts.mergeRanges(changes, highlights); // H _ _ _
		assertEquals(1, merged.size());
		assertEquals(0, (merged.get(0)).start);
		assertEquals(1, (merged.get(0)).length);

		highlights.set(0, Integer.valueOf(1));
		highlights.set(1, Integer.valueOf(2));
		merged = hexTexts.mergeRanges(changes, highlights); // _ H H _
		assertEquals(1, merged.size());
		assertEquals(1, (merged.get(0)).start);
		assertEquals(2, (merged.get(0)).length);

		changes.add(Long.valueOf(0));
		changes.add(Long.valueOf(1));
		merged = hexTexts.mergeRanges(changes, highlights); // C H H _
		assertEquals(2, merged.size());
		assertEquals(0, (merged.get(0)).start);
		assertEquals(1, (merged.get(0)).length);
		assertEquals(1, (merged.get(1)).start);
		assertEquals(2, (merged.get(1)).length);

		changes.set(0, Long.valueOf(3));
		changes.set(1, Long.valueOf(1));
		merged = hexTexts.mergeRanges(changes, highlights); // _ H H C _
		assertEquals(2, merged.size());
		assertEquals(1, (merged.get(0)).start);
		assertEquals(2, (merged.get(0)).length);
		assertEquals(3, (merged.get(1)).start);
		assertEquals(1, (merged.get(1)).length);

		changes.set(0, Long.valueOf(4));
		changes.set(1, Long.valueOf(2));
		merged = hexTexts.mergeRanges(changes, highlights); // _ H H _ C C _
		assertEquals(2, merged.size());
		assertEquals(1, (merged.get(0)).start);
		assertEquals(2, (merged.get(0)).length);
		assertEquals(4, (merged.get(1)).start);
		assertEquals(2, (merged.get(1)).length);

		changes.set(0, Long.valueOf(1));
		changes.set(1, Long.valueOf(2));
		merged = hexTexts.mergeRanges(changes, highlights); // _ CH CH _
		assertEquals(1, merged.size());
		assertEquals(1, (merged.get(0)).start);
		assertEquals(2, (merged.get(0)).length);

		changes.set(0, Long.valueOf(1));
		changes.set(1, Long.valueOf(1));
		merged = hexTexts.mergeRanges(changes, highlights); // _ CH H _
		assertEquals(2, merged.size());
		assertEquals(1, (merged.get(0)).start);
		assertEquals(1, (merged.get(0)).length);
		assertEquals(2, (merged.get(1)).start);
		assertEquals(1, (merged.get(1)).length);

		changes.set(0, Long.valueOf(2));
		changes.set(1, Long.valueOf(1));
		merged = hexTexts.mergeRanges(changes, highlights); // _ H CH _
		assertEquals(2, merged.size());
		assertEquals(1, (merged.get(0)).start);
		assertEquals(1, (merged.get(0)).length);
		assertEquals(2, (merged.get(1)).start);
		assertEquals(1, (merged.get(1)).length);

		changes.set(0, Long.valueOf(2));
		changes.set(1, Long.valueOf(2));
		merged = hexTexts.mergeRanges(changes, highlights); // _ H CH C _
		assertEquals(3, merged.size());
		assertEquals(1, (merged.get(0)).start);
		assertEquals(1, (merged.get(0)).length);
		assertEquals(2, (merged.get(1)).start);
		assertEquals(1, (merged.get(1)).length);
		assertEquals(3, (merged.get(2)).start);
		assertEquals(1, (merged.get(2)).length);

		highlights.set(1, Integer.valueOf(4));
		merged = hexTexts.mergeRanges(changes, highlights); // _ H CH CH H _
		assertEquals(3, merged.size());
		assertEquals(1, (merged.get(0)).start);
		assertEquals(1, (merged.get(0)).length);
		assertEquals(2, (merged.get(1)).start);
		assertEquals(2, (merged.get(1)).length);
		assertEquals(4, (merged.get(2)).start);
		assertEquals(1, (merged.get(2)).length);

		highlights.set(0, Integer.valueOf(2));
		merged = hexTexts.mergeRanges(changes, highlights); // _ _ CH CH H H _
		assertEquals(2, merged.size());
		assertEquals(2, (merged.get(0)).start);
		assertEquals(2, (merged.get(0)).length);
		assertEquals(4, (merged.get(1)).start);
		assertEquals(2, (merged.get(1)).length);
	}
}
