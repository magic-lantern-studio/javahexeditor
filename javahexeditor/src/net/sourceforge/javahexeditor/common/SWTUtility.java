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

package net.sourceforge.javahexeditor.common;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.graphics.FontMetrics;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;

/**
 * Utility class to handle SWT widgets.
 *
 * @author Peter Dell
 */
public final class SWTUtility {

	/**
	 * Blocks the caller until the task is finished. Does not block the user
	 * interface thread.
	 *
	 * @param task
	 *            independent of the user interface thread (no widgets used)
	 */
	public static void blockUntilFinished(Runnable task) {
		Thread thread = new Thread(task);
		thread.start();
		Display display = Display.getCurrent();
		final boolean[] pollerEnabled = { false };
		while (thread.isAlive() && !display.isDisposed()) {
			if (!display.readAndDispatch()) {
				// awake periodically so it returns when task has finished
				if (!pollerEnabled[0]) {
					pollerEnabled[0] = true;
					display.timerExec(300, new Runnable() {
						@Override
						public void run() {
							pollerEnabled[0] = false;
						}
					});
				}
				display.sleep();
			}
		}
	}

	/**
	 * Helper method to make a center a shell or dialog in the center of another
	 * shell.
	 *
	 * * @param movingShell shell to be relocated, not <code>null</code>
	 * 
	 * @param fixedShell
	 *            shell to be used as reference, not <code>null</code>
	 * 
	 */
	public static void placeInCenterOf(Shell movingShell, Shell fixedShell) {
		if (movingShell == null) {
			throw new IllegalArgumentException("Parameter 'movingShell' must not be null.");
		}
		if (fixedShell == null) {
			throw new IllegalArgumentException("Parameter 'fixedShell' must not be null.");
		}

		movingShell.pack();

		Rectangle fixedShellSize = fixedShell.getBounds();
		Rectangle dialogSize = movingShell.getBounds();

		int locationX, locationY;
		locationX = (fixedShellSize.width - dialogSize.width) / 2 + fixedShellSize.x;
		locationY = (fixedShellSize.height - dialogSize.height) / 2 + fixedShellSize.y;

		movingShell.setLocation(new Point(locationX, locationY));
	}

	public static int showMessage(Shell shell, int style, String title, String message, String... parameters) {
		MessageBox messageBox = new MessageBox(shell, style);
		messageBox.setText(title);
		messageBox.setMessage(TextUtility.format(message, parameters));
		return messageBox.open();
	}

	public static int showErrorMessage(Shell shell, String title, String message, String... parameters) {
		return showMessage(shell, SWT.ERROR | SWT.OK, title, message, parameters);

	}

	/**
	 * Compatibility between old and new SWT versions.
	 * 
	 * @param gc
	 *            The graphics context, not <code>null</code>
	 * @return The average character width, a positive integer.
	 */
	public static double getAverageCharacterWidth(GC gc) {
		if (gc == null) {
			throw new IllegalArgumentException();
		}
		FontMetrics fm = gc.getFontMetrics();
		Method method = getMethod(FontMetrics.class, "getAverageCharacterWidth");
		if (method != null) {

			Double result = null;
			try {
				result = (Double) method.invoke(fm);
			} catch (IllegalAccessException e) {
				throw new RuntimeException(e);
			} catch (IllegalArgumentException e) {
				throw new RuntimeException(e);
			} catch (InvocationTargetException e) {
				throw new RuntimeException(e);
			}
			return result.doubleValue();
		}

		method = getMethod(FontMetrics.class, "getAverageCharWidth");
		Integer result = null;
		try {
			result = (Integer) method.invoke(fm);
		} catch (IllegalAccessException e) {
			throw new RuntimeException(e);
		} catch (IllegalArgumentException e) {
			throw new RuntimeException(e);
		} catch (InvocationTargetException e) {
			throw new RuntimeException(e);
		}
		return result.doubleValue();
	}

	/**
	 * Compatibility between old and new SWT versions.
	 * 
	 * @param styledText
	 *            The styled text, not <code>null</code>
	 * @param point
	 *            The point, not <code>null</code>
	 * @return The offset at location point.
	 */
	public static int getOffsetAtPoint(StyledText styledText, Point point) {
		if (styledText == null) {
			throw new IllegalArgumentException();
		}
		Method method = getMethod(StyledText.class, "getOffsetAtPoint");
		if (method == null) {
			method = getMethod(StyledText.class, "getOffsetAtLocation");
		}
		Integer result = null;
		try {
			result = (Integer) method.invoke(styledText);
		} catch (IllegalAccessException e) {
			throw new RuntimeException(e);
		} catch (IllegalArgumentException e) {
			throw new RuntimeException(e);
		} catch (InvocationTargetException e) {
			throw new RuntimeException(e);
		}
		return result.intValue();
	}

	private static Method getMethod(Class<?> clazz, String methodName) {
		Method method = null;
		try {
			method = clazz.getMethod(methodName);
		} catch (NoSuchMethodException ex1) {
			method = null;
		}
		return method;
	}
}
