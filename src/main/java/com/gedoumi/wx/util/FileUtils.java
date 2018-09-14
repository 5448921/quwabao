package com.gedoumi.wx.util;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

import javax.activation.MimetypesFileTypeMap;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class FileUtils {

	/**
	 * 路径存在则覆盖，不存在则创建。
	 * 
	 * @param path
	 *            路径
	 * @param content
	 *            内容
	 * @throws IOException
	 */
	public static void saveFile(String path, String content) throws IOException {
		// System.out.println("save-------------------"+path);
		if (content == null)
			content = "";
		File file = new File(path);
		if (file.exists()) {
			file.delete();
		}
		if (!file.getParentFile().exists()) {
			mkdir(file.getParentFile());
		}
		BufferedWriter bw = null;
		try {
			file.createNewFile();
			bw = new BufferedWriter(new OutputStreamWriter(
					new FileOutputStream(file), "utf-8"));
			bw.write(new String(content.getBytes()));// 输出字符串
			bw.flush();
		} catch (IOException e) {
			e.printStackTrace();
			throw e;
		} finally {
			try {
				if (bw != null) {
					bw.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public static void saveFile(String path, InputStream inputStream) {
		File file = new File(path);
		if (file.exists()) {
			file.delete();
		}
		if (!file.getParentFile().exists()) {
			mkdir(file.getParentFile());
		}
		FileOutputStream fos = null;
		try {
			fos = new FileOutputStream(path);
			byte buffer[] = new byte[4 * 1024];
			while ((inputStream.read(buffer)) != -1) {
				fos.write(buffer);
			}
			fos.flush();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (fos != null) {
					fos.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 路径存在则覆盖，不存在则创建。
	 * 
	 * @param path
	 *            路径
	 * @param content
	 *            内容
	 * @throws IOException
	 */
	public static void saveFile(String path, byte[] content) throws IOException {
		File file = new File(path);
		if (file.exists()) {
			file.delete();
		}
		if (!file.getParentFile().exists()) {
			mkdir(file.getParentFile());
		}
		FileOutputStream fos = null;
		try {
			fos = new FileOutputStream(path);
			fos.write(content); // 写入文件
			fos.flush();
		} catch (IOException e) {
			e.printStackTrace();
			throw e;
		} finally {
			try {
				if (fos != null) {
					fos.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 递归创建目录
	 * 
	 * @param file
	 */
	public static void mkdir(File file) {
		if (file.exists()) {
			return;
		}
		if (!file.getParentFile().exists()) {
			mkdir(file.getParentFile());
		}
		file.mkdir();
	}

	/**
	 * 路径存在则覆盖，不存在则创建。
	 * 
	 * @param path
	 *            路径
	 * @param content
	 *            内容
	 * @throws IOException
	 */
	public static void deleteFile(String path) {
		// System.out.println("delete-------------------"+path);
		File file = new File(path);
		if (file.exists()) {
			file.delete();
		}
	}

	public static String getFileString(String path) throws IOException {
		File file = new File(path);
		if (!file.exists() || file.isDirectory()) {
			return "";
		}
		BufferedReader br = new BufferedReader(new InputStreamReader(
				new FileInputStream(file), "utf-8"));
		String b;
		StringBuffer sb = new StringBuffer();
		int len = 0;

		while ((b = br.readLine()) != null) {

			if (len != 0) // 处理换行符的问题
			{
				sb.append("\r\n" + new String(b.getBytes()));
			} else {
				sb.append(new String(b.getBytes()));
			}
			len++;
		}
		return sb.toString();
	}

	/**
	 * 保留换行
	 * 
	 * @param path
	 * @return
	 * @throws IOException
	 */
	public static String getFileStringBr(String path) throws IOException {
		File file = new File(path);
		if (!file.exists() || file.isDirectory()) {
			return "";
		}
		BufferedReader br = new BufferedReader(new InputStreamReader(
				new FileInputStream(file), "utf-8"));
		String b;
		StringBuffer sb = new StringBuffer();
		while ((b = br.readLine()) != null) {
			sb.append(new String(b.getBytes()) + "\n ");
		}
		return sb.toString();
	}

	public static void deleteDir(String path) {
		File file = new File(path);
		if (file.exists()) {
			file.delete();
		}
	}

	// /* 复制整个文件夹内容
	// *
	// * @param oldPath
	// * String 原文件路径 如：c:/old
	// * @param newPath
	// * String 复制后路径 如：f:/new
	// * @return boolean
	// */
	public static void copyDirectory(String oldPath, String newPath,
			String rodmomCPId) {

		try {
			(new File(newPath)).mkdirs(); // 如果文件夹不存在 则建立新文件夹
			File a = new File(oldPath);
			String[] file = a.list();
			File temp = null;
			for (int i = 0; i < file.length; i++) {
				if (oldPath.endsWith(File.separator)) {
					temp = new File(oldPath + file[i]);
				} else {
					temp = new File(oldPath + File.separator + file[i]);
				}
				boolean updateDflag = false;
				if (temp.getAbsolutePath().indexOf("document") != -1) {
					updateDflag = true;
					if (temp.getAbsolutePath().indexOf("siteimg") != -1) {
						updateDflag = false;
					}
					if (temp.getName().equals("document")) {
						updateDflag = false;
					}
				}
				if (temp.isFile()) {
					String newFileName = "";
					if (updateDflag) {
						newFileName = newPath + "/" + rodmomCPId
								+ (temp.getName()).toString();
					} else {
						newFileName = newPath + "/"
								+ (temp.getName()).toString();
					}
					FileInputStream input = new FileInputStream(temp);
					FileOutputStream output = new FileOutputStream(newFileName);
					byte[] b = new byte[1024 * 5];
					int len;
					while ((len = input.read(b)) != -1) {
						output.write(b, 0, len);
					}
					output.flush();
					output.close();
					input.close();
				}
				if (temp.isDirectory()) {// 如果是子文件夹
					if (updateDflag) {
						copyDirectory(oldPath + "/" + file[i], newPath + "/"
								+ rodmomCPId + file[i], rodmomCPId);
					} else {
						copyDirectory(oldPath + "/" + file[i], newPath + "/"
								+ file[i], rodmomCPId);
					}
				}
			}
		} catch (Exception e) {
			System.out.println("复制整个文件夹内容操作出错");
			e.printStackTrace();
		}
	}

	/**
	 * 复制单个文件
	 * 
	 * @param oldPath
	 *            String 原文件路径 如：c:/fqf.txt
	 * @param newPath
	 *            String 复制后路径 如：f:/fqf.txt
	 * @return boolean
	 */
	public static void copyFile(String oldPath, String newPath) {
		try {
			int bytesum = 0;
			int byteread = 0;
			File oldfile = new File(oldPath);
			mkdir((new File(newPath)).getParentFile());
			if (oldfile.exists()) { // 文件存在时
				InputStream inStream = new FileInputStream(oldPath); // 读入原文件
				FileOutputStream fs = new FileOutputStream(newPath);
				byte[] buffer = new byte[1444];
				int length;
				while ((byteread = inStream.read(buffer)) != -1) {
					bytesum += byteread; // 字节数 文件大小
					fs.write(buffer, 0, byteread);
				}
				inStream.close();
			}
		} catch (Exception e) {
			System.out.println("复制单个文件操作出错");
			e.printStackTrace();

		}
	}

	/**
	 * 将gb2312的文件转换成utf-8的文件格式
	 * 
	 * @param gbkFilePath
	 * @param utf8filePath
	 */
	public static void parserGbkFileToUtf8File(String gbkFilePath,
			String utf8filePath) {
		File gbkFile = new File(gbkFilePath);
		InputStream is = null;
		Reader isReader = null;
		BufferedReader reader = null;

		OutputStream out = null;
		Writer outWriter = null;
		BufferedWriter writer = null;

		try {
			if (gbkFile.exists()) {
				File utf8File = new File(utf8filePath);
				if (!utf8File.getParentFile().exists()) {
					utf8File.getParentFile().mkdirs();
				}
				StringBuffer sb = new StringBuffer();
				is = new FileInputStream(gbkFile);
				isReader = new InputStreamReader(is, "gb2312");
				reader = new BufferedReader(isReader);

				out = new FileOutputStream(utf8filePath);
				outWriter = new OutputStreamWriter(out, "utf-8");
				writer = new BufferedWriter(outWriter);
				String line = null;
				while ((line = reader.readLine()) != null) {
					if (line.indexOf("gb2312") != -1) {
						line = line.replace("gb2312", "utf-8");
					}
					sb.append(line + "\n");
				}
				writer.write(sb.toString());
				writer.flush();
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				if (reader != null) {
					reader.close();
					reader = null;
				}
				if (writer != null) {
					writer.close();
					writer = null;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}

		}

	}

	/**
	 * 重命名文件或文件夹
	 * 
	 * @param resFilePath
	 *            源文件路径
	 * @param newFileName
	 *            重命名
	 * @return 操作成功标识
	 */
	public static boolean renameFile(String resFilePath, String newFileName) {
		String newFilePath = (new File(resFilePath).getParentFile().getPath()
				+ "/" + newFileName);
		File resFile = new File(resFilePath);
		File newFile = new File(newFilePath);
		return resFile.renameTo(newFile);
	}

	/**
	 * 下载附件
	 * 
	 * @param in
	 * @param filename
	 * @param request
	 * @param response
	 */
	public void downFile(InputStream in, String filename,
			HttpServletRequest request, HttpServletResponse response) {
		try {
			BufferedOutputStream out = new java.io.BufferedOutputStream(
					response.getOutputStream());
			byte[] buff = new byte[1024 * 4];
			int n = 0;
			response.reset(); //
			response.setContentType("application/x-msdownload; charset=utf-8");
			if (request.getHeader("User-Agent").toLowerCase()
					.indexOf("firefox") > 0)
				response.setHeader(
						"Content-Disposition",
						"attachment; filename="
								+ new String(filename.getBytes("UTF-8"),
										"ISO8859-1"));// firefox
			else if (request.getHeader("User-Agent").toUpperCase()
					.indexOf("MSIE") > 0)
				response.setHeader(
						"Content-Disposition",
						"attachment; filename="
								+ URLEncoder.encode(filename, "UTF-8"));// IE
			while ((n = in.read(buff)) != -1)
				out.write(buff, 0, n);
			out.flush();
			out.close();
			in.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 下载附件
	 * 
	 * @param in
	 * @param filename
	 * @param request
	 * @param response
	 */
	public void downFile(String fileurl, String filename,
			HttpServletRequest request, HttpServletResponse response) {

		try {
			File fileout = new File(fileurl);
			InputStream in = new FileInputStream(fileout);

			BufferedOutputStream out = new java.io.BufferedOutputStream(
					response.getOutputStream());
			byte[] buff = new byte[1024 * 4];
			int n = 0;
			response.reset(); //
			String mimetype = new MimetypesFileTypeMap()
					.getContentType(fileout);
			response.setContentType(mimetype);
			if (request.getHeader("User-Agent").toLowerCase()
					.indexOf("firefox") > 0)
				response.setHeader(
						"Content-Disposition",
						"attachment; filename="
								+ new String(filename.getBytes("UTF-8"),
										"ISO8859-1"));// firefox
			else if (request.getHeader("User-Agent").toUpperCase()
					.indexOf("MSIE") > 0)
				response.setHeader(
						"Content-Disposition",
						"attachment; filename="
								+ URLEncoder.encode(filename, "UTF-8"));// IE
			while ((n = in.read(buff)) != -1)
				out.write(buff, 0, n);
			out.flush();
			out.close();
			in.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void download(String urlString, String filename,
			String savePath) throws Exception {
		// 构造URL
		URL url = new URL(urlString);
		// 打开连接
		URLConnection con = url.openConnection();
		// 设置请求超时为5s
		con.setConnectTimeout(5 * 1000);
		// 输入流
		InputStream is = con.getInputStream();

		// 1K的数据缓冲
		byte[] bs = new byte[1024];
		// 读取到的数据长度
		int len;
		// 输出的文件流
		File sf = new File(savePath);
		if (!sf.exists()) {
			sf.mkdirs();
		}
		OutputStream os = new FileOutputStream(sf.getPath() + "\\" + filename);
		// 开始读取
		while ((len = is.read(bs)) != -1) {
			os.write(bs, 0, len);
		}
		// 完毕，关闭所有链接
		os.close();
		is.close();
	}

	public static void main(String[] a) throws Exception {

		// String oldimagepath=
		// "E:/workspace_youxue/tomcat-6.0.26/webapps/html/upload/AFECZYW033/000000020013c8a861d3e/000000020013c8a861d3e.files";
		// FileUtils.renameFile(oldimagepath,"000000020013c8a861d3e_images");

		FileUtils.download(
				"http://ui.51bi.com/opt/siteimg/images/fanbei0923/Mid_07.jpg",
				"51bi.jpg", "d:/img1/");
	}

}
