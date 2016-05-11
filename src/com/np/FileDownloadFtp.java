package com.np;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.nio.file.Files;

public class FileDownloadFtp {

	// ftp://username:password@hostname:port/path
	// ftp://oracle:secret@www.myclientserver.com/myproject/2013/Project.zip;type=i

	private static final int BUFFER_SIZE = 4096;
	public static final int EOF = -1;
	private static final int DEFAULT_BUFFER_SIZE = 1024 * 4;
	
	public static void main(String[] args) {
		FileDownloadFtp d1 = new FileDownloadFtp();
		d1.download();
		try {
			Thread.sleep(500);
		} catch (Exception e) {
			System.out.println(e);
		}
	}
	
	//ftp://lt-fs1/Kepler/BLR_Scan/OutingBills/rent

	public void download() { // this is a function
		long startTime = System.currentTimeMillis();
		String ftpUrl = "";// **username**:**password**@filePath ;
		String file = "filename"; // name of the file which has to be download
		String host = ""; // ftp server
		String user = "username"; // user name of the ftp server
		String pass = "**password"; // password of the ftp server

		String savePath ="D:/Download";
		ftpUrl = "ftp://speedtest.tele2.net/500MB.zip"; // String.format(ftpUrl,
																									// user,
																									// pass,
																									// host)
																									// ;
		System.out.println("Connecting to FTP server");

		try {
			URL url = new URL(ftpUrl);
			URLConnection conn = url.openConnection();
			InputStream inputStream = conn.getInputStream();
			long contentLength = conn.getContentLength();
			String disposition = conn.getHeaderField("Content-Disposition");
			System.out.println("Size of the file to download in kb is:-"
					+ contentLength / 1024);
			String fileName = "";
			System.out.println("Content-Disposition = " + disposition);
			System.out.println("Content-Length = " + contentLength);
			fileName = ftpUrl.substring(ftpUrl.lastIndexOf("/") + 1, ftpUrl.length());
			System.out.println("fileName = " + fileName);
			String saveDir = "D:/Download";
			
			String saveFilePath = saveDir + File.separator + fileName;
			
			File destination = new File(saveFilePath);
			FileOutputStream outputStream = openOutputStream(destination);

			long dounloadSize = 0;
			try {

				byte[] buffer = new byte[DEFAULT_BUFFER_SIZE];
				dounloadSize = copyLarge(inputStream, outputStream, buffer);
				outputStream.close();
			} catch (IOException ex) {
				closeQuietly(outputStream);
				if (dounloadSize < contentLength) {
					forceDelete(destination);
				}
			} finally {
				closeQuietly(outputStream);
			}
			
		/*	byte[] buffer = new byte[BUFFER_SIZE];
			int bytesRead = -1;
			while ((bytesRead = inputStream.read(buffer)) != -1) {
				outputStream.write(buffer, 0, bytesRead);
			}*/
			long endTime = System.currentTimeMillis();
			System.out.println("File downloaded");
			System.out.println("Download time in sec. is:-"
					+ (endTime - startTime) / 1000);
			outputStream.close();
			inputStream.close();
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}

	
	public static void forceDelete(final File file) throws IOException {
		if (file.isDirectory()) {
		} else {
			
			final boolean filePresent = file.exists();
			try{
			Files.delete(file.toPath());
			}catch(IOException ex){
				System.out.println(ex);
			}
		}
	}
	
	public static FileOutputStream openOutputStream(final File file) throws IOException {
		if (file.exists()) {
			if (file.isDirectory()) {
				throw new IOException("File '" + file + "' exists but is a directory");
			}
			if (file.canWrite() == false) {
				throw new IOException("File '" + file + "' cannot be written to");
			}
		} else {
			final File parent = file.getParentFile();
			if (parent != null) {
				if (!parent.mkdirs() && !parent.isDirectory()) {
					throw new IOException("Directory '" + parent + "' could not be created");
				}
			}
		}
		return new FileOutputStream(file);
	}
	
	public static long copyLarge(final InputStream input, final OutputStream output, final byte[] buffer)
			throws IOException {
		long count = 0;
		int n;
		while (EOF != (n = input.read(buffer))) {
			output.write(buffer, 0, n);
			count += n;
		}
		return count;
	}
	
	public static void closeQuietly(OutputStream output) {
		try {
			if (output != null) {
				output.close();
			}
		} catch (IOException ioe) {
			// ignore
		}
	}

	public static void closeQuietly(InputStream input) {
		try {
			if (input != null) {
				input.close();
			}
		} catch (IOException ioe) {
			// ignore
		}
	}
}
