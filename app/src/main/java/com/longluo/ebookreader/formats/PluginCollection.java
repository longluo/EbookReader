package com.longluo.ebookreader.formats;

import java.util.*;

import android.os.Build;

import com.longluo.zlibrary.core.filesystem.ZLFile;
import com.longluo.zlibrary.core.filetypes.*;
import com.longluo.zlibrary.core.util.SystemInfo;

public class PluginCollection implements IFormatPluginCollection {
	static {
		System.loadLibrary("NativeFormats-v4");
	}

	private static volatile PluginCollection ourInstance;

	private final List<BuiltinFormatPlugin> myBuiltinPlugins =
		new LinkedList<BuiltinFormatPlugin>();
	private final List<ExternalFormatPlugin> myExternalPlugins =
		new LinkedList<ExternalFormatPlugin>();

	public static PluginCollection Instance(SystemInfo systemInfo) {
		if (ourInstance == null) {
			createInstance(systemInfo);
		}
		return ourInstance;
	}

	private static synchronized void createInstance(SystemInfo systemInfo) {
		if (ourInstance == null) {
			ourInstance = new PluginCollection(systemInfo);

			// This code cannot be moved to constructor
			// because nativePlugins() is a native method
			for (NativeFormatPlugin p : ourInstance.nativePlugins(systemInfo)) {
				ourInstance.myBuiltinPlugins.add(p);
				System.err.println("native plugin: " + p);
			}
		}
	}

	public static void deleteInstance() {
		if (ourInstance != null) {
			ourInstance = null;
		}
	}

	private PluginCollection(SystemInfo systemInfo) {
		if (Build.VERSION.SDK_INT >= 8) {
			myExternalPlugins.add(new DjVuPlugin(systemInfo));
			myExternalPlugins.add(new PDFPlugin(systemInfo));
			myExternalPlugins.add(new ComicBookPlugin(systemInfo));
		}
	}

	public FormatPlugin getPlugin(ZLFile file) {
		final FileType fileType = FileTypeCollection.Instance.typeForFile(file);
		final FormatPlugin plugin = getPlugin(fileType);
		if (plugin instanceof ExternalFormatPlugin) {
			return file == file.getPhysicalFile() ? plugin : null;
		}
		return plugin;
	}

	public FormatPlugin getPlugin(FileType fileType) {
		if (fileType == null) {
			return null;
		}

		for (FormatPlugin p : myBuiltinPlugins) {
			if (fileType.Id.equalsIgnoreCase(p.supportedFileType())) {
				return p;
			}
		}
		for (FormatPlugin p : myExternalPlugins) {
			if (fileType.Id.equalsIgnoreCase(p.supportedFileType())) {
				return p;
			}
		}
		return null;
	}

	public List<FormatPlugin> plugins() {
		final ArrayList<FormatPlugin> all = new ArrayList<FormatPlugin>();
		all.addAll(myBuiltinPlugins);
		all.addAll(myExternalPlugins);
		Collections.sort(all, new Comparator<FormatPlugin>() {
			public int compare(FormatPlugin p0, FormatPlugin p1) {
				final int diff = p0.priority() - p1.priority();
				if (diff != 0) {
					return diff;
				}
				return p0.supportedFileType().compareTo(p1.supportedFileType());
			}
		});
		return all;
	}

	private native NativeFormatPlugin[] nativePlugins(SystemInfo systemInfo);
	private native void free();

	protected void finalize() throws Throwable {
		free();
		super.finalize();
	}
}
