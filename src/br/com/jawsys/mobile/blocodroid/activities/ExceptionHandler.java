package br.com.jawsys.mobile.blocodroid.activities;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Date;
import java.util.Random;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Environment;
import android.os.StatFs;

public class ExceptionHandler implements Thread.UncaughtExceptionHandler {

	String VersionName;
	String PackageName;
	String FilePath;
	String PhoneModel;
	String AndroidVersion;
	String Board;
	String Brand;
	// String CPU_ABI;
	String Device;
	String Display;
	String FingerPrint;
	String Host;
	String ID;
	String Manufacturer;
	String Model;
	String Product;
	String Tags;
	long Time;
	String Type;
	String User;

	private Thread.UncaughtExceptionHandler PreviousHandler;
	private static ExceptionHandler S_mInstance;
	private Context CurContext;

	public void init(Context context) {
		PreviousHandler = Thread.getDefaultUncaughtExceptionHandler();
		Thread.setDefaultUncaughtExceptionHandler(this);
		recoltInformations(context);
		CurContext = context;
	}

	public long getAvailableInternalMemorySize() {
		File path = Environment.getDataDirectory();
		StatFs stat = new StatFs(path.getPath());
		long blockSize = stat.getBlockSize();
		long availableBlocks = stat.getAvailableBlocks();
		return availableBlocks * blockSize;
	}

	public long getTotalInternalMemorySize() {
		File path = Environment.getDataDirectory();
		StatFs stat = new StatFs(path.getPath());
		long blockSize = stat.getBlockSize();
		long totalBlocks = stat.getBlockCount();
		return totalBlocks * blockSize;
	}

	void recoltInformations(Context context) {
		PackageManager pm = context.getPackageManager();
		try {
			PackageInfo pi;
			// Version
			pi = pm.getPackageInfo(context.getPackageName(), 0);
			VersionName = pi.versionName;
			// Package name
			PackageName = pi.packageName;
			// Files dir for storing the stack traces
			FilePath = context.getFilesDir().getAbsolutePath();
			// Device model
			PhoneModel = android.os.Build.MODEL;
			// Android version
			AndroidVersion = android.os.Build.VERSION.RELEASE;

			Board = android.os.Build.BOARD;
			Brand = android.os.Build.BRAND;
			// CPU_ABI = android.os.Build.;
			Device = android.os.Build.DEVICE;
			Display = android.os.Build.DISPLAY;
			FingerPrint = android.os.Build.FINGERPRINT;
			Host = android.os.Build.HOST;
			ID = android.os.Build.ID;
			// Manufacturer = android.os.Build.;
			Model = android.os.Build.MODEL;
			Product = android.os.Build.PRODUCT;
			Tags = android.os.Build.TAGS;
			Time = android.os.Build.TIME;
			Type = android.os.Build.TYPE;
			User = android.os.Build.USER;

		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
	}

	public String createInformationString() {
		String returnVal = "";
		returnVal += "Version : " + VersionName;
		returnVal += "\n";
		returnVal += "Package : " + PackageName;
		returnVal += "\n";
		returnVal += "FilePath : " + FilePath;
		returnVal += "\n";
		returnVal += "Phone Model" + PhoneModel;
		returnVal += "\n";
		returnVal += "Android Version : " + AndroidVersion;
		returnVal += "\n";
		returnVal += "Board : " + Board;
		returnVal += "\n";
		returnVal += "Brand : " + Brand;
		returnVal += "\n";
		returnVal += "Device : " + Device;
		returnVal += "\n";
		returnVal += "Display : " + Display;
		returnVal += "\n";
		returnVal += "Finger Print : " + FingerPrint;
		returnVal += "\n";
		returnVal += "Host : " + Host;
		returnVal += "\n";
		returnVal += "ID : " + ID;
		returnVal += "\n";
		returnVal += "Model : " + Model;
		returnVal += "\n";
		returnVal += "Product : " + Product;
		returnVal += "\n";
		returnVal += "Tags : " + Tags;
		returnVal += "\n";
		returnVal += "Time : " + Time;
		returnVal += "\n";
		returnVal += "Type : " + Type;
		returnVal += "\n";
		returnVal += "User : " + User;
		returnVal += "\n";
		returnVal += "Total Internal memory : " + getTotalInternalMemorySize();
		returnVal += "\n";
		returnVal += "Available Internal memory : "
				+ getAvailableInternalMemorySize();
		returnVal += "\n";

		return returnVal;
	}

	public void uncaughtException(Thread t, Throwable e) {
		String Report = "";
		Date CurDate = new Date();
		Report += "Error Report collected on : " + CurDate.toString();
		Report += "\n";
		Report += "\n";
		Report += "Informations :";
		Report += "\n";
		Report += "==============";
		Report += "\n";
		Report += "\n";
		Report += createInformationString();

		Report += "\n\n";
		Report += "Stack : \n";
		Report += "======= \n";
		final Writer result = new StringWriter();
		final PrintWriter printWriter = new PrintWriter(result);
		e.printStackTrace(printWriter);
		String stacktrace = result.toString();
		Report += stacktrace;

		Report += "\n";
		Report += "Cause : \n";
		Report += "======= \n";

		// If the exception was thrown in a background thread inside
		// AsyncTask, then the actual exception can be found with getCause
		Throwable cause = e.getCause();
		while (cause != null) {
			cause.printStackTrace(printWriter);
			Report += result.toString();
			cause = cause.getCause();
		}
		printWriter.close();
		Report += "****  End of current Report ***";
		saveAsFile(Report);
		PreviousHandler.uncaughtException(t, e);
	}

	static ExceptionHandler getInstance() {
		if (S_mInstance == null)
			S_mInstance = new ExceptionHandler();
		return S_mInstance;
	}

	private void saveAsFile(String ErrorContent) {
		try {
			Random generator = new Random();
			int random = generator.nextInt(99999);
			String FileName = "stack-" + random + ".stacktrace";
			FileOutputStream trace = CurContext.openFileOutput(FileName,
					Context.MODE_PRIVATE);
			trace.write(ErrorContent.getBytes());
			trace.close();
		} catch (IOException ioe) {
			// ...
		}
	}

}