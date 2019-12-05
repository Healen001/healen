package com.tencent.fiba.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.HashSet;
import java.util.Set;

/**
 * 代码行统计工具
*/
public class CodeCount {
	
	/** 不统计与主程序无关文件 */
	public static final Set<String> OUT_FILE;
	
	static {
		OUT_FILE = new HashSet<String>();
		// 代码备份工具
		OUT_FILE.add("CodeBackup.java");
		// 代码行统计工具
		OUT_FILE.add("CodeCount.java");
	}
	
	/** 代码行 */
	public long code;
	/** 注释行 */
	public long doc;
	/** 空行 */
	public long sp;
	
	private CodeCount(){}
	
	/**
	 * 递归目录下所有的.java文件
	 * 
	 * @param dir 要查找的目录
	 * @throws Exception
	 */
	private void conuntAllFile(File dir) throws Exception {
		File[] childs = dir.listFiles();
		for (int i = 0; i < childs.length; i++) {
			if(OUT_FILE.contains(childs[i].getName())) {
				continue;
			}
			if (childs[i].isDirectory()) {
				this.conuntAllFile(childs[i]);
			} else if (childs[i].getName().matches(".*\\.vue$")) {
				this.countCodeInFile(childs[i]);
			}
		}
	}

	/**
	 * 统计单个文件的
	 * 代码行，空行，注释行
	 * 
	 * @param file 要统计文件
	 * @throws Exception 
	 */
	private void countCodeInFile(File file) throws Exception {
		BufferedReader br = null;
		boolean isComment = false;
		br = new BufferedReader(new FileReader(file));
		String line = "";
		while ((line = br.readLine()) != null) {
			line = line.trim();
			if (line.matches("^[\\s&&[^\\n]]*$")) {
				// 满足正则表达式，则为空行
				this.sp++;
			} else if (line.startsWith("/*") || isComment) {
				// 以「/*」开头的为注释
				this.doc++;
				// 不以「*/」结尾，则下一行是注释
				isComment = !line.endsWith("*/");
			} else if (line.startsWith("//")) {
				// 以「//」开头的为注释
				this.doc++;
			} else {
				// 其余为代码行
				this.code++;
			}
		}
		br.close();
	}

	public static void main(String[] args) throws Exception {
		CodeCount count = new CodeCount();
		count.conuntAllFile(new File("C:\\Users\\v_rujinghe\\Desktop\\edu_kyfz\\src"));
		System.out.println("代码行：\t" + count.code);
		System.out.println("注释行：\t" + count.doc);
		System.out.println("空行：\t" + count.sp);
	}
}