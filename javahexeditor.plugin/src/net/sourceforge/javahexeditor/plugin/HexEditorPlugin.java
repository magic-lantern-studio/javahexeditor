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
package net.sourceforge.javahexeditor.plugin;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

import net.sourceforge.javahexeditor.FindReplaceHistory;
import net.sourceforge.javahexeditor.Manager;
import net.sourceforge.javahexeditor.common.Log;

/**
 * The main plugin class to be used in the desktop.
 */
public final class HexEditorPlugin extends AbstractUIPlugin {

	public static final String ID = Manager.ID;

	// The shared instance.
	private static HexEditorPlugin plugin;

	private FindReplaceHistory findReplaceHistory;

	/**
	 * The constructor.
	 */
	public HexEditorPlugin() {
		findReplaceHistory = new FindReplaceHistory();
	}

	/**
	 * Returns the shared instance.
	 *
	 * @return The shared instance, not <code>null</code>.
	 */
	public static HexEditorPlugin getDefault() {
		return plugin;
	}

	/**
	 * This method is called upon plug-in activation.
	 */
	@Override
	public void start(BundleContext context) throws Exception {
		super.start(context);
		plugin = this;
		Log.setDelegate(new Log.Delegate() {
			
			public void log(String message, Throwable th) {
				if (message == null) {
					message = th.getMessage();
					if (message == null) {
						message = th.toString();
					}
				}
				getDefault().getLog().log(new Status(IStatus.ERROR, ID, IStatus.OK, message, th));
			}

			public boolean isTraceActive() {
				return isDebugging();
			}
		});
	}

	/**
	 * This method is called when the plug-in is stopped.
	 */
	@Override
	public void stop(BundleContext context) throws Exception {
		super.stop(context);
		plugin = null;
	}

	/**
	 * Gets the find-replace history.
	 *
	 * @return The modifiable find-replace history, not <code>null</code>.
	 */
	public FindReplaceHistory getFindReplaceHistory() {
		return findReplaceHistory;
	}


}
